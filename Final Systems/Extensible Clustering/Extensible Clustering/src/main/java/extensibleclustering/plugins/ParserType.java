package extensibleclustering.plugins;

import java.lang.reflect.Method;
import java.util.ArrayList;

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
  public void storePlugin(ImportedPlugins importedPlugins, String pluginName, 
      Class<?> plugin) throws Exception {
    if (importedPlugins == null || pluginName == null || plugin == null) {
      return;
    }
    importedPlugins.getImportedParsers().put(pluginName, plugin);
    
    Object instance = plugin.newInstance();
    Method getSupportedFileExtensions = plugin.getMethod("getSupportedFileExtensions");
    //The returned type of array list should be a String to follow the interface method.
    //The warnings are suppressed due to the list only supporting strings instead of a generic
    //due to the way the file chooser will have to use the list.
    @SuppressWarnings("unchecked")
    ArrayList<String> supportedFiles = (ArrayList<String>)
        getSupportedFileExtensions.invoke(instance);
    importedPlugins.getParserSupportedFileTypes().put(pluginName, supportedFiles);
  }

}
