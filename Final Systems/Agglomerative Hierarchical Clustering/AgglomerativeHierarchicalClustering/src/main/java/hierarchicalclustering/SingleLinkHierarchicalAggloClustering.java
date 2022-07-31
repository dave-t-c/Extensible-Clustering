package hierarchicalclustering;

import extensibleclustering.dependencies.Centroid;
import extensibleclustering.dependencies.ClusteringAlgorithm;
import extensibleclustering.dependencies.DirectoryHelper;
import extensibleclustering.dependencies.IncomparableComponentsException;
import extensibleclustering.dependencies.Position;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 * Implements the single link variant of agglomerative 
 * hierarchical clustering.
 * This implements the Clustering Algorithm interface so it can be detected 
 * by the plug-in loader in the extensible clustering system. 
 * @author David Cook
 */
public class SingleLinkHierarchicalAggloClustering implements ClusteringAlgorithm {
  
  private Path lastOutputPath;
  private Path outputDirectory = new DirectoryHelper().getOutputDirectory();

  /**
   * Performs Single Link Agglomerative Hierarchical Clustering.
   * @param data - Data to cluster
   * @param fileName - The file used to get the data. This is used for outputting results.
   * @return Centroid[] - An array with the single remaining centroid.
   */
  @Override
  public Centroid[] clusterData(Position[] data, String fileName) {
    ArrayList<Centroid> clusterList = new ArrayList<>();
    
    for (Position pos : data) {
      clusterList.add(new Centroid(pos.getID(), pos));
      clusterList.get(clusterList.size() - 1).assignPosition(pos);
    }
    
    //Create the file the result will be stored in.
    lastOutputPath = createOutputFile(data.length, fileName);
    
    HashMap<String, Double> distancesMap = new HashMap<>(); 
    int height = 1;
    while (clusterList.size() > 1) {
      try {
        performIteration(clusterList, distancesMap, height);
        height++;
      } catch (IncomparableComponentsException e) {
        e.printStackTrace();
      }
    }
    
    //Now the data is in a single cluster, we can output the data used.
    appendUsedDataPoints(clusterList.get(0));
    
    return clusterList.toArray(new Centroid[clusterList.size()]);
  }
  
