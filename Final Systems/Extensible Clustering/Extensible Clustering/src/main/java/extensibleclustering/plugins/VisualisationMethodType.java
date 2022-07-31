package extensibleclustering.plugins;

/**
 * Class for storing a visualisation plug-in. 
 * This will work with the PluginTypeFactory.
 * @author David Cook
 */
public class VisualisationMethodType implements PluginType {

  @Override
  public void storePlugin(ImportedPlugins importedPlugins, String pluginName, Class<?> plugin) {
    if (importedPlugins == null || pluginName == null || plugin == null) {
      return;
    }
    importedPlugins.getImportedVisualisationMethods().put(pluginName, plugin);    
  }

}
