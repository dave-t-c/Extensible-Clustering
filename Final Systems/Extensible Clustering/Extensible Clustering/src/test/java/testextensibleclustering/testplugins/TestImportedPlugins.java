package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import extensibleclustering.plugins.ImportedPlugins;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing and development of the 'ImportedPlugin' class.
 * This will be used for storing the plugins that are imported into
 * the system.
 * @author David Cook
 */
public class TestImportedPlugins {
  
  ImportedPlugins testImported;
  ImportedPlugins diffImported;
  
  @Before
  public void setUp() {
    testImported = new ImportedPlugins();
    diffImported = new ImportedPlugins();
  }
  
  /**
   * Test to try and create a new instance of the imported plug-in type 
   * and get the imported Parsers. This map should have 0 items.
   */
  @Test
  public void testCreateImportedPlugin() {
    assertTrue("Could not get correct number of keys for empty plugins", 
        testImported.getImportedParsers().keySet().size() == 0);
  }
  
  /**
   * Test to try and create a new instance of the imported plug-in type
   * then add a class to the hash map.
   * The hash map when the getImportedParsers method is called again should
   * have a key size of 1.
   */
  @Test
  public void testGetUpdatedImportsForParsers() {
    testImported.getImportedParsers().put("Example", null);
    assertTrue("Could not get correct number of keys after adding to parsers map",
        testImported.getImportedParsers().keySet().size() == 1);
  }
  
  /**
   * Test to try and get equal hash codes for identical ImportedPlugins.
   * These should be the same as they are both empty.
   */
  @Test
  public void testGetEqualHashCodes() {
    assertEquals("Could not get equal hash codes for identical ImportedPlugins",
        testImported.hashCode(), diffImported.hashCode());
  }
  
  /**
   * Test to try and get different hash codes for different Imported Plug-ins.
   * These should return different values.
   */
  @Test
  public void testGetDiffHashCodes() {
    testImported.getImportedParsers().put("Example", null);
    assertTrue("Could not get different hash codes for different ImportedPlugins",
        testImported.hashCode() != diffImported.hashCode());
  }
  
  /**
   * Test to try and see if two identical sets of imported plug-ins are equal.
   * This should return true.
   */
  @Test
  public void testGetEqualIdentical() {
    assertTrue("Could not get equal imported plug-ins to be equal",
        testImported.equals(diffImported));
  }
  
  /**
   * Test to try and see if two different ImportedPlugins are equal.
   * This should return false.
   */
  @Test
  public void testGetUnequalImportedPlugins() {
    testImported.getImportedParsers().put("Example", null);
    assertFalse("Could not different ImportedPlugins to be unequal",
        testImported.equals(diffImported));
  }
  
  /**
   * Test to try and see if a different data type is equal to a 
   * ImportedPlugins type.
   * This should return false but should not throw an exception.
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEqualsDiffClass() {
    assertFalse("Could not handle checking if different class is equal",
        testImported.equals(new String("Example")));
  }
  
  /**
   * Test to try and see if equals method can handle null.
   * This should return false.
   */
  @Test
  public void testNullEquals() {
    assertFalse("Could not handle null being given to equals method",
        testImported.equals(null));
  }
  
  /**
   * Test to try and get the total number of imported parser plug-ins when none are added.
   * This should return 0.
   */
  @Test
  public void testGetTotalNumOfParserPlugins() {
    assertEquals("Could not get the correct num of parser plugins for empty ImportedPlugins",
        0, testImported.getTotalNumOfParsers());
  }
  
  /**
   * Test to try and get the total number of imported parser plug-ins when one has been added.
   * This should return 1.
   */
  @Test
  public void testGetNumOfParsersAdded() {
    testImported.getImportedParsers().put("Example", null);
    assertEquals("Could not get the correct num of parsers when parser added to ImportedPlugins",
        1, testImported.getTotalNumOfParsers());
  }
  
  /**
   * Test to try and get the total number of plugins that have been imported into the system.
   * None have been added so this should be zero.
   */
  @Test
  public void testGetTotalImportedNoneAdded() {
    assertEquals("Could not get correct total num of plugins for empty ImportedPlugins",
        0, testImported.getTotalNumOfPlugins());
  }
  
