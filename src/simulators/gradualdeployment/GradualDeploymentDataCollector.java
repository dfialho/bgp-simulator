package simulators.gradualdeployment;

import core.Engine;
import core.topology.ConnectedNode;
import simulators.DataCollector;
import simulators.Dataset;
import simulators.basic.BasicDataCollector;

/**
 * Adds to the basic data collector, the collection of data from a gradual deployment simulation.
 * Collects the nodes which have deployed a new protocol during one simulation instance.
 */
public class GradualDeploymentDataCollector implements DataCollector {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private fields
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private final GradualDeploymentDataset gradualDeploymentDataSet;
    private final BasicDataCollector basicDataCollector;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Constructors
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Creates new gradual deployment data collector with an empty dataset.
     */
    public GradualDeploymentDataCollector() {
        gradualDeploymentDataSet = new GradualDeploymentDataset();

        // basic data collector stores data to the underlying basic dataset of the timed deployment dataset
        basicDataCollector = new BasicDataCollector(gradualDeploymentDataSet.getBasicDataset());
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Public Interface
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Notifies the collector that a new node has deployed a new protocol. Must be invoked when a new node deploys a
     * new protocol.
     *
     * @param node deploying node.
     */
    public void notifyDeployment(ConnectedNode node) {
        gradualDeploymentDataSet.setAsDeployingNode(node);
    }

    /**
     * Clears all data that has been collected.
     */
    @Override
    public void clear() {
        gradualDeploymentDataSet.clear();
    }

    /**
     * Gives access to the data set storing the collected data.
     *
     * @return a timed deployment dataset instance with the collected data.
     */
    @Override
    public Dataset getDataset() {
        return gradualDeploymentDataSet;
    }

    /**
     * Registers the collector as an export, detect, and time listener of the engine.
     *
     * @param engine engine used for simulating.
     * @throws IllegalStateException if the data collector is already registered.
     */
    @Override
    public void register(Engine engine) throws IllegalStateException {
        basicDataCollector.register(engine);
    }

    /**
     * Unregisters the collector as an export, detect, and time listener of the engine. If the collector is not
     * registered the method will take no effect.
     */
    @Override
    public void unregister() {
        basicDataCollector.unregister();
    }

}
