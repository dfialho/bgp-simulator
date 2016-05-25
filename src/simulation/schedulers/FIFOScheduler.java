package simulation.schedulers;

public class FIFOScheduler extends AbstractScheduler {

    @Override
    protected long schedule(ScheduledRoute scheduledRoute) {
        return scheduledRoute.getTimestamp() + 1;
    }

}
