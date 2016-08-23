package io.reporters;

import core.Path;
import core.Protocol;
import core.topology.Link;
import core.topology.Network;
import core.topology.Node;
import core.topology.Topology;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import simulators.Simulator;
import simulators.data.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates reports in CSV format.
 */
public class CSVReporter implements Reporter {

    private static final char DELIMITER = ';';

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private fields
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private final File baseOutputFile;  // path with the base file name for the output

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private fields
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private int simulationCounter = 0;                     // counts the simulations
    private boolean isCountsFileMissingHeaders = true;     // indicates if the counts file is missing the headers
    private boolean isDetectionsFileMissingHeaders = true; // indicates if the detections file is missing the headers
    private boolean isDeploymentsFileMissingHeaders = true; // indicates if the deployments file is missing the headers

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Constructors
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Constructs a reporter associating the output file.
     *
     * @param baseOutputFile file to output report to.
     */
    public CSVReporter(File baseOutputFile) throws IOException {
        this.baseOutputFile = baseOutputFile;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Public interface - Write methods from the Reporter Interface
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Writes a summary of the simulation. Containing basic information about the topology and the simulation
     * parameters.
     */
    public void writeSummary(Topology topology, int destinationId, int minDelay, int maxDelay, Protocol protocol,
                             Simulator simulator) throws IOException {

        Network network = topology.getNetwork();

        try (CSVPrinter csvPrinter = getSummaryFilePrinter()) {
            csvPrinter.printRecord("Policy", topology.getPolicy());
            csvPrinter.printRecord("Node Count", network.getNodeCount());
            csvPrinter.printRecord("Link Count", network.getLinkCount());
            csvPrinter.printRecord("Destination", destinationId);
            csvPrinter.printRecord("Message Delay", minDelay, maxDelay);
            csvPrinter.printRecord("Protocol", protocol);
            csvPrinter.printRecord("Simulation Type", simulator);
            csvPrinter.printRecord("Simulation Count", simulationCounter);
        }
    }

    @Override
    public void write(BasicDataSet dataSet) throws IOException {
        mainWrite(dataSet, null, null, null);
    }

    @Override
    public void write(BasicDataSet basicDataSet, SPPolicyDataSet spPolicyDataSet) throws IOException {
        mainWrite(basicDataSet, null, null, spPolicyDataSet);
    }

    @Override
    public void write(BasicDataSet basicDataSet, FullDeploymentDataSet fullDeploymentDataSet) throws IOException {
        mainWrite(basicDataSet, fullDeploymentDataSet, null, null);
    }

    @Override
    public void write(BasicDataSet basicDataSet, FullDeploymentDataSet fullDeploymentDataSet,
                      SPPolicyDataSet spPolicyDataSet) throws IOException {
        mainWrite(basicDataSet, fullDeploymentDataSet, null, spPolicyDataSet);
    }

    @Override
    public void write(BasicDataSet basicDataSet, GradualDeploymentDataSet gradualDeploymentDataSet) throws IOException {
        mainWrite(basicDataSet, null, gradualDeploymentDataSet, null);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private Main write called by all other write methods
     *  Centralized way to write reports
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Main write method. All write methods call this method underneath.
     */
    private void mainWrite(BasicDataSet basicDataSet, FullDeploymentDataSet fullDeploymentDataSet,
                           GradualDeploymentDataSet gradualDeploymentDataSet, SPPolicyDataSet spPolicyDataSet)
            throws IOException {

        simulationCounter++;    // every time write is called it is for a new simulation

        // write counts headers

        if (isCountsFileMissingHeaders) {
            isCountsFileMissingHeaders = false;

            writeColumns(countsWriter, "Time", "Total Message Count", "Detecting Nodes Count", "Cut-Off " +
                    "Links " +
                    "Count");
            if (fullDeploymentDataSet != null) {
                appendColumn(countsWriter, "Messages After Deployment Count");
            }
            if (gradualDeploymentDataSet != null) {
                appendColumn(countsWriter, "Deployed Nodes Count");
            }
            if (spPolicyDataSet != null) {
                appendColumn(countsWriter, "False Positive Count");
            }
            countsWriter.newLine();
        }

        // write counts data

        writeColumns(countsWriter,
                basicDataSet.getSimulationTime(),
                basicDataSet.getTotalMessageCount(),
                basicDataSet.getDetectingNodesCount(),
                basicDataSet.getCutOffLinksCount()
        );

        if (fullDeploymentDataSet != null) {
            appendColumn(countsWriter, fullDeploymentDataSet.getMessageCount());
        }
        if (gradualDeploymentDataSet != null) {
            appendColumn(countsWriter, gradualDeploymentDataSet.getDeployedNodesCount());
        }
        if (spPolicyDataSet != null) {
            appendColumn(countsWriter, spPolicyDataSet.getFalsePositiveCount());
        }

        countsWriter.newLine();

        // write the detections table headers

        if (isDetectionsFileMissingHeaders) {
            isDetectionsFileMissingHeaders = false;

            writeColumns(detectionsWriter, "Simulation", "Detections", "Detecting Nodes", "Cut-Off Links", "Cycles");
            if (spPolicyDataSet != null) {
                appendColumn(detectionsWriter, "False Positive");
            }

            detectionsWriter.newLine();
        }

        // write the detections table data

        int detectionNumber = 1;
        for (Detection detection : basicDataSet.getDetections()) {
            writeColumns(detectionsWriter, simulationCounter,
                    detectionNumber++,
                    pretty(detection.getDetectingNode()),
                    pretty(detection.getCutOffLink()),
                    pretty(detection.getCycle()));
            if (spPolicyDataSet != null) {
                appendColumn(detectionsWriter, (detection.isFalsePositive() ? "Yes" : "No"));
            }
            detectionsWriter.newLine();
        }

        if (gradualDeploymentDataSet != null) {

            // write the deployments table headers

            if (isDeploymentsFileMissingHeaders) {
                isDeploymentsFileMissingHeaders = false;

                writeColumns(deploymentsWriter, "Simulation", "Deployed Nodes");

                deploymentsWriter.newLine();
            }

            // write the deployments table data

            String deployedNodes = gradualDeploymentDataSet.getDeployedNodes().stream()
                    .map(Node::toString)
                    .collect(Collectors.joining(", "));

            writeColumns(deploymentsWriter, simulationCounter, deployedNodes);
            deploymentsWriter.newLine();
        }

    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private Helper Methods to get a CSV printer for each type of output file
     *
     *  They all return a file printer for the respective file type
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private CSVPrinter getSummaryFilePrinter() throws IOException {
        return getFilePrinter(getFile("summary"), false);
    }

    private CSVPrinter getCountsFilePrinter() throws IOException {
        return getFilePrinter(getFile("counts"), true);
    }

    private CSVPrinter getDetectionsFilePrinter() throws IOException {
        return getFilePrinter(getFile("detections"), true);
    }

    private CSVPrinter getDeploymentsFilePrinter() throws IOException {
        return getFilePrinter(getFile("deployments"), true);
    }

    /**
     * Base method to get a CSV printer for any file. All other "get printer" methods call this base method.
     *
     * @param file      file to associate with the printer.
     * @param append    true to open the file in append mode and false to truncate the file.
     * @return a new instance of a CSV printer associated with the given file.
     * @throws IOException if fails to open the file.
     */
    private static CSVPrinter getFilePrinter(File file, boolean append) throws IOException {
        return new CSVPrinter(new FileWriter(file, append), CSVFormat.EXCEL.withDelimiter(DELIMITER));
    }

    /**
     * Appends the given tag to the end of the base output filename and returns the result file. Keeps original file
     * extension.
     *
     * @param tag   tag to add to the base output file.
     * @return file with the class name associated to its name.
     */
    private File getFile(String tag) {
        String extension = FilenameUtils.getExtension(baseOutputFile.getName());

        // append the tag to the original file name (keep the extension)
        String filename = FilenameUtils.getBaseName(baseOutputFile.getName()) + String.format("-%s.%s", tag, extension);

        return new File(baseOutputFile.getParent(), filename);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Set of helper methods to display any element like Nodes, Links, Paths, etc in a prettier
     *  format then its standard toString() result.
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private static String pretty(Node node) {
        return String.valueOf(node.getId());
    }

    private static String pretty(Link link) {
        return link.getSource().getId() + " → " + link.getDestination().getId();
    }

    private static String pretty(Path path) {
        List<Integer> pathNodesIds = path.stream()
                .map(Node::getId)
                .collect(Collectors.toList());

        return StringUtils.join(pathNodesIds.iterator(), " → ");
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private Helper Methods
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Writes a sequence of columns in the CSV format.
     *
     * @param writer      writer used to write columns.
     * @param firstColumn first column to write.
     * @param columns     following columns to write
     * @throws IOException
     */
    private void writeColumns(BufferedWriter writer, Object firstColumn, Object... columns) throws IOException {
        writer.write(firstColumn.toString());

        for (Object column : columns) {
            appendColumn(writer, column);
        }
    }

    private void appendColumn(BufferedWriter writer, Object column) throws IOException {
        writer.write(COMMA + column.toString());
    }

}
