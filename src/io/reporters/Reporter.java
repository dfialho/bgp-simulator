package io.reporters;

import network.Network;
import simulators.data.BasicDataSet;
import simulators.data.FullDeploymentDataSet;
import simulators.data.SPPolicyDataSet;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Base class that all reports must extend.
 * A reporter generates reports depending on the given stats collector.
 */
public abstract class Reporter implements Closeable, AutoCloseable {

    protected File outputFile;
    protected Network network;    // holds the network to dump network information

    /**
     * Constructs a reporter associating the output file.
     *
     * @param outputFile file to output report to.
     * @param network    network being simulated.
     */
    public Reporter(File outputFile, Network network) {
        this.outputFile = outputFile;
        this.network = network;
    }

    /**
     * Dumps that data from the data set to the current output file.
     *
     * @param dataSet data set to dump to the output file.
     */
    public abstract void dump(BasicDataSet dataSet) throws IOException;

    public abstract void dump(BasicDataSet basicDataSet, SPPolicyDataSet spPolicyDataSet) throws IOException;

    public abstract void dump(BasicDataSet basicDataSet, FullDeploymentDataSet fullDeploymentDataSet) throws IOException;
}
