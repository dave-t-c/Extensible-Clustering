package extensibleclustering.application;

/**
 * Functional interface for handing the method that needs 
 * to be called on the button even to the parser view.
 * This must be a functional interface as to use a method as a parameter. 
 * This class will be used when a string needs to be returned to the UI.
 * @author David Cook
 */
@FunctionalInterface
public interface StringObserver {
  /**
   * Returns a string that can be placed in the view to detail if a 
   * event was a success / failure and why.
   * @return The message to be output to the user.
   */
  public String callMethod();
}
