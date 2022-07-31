package extensibleclustering.application;

import javafx.scene.Scene;

/**
 * Functional interface for returning a Scene to the controller for 
 * visualising data.
 * @author David Cook
 */
@FunctionalInterface
public interface SceneObserver {
  
  public Scene callMethod();

}
