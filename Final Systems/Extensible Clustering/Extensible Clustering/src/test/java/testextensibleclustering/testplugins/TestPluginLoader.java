package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import extensibleclustering.plugins.ImportedPlugins;
import extensibleclustering.plugins.PluginLoader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Test class for development of the PluginLoader.
 * This class will eventually load plug-ins from the plug-in folder.
 * @author David Cook
 */
public class TestPluginLoader {
  
  static Path documentsDirectory;
  static Path rootDirectory;
  static Path pluginDirectory;
  static Path rootTestResources;
  static Path standardJar;
  static Path clusteringJar;
  static Path destClusteringJar;
  static Path destStandardJar;
  static Path diffJar;
  static Path destDiffJar;
  static Path visualisationJar;
  static Path destVisualisationJar;
  static PluginLoader testLoader;
  
  /**
   * Setup the initial directory structure for the 
   * tests. This involves creating the 'Plugins' Directory.
   */
  @BeforeClass
  public static void initialSetUp() {
    documentsDirectory = Paths.get(System.getProperty("user.home") + File.separator 
        + "Documents");
    if (Files.notExists(documentsDirectory)) {
      //Create the Directory
      try {
        Files.createDirectory(documentsDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    
    rootDirectory = Paths.get(documentsDirectory.toString()
        + File.separator + "Extensible Clustering");
    if (Files.notExists(rootDirectory)) {
      //Create the Directory
      try {
        Files.createDirectory(rootDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    pluginDirectory = Paths.get(rootDirectory.toString() 
        + File.separator + "Plugins" + File.separator);
    if (Files.notExists(pluginDirectory)) {
      //Create the output directory;
      try {
        Files.createDirectory(pluginDirectory);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Sets up the variables used in the test before each fun.
   */
  @Before
  public void setUp() {
    rootTestResources = Paths.get("src" + File.separator + "test" + File.separator 
        + "resources" + File.separator + "PluginLoader");
    standardJar = Paths.get(rootTestResources + File.separator + "parser.jar");
    clusteringJar = Paths.get(rootTestResources + File.separator + "clustering.jar");
    destStandardJar = Paths.get(pluginDirectory + File.separator + "parser.jar");
    destClusteringJar = Paths.get(pluginDirectory + File.separator + "clustering.jar");
    diffJar = Paths.get(rootTestResources + File.separator + "diffParser.jar");
    destDiffJar = Paths.get(pluginDirectory + File.separator + "diffParser.jar");
    visualisationJar = Paths.get(rootTestResources + File.separator + "visualisation.jar");
    destVisualisationJar = Paths.get(pluginDirectory + File.separator + "visualisation.jar");
    testLoader = new PluginLoader();
  }
  
  /**
   * Removes all of the files created during the test.
   */
  @After
  public void tearDown() {
    //Clear the files out of the plug-ins folder.
    try {
      Files.walk(pluginDirectory)
      .map(Path::toFile).forEach(File::delete);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Removes the files created in the tests and then removes the directories 
   * created.
   */
  @AfterClass
  public static void finalTearDown() {
    //Delete the files that were created during testing the plug-in loader.
    try {
      Files.walk(rootDirectory)
      .map(Path::toFile).forEach(File::delete);
      Files.deleteIfExists(pluginDirectory);
      Files.deleteIfExists(rootDirectory);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Test to try and create a new plug-in loader and get the result from the 
   * 'loadPlugins' method.
   * This should return an ImportedPlugins where there are no plug-ins in the 
   * plug-ins folder.
   */
  @Test
  public void testGetEmptyPlugins() {
    ImportedPlugins result = testLoader.loadPlugins();
    assertEquals("Could not create a new ImportedPlugin from an empty Plugins folder", 
        0, result.getTotalNumOfPlugins());
  }
  
  /**
   * Test to try and load a single parser plug-in. 
   * This should have the name 'Gene Micro-Array Series Matrix Parser'.
   */
  @Test
  public void testImportSingleParser() {
    try {
      //Copy the standardJar into the Plug-ins folder.
      Files.copy(standardJar, destStandardJar, StandardCopyOption.REPLACE_EXISTING);
      //Run the importPlugins method that should contain a total of one items.
      //This should have a total of one imported plug-in.
      ImportedPlugins result = testLoader.loadPlugins();
      assertEquals("Could not import a Parser Plugin and have the correct number of plugins",
          1, result.getTotalNumOfPlugins());
    } catch (IOException e) {
      e.printStackTrace();
      fail("Could not copy in required jar");
    }
  }
  
  /**
   * Test to try and load in two parser plug-ins. This should contain two items.
   */
  @Test
  public void testImportTwoParsers() {
    try {
      Files.copy(standardJar, destStandardJar, StandardCopyOption.REPLACE_EXISTING);
      Files.copy(diffJar, destDiffJar, StandardCopyOption.REPLACE_EXISTING);
      ImportedPlugins result = testLoader.loadPlugins();
      assertEquals("Could not import two parser plugins and have correct total.",
          2, result.getTotalNumOfPlugins());
    } catch (IOException e) {
      e.printStackTrace();
      fail("Could not copy in the required jars for test case");
    }
  }
  
  /**
   * Test to try and load in a clustering plug-ins.
   * The total number of plug-ins should be 1 and the num of clustering plug-ins should
   * be 1.
   */
  @Test
  public void testImportClustering() {
    try {
      Files.copy(clusteringJar, destClusteringJar);      
      ImportedPlugins result = testLoader.loadPlugins();
      assertEquals("Could not get the expected num of imported clustering algos",
          1, result.getTotalNumOfClusteringAlgos());
      assertEquals("Could not get the correct total num of imported plugins",
          1, result.getTotalNumOfPlugins());
    } catch (IOException ex) {
      ex.printStackTrace();
      fail("Could not import the required jars");
    }
  }
  
  /**
   * Test to try and load in a visualisation plug-in. 
   * This should end up with there being 1 total plug-in imported, also with
   * 1 visualisation plug-in imported.
   */
  @Test
  public void testImportVisualisation() {
    try {
      Files.copy(visualisationJar, destVisualisationJar);  
      ImportedPlugins result = testLoader.loadPlugins();
      assertEquals("Could not get the expected num of imported plugins",
          1, result.getTotalNumOfPlugins());
      assertEquals("Could not get correct num of imported visualisation methods",
          1, result.getTotalNumOfVisualisationMethods());
    } catch (IOException ex) {
      ex.printStackTrace();
      fail("Could not import the required jar");
    }
  }
  
  /**
   * Test to try and successfully load in a description.
   * This should mean the size of the descriptions map is 1.
   */
  @Test
  public void testImportDescriptions() {
    try {
      Files.copy(clusteringJar, destClusteringJar);      
      ImportedPlugins result = testLoader.loadPlugins();
      assertEquals("Could not get the expected size of the description map",
          1, result.getImportedPluginDescriptions().size());
    } catch (IOException ex) {
      ex.printStackTrace();
      fail("Could not improt the reqired jar");
    } 
  }
}
