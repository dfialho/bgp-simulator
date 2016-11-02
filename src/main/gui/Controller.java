package main.gui;

import core.Protocol;
import io.networkreaders.GraphvizReaderFactory;
import io.reporters.CSVReporterFactory;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import main.ProgressHandler;
import main.SimulatorLauncher;
import main.SimulatorParameters;
import main.gui.basics.NumberSpinner;
import main.gui.fulldeployment.FullDeploymentController;
import main.gui.gradualdeployment.GradualDeploymentController;
import main.gui.radiobuttons.ProtocolToggleGroup;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Simulator launcher with the error handler implemented for the GUI interface
     *  Note the error handler takes advantage of the GUI interface to display graphical alerts
     *  to the user
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private final SimulatorLauncher simulatorLauncher = new SimulatorLauncher(
            new GUIErrorHandler(), new ProgressHandler() {});   // no support for progress right now

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Controls
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public GridPane pane;
    public TextField networkTextField;
    public Button startButton;
    public Spinner<Integer> destinationIdSpinner;
    public Spinner<Integer> repetitionsSpinner;
    public Spinner<Integer> minDelaySpinner;
    public Spinner<Integer> maxDelaySpinner;
    public FullDeploymentController fullDeploymentFormController;
    public GradualDeploymentController gradualDeploymentFormController;
    public CheckBox debugCheckBox;
    public ProtocolToggleGroup detectionGroup;
    private FileChooser fileChooser = new FileChooser();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Initializer
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // ensure the start button is disabled when the text field is empty
        networkTextField.textProperty().addListener((observable, oldText, newText) -> {
            startButton.setDisable(newText.isEmpty());
        });

        NumberSpinner.setupNumberSpinner(destinationIdSpinner, 0, Integer.MAX_VALUE, 0);
        NumberSpinner.setupNumberSpinner(repetitionsSpinner, 1, Integer.MAX_VALUE, 1);
        NumberSpinner.setupNumberSpinner(minDelaySpinner, 0, Integer.MAX_VALUE, 0);
        NumberSpinner.setupNumberSpinner(maxDelaySpinner, 0, Integer.MAX_VALUE, 10);

        // enforces the minimum delay is never higher than the maximum delay
        minDelaySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue > maxDelaySpinner.getValue()) {
                maxDelaySpinner.getValueFactory().setValue(newValue);
            }
        });

        // enforces the maximum delay is never lower than the minimum delay
        maxDelaySpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue < minDelaySpinner.getValue()) {
                minDelaySpinner.getValueFactory().setValue(newValue);
            }
        });

        // accept only *.gv file in the file chooser
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Network files (*.gv)", "*.gv"));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Action methods - Invoked when the user interacts with the interface
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Handles browsing for topology files. Opens up a file chooser for the user to select the topology files to
     * simulate.
     */
    public void handleClickedBrowseButton(ActionEvent actionEvent) {
        List<File> files = fileChooser.showOpenMultipleDialog(pane.getScene().getWindow());

        if (files != null) {
            List<String> filePaths = files.stream()
                                             .filter(File::exists)
                                             .map(File::getAbsolutePath)
                                             .collect(Collectors.toList());

            networkTextField.setText(String.join(" ; ", filePaths));
        }
    }

    /**
     * Starts the simulation according to the current configuration inputted by the user.
     */
    public void handleClickedStartButton(ActionEvent actionEvent) {

        for (String networkFilePath : networkTextField.getText().split(" ; ")) {
            simulate(new File(networkFilePath));
        }

    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *  Private helper methods
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Simulates each topology file.
     *
     * @param topologyFile topology file to be simulated.
     */
    private void simulate(File topologyFile) {

        Protocol protocol = detectionGroup.getSelectedProtocol();

        Integer deployPeriod = null;
        Integer deployPercentage = null;
        Integer deployTime = null;

        //
        // Choose the correct simulator factory based on the user input
        //
        if (fullDeploymentFormController.activateToggle.isSelected()) {
            deployTime = fullDeploymentFormController.detectingTimeSpinner.getValue();

        } else if (gradualDeploymentFormController.activateToggle.isSelected()) {
            deployPeriod = gradualDeploymentFormController.deployPeriodSpinner.getValue();
            deployPercentage = gradualDeploymentFormController.deployPercentageSpinner.getValue();
        }

        // report filename keeps the topology file name - the extension will be defined by the reporter
        simulatorLauncher.setParameters(new SimulatorParameters.Builder(topologyFile, topologyFile)
                .readerFactory(new GraphvizReaderFactory())
                .minDelay(minDelaySpinner.getValue())
                .maxDelay(maxDelaySpinner.getValue())
                .destinationId(destinationIdSpinner.getValue())
                .repetitionCount(repetitionsSpinner.getValue())
                .protocol(protocol)
                .timedDeployment(deployTime)
                .gradualDeployment(deployPeriod, deployPercentage)
                .reporterFactory(new CSVReporterFactory())
                .build());

        simulatorLauncher.launch();

    }

}
