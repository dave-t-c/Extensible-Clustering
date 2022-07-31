package extensibleclustering.application;

import extensibleclustering.plugins.ImportedPlugins;
import java.io.File;
import java.util.Set;
import javafx.stage.Stage;

/**
 * Interface that will need to be implemented by parser views.
 * @author David Cook
 */
public interface StandardViewInterface {

  /**
   * Returns the file that has been selected.
   * @return - File that has been selected by the user.
   */
  public File getSelectedFile();
  
  /**
   * Sets the result from any performed operation.
   * @param result - The result of the operation that has occured.
   */
  public void setOutputLabel(String result);
  
  /**
   * Sets the set of parsers that can be selected.
   * @param parsersSet - Set to update the plugins with.
   */
  public void setParserPluginsSet(Set<String> parsersSet);
  
  /**
   * Sets the set of clustering algorithms that can be selected.
   * @param clusterSet - Set to update the clustering set with.
   */
  public void setClusteringPluginSet(Set<String> clusterSet);
  
  /**
   * Sets the set of visualisation methods that can be selected.
   * @param visualisationSet - Set to update the visualisation set with.
   */
  public void setVisualisationPluginSet(Set<String> visualisationSet);
  
  /**
   * Gets the set of parsers being used by the view.
   * @return Set of names of the parsers.
   */
  public Set<String> getParserPluginsSet();
  
  public Set<String> getClusteringPluginSet();
  
  /**
   * This sets the imported plugins that the UI will use, e.g. for file filters 
   * when selecting a file.
   * @param importedPlugins - THe imported plugins to set in the combo boxes.
   */
  public void setImportedPlugins(ImportedPlugins importedPlugins);
  
  /**
   * Returns the name of the parser selected for use.
   * @return - Name of the parser that will be used for parsing.
   */
  public String getSelectedParserName();
  
  /**
   * Returns the name of the clustering algorithm selected.
   * @return - Name of the clustering algorithm that has been selected.
   */
  public String getSelectedClusteringAlgoName();
  
  /**
   * Returns the visualisation method selected by the user.
   * @return - Name of the visualisation method selected.
   */
  public String getSelectedVisualisationMethod();
  
  /**
   * Sets the method that will observe the parse button for changes.
   * @param parseObserver - The method to observe when the action is called.
   */
  public void addParserFileObserver(StringObserver parseObserver);
  
  /**
   * Specifies the method to be used when the plug-ins need to be updated or reloaded.
   * @param updateObserver - Method to call when this action occurs.
   */
  public void addUpdateParsersObserver(ImportedPluginObserver updateObserver);
  
  /**
   * Sets the method that will be called when data needs to be clustered.
   * @param clusteringObserver - Method to be called when data needs to be clustered.
   */
  public void addClusterDataObserver(StringObserver clusteringObserver);
  
  /**
   * Sets the method that will be called when data needs to be visualised.
   * @param visualisationObserver - Method to call when dat aneeds to be visualised.
   */
  public void addVisualisationObserver(SceneObserver visualisationObserver);
  
  /**
   * Method for specifying the stage of the parent window.
   * This is required for using file choosers.
   * @param newParent - The parent stage of this view.
   */
  public void setParentStage(Stage newParent);
  
  /**
   * Method that is invoked where the actions for the buttons can be set.
   */
  public void setButtonActions();
  
}
