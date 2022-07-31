package visualisation;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class for running the dendrogram visualisation on its own.
 * @author David Cook
 */
public class Main  extends Application {
  
  /**
   * Main method for running the visualisation method.
   * @param args - Args given to the system. These are not used.
   * @throws IOException - Thrown if an error occurs when reading the file.
   */
  public static void main(String[] args) throws IOException {
    launch(args);    
  }

  @Override
  public void start(Stage stage) throws Exception {
    Scanner readIn = new Scanner(System.in);
    System.out.println("Please enter file to visualise");
    System.out.flush();
    String fileLocation = readIn.nextLine();
    readIn.close();
    Path dataPath = Paths.get(fileLocation);
    System.out.println(dataPath);
    System.out.println(dataPath.toFile().exists());
    DendrogramVisualisation vis = new DendrogramVisualisation();
    Scene visualisationScene = vis.visualiseData(dataPath);
    stage.setScene(visualisationScene);
    stage.setTitle(vis.getName());
    stage.show();
  }

}
