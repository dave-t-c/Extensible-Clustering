package extensibleclustering.plugins;

import java.util.TreeMap;

/**
 * Class for storing the plug-ins that are imported into the system.
 * @author David Cook 
 */
public class ImportedPlugins {
  
  TreeMap<String, Class<?>> importedParsers;
  
  /**
   * Creates a new ImportedPlugin type.
   * This initialises all of the required tree maps.
   */
  public ImportedPlugins() {
    importedParsers = new TreeMap<>();
  }
  
  /**
   * Returns the Parser plug-ins that have been imported into the system.
   * @return TreeMap - The imported parser plug-ins sorted by their name in alphabetical order.
   */
  public TreeMap<String, Class<?>> getImportedParsers(){
    return importedParsers;
  }
  
  /**
   * Method that returns a hash code for the ImportedPlugin.
   * @return - int, hash code for this ImportedPlugin.
   */
  @Override
  public int hashCode() {
    return importedParsers.hashCode();
  }
  
  /**
   * Checks to see if an object is equal to this ImportedPlugins instance.
   * @return - boolean, result of the equality test between this instance and the obj given.
   */
  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }
    ImportedPlugins diffImport = null;
    try {
      diffImport = (ImportedPlugins) obj;      
    } catch (ClassCastException ex){
      return false;
    }
    return importedParsers.equals(diffImport.getImportedParsers());
  }
  
  /**
   * Returns the total number of parsers that have been imported.
   * @return - int, total number of parsers imported.
   */
  public int getTotalNumOfParsers() {
    return importedParsers.keySet().size();
  }

  /**
   * Returns the total number of plug-ins that have been imported.
   * @return int, total number of plug-ins imported into the system.
   */
  public int getTotalNumOfPlugins() {
    return getTotalNumOfParsers();
  }

}
