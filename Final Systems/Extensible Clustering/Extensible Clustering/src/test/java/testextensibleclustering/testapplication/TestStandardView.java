package testextensibleclustering.testapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import extensibleclustering.application.ExtensibleClusteringController;
import extensibleclustering.application.StandardView;
import extensibleclustering.dependencies.DirectoryHelper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Class for testing the standard parser view.
 * 
 * @author David Cook
 *
 */
public class TestStandardView extends ApplicationTest {

  StandardView testView;
  Path testGseFile;
  Path rootTestResources;
  static DirectoryHelper dirHelper;
  static Path extensibleHome;

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader = new FXMLLoader(
        extensibleclustering.application.Main.class.getClassLoader()
        .getResource("StandardView.fxml"));
    Parent sceneRoot = loader.load();
    Scene scene = new Scene(sceneRoot);
    stage.setScene(scene);
    stage.show();
    testView = loader.getController();
    testView.setButtonActions();
    ExtensibleClusteringController.getInstance().setParserView(testView, stage);
    ExtensibleClusteringController.getInstance().loadPlugins();
    dirHelper = new DirectoryHelper();
    extensibleHome = Paths.get(System.getProperty("user.home") + File.separator 
        + "Documents" + File.separator
        + "Extensible Clustering");
  }

  /**
   * Setup the required variables for the tests.
   */
  @Before
  public void setUp() {
    // testView = StandardView.getInstance();
    rootTestResources = Paths
        .get("src" + File.separator + "test" + File.separator + "resources" 
            + File.separator + "PluginLoader");
    testGseFile = Paths.get(rootTestResources.toString() 
        + File.separator + "GSE4014-GPL32_series_matrix.txt");
  }
  
  /**
   * Delete the folders created after the tests have run.
   * @throws IOException  - Thrown if files cannot be deleted.
   */
  @AfterClass
  public static void finalTearDown() throws IOException {
    Files.deleteIfExists(dirHelper.getPlugInDirectory());
    Files.walk(dirHelper.getOutputDirectory())
    .map(Path::toFile).forEach(File::delete);
    Files.deleteIfExists(dirHelper.getOutputDirectory());
    Files.deleteIfExists(extensibleHome);
  }

  /**
   * Test to try and get the same instance of the standard parser view. This
   * should be the same instance as it is a singleton.
   */
  @Test
  public void testGetSingleton() {
    assertTrue("Could not get the same instance to ensure this is a singleton",
        StandardView.getInstance() == StandardView.getInstance());
  }

  /**
   * Test to try and click on the select file btn. This should open the file
   * dialog.
   */
  @Test
  public void testSelectBtn() {
    verifyThat("#selectBtn", hasText("Select File"));
  }

  /**
   * Test to try and click on the reload parser button.
   */
  @Test
  public void testReloadBtn() {
    verifyThat("#reloadBtn", hasText("Reload Plugins"));
    clickOn("#reloadBtn");
  }

  /**
   * Test to try and set the selected file. This should be the same file as the
   * one returned in the get selected file method.
   */
  @Test
  public void testSetSelectedFile() {
    testView.setSelectedFile(testGseFile.toFile());
    assertEquals("Could not successfully set the file in StandardParserView", testGseFile.toFile(),
        testView.getSelectedFile());
  }

  /**
   * Test to click the parse button without selecting a file. This should output
   * the error message to the output label.
   * 
   * @throws IOException .
   */
  @Test
  public void testSetParserResult() throws IOException {
    clickOn("#parseBtn");
    verifyThat("#outputLabel", hasText(
        "An Illegal argument was provided. " 
            + "This may be due to the file not existing or the parser not existing"));
  }

  /**
   * Test to set the set of clustering names to use. This should then be returned.
   * This should include the name given.
   */
  @Test
  public void testSetClusteringSet() {
    HashSet<String> clusteringSet = new HashSet<>();
    clusteringSet.add("Example");
    testView.setClusteringPluginSet(clusteringSet);
    assertTrue("Could not get the expected clustering set from setClustering set method",
        testView.getClusteringPluginSet().contains("Example"));
  }

  /**
   * Test to set the set of parser names to use. This should be the same set as is
   * returned and should contain the key given.
   */
  @Test
  public void testSetParserSet() {
    HashSet<String> parserSet = new HashSet<>();
    parserSet.add("Example");
    testView.setParserPluginsSet(parserSet);
    assertTrue("Could not get the expected set from the setParser set method",
        testView.getParserPluginsSet().contains("Example"));
  }

  /**
   * Test to try and get the selected parser when none has been selected. This
   * should return null as no item has been selected.
   */
  @Test
  public void testGetSelectedParserNoneSelected() {
    assertTrue("Could not get the expected return value when no parser has been selcted.",
        testView.getSelectedParserName() == null);
  }

  /**
   * Test to try and cluster data, this should display that the parser selected
   * does not exist as one has not been chosen.
   */
  @Test
  public void testClusterData() {
    clickOn("#clusterBtn");
    verifyThat("#outputLabel", hasText(
        "An Illegal argument was provided. " 
            + "This may be due to the file not existing or the parser not existing"));
  }

  /**
   * Test to try and see if the clustering drop down is disabled. This should not
   * be the case as it needs to be enabled to allow for different clustering
   * algorithms to be supported.
   */
  @Test
  public void testClusterDropDownIsEnabled() {
    verifyThat("#clusteringDropDown", isEnabled());
  }

  /**
   * Test to try and reload plugins. This should display the number of plugins
   * reloaded.
   */
  @Test
  public void testReloadPlugins() {
    clickOn("#reloadBtn");
    verifyThat("#outputLabel", hasText("Successfully reloaded plugins. 0 plugins were loaded."));
  }

  /**
   * Test to try and get the selected clustering name when none have been
   * selected. This should return null.
   */
  @Test
  public void testGetUnselectedClustering() {
    assertTrue("Could not get the expected return value when no clustering algo has been selcted.",
        testView.getSelectedClusteringAlgoName() == null);
  }

  /**
   * Test to try and visualise data. This should say finished visualisation, as
   * the error should be displayed in a new scene.
   */
  @Test
  public void testVisualiseData() {
    clickOn("#visualisationBtn");
    verifyThat("#outputLabel", hasText("Finished Visualisation"));
  }
  
  /**
   * Test to try and get the selected visualisation method when none have been
   * selected.
   * This should return null.
   */
  @Test
  public void testGetSelectedVisualisationMethod() {
    assertEquals("Could not get the selected visualisation method with non selected",
        null, testView.getSelectedVisualisationMethod());
  }

}
