package extensibleclustering.application;

import extensibleclustering.dependencies.DirectoryHelper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  
  private static FXMLLoader loader;
  private static Parent root;
  private static StandardViewInterface parserView;

  @Override
  public void start(Stage primaryStage) throws IOException {
    loader = new FXMLLoader(extensibleclustering.application
        .Main.class.getClassLoader().getResource("StandardView.fxml"));
    root = loader.load();
    parserView = loader.getController();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Extensible Clustering");
    primaryStage.setResizable(false);
    setUpInitialFiles();
    ExtensibleClusteringController.getInstance().setParserView(parserView, primaryStage);
    ExtensibleClusteringController.getInstance().loadPlugins();
    primaryStage.show();
  }

  /**
   * Main method for the application entry-point.
   * This loads the fxml resource and sets the view. 
   * @param args - args given to main method, these are not currently used.
   * @throws IOException - Thrown if the fxml file cannot be loaded.
   */
  public static void main(String[] args) throws IOException {
    launch(args);
    
  }
  
  /**
   * Sets up the initial directories and copies in the GMA parser jar so it can be used.
   */
  private static void setUpInitialFiles() {
    DirectoryHelper dirHelper = new DirectoryHelper();
    dirHelper.createRequiredDirectories();
    try {
      
      InputStream gmaParserJar = extensibleclustering.application.Main.class
          .getClassLoader().getResourceAsStream("GMA-Series-Matrix-Parser.jar");
      Path destGmaParserJar = Paths.get(dirHelper.getPlugInDirectory() 
          + File.separator + "GMA-Series-Matrix-Parser.jar");
      
      InputStream clusteringJar = extensibleclustering.application.Main.class
          .getClassLoader().getResourceAsStream("K-Means-Clustering.jar");
      Path destClusteringJar = Paths.get(dirHelper.getPlugInDirectory() 
          + File.separator + "K-Means-Clustering.jar");
      
      InputStream visualisationJar = extensibleclustering.application.Main.class
          .getClassLoader().getResourceAsStream("AHC-Dendrogram-Visualisation.jar");
      Path destVisualisationJar = Paths.get(dirHelper.getPlugInDirectory() 
          + File.separator + "AHC-Dendrogram-Visualisation.jar");
      
      InputStream dnaParserJar = extensibleclustering.application.Main.class
          .getClassLoader().getResourceAsStream("DNA-SEQ-Parser.jar");
      Path destDnaParserJar = Paths.get(dirHelper.getPlugInDirectory() 
          + File.separator + "DNA-SEQ-Parser.jar");
      
      InputStream hierarchicalClusteringJar = extensibleclustering.application.Main.class
          .getClassLoader().getResourceAsStream("Agglomerative-Hierarchical-Clustering.jar");
      Path destHierarchicalClusteringJar = Paths.get(dirHelper.getPlugInDirectory() 
          + File.separator + "Agglomerative-Hierarchical-Clustering.jar");

      
      if (!Files.exists(destGmaParserJar) || !Files.exists(destClusteringJar) 
          || !Files.exists(destVisualisationJar) || !Files.exists(destDnaParserJar) 
          || !Files.exists(destHierarchicalClusteringJar)) {
        Files.copy(gmaParserJar, destGmaParserJar, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(clusteringJar, destClusteringJar, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(visualisationJar, destVisualisationJar, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(dnaParserJar, destDnaParserJar, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(hierarchicalClusteringJar, destHierarchicalClusteringJar,
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
    
  }
}
