package extensibleclustering.plugins;

/**
 * Interface that will need to be implemented for each 
 * type of plug-in.
 * These will modify the relevant map in the ImportedPlugin class.
 * @author David Cook
 */
public interface PluginType {
  
  /**
   * This method will add the class to the correct map in the ImportedPlugin class.
   * @param plugin - the class to store.
   */
  public void storePlugin(ImportedPlugins importedPlugins, String pluginName, Class<?> plugin);

}