  /**
   * Test to try and get the total number of plug-ins after one has been added.
   * This should return 1.
   */
  @Test
  public void testGetTotalImported() {
    testImported.getImportedParsers().put("Example", null);
    assertEquals("Could not get the correct total num of imported parsers with one added.",
        1, testImported.getTotalNumOfPlugins());
  }
  
  /**
   * Test to try and get the importedClusteringAlgorithms.
   * This should have an empty key set (size 0).
   */
  @Test
  public void testGetImportedClusteringAlgos() {
    assertTrue("Could not get correct num of keys import clustering algorithms", 
        testImported.getImportedClusteringAlgorithms().keySet().size() == 0);
  }
  
  /**
   * Test to try and add a class to the imported plug-ins and see if the key set size
   * increases.
   * This should be 1.
   */
  @Test
  public void testAddImportedClusteringAlgo() {
    testImported.getImportedClusteringAlgorithms().put("Example", null);
    assertTrue("Could not get the correct number of clustering algos after adding one.",
        testImported.getImportedClusteringAlgorithms().keySet().size() == 1);
  }
  
  /**
   * Test to try and add a clustering algo and check the total num of clustering algo plugins.
   * This should be 1.
   */
  @Test
  public void testGetNewTotalNumOfPlugins() {
    testImported.getImportedClusteringAlgorithms().put("Example", null);
    assertEquals("Could not get the correct total num of plugins",
        1, testImported.getTotalNumOfClusteringAlgos());
  }
  
  /**
   * Test to try and get the total num of imported plugins after adding a clustering algo.
   * This should return 1.
   */
  @Test
  public void testGetTotalImportedPluginsWithClustering() {
    testImported.getImportedClusteringAlgorithms().put("Example", null);
    assertEquals("Could not get the correct number of plugins after adding a clustering algo",
        1, testImported.getTotalNumOfPlugins());
  }
  
  /**
   * Test to try and get a different hash code for imported plug-ins with imported clustering
   * algorithm plug-ins.
   * This should be different to imported plugin with none added.
   */
  @Test
  public void testGetDiffHashCodeForClusteringAlgos() {
    testImported.getImportedClusteringAlgorithms().put("Example", null);
    assertTrue("Could nto get different hash codes for different imported plugins",
        testImported.hashCode() != diffImported.hashCode());
  }
  
  /**
   * Test to try and see if two imported plug-ins are equal after one has a clustering 
   * algorithm added.
   * This should return false.
   */
  @Test
  public void testGetUnequalWithDiffClusteringAlgos() {
    testImported.getImportedClusteringAlgorithms().put("Example", null);
    assertFalse("Could not get imported plugins to be unequal with diff imported clustering algos",
        testImported.equals(diffImported));
  }
  
  /**
   * Test to try and get the Supported file types for the imported parsers.
   * As none have been imported, this should return an empty hash map.
   */
  @Test
  public void testGetNoSupportedFileTypes() {
    assertTrue("Could not get empty supported file types from Imported Plugins",
        testImported.getParserSupportedFileTypes().isEmpty());
  }
  
  /**
   * Test to try and get the same instance of the map supported file types.
   * These should have the same memory addresses.
   */
  @Test
  public void testGetSameInstanceSupportedFiles() {
    assertTrue("Could not get the same instance of the reported file types",
        testImported.getParserSupportedFileTypes() == testImported.getParserSupportedFileTypes());
  }
  
  /**
   * Test to try and get a different hash code with a different supported file map.
   */
  @Test
  public void testDiffHashCodeDiffSupportedFiles() {
    testImported.getParserSupportedFileTypes().put("Example", new ArrayList<String>());
    assertTrue("Could not get different hash codes with different parser supported file types map",
        testImported.hashCode() != diffImported.hashCode());
  }
  
  /**
   * Test to try and get two different imported plug-ins with different parser 
   * supported file types to be unequal.
   */
  @Test
  public void testGetDiffParserSupportedFilesUnequal() {
    testImported.getParserSupportedFileTypes().put("Example", new ArrayList<String>());
    assertFalse("Could not get imported plugins with different supported file maps to be unequal",
        testImported.equals(diffImported));
  }
  
