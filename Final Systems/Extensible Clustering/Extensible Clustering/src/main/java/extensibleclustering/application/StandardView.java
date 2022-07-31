package extensibleclustering.application;

import extensibleclustering.plugins.ImportedPlugins;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Implements the standard view that will be displayed to the user.
 * @author David Cook
 */
public class StandardView implements StandardViewInterface {
  
  private File selectedFile;
  private Stage parentWindow;
  private HashMap<Button, Boolean> prevStates = new HashMap<>();
  private Set<String> parserNames = new HashSet<>();
  private Set<String> clusteringNames = new HashSet<>();
  private Set<String> visualisationNames = new HashSet<>();
  private ImportedPlugins lastImportedPlugins;
  
  @FXML
  private ComboBox<String> parserDropDown;
  
  @FXML
  private ComboBox<String> clusteringDropDown;
  
  @FXML
  private ComboBox<String> visualisationDropDown;

  @FXML
  private Button selectBtn;

  @FXML
  private Label fileUsedLabel;

  @FXML
  private Label outputLabel;

  @FXML
  private Button parseBtn;
  
  @FXML
  private Button reloadBtn;
  
  @FXML
  private Button clusterBtn;
  
  @FXML
  private Button visualisationBtn;
  
  @FXML
  private ProgressIndicator progressIndicator;
  
  private static StandardView instance = new StandardView();
  
  public static StandardView getInstance() {
    return instance;
  }
  
