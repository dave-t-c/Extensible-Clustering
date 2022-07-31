package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import extensibleclustering.plugins.ImportedPlugins;
import extensibleclustering.plugins.ParserType;

/**
 * Class for testing and development of the 
 * ParserType class.
 * This class will modify the imported parsers in the ImportedPlugin Class.
 * @author David Cook
 */
public class TestParserType {
  
  ParserType testParserType;
  ImportedPlugins testPlugins;
  
  @Before
  public void setUp() {
    testParserType = new ParserType();
    testPlugins = new ImportedPlugins();
  }

  /**
   * Test to try and add a class to the ImportedPlugins parser
   * map using the ParserType.
   * This should have a size of 1 with a class of 'Example'.
   * String is used in this test as it 
   */
  @Test
  public void testParserTypeAddClass() {
    testParserType.storePlugin(testPlugins, "Example", Integer.class);
    assertEquals("Could not add the class to the imported plugins parser map",
        1, testPlugins.getImportedParsers().keySet().size());
  }
  
  /**
   * Test to try and add a class with a different name.
   * The map should contain the different name.
   */
  @Test
  public void testParserTypeDiffName() {
    testParserType.storePlugin(testPlugins, "Diff", Integer.class);
    assertTrue("Could not get imported parser to contain diff name",
        testPlugins.getImportedParsers().containsKey("Diff"));
  }
  
  /**
   * Test to add a different class to the parsers.
   * This should be the String class.
   */
  @Test
  public void testGetDifferentClass() {
    testParserType.storePlugin(testPlugins, "String", String.class);
    assertEquals("Could not get the different class from the ImportedParser",
        String.class, testPlugins.getImportedParsers().get("String"));
  }
  
  /**
   * Test to see if the store plug-in method can handle null for ImportedPlugins.
   * This should not throw a NullPointerException.
   */
  @Test
  public void testAddNullImportedPlugins() {
    testParserType.storePlugin(null, "Example", String.class);
  }
  
  /**
   * Test to try and see if storePlugin can handle null string key.
   * The size of the map should be zero after.
   */
  @Test
  public void testNullKey() {
    testParserType.storePlugin(testPlugins, null, String.class);
    assertEquals("Could not get the correct number of keys after null key is added",
        0, testPlugins.getImportedParsers().keySet().size());
  }
  
  /**
   * Test to try and add a null class.
   * This should not be added and the size of the key set should be zero.
   */
  @Test
  public void testNullClass() {
    testParserType.storePlugin(testPlugins, "Example", null);
    assertEquals("Could not get the correct number of keys after null class is added",
        0, testPlugins.getImportedParsers().keySet().size());
  }
}
