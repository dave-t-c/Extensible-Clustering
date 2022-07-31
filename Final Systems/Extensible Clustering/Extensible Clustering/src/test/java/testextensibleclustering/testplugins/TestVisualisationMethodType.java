package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import extensibleclustering.plugins.ImportedPlugins;
import extensibleclustering.plugins.VisualisationMethodType;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing and development of 'VisualisationMethodType' that will work
 * alongside the PluginTypeFactory.
 * @author David Cook
 */
public class TestVisualisationMethodType {
  
  private ImportedPlugins testPlugins;
  private VisualisationMethodType testType;
  
  @Before
  public void setUp() throws Exception {
    testPlugins = new ImportedPlugins();
    testType = new VisualisationMethodType();
  }
  
  /**
   * Test to try and store a visualisation method, and check the number
   * of imported visualisation plug-ins.
   * This should be 1.
   */
  @Test
  public void testAddVisualisationMethod() {
    testType.storePlugin(testPlugins, "Example", Integer.class);
    assertEquals("Could not add visualisation method using VisualisationMethodType",
        1, testPlugins.getTotalNumOfVisualisationMethods());
  }
  
  /**
   * Test to try and store a visualisation method with a different name.
   * This name should be contained in the key set.
   */
  @Test
  public void testImportedDifferentName() {
    testType.storePlugin(testPlugins, "Diff", Integer.class);
    assertTrue("Could not find the given name in imported visualisation plugin",
        testPlugins.getImportedVisualisationMethods().keySet().contains("Diff"));
  }
  
  /**
   * Test to try and add a different class to the plug-ins.
   * This should be retrieved when using the name key given.
   */
  @Test
  public void testImportDifferentClass() {
    testType.storePlugin(testPlugins, "Example", String.class);
    assertEquals("Could not import a different visualisation class",
        String.class, testPlugins.getImportedVisualisationMethods().get("Example"));
  }
  
  /**
   * Test to try and store a null imported plug-ins.
   * This should not be added and the num of imported plugins should be 0. 
   */
  @Test
  public void testNullImportedPlugins() {
    testType.storePlugin(null, "Example", Integer.class);
    assertTrue("Could not handle a null imported plugin as expected",
        testPlugins.getImportedVisualisationMethods().isEmpty());
  }
  
  /**
   * Test to try and store a null name. 
   * This should not be added and the map should be empty.
   */
  @Test
  public void testNullName() {
    testType.storePlugin(testPlugins, null, Integer.class);
    assertTrue("Could not handle null name as expected",
        testPlugins.getImportedVisualisationMethods().isEmpty());
  }
  
  /**
   * Test to try and store a null class.
   * This should not be added and the map should be empty.
   */
  @Test
  public void testNullClass() {
    testType.storePlugin(testPlugins, "Example", null);
    assertTrue("Could not handle null class as expected",
        testPlugins.getImportedVisualisationMethods().isEmpty());
  }
}
