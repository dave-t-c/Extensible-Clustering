package extensibleclustering.plugins;

/**
 * Class for modifying the parsers in the ImportedPlugins type.
 * @author David Cook
 */
public class ParserType implements PluginType {

  /**
   * Stores the parser plug-in class that is given.
   * This method modifies the ImportedParsers map in the ImportedPlugins class. 
   */
  @Override
  public void storePlugin(ImportedPlugins importedPlugins, String pluginName, Class<?> plugin) {
    if(importedPlugins == null || pluginName == null || plugin == null) {
      return;
    }
    importedPlugins.getImportedParsers().put(pluginName, plugin);
  }

}
