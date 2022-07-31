package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import extensibleclustering.plugins.PluginType;
import extensibleclustering.plugins.PluginTypeFactory;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing and development of the PluginTypeFactory class.
 * This class will return the correct plug-in type depending on the interface that is 
 * implemented. 
 * @author David Cook
 */
public class TestPluginTypeFactory {
  
  PluginTypeFactory testFactory;
  
  @Before
  public void setUp() {
    testFactory = new PluginTypeFactory();
  }
  
  /**
   * Test to try and get the correct PluginType from the Factory.
   * This should return the ParserType.
   */
  @Test
  public void testGetParserType() {
    assertEquals("Could not get the correct plugin type for parser", 
        "ParserType", testFactory.getPluginType("Parser").getClass().getSimpleName());
  }
  
  /**
   * Test to try and see if it is the same instance returned.
   * The memory address should be the same.
   */
  @Test
  public void testGetSameObject() {
    PluginType objectA = testFactory.getPluginType("Parser");
    PluginType objectB = testFactory.getPluginType("Parser");
    assertTrue("Could not determine that the ParserPlugin is a Singleton",
        objectA == objectB);
  }
  
  /**
   * Test to try and get the correct PluginType from the factory
   * when requesting a 'Clustering' type.
   * This should return the ClusteringAlgorithmType.
   */
  @Test
  public void testGetClusteringType() {
    assertEquals("Could not get the correct plugin type for clustering algorithm",
        "ClusteringAlgorithmType", 
        testFactory.getPluginType("ClusteringAlgorithm").getClass().getSimpleName());
  }
  
  /**
   * Test to try and see if the same instance of the clustering algorithm type is returned.
   * This should be the case as it should follow the singleton pattern.
   */
  @Test
  public void testGetSameClustering() {
    PluginType objectA = testFactory.getPluginType("ClusteringAlgorithm");
    PluginType objectB = testFactory.getPluginType("ClusteringAlgorithm");
    assertTrue("Could not get the same copy of clustering algorithm type from Plugin type factory",
        objectA == objectB);
  }
  
  /**
   * Test to try and get the get the correct PluginType from the factory
   * when requesting a visualisation method. 
   * This should return the VisualisationMethodType.
   */
  @Test
  public void testGetVisualisationType() {
    assertEquals("Could not get the visualisation method type when requested",
        "VisualisationMethodType",
        testFactory.getPluginType("Visualisation").getClass().getSimpleName());
  }
  
  /**
   * Test to try and get the same instance of the visualisation method type to be returned.
   * This needs to be the case to follow the singleton pattern.
   */
  @Test
  public void testGetSameVisualisation() {
    PluginType objectA = testFactory.getPluginType("Visualisation");
    PluginType objectB = testFactory.getPluginType("Visualisation");
    assertTrue("Could not get the same object for the visualisation type",
        objectA == objectB);
  }
}
