package extensibleclustering.plugins;

/**
 * Factory for returning the correct PluginType depending on the
 * interface that is given to the getPluginType method.
 * @author David Cook
 */
public class PluginTypeFactory {
  
  private ParserType parserType = new ParserType();
  private ClusteringAlgorithmType clusteringType = new ClusteringAlgorithmType();
  private VisualisationMethodType visualisationType = new VisualisationMethodType();

  /**
   * Gets the correct plug-in type for the given interface name. 
   * @param simpleInterfaceName - the simple name of the interface to get the plug-in type for.
   * @return - Plug-in type for the given interface, null if not found.
   */
  public PluginType getPluginType(String simpleInterfaceName) {
    switch (simpleInterfaceName)  {
      case "Parser":
        return parserType;
      case "ClusteringAlgorithm":
        return clusteringType;
      case "Visualisation":
        return visualisationType;
      default:
        return null;
    }
  }

}
