package extensibleclustering.application;

import extensibleclustering.plugins.ImportedPlugins;

/**
 * Class that is used for a method that needs to be used in a task 
 * that returns a set to use in the UI.
 * @author David Cook
 */
@FunctionalInterface
public interface ImportedPluginObserver {

  /**
   * Returns the a Set of Strings to the method. This can be used in tasks to modify the UI.
   * @return - The ImportedPlugins found by the caller.
   */
  public ImportedPlugins callMethod();
  
}
