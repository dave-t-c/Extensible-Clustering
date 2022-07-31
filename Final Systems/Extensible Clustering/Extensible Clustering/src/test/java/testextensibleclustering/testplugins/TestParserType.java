package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import extensibleclustering.plugins.ImportedPlugins;
import extensibleclustering.plugins.ParserType;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing and development of the 
 * ParserType class.
 * This class will modify the imported parsers in the ImportedPlugin Class.
 * @author David Cook
 */
public class TestParserType {
  
  ParserType testParserType;
  ImportedPlugins testPlugins;
  Class<?> importedParser;
  Class<?> diffParser;
  
  /**
   * Sets up the importer parser and the other required variables before each test is run.
   * @throws ClassNotFoundException - Thrown if the parser cannot be found.
   * @throws MalformedURLException - Thrown if the URL provided is malformed and cant be used.
   */
  @Before
  public void setUp() throws ClassNotFoundException, MalformedURLException {
    testParserType = new ParserType();
    testPlugins = new ImportedPlugins();
    Path rootTestResources = Paths.get("src" + File.separator + "test" + File.separator 
        + "resources" + File.separator + "PluginLoader");
    Path standardJar = Paths.get(rootTestResources.toString() + File.separator + "parser.jar");
    Path diffJar = Paths.get(rootTestResources.toString() + File.separator + "diffParser.jar");
    File jarFile = new File(standardJar.toString());
    URL[] urls = { new URL("jar:file:" + jarFile.getAbsolutePath() + "!/") };
    URLClassLoader classLoader = URLClassLoader.newInstance(urls);
    importedParser = classLoader.loadClass("parser.SeriesMatrixParser");
    
    jarFile = new File(diffJar.toString());
    urls = new URL[]{ new URL("jar:file:" + jarFile.getAbsolutePath() + "!/") };
    classLoader = URLClassLoader.newInstance(urls);
    diffParser = classLoader.loadClass("parser.SeriesMatrixParser");
  }

  /**
   * Test to try and add a class to the ImportedPlugins parser
   * map using the ParserType.
   * This should have a size of 1 with a class of 'Example'.
   * String is used in this test as it 
   * @throws Exception - Thrown if the parser cannot be created.
   */
  @Test
  public void testParserTypeAddClass() throws Exception {
    testParserType.storePlugin(testPlugins, "Example", importedParser);
    assertEquals("Could not add the class to the imported plugins parser map",
        1, testPlugins.getImportedParsers().keySet().size());
  }
  
  /**
   * Test to try and add a class with a different name.
   * The map should contain the different name.
   * @throws Exception - Thrown if the parser cannot be created. 
   */
  @Test
  public void testParserTypeDiffName() throws Exception {
    testParserType.storePlugin(testPlugins, "Diff", importedParser);
    assertTrue("Could not get imported parser to contain diff name",
        testPlugins.getImportedParsers().containsKey("Diff"));
  }
  
  /**
   * Test to add a different class to the parsers.
   * This should be the String class.
   * @throws Exception - Thrown if the parser cannot be created.
   */
  @Test
  public void testGetDifferentClass() throws Exception {
    testParserType.storePlugin(testPlugins, "String", diffParser);
    assertEquals("Could not get the different class from the ImportedParser",
        diffParser, testPlugins.getImportedParsers().get("String"));
  }
  
  /**
   * Test to see if the store plug-in method can handle null for ImportedPlugins.
   * This should not throw a NullPointerException.
   * @throws Exception - Thrown if the parser cannot be created. 
   */
  @Test
  public void testAddNullImportedPlugins() throws Exception {
    testParserType.storePlugin(null, "Example", importedParser);
  }
  
  /**
   * Test to try and see if storePlugin can handle null string key.
   * The size of the map should be zero after.
   * @throws Exception - Thrown if the parser cannot be created.
   */
  @Test
  public void testNullKey() throws Exception {
    testParserType.storePlugin(testPlugins, null, importedParser);
    assertEquals("Could not get the correct number of keys after null key is added",
        0, testPlugins.getImportedParsers().keySet().size());
  }
  
  /**
   * Test to try and add a null class.
   * This should not be added and the size of the key set should be zero.
   * @throws Exception - Thrown if the parser cannot be created.
   */
  @Test
  public void testNullClass() throws Exception {
    testParserType.storePlugin(testPlugins, "Example", null);
    assertEquals("Could not get the correct number of keys after null class is added",
        0, testPlugins.getImportedParsers().keySet().size());
  }
  
  /**
   * Test to try and get the supported file types in ImportedPlugins from the 
   * parser type.
   * This should add an item to the parserSupportedFilesMap.
   * The first item should be "txt files (*.txt)"
   * @throws Exception - Thrown if the parser cannot be created.
   */
  @Test
  public void testAddSupportedFiles() throws Exception {
    testParserType.storePlugin(testPlugins, "Example", importedParser);
    assertEquals("Could not get the correct num of supported file types after storing parser",
        1, testPlugins.getParserSupportedFileTypes().keySet().size());
  }
  
  /**
   * Test to try and get the name of a different parser to be in the map.
   * This should have the different name in the map.
   * @throws Exception - Thrown if the parser cannot be created.
   */
  @Test
  public void testAddDiffParserNameSupportedFiles() throws Exception {
    testParserType.storePlugin(testPlugins, "Diff Plugin", diffParser);
    assertTrue("Could not get the expected name in the supportedParser map",
        testPlugins.getParserSupportedFileTypes().containsKey("Diff Plugin"));
  }
  
  /**
   * Test to try and get the list from the supported file types.
   * This list should contain 2 items, the description and the supported files.
   * @throws Exception - Thrown if the parser cannot be created.
   */
  @Test
  public void testGetSupportedFilesList() throws Exception {
    testParserType.storePlugin(testPlugins, "Example", importedParser);
    assertEquals("Could not get the expected supported files list",
        2, testPlugins.getParserSupportedFileTypes().get("Example").size());
  }
}
