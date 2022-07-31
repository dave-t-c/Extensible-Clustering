package testextensibleclustering.testdependencies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import extensibleclustering.dependencies.DirectoryHelper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Class for testing and development of the DirectoryHelper class.
 * This will be used for creating the required directories, and checking they exist.
 * @author David Cook
 */
public class TestDirectoryHelper {
  
  static DirectoryHelper testHelper;
  Path documentsDirectory;
  static Path extensibleHome;
  static Path outputDirectory;
  static Path pluginDirectory;
  
  /**
   * Remove all of the files if there are some existing from before the tests are run, 
   * e.g. the system has been run and a parser has been moved into the plugins folder.
   */
  @BeforeClass
  public static void initialSetUp() {
    testHelper = new DirectoryHelper();
    try {
      if (Files.exists(testHelper.getPlugInDirectory())) {
        Files.walk(testHelper.getPlugInDirectory())
        .map(Path::toFile).forEach(File::delete);
        Files.deleteIfExists(testHelper.getPlugInDirectory());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Setup code for the Paths and classes required for testing.
   */
  @Before
  public void setUp() {
    testHelper = new DirectoryHelper();
    documentsDirectory = Paths.get(System.getProperty("user.home") + File.separator 
        + "Documents");
    extensibleHome = Paths.get(documentsDirectory.toString() + File.separator
        + "Extensible Clustering");
    outputDirectory = Paths.get(extensibleHome.toString() + File.separator
        + "Output");
    pluginDirectory = Paths.get(extensibleHome.toString() + File.separator
        + "Plugins");
  }
  
  /**
   * Remove the folders created in the tests if they exist.
   */
  @After
  public void tearDown() {
    try {
      Files.deleteIfExists(pluginDirectory);
      Files.deleteIfExists(outputDirectory);
      Files.deleteIfExists(extensibleHome);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Test to try and see of the correct folder structure is there.
   * This should return false as it is not.
   */
  @Test
  public void testVerifyCorrectFolderStructure() {
    assertFalse("Could not get that the folder structure does not exist when not created",
        testHelper.correctFolderStructureExists());
  }
  
  /**
   * Test to try and create the required directories.
   * These should all exist after the method is called. 
   */
  @Test
  public void testCreateDirectories() {
    testHelper.createRequiredDirectories();
    assertTrue("Could not create extensible home directory", Files.exists(extensibleHome));
    assertTrue("Could not create output directory", Files.exists(outputDirectory));
    assertTrue("Could not create plugins directory", Files.exists(pluginDirectory));
  }
  
  /**
   * Test to try and see if the correct folder structure exists when the required 
   * directories have been created.
   */
  @Test
  public void testFodlderStructureDoesExist() {
    testHelper.createRequiredDirectories();
    assertTrue("Could not determine that the correct folders do indeed exist",
        testHelper.correctFolderStructureExists());
  }
  
  /**
   * Test to try and get the output folder path.
   * This should be the same as the output folder in this test class.
   */
  @Test
  public void testGetOutputDirectory() {
    assertEquals("Could not get the the correct path for the output folder",
        outputDirectory.toString(), testHelper.getOutputDirectory().toString());
  }
  
  /**
   * Test to try and get the plug-in folder path.
   * This should be the same as the folder in the test class.
   */
  @Test
  public void testGetPluginDirecotry() {
    assertEquals("Could not get the correct plugin folder path",
        pluginDirectory.toString(), testHelper.getPlugInDirectory().toString());
  }

}
