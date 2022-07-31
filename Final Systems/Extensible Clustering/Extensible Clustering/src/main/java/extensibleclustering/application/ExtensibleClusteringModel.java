package extensibleclustering.application;

import extensibleclustering.dependencies.DirectoryHelper;
import extensibleclustering.dependencies.Position;
import extensibleclustering.plugins.ImportedPlugins;
import extensibleclustering.plugins.PluginExecutor;
import extensibleclustering.plugins.PluginLoader;
import java.io.File;
import java.nio.file.Path;
import javafx.scene.Scene;

public class ExtensibleClusteringModel {
  
  DirectoryHelper dirHelper;
  ImportedPlugins importedPlugins;
  PluginLoader pluginLoader;
  PluginExecutor execPlugin;
  
  /**
   * Constructor for the ExtensibleClusteringModel.
   * This creates the required support classes and loads the plug-ins into the system.
   * If the directories for the output / plug-ins folder do not exist, the load plug-ins 
   * class will create them (needs to create the plug-in folder to read from).
   */
  public ExtensibleClusteringModel() {
    dirHelper = new DirectoryHelper();
    pluginLoader = new PluginLoader();
    importedPlugins = pluginLoader.loadPlugins();
    execPlugin = new PluginExecutor();
  }
  
  /**
   * Returns the imported plug-ins imported to the model.
   * @return - ImportedPlugins, plug-ins that have been imported to the system.
   */
  public ImportedPlugins getImportedPlugins() {
    return importedPlugins;
  }
  
  /**
   * Import the plug-ins and update the importedPlugins object.
   * This method could be used to update the plug-ins after the user may 
   * have added more to the plug-in directory.
   */
  public void importPlugins() {
    importedPlugins = pluginLoader.loadPlugins();
  }
  
  /**
   * Method that parses a specified file using a specified parser.
   * @param parserName - Name of the parser that has been imported to use.
   * @param fileToParse - File to parsed.
   * @return - Position[] - Positions that have been parsed.
   * @throws Exception - May be thrown by the plugin, exception type unknown.
   */
  public Position[] parseFile(String parserName, File fileToParse) throws Exception {
    if (parserName == null || fileToParse == null 
        || !importedPlugins.getImportedParsers().containsKey(parserName)) {
      throw new IllegalArgumentException("An Illegal argument was provided. "
          + "This may be due to the file not existing or the parser not existing");
    }
    return execPlugin.execParser(fileToParse, 
          importedPlugins.getImportedParsers().get(parserName));
  }
  
  /**
   * Clusters data with the data, filename and clustering algorithm provided.
   * @param data - The data to cluster.
   * @param fileName - The file name the data was parsed from.
   * @param clusteringAlgorithmName - The name of the clustering algorithm to use.
   * @return - Path - The path of the resultant file from clustering.
   * @throws Exception - May be thrown from the clustering algorithm or due to an illegal argument.
   */
  public Path clusterData(Position[] data, String fileName, 
      String clusteringAlgorithmName) throws Exception {
    if (data == null || fileName == null || clusteringAlgorithmName == null 
        || !importedPlugins.getImportedClusteringAlgorithms()
        .containsKey(clusteringAlgorithmName))  {
      throw new IllegalArgumentException("An Illegal argument was provided. "
          + "This occured because the data, file name or clustering algorothm were not provided");
    }
    return execPlugin.execClustering(data, fileName, 
        importedPlugins.getImportedClusteringAlgorithms().get(clusteringAlgorithmName));
  }
  
  /**
   * Visualises the output file provided with the visualisation method given.
   * @param outputFile - Output file to visualise.
   * @param visualisationMethodName - Visualisation method to use.
   * @return - Scene created by the visualisation method.
   * @throws Exception - May be thrown by the visualisation method or due to an illegal argument.
   */
  public Scene visualiseData(File outputFile, String visualisationMethodName) throws Exception {
    if (outputFile == null || visualisationMethodName == null 
        || !importedPlugins.getImportedVisualisationMethods()
        .containsKey(visualisationMethodName)) {
      throw new IllegalArgumentException("An Illegal argument was provideed. "
          + "This may be due to the file given or visualisation method specified not existing");
    }
    return execPlugin.execVisualisation(outputFile.toPath(), 
        importedPlugins.getImportedVisualisationMethods().get(visualisationMethodName));
  }

}
