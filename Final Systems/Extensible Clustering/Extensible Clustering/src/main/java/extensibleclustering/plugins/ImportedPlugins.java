package extensibleclustering.plugins;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * Class for storing the plug-ins that are imported into the system.
 * @author David Cook 
 */
public class ImportedPlugins {
  
  private TreeMap<String, Class<?>> importedParsers;
  private TreeMap<String, Class<?>> importedClusteringAlgorithms;
  private TreeMap<String, Class<?>> importedVisualisationMethods;
  private HashMap<String, List<String>> parserSupportedFileTypes;
  private HashMap<String, String> importedPluginDescriptions;
  
  /**
   * Creates a new ImportedPlugin type.
   * This initialises all of the required tree maps.
   */
  public ImportedPlugins() {
    importedParsers = new TreeMap<>();
    importedClusteringAlgorithms = new TreeMap<>();
    importedVisualisationMethods = new TreeMap<>();
    parserSupportedFileTypes = new HashMap<>();
    importedPluginDescriptions = new HashMap<>();
  }
  
  /**
   * Returns the Parser plug-ins that have been imported into the system.
   * @return TreeMap - The imported parser plug-ins sorted by their name in alphabetical order.
   */
  public TreeMap<String, Class<?>> getImportedParsers() {
    return importedParsers;
  }
  
  /**
   * Returns the imported clustering algorithm plug-ins.
   * @return TreeMap - Imported clustering algorithm plug-ins sorted by name in alphabetical order.
   */
  public TreeMap<String, Class<?>> getImportedClusteringAlgorithms() {
    return importedClusteringAlgorithms;
  }
  
  public HashMap<String, List<String>> getParserSupportedFileTypes() {
    return parserSupportedFileTypes;
  }
  
  /**
   * Returns the imported visualisation method plug-ins.
   * @return TreeMap - Imported visualisation methods sorted by name in alphabetical order.
   */
  public TreeMap<String, Class<?>> getImportedVisualisationMethods() {
    return importedVisualisationMethods;
  }
  
  /**
   * Returns the imported descriptions for the plugins imported.
   * @return HashMap - Uses the name of the plugin as the key.
   */
  public HashMap<String, String> getImportedPluginDescriptions() {
    return importedPluginDescriptions;
  }
  
  /**
   * Method that returns a hash code for the ImportedPlugin.
   * @return - int, hash code for this ImportedPlugin.
   */
  @Override
  public int hashCode() {
    return importedParsers.hashCode() 
        + importedClusteringAlgorithms.hashCode()
        + importedVisualisationMethods.hashCode()
        + parserSupportedFileTypes.hashCode()
        + importedPluginDescriptions.hashCode();
  }
  
  /**
   * Checks to see if an object is equal to this ImportedPlugins instance.
   * @return - boolean, result of the equality test between this instance and the obj given.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    ImportedPlugins diffImport = null;
    try {
      diffImport = (ImportedPlugins) obj;      
    } catch (ClassCastException ex) {
      return false;
    }
    return importedParsers.equals(diffImport.getImportedParsers()) 
        && importedClusteringAlgorithms.equals(diffImport.getImportedClusteringAlgorithms())
        && importedVisualisationMethods.equals(diffImport.getImportedVisualisationMethods())
        && parserSupportedFileTypes.equals(diffImport.getParserSupportedFileTypes())
        && importedPluginDescriptions.equals(diffImport.getImportedPluginDescriptions());
  }
  
  /**
   * Returns the total number of parsers that have been imported.
   * @return - int, total number of parsers imported.
   */
  public int getTotalNumOfParsers() {
    return importedParsers.keySet().size();
  }
  
  /**
   * Returns the total number of imported clustering algorithms.
   * @return - int, total number of clustering algorithms.
   */
  public int getTotalNumOfClusteringAlgos() {
    return importedClusteringAlgorithms.keySet().size();
  }
  
  /**
   * Returns the total number of imported visualisation methods.
   * @return - int, total number of visualisation methods.
   */
  public int getTotalNumOfVisualisationMethods() {
    return importedVisualisationMethods.keySet().size();
  }

  /**
   * Returns the total number of plug-ins that have been imported.
   * @return int, total number of plug-ins imported into the system.
   */
  public int getTotalNumOfPlugins() {
    return getTotalNumOfParsers() + getTotalNumOfClusteringAlgos() 
      + importedVisualisationMethods.size();
  }

}