  /**
   * Test to try and get the imported visualisation methods.
   * This should have a key set size of 0 and return a tree map.
   */
  @Test
  public void testGetEmptyImportedVisualisation() {
    assertTrue("Could not get expected empty visualisation map",
        testImported.getImportedVisualisationMethods().keySet().isEmpty());
  }
  
  /**
   * Test to try and add a visualisation method to the imported map.
   * This should be in the returned map, and the key set size should be 1.
   */
  @Test
  public void testAddVisualisationImport() {
    testImported.getImportedVisualisationMethods().put("Example", null);
    assertEquals("Could not get the expected visualisation key set size after importing",
        1, testImported.getImportedVisualisationMethods().keySet().size());
  }
  
  /**
   * Test to try and get the hash code for imported plug ins after
   * adding a visualisation method. 
   * This should be different to the instance without any.
   */
  @Test
  public void testGetDiffHashCodesVisualisation() {
    testImported.getImportedVisualisationMethods().put("Example", null);
    assertTrue("Could not get different hash codes for different imported visualisation methods",
        testImported.hashCode() != diffImported.hashCode());
  }
  
  /**
   * Test to try and get unequal imported plug ins if the visualisation map is different.
   * This should return false.
   */
  @Test
  public void testGetUnequalDiffVisualisation() {
    testImported.getImportedVisualisationMethods().put("Example", null);
    assertFalse("Could not get unequal imported plugins with different visualisation methods",
        testImported.equals(diffImported));
  }
  
  /**
   * Test to try and get the total num of imported plugins after adding a visualisation method.
   * This should return 1.
   */
  @Test
  public void testGetTotalNumPluginsVisualisationImport() {
    testImported.getImportedVisualisationMethods().put("Example", null);
    assertEquals("Could not get the expected total number of plugins after importing visualisation",
        1, testImported.getTotalNumOfPlugins());
  }
  
  /**
   * Test to try and get the num of imported visualisation methods.
   * This should return 1.
   */
  @Test
  public void testNumImportedVisualisationMethodsSingle() {
    testImported.getImportedVisualisationMethods().put("Example", null);
    assertEquals("Could not get the correct num of visualisation methods after importing 1",
        1, testImported.getTotalNumOfVisualisationMethods());
  }
  
  /**
   * Test to try and get the expected number of imported visualisation methods.
   * This should return two this time as two have been added.:
   */
  @Test
  public void testNumImportedVisualisationMethodsDouble() {
    testImported.getImportedVisualisationMethods().put("Example", null);
    testImported.getImportedVisualisationMethods().put("Other", null);
    assertEquals("Could not get the correct num of visualisation methods after importing two",
        2, testImported.getTotalNumOfVisualisationMethods());
  }
  
  /**
   * Test to try and get the imported descriptions from ImportedPlugins,
   * and add one, the returned map should contain the added entry.
   */
  @Test
  public void testAddImportedDescription() {
    testImported.getImportedPluginDescriptions().put("Example", "Example description");
    assertTrue("Could not get the entry to be added to the description", 
        testImported.getImportedPluginDescriptions().containsKey("Example"));
  }
  
  /**
   * Test to try and add a different description with a different key.
   * This should also exist in the key set when checked.
   */
  @Test
  public void testAddDiffDescription() {
    testImported.getImportedPluginDescriptions().put("Other", "Other description");
    assertTrue("Could not get a different entry to be added to the description", 
        testImported.getImportedPluginDescriptions().containsKey("Other"));
  }
  
  /**
   * Test to try and get if imported plugins with different descriptions are equal.
   * This should return false.
   */
  @Test
  public void testUnequalImportedDescriptions() {
    testImported.getImportedPluginDescriptions().put("Example", "Example description");
    assertFalse("Could not get different imported descriptions to be unequal",
        testImported.equals(diffImported));
  }
  
  /**
   * Test to see if the hash codes of imported plugins with different description maps 
   * are different. They should not be equal.
   */
  @Test
  public void testDiffHashCodeDiffDescriptionMaps() {
    testImported.getImportedPluginDescriptions().put("Example", "Example description");
    assertTrue("Could not get imported plugins with different description maps to have "
        + "a different hash code", testImported.hashCode() != diffImported.hashCode());
  }

}
