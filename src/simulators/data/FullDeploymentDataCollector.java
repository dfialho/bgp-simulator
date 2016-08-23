package simulators.data;

import core.events.ExportEvent;
import io.reporters.Reporter;

import java.io.IOException;

public class FullDeploymentDataCollector extends BasicDataCollector {

    protected FullDeploymentDataSet fullDeploymentDataSet = new FullDeploymentDataSet();
    private boolean deployed = false;

    /**
     * Dumps the current data to the reporter.
     *
     * @param reporter reporter to write data to.
     */
    @Override
    public void dump(Reporter reporter) throws IOException {
        reporter.write(basicDataSet, fullDeploymentDataSet);
    }

    /**
     * Clears all data from a data collector.
     */
    @Override
    public void clear() {
        super.clear();
        deployed = false;
        fullDeploymentDataSet.clear();
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    /**
     * Invoked when a export event occurs.
     *
     * @param event export event that occurred.
     */
    @Override
    public void onExported(ExportEvent event) {
        super.onExported(event);
        if (deployed) {
            fullDeploymentDataSet.addMessage();
        }
    }
}
