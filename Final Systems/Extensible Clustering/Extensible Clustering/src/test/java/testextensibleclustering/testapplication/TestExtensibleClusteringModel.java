package testextensibleclustering.testapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import extensibleclustering.application.ExtensibleClusteringModel;
import extensibleclustering.dependencies.DirectoryHelper;
import extensibleclustering.dependencies.Position;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class TestExtensibleClusteringModel extends ApplicationTest {
  
  ExtensibleClusteringModel testModel;
  static DirectoryHelper dirHelper;
  static Path rootTestResources;
  static Path standardJar;
  static Path destStandardJar;
  static Path clusteringJar;
  static Path destClusteringJar;
  static Path extensibleHome;
  static Path testGseFile;
  static Path testDiffGse;
  static Path visualisationJar;
  static Path destVisualisationJar;
  static Path testHierarchicalOutput;
  
  /**
   * Sets the paths for the initial resources before the test class is run.
   * @throws Exception - May be thrown from IOException or FileNotFound.
   */
  @BeforeClass
  public static void initialSetUp() throws Exception {
    dirHelper = new DirectoryHelper();
    rootTestResources = Paths.get("src" + File.separator + "test" + File.separator 
        + "resources" + File.separator + "PluginLoader");
    standardJar = Paths.get(rootTestResources.toString() + File.separator + "parser.jar");
    destStandardJar = Paths.get(dirHelper.getPlugInDirectory() 
        + File.separator + "parser.jar");
    extensibleHome = Paths.get(System.getProperty("user.home") + File.separator 
        + "Documents" + File.separator
        + "Extensible Clustering");
    clusteringJar = Paths.get(rootTestResources + File.separator + "clustering.jar");
    destClusteringJar = Paths.get(dirHelper.getPlugInDirectory() 
        + File.separator + "clustering.jar");
    visualisationJar = Paths.get(rootTestResources + File.separator + "visualisation.jar");
    destVisualisationJar = Paths.get(dirHelper.getPlugInDirectory() 
        + File.separator + "visualisation.jar");
    dirHelper.createRequiredDirectories();
    Files.copy(standardJar, destStandardJar);
    Files.copy(clusteringJar, destClusteringJar);
    Files.copy(visualisationJar, destVisualisationJar);
    testGseFile = Paths.get(rootTestResources.toString() + File.separator 
        + "GSE4014-GPL32_series_matrix.txt");
    testDiffGse = Paths.get(rootTestResources.toString() + File.separator 
        + "GSE124672_series_matrix.txt");
    testHierarchicalOutput = Paths.get(rootTestResources.toString() + File.separator 
        + "ahc-output.tsv");
  }
  
  /**
   * Setup the jar files and reset model before each test is run.
   * @throws Exception - Thrown if a file cannot be copied or deleted.
   */
  @Before
  public void setUp() throws Exception {
    testModel = new ExtensibleClusteringModel();
  }
  
  /**
   * Removes the resources created by the test class.
   * @throws Exception - Thrown if the directories cannot be deleted.
   */
  @AfterClass
  public static void finalTearDown() throws Exception {
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
   * Test to try and create a new ecModel. This should import the plug-ins for the parser.
   * This should import the plug-in moved in, so there should be a total number of 1 plug-ins.
   */
  @Test
  public void testLoadPluginsOnCreation() {
    assertEquals("Could not import the correct num of plugins in the model constructor",
        1, testModel.getImportedPlugins().getTotalNumOfParsers());
    assertEquals("Could not import correct num of clustering plugins",
        1, testModel.getImportedPlugins().getTotalNumOfClusteringAlgos());
  }
  
  /**
   * Test to try and import the plug-ins after the constructor method is called.
   * This should have 0 plug-ins as the one loaded in the constructor will have been deleted.
   * @throws IOException - Thrown if the file cannot be deleted.
   */
  @Test
  public void testLoadPluginsMethod() throws IOException {
    Files.deleteIfExists(destStandardJar);
    Files.deleteIfExists(destClusteringJar);
    testModel.importPlugins();
    assertEquals("Could not get the correct number of plugins when re-importing parser plugins",
        0, testModel.getImportedPlugins().getTotalNumOfParsers());
  }
  
  /**
   * Test to try and parse a file using the model.
   * This should return a position array of the expected length.
   * @throws Exception  - Thrown if the parser cannot be executed.
   */
  @Test
  public void testParseFile() throws Exception {
    assertEquals("Could not get the correct length of positions from the parseFile method",
        12654, 
        testModel.parseFile("Gene Micro-Array Series Matrix Parser", testGseFile.toFile()).length);
  }
  
  /**
   * Test to try and parse a different file using the model.
   * This should return a different number of positions.
   * @throws Exception - Thrown if the parser cannot be executed.
   */
  @Test
  public void testParseDiffFile() throws Exception {
    assertEquals("Could not get the expected number of positions from parsFile with different file",
        44290, 
        testModel.parseFile("Gene Micro-Array Series Matrix Parser", testDiffGse.toFile()).length);
  }
  
  /**
   * Test to try and parse a null name.
   * This should throw an IllegalArguemntException.
   * @throws Exception - Exception should be thrown and be an illegal args exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testParseNullName() throws Exception {
    testModel.parseFile(null, testGseFile.toFile());
  }
  
  /**
   * Test to try and parse a null file.
   * This should throw an IllegalArgs Exception.
   * @throws Exception - Exception should be thrown and be an illegal args exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testParseNullFile() throws Exception {
    testModel.parseFile("Gene Micro-Array Series Matrix Parser", null);
  }
  
  /**
   * Test to try and parse a file using a parser that has not been imported.
   * This should throw an IllegalArgs exception.
   * @throws Exception - Exception should be thrown and be an illegal args exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoneAddedParser() throws Exception {
    testModel.parseFile("Example", testGseFile.toFile());
  }
  
  /**
   * Test to try and provide a parser that has not been imported and get the error message.
   * This should contain why the parser has not been able to be completed.
   */
  @Test
  public void testGetParserErrorMsg() {
    try {
      testModel.parseFile("Example", testGseFile.toFile());
    } catch (Exception ex) {
      assertEquals("Could not get an explanatory error message",
          "An Illegal argument was provided. "
          + "This may be due to the file not existing or the parser not existing",
          ex.getMessage());
    }
  } 
  
  /**
   * Test to try and cluster a file using a parsed data set.
   * This should return a Path that should exist.
   * @throws Exception - Thrown if the data cannot be parsed or clustered.
   */
  @Test
  public void testClusterData() throws Exception {
    Position[] data = testModel.parseFile("Gene Micro-Array Series Matrix Parser", 
        testDiffGse.toFile());
    Path returnedPath = testModel.clusterData(data, "GSE124672_series_matrix", 
        "K-Means Clustering");
    assertTrue("Could not successfully get the output path of clustering to exist",
        returnedPath.toFile().exists());    
  }
  
  /**
   * Test to try and cluster data with null data.
   * This should throw an illegal argument exception.
   * @throws Exception - May be thrown from the cluster data method.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullData() throws Exception {
    testModel.clusterData(null, "GSE124672_series_matrix", "K-Means Clustering");
  }
  
  /**
   * Test to try and cluster data with a null file name.
   * This should throw an illegal args exception.
   * @throws Exception - May be thrown from the cluster data method.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullFileName() throws Exception {
    Position[] data = testModel.parseFile("Gene Micro-Array Series Matrix Parser", 
        testDiffGse.toFile());
    testModel.clusterData(data, null, "K-Means Clustering");
  }
  
  /**
   * Test to try and cluster data with a null clustering algorithm.
   * This should throw an illegal args exception.
   * @throws Exception - May be thrown from the cluster data method.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullClusteringAlgoName() throws Exception {
    Position[] data = testModel.parseFile("Gene Micro-Array Series Matrix Parser", 
        testDiffGse.toFile());
    testModel.clusterData(data, "GSE124672_series_matrix", null);
  }
  
  /**
   * Test to try and clsuter data with a clustering algorithm that 
   * has not been imported.
   * This should throw an illegal args exception.
   * @throws Exception - May be thrown from the cluster data method.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonImportedClusteringAlgo() throws Exception {
    Position[] data = testModel.parseFile("Gene Micro-Array Series Matrix Parser", 
        testDiffGse.toFile());
    testModel.clusterData(data, "GSE124672_series_matrix", "Example");
  }
  
  /**
   * Test to try and visualise data using the agglomerative dendrogram plug-in.
   * This should return a scene that should contain a scroll pane with 10 items.
   * @throws Exception - May be thrown by the visaulisation method.
   */
  @Test
  public void testVisualiseData() throws Exception {
    Scene testScene = testModel.visualiseData(testHierarchicalOutput.toFile(),
        "Agglomerative Dendrogram");
    ScrollPane scroll = null;
    for (Node node : testScene.getRoot().getChildrenUnmodifiable()) {
      if (node instanceof ScrollPane) {
        scroll = (ScrollPane) node;
      }
    }
    
    Group scrollGroup = (Group) scroll.getContent();
    assertEquals("Could not get the expected num of scroll group items from executing visualisation"
        + "from model.",
        10, scrollGroup.getChildrenUnmodifiable().size());
  }
  
  /**
   * Test to try and run a visualisation method with a null Path.
   * This should throw an illegal argument exception.
   * @throws Exception - Illegal Args Exception should be thrown in this case
   */
  @Test (expected = IllegalArgumentException.class)
  public void testVisualiseNullPath() throws Exception {
    testModel.visualiseData(null, "Agglomerative Dendrogram");
  }
  
  /**
   * Test to try and run a visualisation method with a null name.
   * This should throw an illegal args exception.
   * @throws Exception - Illegal Args Exception should be thrown in this case
   */
  @Test (expected = IllegalArgumentException.class)
  public void testVisualiseNullName() throws Exception {
    testModel.visualiseData(testHierarchicalOutput.toFile(), null);
  }
  
  /**
   * Test to try and run a visualisation method with a visualisation method 
   * that has not been imported.
   * This should thrown an illegal args exception.
   * @throws Exception - Illegal Args Exception should be thrown in this case
   */
  @Test (expected = IllegalArgumentException.class)
  public void testVisualiseNonImportedMethod() throws Exception {
    testModel.visualiseData(testHierarchicalOutput.toFile(), "Example");
  }
  
  
}
