package testextensibleclustering.testapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import extensibleclustering.application.ExtensibleClusteringController;
import extensibleclustering.dependencies.DirectoryHelper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


/**
 * Class for testing and development of the ecController.
 * @author David Cook
 */
public class TestExtensibleClusteringController extends ApplicationTest {
  
  static ExtensibleClusteringController testController;
  static MockParserView mockView;
  static Path rootTestResources;
  static Path testGseFile;
  static Path testDiffGse;
  static DirectoryHelper dirHelper;
  static Path standardJar;
  static Path destStandardJar;
  static Path clusteringJar;
  static Path destClusteringJar;
  static Path extensibleHome;
  static Path visualisationJar;
  static Path destVisualisationJar;
  static Path testHierarchicalOutput;
  
  /**
   * Sets up the required paths before the test class is executed.
   * @throws Exception - May be thrown by failing to find or copy a file.
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
    dirHelper.createRequiredDirectories();
    clusteringJar = Paths.get(rootTestResources + File.separator + "clustering.jar");
    destClusteringJar = Paths.get(dirHelper.getPlugInDirectory() 
        + File.separator + "clustering.jar");
    Files.copy(standardJar, destStandardJar);
    Files.copy(clusteringJar, destClusteringJar);
    testGseFile = Paths.get(rootTestResources.toString() + File.separator 
        + "GSE4014-GPL32_series_matrix.txt");
    testDiffGse = Paths.get(rootTestResources.toString() + File.separator 
        + "GSE124672_series_matrix.txt");
    visualisationJar = Paths.get(rootTestResources + File.separator + "visualisation.jar");
    destVisualisationJar = Paths.get(dirHelper.getPlugInDirectory() 
        + File.separator + "visualisation.jar");
    testHierarchicalOutput = Paths.get(rootTestResources.toString() + File.separator 
        + "ahc-output.tsv");
    testController = ExtensibleClusteringController.getInstance();
    mockView = MockParserView.getInstance();
    testController.setParserView(mockView, null);
  }
  
  /**
   * Replace the jar file as it will have been deleted in one of the tests.
   * @throws Exception - Thrown if the jar cannot be copied in.
   */
  @Before
  public void setUp() throws Exception {
    Files.copy(standardJar, destStandardJar, StandardCopyOption.REPLACE_EXISTING);
    Files.copy(clusteringJar, destClusteringJar, StandardCopyOption.REPLACE_EXISTING);
    Files.copy(visualisationJar, destVisualisationJar, StandardCopyOption.REPLACE_EXISTING);
    testController.loadPlugins();
  }
  
  /**
   * Clears the directories created by the test class.
   * @throws Exception - Thrown if a directory cannot be deleted.
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
   * Test to try and create a new instance of the controller.
   * As this is a singleton, the memory addresses of two different instances should
   * be the same.
   */
  @Test
  public void testCreateNewController() {
    assertTrue("Could not get the same memory address for different controller instances",
        testController == ExtensibleClusteringController.getInstance());
  }
  
  /**
   * Test to try and get the controller to parse a file.
   * This should change the output in the view.
   */
  @Test
  public void testParseFile() {
    mockView.setSelectedFile(testGseFile.toFile());
    mockView.setSelectedName("Gene Micro-Array Series Matrix Parser");
    assertEquals("Could not get the expected output from the controller",
        "Parsed 12654 positions, each with 2 dimensions", testController.parseFile());
  }
  
  /**
   * Test to try and get the output from parsing a different file.
   * This should output a different result.
   */
  @Test
  public void testParseDifferentFile() {
    mockView.setSelectedFile(testDiffGse.toFile());
    mockView.setSelectedName("Gene Micro-Array Series Matrix Parser");
    assertEquals("Could not get the expected output from the controller",
        "Parsed 44290 positions, each with 8 dimensions", testController.parseFile());
  }
  
  /**
   * Test to try and load the plug-ins in again once one has been deleted.
   * @throws IOException - Thrown if the jar cannot be deleted.
   */
  @Test
  public void testLoadPlugins() throws IOException {
    Files.deleteIfExists(destStandardJar);
    testController.loadPlugins();
    mockView.setSelectedFile(testDiffGse.toFile());
    mockView.setSelectedName("Gene Micro-Array Series Matrix Parser");
    assertEquals("Could not get the expected error message as output from ecController",
        "An Illegal argument was provided. This may be due to the file "
        + "not existing or the parser not existing", testController.parseFile());
  }
  
  /**
   * Test to try and cluster a piece of data. 
   * The parser result should be changed to show how many positions were clustered with 
   * the number of clusters used.
   * This should cluster data and then use that.
   */
  @Test
  public void testClusterData() {
    mockView.setSelectedFile(testDiffGse.toFile());
    mockView.setSelectedName("Gene Micro-Array Series Matrix Parser");
    mockView.setSelectedClustering("K-Means Clustering");
    assertTrue("Could not get the expected output from clustering in the view",
        testController.clusterData().contains("Successfuly completed clustering"));
  }
  
  /**
   * Test to see if the controller sets the clustering plugins set to use.
   * This should be set when the view is assigned.
   * This set should contain a single item as only 1 clustering algorithm has been imported.
   */
  @Test
  public void testSetClusteringSet() {
    assertEquals("Could not get the expected clustering set size set by controller",
        1, mockView.getClusteringPluginSet().size());
  }
  
  /**
   * Test to try and visualise data. This will return a scene, which can then be visualised. 
   */
  @Test
  public void testVisualiseData() {
    mockView.setSelectedFile(testHierarchicalOutput.toFile());
    mockView.setSelectedVisualisation("Agglomerative Dendrogram");
    Scene testScene = testController.visualiseData();
    ScrollPane scroll = null;
    for (Node node : testScene.getRoot().getChildrenUnmodifiable()) {
      if (node instanceof ScrollPane) {
        scroll = (ScrollPane) node;
      }
    }
    
    Group scrollGroup = (Group) scroll.getContent();
    assertEquals("Could not get expected num of items in scroll group "
        + "from controller visualisation exec.",
        10, scrollGroup.getChildrenUnmodifiable().size());
  }
}
