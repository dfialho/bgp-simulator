package addons.protocolchangers;

import simulation.Engine;
import simulation.State;
import simulation.TimeListener;


/**
 * Periodical protocol changer. It calls the onTimeToChange() method periodically according to the defined
 * change period.
 */
public abstract class PeriodicProtocolChanger extends ProtocolChanger implements TimeListener {

    private long changePeriod;
    private long timeToChange;

    /**
     * Creates a protocol changer by assigning it an engine and a state.
     *
     * @param engine engine to assign to.
     * @param state  state to assign to.
     */
    public PeriodicProtocolChanger(Engine engine, State state, long changePeriod) {
        super(engine, state);
        this.changePeriod = changePeriod;
        this.timeToChange = changePeriod;
    }

    @Override
    public void onTimeChange(long newTime) {
        if (newTime >= timeToChange) {
            onTimeToChange();
            timeToChange += changePeriod;
        }
    }

    /**
     * Called periodically according to the defined change period.
     */
    public abstract void onTimeToChange();
}
