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
   * @param importedPlugins - The ImportedPlugins to store the class in.
   * @param pluginName - The name of the plugin to store the class under.
   * @param plugin - the class to store.
   * @throws Exception - May be thrown by the storePlugin method, e.g. a map entry returns null.
   */
  public void storePlugin(ImportedPlugins importedPlugins, String pluginName, Class<?> plugin)
      throws Exception;

}
