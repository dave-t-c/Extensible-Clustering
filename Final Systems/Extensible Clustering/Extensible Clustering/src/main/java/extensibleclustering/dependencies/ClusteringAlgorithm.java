package extensibleclustering.dependencies;

import java.nio.file.Path;

/**
 * This interface will need to be implemented by
 * all clustering algorithms so they can be used by 
 * the system.
 * @author David Cook
 * @version 0.1
 */
public interface ClusteringAlgorithm {

  /**
   * This method will cluster the data. 
   * This will need to output the results to a file so that they can then be 
   * re-interpreted by the visualisation method.
   * @param data - The Positions of the data to be clustered.
   * @param fileName - The name of the file that is being parsed.
   * @return - The File the data has been output to. 
   */
  public Centroid[] clusterData(Position[] data, String fileName);
  
  /**
   * Gets the last file that the clustering algorithm output to.
   * @return - The last file that this clustering algorithm output to.
   */
  public Path getLastOutputFile();
  
  /**
   * Returns the name of the clustering algorithm so it can be displayed 
   * to the user when they are choosing an algorithm to use.
   * @return - The name of the clustering algorithm.
   */
  public String getName();
  
  /**
   * Returns a brief description of the clustering algorithm.
   * This will be displayed to users when they are choosing which algorithm to use.
   * This could include the types of data this clustering algorithm works with for example.
   * @return - Returns a description of the clustering algorithm.
   */
  public String getDescription();
  
}
