package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import extensibleclustering.dependencies.DirectoryHelper;
import extensibleclustering.dependencies.Position;
import extensibleclustering.plugins.ImportedPlugins;
import extensibleclustering.plugins.PluginExecutor;
import extensibleclustering.plugins.PluginLoader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Class for testing and development of the PluginExecutor class.
 * @author David Cook
 */
public class TestPluginExecutor extends ApplicationTest {
  
  PluginExecutor testExec;
  PluginLoader testLoader;
  static DirectoryHelper dirHelper;
  static Path standardJar;
  static Path destStandardJar;
  static Path clusteringJar;
  static Path destClusteringJar;
  static Path rootTestResources;
  static Path testGseFile;
  static Path testDiffGse;
  static Path extensibleHome;
  static Path visualisationJar;
  static Path destVisualisationJar;
  static Path testHierarchicalOutput;
  ImportedPlugins importedPlugins;
  
  @Override
  public void start(Stage stage) throws Exception {
  }
  
  /**
   * Setup the file directories before the test class is executed.
   * @throws IOException - Thrown if the directories or files cannot be moved.
   */
  @BeforeClass
  public static void initialSetUp() throws IOException {
    //Create the required directories and copy the parser into the plug-ins folder.
    dirHelper = new DirectoryHelper();
    dirHelper.createRequiredDirectories();
    rootTestResources = Paths.get("src" + File.separator + "test" + File.separator 
        + "resources" + File.separator + "PluginLoader");
    standardJar = Paths.get(rootTestResources.toString() + File.separator + "parser.jar");
    destStandardJar = Paths.get(dirHelper.getPlugInDirectory() 
        + File.separator + "parser.jar");
    clusteringJar = Paths.get(rootTestResources + File.separator + "clustering.jar");
    destClusteringJar = Paths.get(dirHelper.getPlugInDirectory() 
        + File.separator + "clustering.jar");
    Files.copy(standardJar, destStandardJar);
    testGseFile = Paths.get(rootTestResources.toString() + File.separator 
        + "GSE4014-GPL32_series_matrix.txt");
    testDiffGse = Paths.get(rootTestResources.toString() + File.separator 
        + "GSE124672_series_matrix.txt");
    extensibleHome = Paths.get(System.getProperty("user.home") + File.separator 
        + "Documents" + File.separator
        + "Extensible Clustering");
    visualisationJar = Paths.get(rootTestResources + File.separator + "visualisation.jar");
    destVisualisationJar = Paths.get(dirHelper.getPlugInDirectory() 
        + File.separator + "visualisation.jar");
    testHierarchicalOutput = Paths.get(rootTestResources.toString() + File.separator 
        + "ahc-output.tsv");
    
  }
  
  /**
   * Sets up the classes before each test is run.
   * @throws IOException - Thrown if the jars cannot be moved or deleted.
   */
  @Before
  public void setUp() throws IOException {
    testExec = new PluginExecutor();
    testLoader = new PluginLoader();
    Files.deleteIfExists(destStandardJar);
    Files.deleteIfExists(destClusteringJar);
    Files.deleteIfExists(destVisualisationJar);
    Files.copy(standardJar, destStandardJar);
    Files.copy(clusteringJar, destClusteringJar);
    Files.copy(visualisationJar, destVisualisationJar);
    importedPlugins = testLoader.loadPlugins();
  }
  
  /**
   * Clear the structures set up by the tests after the class is finished.
   * @throws IOException - thrown if the files or directories cannot be deleted.
   */
  @AfterClass
  public static void finalTearDown() throws IOException {
    Files.deleteIfExists(destStandardJar);
    Files.deleteIfExists(destClusteringJar);
    Files.deleteIfExists(destVisualisationJar);
    Files.deleteIfExists(dirHelper.getPlugInDirectory());
    Files.walk(dirHelper.getOutputDirectory())
      .map(Path::toFile).forEach(File::delete);
    Files.deleteIfExists(dirHelper.getOutputDirectory());
    Files.deleteIfExists(extensibleHome);
  }
  
  /**
   * Test to try and create a new PluginExecutor and execute a parser plug-in.
   * This should use return the correct number of positions.
   * @throws Exception  - Thrown if the parser cannot be executed.
   */
  @Test
  public void testExecuteParser() throws Exception {
    Class<?> parser = importedPlugins.getImportedParsers()
        .get("Gene Micro-Array Series Matrix Parser");
    assertEquals("Could not get the expected number of positions from parser",
        12654, testExec.execParser(testGseFile.toFile(), parser).length);
  }
  
  /**
   * Test to try and parse a different file.
   * This should have a different length to the previous test.
   * @throws Exception  - thrown if the parser cannot be executed.
   */
  @Test
  public void testExecuteParserDiffFile() throws Exception {
    Class<?> parser = importedPlugins.getImportedParsers()
        .get("Gene Micro-Array Series Matrix Parser");
    assertEquals("Could not get the expected number of positions from parser",
        44290, testExec.execParser(testDiffGse.toFile(), parser).length);
  }
  
  /**
   * Test to try and cluster a file.
   * This should check the output file exists.
   * @throws Exception - Thrown if an error occurs in parser or clustering.
   */
  @Test
  public void testExecuteClusteringAlgo() throws Exception {
    Class<?> parser = importedPlugins.getImportedParsers()
        .get("Gene Micro-Array Series Matrix Parser");
    Position[] data = testExec.execParser(testDiffGse.toFile(), parser);
    Class<?> clusteringAlgo = importedPlugins.getImportedClusteringAlgorithms()
        .get("K-Means Clustering");
    Path returnedPath = testExec.execClustering(data, 
        "GSE124672_series_matrix", clusteringAlgo);
    assertTrue("Could not successfully get the output file",
        returnedPath.toFile().exists());
  }
  
  /**
   * Test to try and execute a visualisation method. 
   * This should return a scene with a grid pane that contains a scrol pane
   * which in turn contains 10 items.
   * @throws Exception - Expects any exception to be thrown.
   */
  @Test
  public void testExecuteVisualisationMethod() throws Exception {
    Class<?> testVisualisation = importedPlugins.getImportedVisualisationMethods()
        .get("Agglomerative Dendrogram");
    Scene testScene = testExec.execVisualisation(testHierarchicalOutput, testVisualisation);
    ScrollPane scroll = null;
    for (Node node : testScene.getRoot().getChildrenUnmodifiable()) {
      if (node instanceof ScrollPane) {
        scroll = (ScrollPane) node;
      }
    }
    
    Group scrollGroup = (Group) scroll.getContent();
    assertEquals("Could not get the expected num of scroll group items from executing plugin",
        10, scrollGroup.getChildrenUnmodifiable().size());
  }
  
}