  /**
   * Creates the output file that will be used to output the results of the clustering.
   * @param dataLength - The length of the data used. This is used as a metric.
   * @param dataFile - The name of the file used for data. This tells the user which file was used.
   * @return Path - The Path to the output file created.
   */
  private Path createOutputFile(int dataLength, String dataFile) {
    LocalDateTime dateTime = LocalDateTime.now();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy_HH:mm:ss");
    String startedTime = dateTime.format(dateFormatter);
    startedTime = startedTime.replace(":", "");
    String fileName = getName() + "-" + startedTime + ".tsv";
    Path outputPath = null;
    try {
      outputPath = Files.createFile(
          Paths.get(outputDirectory.toString() + File.separator + fileName));
      BufferedWriter writeFile = new BufferedWriter(new FileWriter(outputPath.toFile()));
      String newLine = System.lineSeparator();
      
      //Output information on the data used and generated to the file.
      writeFile.write("Type of clustering: " + getName() + newLine);
      writeFile.write("Time Started: " + startedTime + newLine);
      writeFile.write("Number of data points used: " + dataLength + newLine);
      writeFile.write("File used: " + dataFile + newLine);
      
      //Write the outline for the merged IDs. The data will be added in a different function.
      writeFile.write("Merged IDs and Height Merged At" + newLine);
      writeFile.write("Height\tMerged ID A\tMerged ID B" + newLine);
      writeFile.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return outputPath;
  }
  
  /**
   * Appends the current status of the clusters to the output file.
   * @param height - The height at which the IDs are merged.
   * @param mergedIDa - The ID of the first cluster to be merged.
   * @param mergedIDb - The ID of the second cluster to be merged.
   */
  private void appendClusterStatus(int height, String mergedIDa,
      String mergedIDb) {
    try {
      //If the path is null then it has not been set so should not be written to.
      if (lastOutputPath == null) {
        return;
      }
      
      char entrySeparator = '\t';
      //The data is written as following:
      //Current height  mergedIDs (each of the remainingIDs separated by ::)
      StringBuilder sb = new StringBuilder(String.valueOf(height) + entrySeparator + mergedIDa 
          + entrySeparator + mergedIDb);
      
      //Add the new line to the end 
      sb.append(System.lineSeparator());
      
      //The true arg means the file is not overwritten, it is instead appended to if it exists.
      BufferedWriter writeFile = new BufferedWriter(new FileWriter(lastOutputPath.toFile(), true));
      writeFile.append(sb.toString());
      
      writeFile.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Appends the data points used in clustering so the data can be accessed if required
   * by the user. This means the data is in one place if they want to look up the ID.
   * @param finalCluster - The cluster to which all data should be assigned to.
   */
  private void appendUsedDataPoints(Centroid finalCluster) {
    //If the path is null then it has not been set so should not be written to.
    if (lastOutputPath == null) {
      return;
    }
    
    char entrySeparator = '\t';
    String lineSeparator = System.lineSeparator();
    
    try (BufferedWriter writeFile = new BufferedWriter(
        new FileWriter(lastOutputPath.toFile(), true))) {
      
      
      //Append the section heading.
      writeFile.append("Data Points Used" + lineSeparator);
      
      //Append the column headings
      writeFile.append("Position ID" + entrySeparator + "Location" + lineSeparator);
      
      //Go through the last centroid and output all of the assigned positions.
      for (Position pos : finalCluster.getAssignedPositions()) {
        writeFile.append(pos.getID() + entrySeparator 
            + Arrays.toString(pos.getComponents()) + lineSeparator);
      }
      
    } catch (IOException ex) {
      ex.printStackTrace();
      return;
    }
  }
  
  /**
   * Performs a single iteration and merges two clusters with the closest positions.
   * @param clusterList - The list of clusters to use.
   * @param distancesMap - Map of Position IDs to distances.
   * @param height - The height of the dendrogram at this iteration.
   * @throws IncomparableComponentsException - Thrown if the distance cannot be calculated.
   */
  public void performIteration(ArrayList<Centroid> clusterList, 
      HashMap<String, Double> distancesMap, int height) 
      throws IncomparableComponentsException {
    //This finds the smallest distance between any position in a cluster and any other positions
    //that are not in the cluster.
    
    //This requires going through every position in every cluster and checking the distance 
    // to every other position in every other cluster.
    
    //This could potentially be sped up by using memoization (Mapping Position IDs to distances).
    
    Double minDistance = Double.MAX_VALUE;
    Centroid closestCentroidA = null;
    Centroid closestCentroidB = null;
    
    for (Centroid cluster : clusterList) {
      for (Position clusterPosition: cluster.getAssignedPositions()) {
        for (Centroid otherCluster : clusterList) {
          //If the cluster and the other cluster is the same, do not compare positions,
          //as they should only be compared to positions in different clusters.
          if (cluster == otherCluster) {
            continue;
          }
          for (Position otherClusterPosition : otherCluster.getAssignedPositions()) {
            
            //Create the two possible keys for these two position ids.
            String keyA = clusterPosition.getID() + "->" + otherClusterPosition.getID();
            String keyB = otherClusterPosition.getID() + "->" + clusterPosition.getID();
            
            String existingKey = "";
            //Check to see if the hash map contains either of the keys.
            //If it exists, get the existing key, or add keyA to the map.
            if (distancesMap.containsKey(keyA) || distancesMap.containsKey(keyB)) {
              existingKey = distancesMap.containsKey(keyA) ? keyA : keyB;
            } else {
              distancesMap.put(keyA, clusterPosition.getDistance(otherClusterPosition));
              existingKey = keyA;
            }
            
            Double posDistance = distancesMap.get(existingKey);
            if (posDistance < minDistance) {
              minDistance = posDistance;
              closestCentroidA = cluster;
              closestCentroidB = otherCluster;
            }
          }
        }
      }
    }
    
    //Make a new centroid using the two closest.
    //The position does not matter as it will never be used, so it is given pos of the A centroid.
    
    String mergedIDa = closestCentroidA.getID();
    String mergedIDb = closestCentroidB.getID();
    Centroid mergedCentroid = new Centroid(mergedIDa 
        + "::" + mergedIDb, closestCentroidA.getLocation());
    
    //Assign the positions to the new centroid.
    assignPositions(mergedCentroid, closestCentroidA.getAssignedPositions());
    assignPositions(mergedCentroid, closestCentroidB.getAssignedPositions());
    
    //Add the new centroid and remove the merged 2 from the list.
    clusterList.add(mergedCentroid);
    clusterList.remove(closestCentroidA);
    clusterList.remove(closestCentroidB);
    
    //Update the clustering status in the output file with the updated clusters.
    appendClusterStatus(height, mergedIDa, mergedIDb);
  }
  
  /**
   * Assigns the positions to the cluter given.
   * @param cluster - The cluster to assign the positions to.
   * @param positionsToAssign - The positions to assign to the cluster.
   */
  private void assignPositions(Centroid cluster, Vector<Position> positionsToAssign) {
    for (Position pos : positionsToAssign) {
      cluster.assignPosition(pos);
    }
  }

  @Override
  public Path getLastOutputFile() {
    return lastOutputPath;
  }

  @Override
  public String getName() {
    return "Single Link Agglomerative Hierarchical Clustering";
  }

  @Override
  public String getDescription() {
    return "Performs Single-Link Agglomerative Hierarchical Clustering on this data set."
        + " This will cluster data from individual pieces of data down to a single cluster"
        + " WARNING, this does not scale well to large data sets so will take a long time to run";
  }

}
