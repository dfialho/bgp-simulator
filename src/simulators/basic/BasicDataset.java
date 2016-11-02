package simulators.basic;

import core.topology.ConnectedNode;
import simulators.Dataset;
import simulators.Detection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Stores the following data:
 *  - total message count
 *  - detecting nodes count
 *  - cut-off links count
 *  - false positives count
 *  - detections
 */
public class BasicDataset implements Dataset {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private Structures used to store the data
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private long simulationSeed = 0;
    private int totalMessageCount = 0;
    private Set<ConnectedNode> detectingNodes = new HashSet<>();    // stores all unique detecting nodes
    private int cutOffLinksCount = 0;
    private List<Detection> detections = new ArrayList<>();
    private long simulationTime = 0;
    private int falsePositiveCount = 0;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Public Interface - Methods to access the stored data
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Returns the seed of the simulation which generated the current data.
     *
     * @return the seed of the simulation which generated the current data.
     */
    public long getSimulationSeed() {
        return simulationSeed;
    }

    /**
     * Returns the label for the data property Seed.
     *
     * @return the label for the data property Seed
     */
    public String getSimulationSeedLabel() {
        return "Seed";
    }

    /**
     * Returns the total message count.
     *
     * @return total message count.
     */
    public int getTotalMessageCount() {
        return totalMessageCount;
    }

    /**
     * Returns the label for the data property Total Message Count.
     *
     * @return the label for the data property Total Message Count
     */
    public String getTotalMessageCountLabel() {
        return "Total Message Count";
    }

    /**
     * Returns the number of distinct nodes that detected at least once.
     *
     * @return number of distinct nodes with one detection.
     */
    public int getDetectingNodesCount() {
        return detectingNodes.size();
    }

    /**
     * Returns the label for the data property Detecting Nodes Count.
     *
     * @return the label for the data property Detecting Nodes Count
     */
    public String getDetectingNodesCountLabel() {
        return "Detecting Nodes Count";
    }

    /**
     * Returns the number of cut-off links.
     *
     * @return number of cut-off links.
     */
    public int getCutOffLinksCount() {
        return cutOffLinksCount;
    }

    /**
     * Returns the label for the data property Cut-Off Links Count.
     *
     * @return the label for the data property Cut-Off Links Count
     */
    public String getCutOffLinksCountLabel() {
        return "Cut-Off Links Count";
    }

    /**
     * Returns a list with all the detections.
     *
     * @return a list with all the detections.
     */
    public List<Detection> getDetections() {
        return detections;
    }

    /**
     * Returns the total simulation time.
     *
     * @return total simulation time.
     */
    public long getSimulationTime() {
        return simulationTime;
    }

    /**
     * Returns the label for the data property Time.
     *
     * @return the label for the data property Time
     */
    public String getSimulationTimeLabel() {
        return "Time";
    }

    /**
     * Returns the number of false positives.
     *
     * @return number of false positives.
     */
    public int getFalsePositiveCount() {
        return falsePositiveCount;
    }

    /**
     * Returns the label for the data property False Positive Count.
     *
     * @return the label for the data property False Positive Count
     */
    public String getFalsePositiveCountLabel() {
        return "False Positive Count";
    } 

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Public Interface - Methods to update the data
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Sets a new simulation seed. This should be set for each simulation.
     *
     * @param simulationSeed seed corresponding to the simulation to which the data belongs to.
     */
    public void setSimulationSeed(long simulationSeed) {
        this.simulationSeed = simulationSeed;
    }

    /**
     * Sets the simulation time.
     *
     * @param time simulation time.
     */
    public void setSimulationTime(long time) {
        this.simulationTime = time;
    }

    /**
     * Counts a new message.
     */
    public void addMessage() {
        totalMessageCount++;
    }

    /**
     * Adds a new detection.
     *
     * @param detection detection to add.
     */
    public void addDetection(Detection detection) {
        detections.add(detection);
        detectingNodes.add(detection.getDetectingNode());
        cutOffLinksCount++; // every detection cuts off a new link

        if (detection.isFalsePositive()) {
            falsePositiveCount++;
        }
    }

    /**
     * Clears all data from the dataset.
     */
    public void clear() {
        simulationSeed = 0;
        totalMessageCount = 0;
        detectingNodes.clear();
        cutOffLinksCount = 0;
        detections.clear();
        simulationTime = 0;
        falsePositiveCount = 0;
    }

}
