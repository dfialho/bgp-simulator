package core.exporters;

import core.*;
import core.events.EventNotifier;
import core.events.ExportEvent;
import core.schedulers.Scheduler;

import java.util.Collection;


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

//        if (!timer.isEnabled()) {
//            timer.reset(currentTime, route);
//
//            exportToNeighbors(exportingRouter, timer);
//        }
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

        destination.getInLinks().forEach(link -> export(link, selfRoute, 0));
    }

    /**
     * Takes a collection of timers and exports the route associated with the timer to all
     * neighbors of the owner of the timer.
     *
     * @param timers collection with the timers to export.
     */
    @Override
    public void export(Collection<MRAITimer> timers) {

        for (MRAITimer timer : timers) {
            if (timer.hasExportableRoute()) {
                exportToNeighbors(timer.getOwner(), timer.getExportRoute(),
                        timer.getExpirationTime());
            }
        }
    }

    protected void exportToNeighbors(Router exportingRouter, Route route, int exportTime) {

        for (Link link : exportingRouter.getInLinks()) {
            export(link, route, exportTime);
        }
    }

    protected void export(Link exportLink, Route route, int exportTime) {
        // add the route to the scheduler with the given export time
        scheduler.schedule(new Message(exportTime, exportLink, route));

        EventNotifier.eventNotifier().notifyExportEvent(new ExportEvent(exportTime, exportLink, route));
    }

}
