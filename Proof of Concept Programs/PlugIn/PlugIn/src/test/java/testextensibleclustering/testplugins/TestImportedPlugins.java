package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import extensibleclustering.plugins.ImportedPlugins;

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
    assertEquals("Could not get the correct num of parser plugins when parser added to ImportedPlugins",
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
  

}
