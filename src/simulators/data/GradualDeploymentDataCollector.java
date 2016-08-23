package simulators.data;


import core.topology.Node;
import io.reporters.Reporter;

import java.io.IOException;

/**
 * Adds to the basic data collector, the collection of data from a gradual deployment simulation.
 */
public class GradualDeploymentDataCollector extends BasicDataCollector {

    protected GradualDeploymentDataSet gradualDeploymentDataSet = new GradualDeploymentDataSet();

    /**
     * Marks a new node as deployed.
     *
     * @param node node to be set as deployed
     */
    public void setNodeDeployed(Node node) {
        gradualDeploymentDataSet.addDeployedNode(node);
    }

    /**
     * Dumps the current data to the reporter.
     *
     * @param reporter reporter to write data to.
     */
    @Override
    public void dump(Reporter reporter) throws IOException {
        reporter.write(basicDataSet, gradualDeploymentDataSet);
    }

    /**
     * Clears all data from a data collector.
     */
    @Override
    public void clear() {
        super.clear();
        gradualDeploymentDataSet.clear();
    }
}
