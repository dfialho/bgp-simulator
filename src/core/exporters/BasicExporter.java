package core.exporters;

import core.*;
import core.events.AdvertisementEvent;
import core.events.EventNotifier;
import core.events.ExportEvent;
import core.schedulers.RouteReference;
import core.schedulers.Scheduler;


/**
 * Basic implementation of an exporter.
 */
public class BasicExporter implements Exporter {

    private final Scheduler scheduler;

    /**
     * Constructs an exporter given the scheduler where to put exported messages.
     *
     * @param scheduler scheduler where the messages are pushed to.
     */
    public BasicExporter(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Returns the scheduler used by the exporter to put the messages.
     *
     * @return the scheduler used by the exporter to put the messages.
     */
    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Exports the given message. The message arrival time must be the current time.
     *
     * @param exportingRouter   router exporting the route.
     * @param route             route to export.
     * @param currentTime       current simulation time.
     */
    @Override
    public void export(Router exportingRouter, Route route, int currentTime) {
        MRAITimer timer = exportingRouter.getMRAITimer();

        if (!timer.hasExpired(currentTime)) {
            // update the export route for the current timer
            timer.updateExportRoute(route);

        } else {
            timer.reset(currentTime, route);
            int expirationTime = timer.getExpirationTime();

            // export the route to each in-neighbor
            for (Link link : exportingRouter.getInLinks()) {
                export(link, timer.getExportRouteReference(), expirationTime);
            }

            // fire an Advertisement Event with the time corresponding to the expiration time
            // the expiration time is the time when the event actually occurs
            EventNotifier.eventNotifier().notifyAdvertisementEvent(
                    new AdvertisementEvent(expirationTime, exportingRouter, route));
        }

    }

    /**
     * Exports the self route for the given destination to all of its in-neighbors.
     *
     * @param destination destination to export self routes for.
     * @param policy      policy used to get the self route.
     */
    @Override
    public void export(Destination destination, Policy policy) {
        Route selfRoute = Route.newSelfRoute(policy);
        destination.setSelfRoute(selfRoute);
        RouteReference selfRouteReference = new RouteReference(selfRoute);

        destination.getInLinks().forEach(link -> export(link, selfRouteReference, 0));
    }

    protected void export(Link exportLink, RouteReference routeReference, int exportTime) {
        // add the route to the scheduler with the given export time
        scheduler.schedule(new Message(exportTime, exportLink, routeReference));

        EventNotifier.eventNotifier().notifyExportEvent(new ExportEvent(exportTime, exportLink, routeReference.getRoute()));
    }

}
