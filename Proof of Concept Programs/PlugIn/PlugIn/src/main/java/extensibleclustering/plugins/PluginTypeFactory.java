package extensibleclustering.plugins;

/**
 * Factory for returning the correct PluginType depending on the
 * interface that is given to the getPluginType method.
 * @author David Cook
 */
public class PluginTypeFactory {
  
  private ParserType parserType = new ParserType();

  /**
   * Gets the correct plug-in type for the given interface name. 
   * @param simpleInterfacename - the simple name of the interface to get the plug-in type for.
   * @return - Plug-in type for the given interface.
   */
  public PluginType getPluginType(String simpleInterfacename) {
    return parserType;
  }

}
