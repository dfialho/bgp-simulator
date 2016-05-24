package simulators;

import io.reporters.Reporter;
import network.Network;
import simulators.statscollectors.BasicStatsCollector;
import simulators.statscollectors.SPPolicyBasicStatsCollector;

import java.io.IOException;

public class SPPolicyStandardSimulator extends StandardSimulator {
    /**
     * Constructs a simulator by creating an initial state to be simulated. For this it calls the protected
     * method createInitialState(). Initializes the stats collector.
     *
     * @param network       network to simulate.
     * @param destinationId id of the destination node.
     */
    public SPPolicyStandardSimulator(Network network, int destinationId) {
        super(network, destinationId);
    }

    /**
     * Called in the constructor to create the stats collector. By default it returns a BasicStatsCollector object.
     * Each subclass should override this method if this is not the intended behaviour.
     *
     * @param nodeCount number of nodes in the network.
     * @param linkCount number of links in the network.
     * @return new basic stats collector object.
     */
    @Override
    protected BasicStatsCollector createStatsCollector(int nodeCount, int linkCount) {
        return new SPPolicyBasicStatsCollector(nodeCount, linkCount);
    }

    /**
     * Calls the reporter generate() method to generate a report with the collected stats.
     *
     * @param reporter reporter to generate report.
     * @throws IOException if an error with the output file occurs.
     */
    @Override
    public void report(Reporter reporter) throws IOException {
        // must cast to call the correct generate method
        reporter.generate((SPPolicyBasicStatsCollector) statsCollector);
    }
}