  /**
   * Sets the actions for the buttons in this parser view.
   */
  public void setButtonActions() {
    prevStates.put(selectBtn, false);
    prevStates.put(parseBtn, false);
    prevStates.put(reloadBtn, false);
    prevStates.put(clusterBtn, false); 
    prevStates.put(visualisationBtn, false);
    selectBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        //Open a file dialog and select a file.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to parse");
        File fileSelected = fileChooser.showOpenDialog(parentWindow);
        if (fileSelected != null) {
          selectedFile = fileSelected;
          fileUsedLabel.setText(selectedFile.getName());
        }
      }
    });
  }
  
  @Override
  public void setImportedPlugins(ImportedPlugins importedPlugins) {
    lastImportedPlugins = importedPlugins;
    setParserPluginsSet(lastImportedPlugins.getImportedParsers().keySet());
    setClusteringPluginSet(lastImportedPlugins.getImportedClusteringAlgorithms().keySet());
    setVisualisationPluginSet(lastImportedPlugins.getImportedVisualisationMethods().keySet());
  }
  
  @Override
  public void setParentStage(Stage newParent) {
    parentWindow = newParent;
  }
 
  @Override
  public File getSelectedFile() {
    return selectedFile;
  }
  
  public void setSelectedFile(File updatedSelection) {
    selectedFile = updatedSelection;
  }

  @Override
  public void setOutputLabel(String result) {
    outputLabel.setText(result);
  }

  @Override
  public void setParserPluginsSet(Set<String> parsersSet) {
    parserNames = parsersSet;
    parserDropDown.setItems(FXCollections
        .observableList(new ArrayList<String>(parserNames)));
    
    setDropDownTooltips(parserDropDown);
  }
  
  @Override 
  public void setClusteringPluginSet(Set<String> clusteringSet) {
    clusteringNames = clusteringSet;
    clusteringDropDown.setItems(FXCollections
        .observableList(new ArrayList<String>(clusteringNames)));
    
    setDropDownTooltips(clusteringDropDown);
  }
  
  @Override
  public void setVisualisationPluginSet(Set<String> visualisationSet) {
    visualisationNames = visualisationSet;
    visualisationDropDown.setItems(FXCollections
        .observableList(new ArrayList<String>(visualisationNames)));
    
    setDropDownTooltips(visualisationDropDown);
  }
  
  /**
   * Sets the description tool tips for a given combo box.
   * @param cb - ComboBox to add the Tooltips to.
   */
  private void setDropDownTooltips(ComboBox<String> cb) {
    cb.setCellFactory(param -> {
      return new ListCell<String>() {
        
        @Override
        public void updateItem(String item, boolean empty) {
          super.updateItem(item, empty);
          
          if (item != null) {
            setText(item);
            String foundDescription = lastImportedPlugins.getImportedPluginDescriptions().get(item);
            foundDescription = foundDescription == null
                ? "No description provided" : foundDescription;
            setTooltip(new Tooltip(foundDescription));
            
          }
        }
      };
    });
  }
  
  @Override
  public Set<String> getParserPluginsSet() {
    return parserNames;
  }
  
  @Override
  public Set<String> getClusteringPluginSet() {
    return clusteringNames;
  }

  /**
   * Returns the selected parser name from the drop down box.
   */
  @Override
  public String getSelectedParserName() {
    return parserDropDown.getSelectionModel().getSelectedItem();
  }
  
  @Override
  public String getSelectedClusteringAlgoName() {
    return clusteringDropDown.getSelectionModel().getSelectedItem();
  }
  
  @Override
  public String getSelectedVisualisationMethod() {
    return visualisationDropDown.getSelectionModel().getSelectedItem();
  }

  /**
   * Handles the parser btn event by calling the method in the controller.
   * This also requires the use of a task to not run the parsing on the 
   * UI thread.
   */
  @Override
  public void addParserFileObserver(StringObserver parseObserver) {
    
    parseBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Task<String> parserTask = new Task<String>() {

          @Override
          protected String call() throws Exception {
            return parseObserver.callMethod();
          }
        };
        
        parserTask.setOnRunning((successEvent) -> {
          parseBtn.setDisable(true);
          outputLabel.setText("Parsing file...");
          progressIndicator.setVisible(true);
        });
        
        parserTask.setOnSucceeded((succeededEvent) -> {
          progressIndicator.setVisible(false);
          outputLabel.setText(parserTask.getValue());
          parseBtn.setDisable(false);
        });
        
        new Thread(parserTask).start();
      }
    });

  }

  /**
   * Handles reloading the plug-ins into the system.
   * This uses a task so it does not run on the UI thread.
   */
  @Override
  public void addUpdateParsersObserver(ImportedPluginObserver updateObserver) {
    reloadBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        Task<ImportedPlugins> reloadTask = new Task<ImportedPlugins>() {
          @Override
          protected ImportedPlugins call() throws Exception {
            return updateObserver.callMethod();            
          }
        };        
        reloadTask.setOnRunning((successEvent) -> {
          //Iterate through the added buttons, this will store if they are disabled or not.
          for (Button currButton : prevStates.keySet()) {
            prevStates.put(currButton, currButton.isDisabled());
          }
          clusterBtn.setDisable(true);
          parseBtn.setDisable(true);
          reloadBtn.setDisable(true);
          selectBtn.setDisable(true);
          visualisationBtn.setDisable(true);
        });
        
        reloadTask.setOnSucceeded((succeededEvent) -> {
          setImportedPlugins(reloadTask.getValue());
          
          //Clear the selected item by setting the selected item to null
          parserDropDown.getSelectionModel().clearSelection();
          clusteringDropDown.getSelectionModel().clearSelection();
          outputLabel.setText("Successfully reloaded plugins. " 
              + reloadTask.getValue().getTotalNumOfPlugins() + " plugins were loaded.");
          //Iterate through the buttons and set them back to their previous state.
          for (Button currButton : prevStates.keySet()) {
            currButton.setDisable(prevStates.get(currButton));
          }
        });
        new Thread(reloadTask).start();
      }
    });
  }
  
  /**
   * Handles the cluster btn event by calling the specified method.
   * This uses a task so the UI can remain responsive while clustering is 
   * taking place.
   */
  @Override
  public void addClusterDataObserver(StringObserver clusteringObserver) {
    clusterBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        
        Task<String> clusterTask = new Task<String>() {
          @Override
          protected String call() throws Exception {
            return clusteringObserver.callMethod();          
          }
        };
        
        clusterTask.setOnRunning((successEvent) -> {
          clusterBtn.setDisable(true);
          outputLabel.setText("Clustering...");
          progressIndicator.setVisible(true);
        });
        
        clusterTask.setOnSucceeded((succeededEvent) -> {
          progressIndicator.setVisible(false);
          outputLabel.setText(clusterTask.getValue());
          clusterBtn.setDisable(false);
        });
        
        new Thread(clusterTask).start();
        
      }
    });
  }

  @Override
  public void addVisualisationObserver(SceneObserver visualisationObserver) {
    visualisationBtn.setOnAction(new EventHandler<ActionEvent>() {
      String selectedName;
      String selectedFileName;

      @Override
      public void handle(ActionEvent event) {
        Task<Scene> visualisationTask = new Task<Scene>() {

          @Override
          protected Scene call() throws Exception {
            return visualisationObserver.callMethod();
          }
         
        };
        
        visualisationTask.setOnRunning((successEvent) -> {
          selectedName = getSelectedVisualisationMethod() == null 
              ? "No method selected" : getSelectedVisualisationMethod();
          selectedFileName = getSelectedFile() == null
              ? "No file selected" : getSelectedFile().getName();
          visualisationBtn.setDisable(true);
          outputLabel.setText("Visualising...");
          progressIndicator.setVisible(true);
        });
        
        visualisationTask.setOnSucceeded((succeededEvent) -> {
          progressIndicator.setVisible(false);
          Stage visualisedStage = new Stage();
          visualisedStage.setScene(visualisationTask.getValue());
          visualisedStage.setTitle(selectedName + " - " + selectedFileName);
          visualisedStage.show();
          visualisationBtn.setDisable(false);
          outputLabel.setText("Finished Visualisation");
        });
        
        new Thread(visualisationTask).start();
        
      }
    });
  }
}