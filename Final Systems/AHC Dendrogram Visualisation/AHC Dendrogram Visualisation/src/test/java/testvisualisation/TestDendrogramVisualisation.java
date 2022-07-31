package testvisualisation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import extensibleclustering.dependencies.Visualisation;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import visualisation.DendrogramVisualisation;

/**
 * Class for testing and development of the Dendrogram Visualisation class.
 * @author David Cook
 */
public class TestDendrogramVisualisation extends ApplicationTest {
  
  DendrogramVisualisation testVis;
  Path testResources;
  Path simpleOutputPath;
  Path incorrectFileTypePath;
  Path unsupportedLayoutPath;
  Scene testScene;
  
  @Override
  public void start(Stage stage) throws Exception {
  }
  
  /**
   * Set up the variables required for the tests.
   * @throws IOException  - Thrown if the file cannot be read.
   */
  @Before
  public void setUp() throws IOException {
    testVis = new DendrogramVisualisation();
    testResources = Paths.get("src/test/resources");
    simpleOutputPath = Paths.get(testResources.toString() + "/ClusteringOutputSmall.tsv");
    incorrectFileTypePath = Paths.get(testResources.toString() + "/Output.pdf");
    unsupportedLayoutPath = Paths.get(testResources.toString() + "/EmptyFile.tsv");
    testScene = testVis.visualiseData(simpleOutputPath);
  }
  
  /**
   * Test to see if the visualisation class implements the visualisation interface.
   */
  @Test
  public void testImplementsCorrectInterface() {
    assertTrue("Could not get visualisation to implement the correct interface",
        testVis instanceof Visualisation);
  }
  
  /**
   * Test to try and visualise data of a null path.
   * This should throw an illegal argument exception.
   * @throws IOException - Thrown if the file cannot be read.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testNullPath() throws IOException {
    testVis.visualiseData(null);    
  }
  
  /**
   * Test to try and visualise data with a path that does not exist.
   * This should also throw an illegal args exception
   * @throws IOException - Thrown if the file cannot be read.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testPathNotExist() throws IOException {
    testVis.visualiseData(Paths.get("./File.txt"));
  }
  
  /**
   * Test to try and get the expected window height for the data set. 
   * This should be 1000 high.
   */
  @Test
  public void testGetCorrectSceneSize() {
    assertEquals("Could not get the expected height of 1000", 1000.0, 
        testScene.getHeight(), 0.00);
  }
  
  /**
   * Test to try and get the expected window width. 
   * This should be the same as the height and be 1000px.
   */
  @Test
  public void testGetCorrectScreenWidth() {
    assertEquals("Could not get the expected width of 1000", 1000.0, 
        testScene.getWidth(), 0.00);
  }
  
  /**
   * Test to try and get the expected name from the class.
   * This should be 'Agglomerative Dendrogram"
   */
  @Test
  public void testGetExpectedName() {
    assertEquals("Could not get the expected name from Denedrogram Visualisation",
        "Agglomerative Dendrogram", testVis.getName());
  }
  
  /**
   * Test to try and get the expected description from 
   * the Dendrogram visualisation.
   * This should return "Creates a dendrogram from an agglomerative hierarchical 
   * clustering result to visualise where clusters are merged. This supports the Agglomerative 
   * Hierarchical Clustering Plugin Output"
   */
  @Test
  public void testGetExpectedDescription() {
    assertEquals("Could not get the expected description from Dendrogram Visualisation",
        "Creates a dendrogram from an agglomerative hierarchical "
        + "clustering result to visualise where clusters are merged. This supports "
        + "the Agglomerative Hierarchical Clustering Plugin Output", testVis.getDescription());
  }
  
  /**
   * Test to check that the visualisation method contains a grid as the 
   * scene root.
   * This should have the name
   */
  @Test
  public void testGetExpectedRootGrid() {
    assertTrue("Could not get the root of the scene to be a grid pane",
        testScene.getRoot() instanceof GridPane);
  }
  
  /**
   * Test to try and get the contents of a root grid. This should contain 
   * a stack pane an a grid pane.
   */
  @Test
  public void testGetExpectedRootContents() {
    int scrollCount = 0;
    for (Node node : testScene.getRoot().getChildrenUnmodifiable()) {
      scrollCount += node instanceof ScrollPane ? 1 : 0;
    }
    
    int gridCount = 0;
    for (Node node : testScene.getRoot().getChildrenUnmodifiable()) {
      gridCount += node instanceof GridPane ? 1 : 0;
    }
    
    assertEquals("Could not get the expected number of Scroll Panes in root grid",
        1, scrollCount);
    assertEquals("Could not get the expected number of grids in root grid",
        1, gridCount);
  }
  
  /**
   * Test to see if the grid in the root pane contains 3 buttons. 
   * This should be for zoom in, out and reset selection.
   */
  @Test
  public void testGetExpectedButtons() {
    GridPane buttonPane = null;
    for (Node node : testScene.getRoot().getChildrenUnmodifiable()) {
      if (node instanceof GridPane) {
        buttonPane = (GridPane) node;
      }
    }
    
    //Get the number of buttons in the grid pane. This should be 3.
    int buttonCount = 0;
    for (Node node: buttonPane.getChildrenUnmodifiable()) {
      buttonCount += node instanceof Button ? 1 : 0;
    }
  
    assertEquals("Could not get the expected number of buttons in button grid",
        3, buttonCount);
  }
  
  /**
   * Test to try and get the expected number of nodes in the scroll pane.
   * This should be 3 for the labels and 7 lines, total of 10.
   */
  @Test
  public void testGetScrollPaneContentsSize() {
    ScrollPane scroll = null;
    for (Node node : testScene.getRoot().getChildrenUnmodifiable()) {
      if (node instanceof ScrollPane) {
        scroll = (ScrollPane) node;
      }
    }
    
    Group scrollGroup = (Group) scroll.getContent();
    
    assertEquals("Could not get the expected number of elements in the scroll pane for this file",
        10, scrollGroup.getChildren().size());
  }
  
  /**
   * Test to try and give the visualisation method a unsupported file type.
   * In this case, it will be an empty pdf.
   * @throws IOException - Thrown if an IO error occurs when reading the file.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testVisualsieUnsupportedFileType() throws IOException {
    testVis.visualiseData(incorrectFileTypePath);
  }
  
  /**
   * Test to try and give the visualisation a file with the expected extension,
   * but not the expected internal format.
   * @throws IOException - Thrown if an error occurs reading the file.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testVisualiseUnsupportedFileLayout() throws IOException {
    testVis.visualiseData(unsupportedLayoutPath);
  }
  
}
