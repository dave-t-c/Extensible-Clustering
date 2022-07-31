package extensibleclustering.dependencies;

import java.io.IOException;
import java.nio.file.Path;
import javafx.scene.Scene;

/**
 * Interface that must be implemented by a visualisation method.
 * @author David Cook
 */
public interface Visualisation {
  
  /**
   * Method that visualises the data in the file given. 
   * @param dataFile - Data to visualise.
   * @return - Scene - The visualised data in a JavaFX Scene
   * @throws IOException  - Thrown if an error occurs reading the file.
   */
  public Scene visualiseData(Path dataFile) throws IOException;
  
  /**
   * Method that returns the name of the visualisation to the user.
   * @return - String - Name of the visualisation method.
   */
  public String getName();
  
  /**
   * Returns the description of the visualisation method.
   * This will be used to give insight to the user as to what insights this
   * visualisation provides.
   * @return - String - The description of this visualisation method.
   */
  public String getDescription();

}
