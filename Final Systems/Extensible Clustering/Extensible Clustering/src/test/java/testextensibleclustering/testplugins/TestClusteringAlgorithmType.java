package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import extensibleclustering.plugins.ClusteringAlgorithmType;
import extensibleclustering.plugins.ImportedPlugins;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing and development of the 'ClusteringAlgorithmType' that will be used
 * by the plug-in type factory.
 * @author David Cook
 */
public class TestClusteringAlgorithmType {

  ImportedPlugins testPlugins;
  ClusteringAlgorithmType testAlgoType;
  
  /**
   * Setup the required variables for the tests.
   */
  @Before
  public void setUp() {
    testPlugins = new ImportedPlugins();
    testAlgoType = new ClusteringAlgorithmType();
  }
  
  /**
   * Test to try and add a Clustering algorithm using the 
   * ClusteringAlgoType. This should make the size of the key set in the 
   * test plugins 1.
   */
  @Test
  public void testAddClusteringPlugin() {
    testAlgoType.storePlugin(testPlugins, "Example", Integer.class);
    assertEquals("Could not get the correct num of clustering algo plugins after adding 1",
        1, testPlugins.getTotalNumOfClusteringAlgos());
  }
  
  /**
   * Test to try and add a clustering algorithm with a different name.
   * This name should be in the key set.
   */
  @Test
  public void testAddDifferentName() {
    testAlgoType.storePlugin(testPlugins, "Diff", Integer.class);
    assertTrue("Could not add a different name to algo type.",
        testPlugins.getImportedClusteringAlgorithms().keySet().contains("Diff"));
  }
  
  /**
   * Test to try and add a different class.
   * This should be retrieved when the key is used.
   */
  @Test
  public void testGetDiffClass() {
    testAlgoType.storePlugin(testPlugins, "Example", String.class);
    assertEquals("Could not get the expected class from the algo type",
        String.class, testPlugins.getImportedClusteringAlgorithms().get("Example"));
  }
  
  /**
   * Test to try and store a plugin with a null ImportedPlugin.
   * This should not throw a Null Pointer Exception.
   */
  @Test
  public void testStorePluginNullImported() {
    testAlgoType.storePlugin(null, "Example", Integer.class);
  }
  
  /**
   * Test to try and store a plugin with a null name.
   * This should not be stored and the number of keys should be zero.
   */
  @Test
  public void testStoreNullKey() {
    testAlgoType.storePlugin(testPlugins, null, Integer.class);
    assertEquals("Could not handle a null key as expected with algo type",
        0, testPlugins.getImportedClusteringAlgorithms().keySet().size());
  }
  
  /**
   * Test to try and store a plugin with a null class.
   * This should not be added and the key set should be empty.
   */
  @Test
  public void testSotreNullClass() {
    testAlgoType.storePlugin(testPlugins, "Example", null);
    assertEquals("Could not handle null class as expected with algo type",
        0, testPlugins.getImportedClusteringAlgorithms().keySet().size());
  }
  
}
