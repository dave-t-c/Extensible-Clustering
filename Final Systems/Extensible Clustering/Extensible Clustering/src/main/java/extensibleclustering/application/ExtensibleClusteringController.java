package extensibleclustering.application;

import extensibleclustering.dependencies.Position;
import extensibleclustering.plugins.ImportedPlugins;
import java.io.File;
import java.nio.file.Path;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public final class ExtensibleClusteringController {
  
  private StandardViewInterface standardView;
  private static ExtensibleClusteringModel ecModel;
  private Position[] lastParsedData;
  private Path lastClusterOutput;
  private static ExtensibleClusteringController instance = new ExtensibleClusteringController();
  
  
  public static ExtensibleClusteringController getInstance() {
    ecModel = new ExtensibleClusteringModel();
    return instance;
  }
  
  /**
   * Sets the parser view for the controller to use. This will update the view used in 
   * the controller and set the methods to be used in the view. 
   * @param view - View to be set to the parser view being used.
   * @param primaryStage - The primary stage for the process.
   */
  public void setParserView(StandardViewInterface view, Stage primaryStage) {
    standardView = view;
    standardView.addParserFileObserver(this::parseFile);
    standardView.addUpdateParsersObserver(this::loadPlugins);
    standardView.addClusterDataObserver(this::clusterData);
    standardView.addVisualisationObserver(this::visualiseData);
    standardView.setParentStage(primaryStage);
    standardView.setImportedPlugins(ecModel.importedPlugins);
    standardView.setButtonActions();
  }
  
  /**
   * Parse a specified file in the parser view.
   * This will currently output the results from the parsing in the view.
   * @return - The output to display to the user from the parsing process.
   */
  public String parseFile() {
    File selectedFile = standardView.getSelectedFile();
    String selectedParser = standardView.getSelectedParserName();
    try {
      //Clear last parsed data to allow to check to see if parsing has been successful.
      lastParsedData = null;
      lastParsedData = ecModel.parseFile(selectedParser, selectedFile);
      return "Parsed " + lastParsedData.length + " positions, each with " 
          + lastParsedData[0].getComponents().length + " dimensions";      
    } catch (Exception ex) {
      return getCorrectErrorMessage(ex);
    }
  }
  
  /**
   * Clusters data by using the selected parser and then clustering the data.
   * This will output to the user where the output file for the clustering is,
   * or will output an appropriate error message.
   * @return The result from the clustering process. Is output to user.
   */
  public String clusterData() {
    //Parse the file so it can be used for clustering.
    String parserError = parseFile();
    //Check to make sure the parser has not failed.
    //If last parsed data is null, then the parsing was not successful. 
    if (lastParsedData == null) {
      return parserError;
    }
    try {
      lastClusterOutput = ecModel.clusterData(lastParsedData, 
          standardView.getSelectedFile().getName(), standardView.getSelectedClusteringAlgoName());
      return ("Successfuly completed clustering. Output file can be found at:  "
          + lastClusterOutput.toFile().getName());
      
    } catch (Exception ex) {
      return (getCorrectErrorMessage(ex));
    }
  }
  
  /**
   * Visualises data using the file and the visualisation method selected.
   * @return - Scene created by visualisation method.
   */
  public Scene visualiseData() {
    try {
      return ecModel.visualiseData(standardView.getSelectedFile(), 
          standardView.getSelectedVisualisationMethod());
    } catch (Exception ex) {
      String correctMessage = getCorrectErrorMessage(ex);
      Label errorLabel = new Label(correctMessage);
      errorLabel.setFont(new Font("Arial", 12));
      return new Scene(errorLabel, 625, 300);
    }
  }

  
  /**
   * Re-load the plug-ins into the system.
   * This method will be called when the re-load button is pressed. 
   * This method now has to return the ImportedPlugins, due to issues with JavaFX 
   * having sections of the UI edited from the non-UI thread. 
   * This therefore has to be returned to the UI so it can modify the sections required.
   * @return Imported Plugins loaded by the model.
   */
  public ImportedPlugins loadPlugins() {
    ecModel.importPlugins();
    return ecModel.importedPlugins;
  }
  
  /**
   * Returns the correct error message for the given exception.
   * @param ex - The exception to get the message for.
   * @return - A error message that can be displayed to the user.
   */
  private String getCorrectErrorMessage(Exception ex) {
    if (ex.getClass() == IllegalArgumentException.class) {
      return ex.getMessage();
    } else {
      //Find the cause for the exception, and use the message. 
      //If the error occurs when clustering, the message from the there will be in the cause.
      Throwable cause = ex.getCause();
      return cause == null || cause.getMessage() == null ? "An error occured "
            + "but the plugin did not provide an error message" : cause.getMessage();
    }
  }

}
