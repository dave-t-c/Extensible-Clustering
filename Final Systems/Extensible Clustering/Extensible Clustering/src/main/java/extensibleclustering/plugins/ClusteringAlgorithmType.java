package extensibleclustering.plugins;

/**
 * Modifies the clustering algorithms in a ImportedPlugin class.
 * @author David Cook
 */
public class ClusteringAlgorithmType implements PluginType {

  /**
   * Stores a clustering algorithm plug-in in the imported plug-ins specified.
   * This modifies the map to add the plug-in as an entry using the name provided unless
   * either the name, plug-in or the importedPlugins is null.
   */
  @Override
  public void storePlugin(ImportedPlugins importedPlugins, String pluginName, Class<?> plugin) {
    if (importedPlugins == null || pluginName == null || plugin == null) {
      return;
    }
    importedPlugins.getImportedClusteringAlgorithms().put(pluginName, plugin);
  }

}
