package clustering;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Class that implements a k-means clustering algorithm.
 * @author David Cook
 */
public class KMeansClustering implements ClusteringAlgorithm {
  
  Metrics metrics = new Metrics();
  Path outputDirectory = Paths.get(System.getProperty("user.home")
      + File.separator + "Documents" + File.separator + "Extensible Clustering" 
      + File.separator + "Output");
  Path lastOutputFile = null;

  /**
   * CLusters data using a k-means algorithm.
   * This returns the resulting clusters with their assigned positions.
   * @param data - The data to cluster.
   * @param fileName - The name of the file the data is from. This will be used in the output.
   * @return clusters - the resulting clusters with their assigned positions.
   */
  @Override
  public Centroid[] clusterData(Position[] data, String fileName) {
    if (data.length < 2) {
      throw new IllegalArgumentException("Data is not long enough to cluster");
    }
    //Calculate the number of clusters, then use that for initialisation.
    int numClusters = getNumOfClusters(data);
    //Run the garbage collector to collect the clusters from before. 
    Runtime.getRuntime().gc();
    Centroid[] clusters = initialiseCentroids(data, numClusters);
    Centroid[] previous = clusters.clone();
    //We will always need to run at least one clustering operation.
    runIteration(data, clusters);
    while (!hasConverged(previous, clusters)) {
      //Clear the current clusters
      clearCentroids(clusters);
      
      //Set the previous equal to the current, so it can be compared.
      previous = clusters.clone();      
      
      //Run another iteration
      runIteration(data, clusters);
      
      //Run the garbage collector to collect the old clusters.
      Runtime.getRuntime().gc();
    }
    
    //Once clustering has been completed, the data can be 
    //output to a file
    lastOutputFile = outputData(clusters, data.length, fileName);
    //Return the resulting clusters with their assignments.
    return clusters;
  }

  /**
   * Method that returns the file that was last successfully output to.
   * @return - Path for the last successfully output file.
   */
  @Override
  public Path getLastOutputFile() {
    return lastOutputFile;
  }

  /**
   * Returns the name of this clustering algorithm.
   * This can be used to display to the user.
   * @return - name of the clustering algorithm.
   */
  @Override
  public String getName() {
    return "K-Means Clustering";
  }

  /**
   * Returns the description of this clustering algorithm.
   * @return Description of this algorithm in the format of a string.
   */
  @Override
  public String getDescription() {
    return "Splits a data set into K Clusters. For more info, please see the javadoc.";
  }
  
  private Path outputData(Centroid[] clusters, int dataLength, String dataFile) {
    //The file name will be a combination of the algorithm name, 
    // and the time that it was completed.
    String completedTime = LocalDateTime.now().toString();
    String fileName = getName() + "-" + completedTime + ".tsv";
    Path outputFile = null;
    try {
      outputFile = Files.createFile(
          Paths.get(outputDirectory.toString() + File.separator + fileName));
      BufferedWriter writeFile = new BufferedWriter(new FileWriter(outputFile.toFile()));
      String newLine = System.lineSeparator();
      char separator = '\t';
      
      //Output information on the data used and generated to the file.
      writeFile.write("Type of clustering: K-Means Clustering" + newLine);
      writeFile.write("Time Completed: " + completedTime + newLine);
      writeFile.write("Number of clusters generated: " + clusters.length + newLine);
      writeFile.write("Number of data points used: " + dataLength + newLine);
      writeFile.write("File used: " + dataFile + newLine);
      writeFile.write("Clusters" + newLine);
      writeFile.write("Cluster ID\tNum of assigned positions\tLocation" + newLine);
      
      //Add each of the clusters to the output
      for (Centroid cluster : clusters) {
        writeFile.write(cluster.getID() + separator + cluster.getAssignedPositions().size() 
            + separator + Arrays.toString(cluster.getLocation().getComponents()) + newLine);
      }
      
      //Output the start of the Positions section
      writeFile.write("Data Points Used" + newLine);
      writeFile.write("Position ID\tAssigned Cluster\tLocation" + newLine);
      
      //Run through all of the positions from each cluster.
      for (Centroid cluster : clusters) {
        for (Position pos : cluster.getAssignedPositions()) {
          writeFile.write(pos.getID() + separator + cluster.getID() + separator 
              + Arrays.toString(pos.getComponents()) + newLine);
        }
      }
      
      writeFile.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return outputFile;
  }
  
  /**
   * Method that assigns a specified position to the closest cluster
   * in an array of clusters.
   * The method will pick the first cluster found
   * if two clusters have the same distance to the position.
   * @param position The position to assign.
   * @param clusters The array of clusters to assign the position to.
   */
  public void assignPosition(Position position, Centroid[] clusters) {
    if (position == null || clusters == null) {
      return;
    }
    //Iterate through the clusters and see if it is the smallest distance.
    //Store the index and the distance.
    int smallestIndex = -1;
    //Set the smallest distance to max value so if there is valid value it could be used.
    Double smallestDistance = Double.MAX_VALUE;
    for (int i = 0; i < clusters.length; i++) {
      //Set the currDistance to the max value, so if it is not modified, it will not be used.
      Double currDistance = Double.MAX_VALUE;
      try {
        currDistance = position.getDistance(clusters[i].getLocation());
      } catch (IncomparableComponentsException e) {
        e.printStackTrace();
      }
      //If the current distance is less than the smallest distance, it will be set as the smallest 
      //index so the position can be assigned.
      if (currDistance < smallestDistance) {
        smallestDistance = currDistance;
        smallestIndex = i;
      }
    }
    clusters[smallestIndex].assignPosition(position);
  }
  
  /**
   * Calculate the best number of clusters for a given data set.
   * @param data - the data set to calculate the number of clusters for.
   * @return - Best number of clusters for this data set, calculated by its CH index.
   */
  public int getNumOfClusters(Position[] data) {
    Double bestChScore = Double.MIN_VALUE;
    int bestNum = -1;
    //This will use random initialisation
    //This will be calculated by starting with 2, then increasing the number of clusters by 1.5x, 
    //until the max number is reached. 
    for (int numClusters = 2; numClusters < data.length; numClusters *= 1.5) {
      //Initiate the clusters for up to and including i clusters.
      Centroid[] clusterArr = new Centroid[numClusters];
      for (int j = 0; j < numClusters; j++) {
        Centroid cluster = new Centroid("Cluster-" + j, data[j]);
        clusterArr[j] = cluster;
      }
      
      //Assign all the positions.
      //This uses a stream, which means it can be completed in parallel.
      //This speeds up the execution time but uses multiple threads.
      Arrays.stream(data).parallel()
              .forEach(currPos -> assignPosition(currPos, clusterArr));
      
      //Then calculate the ch value
      Double chScore = metrics.calinskiHarabasz(clusterArr, data.length);
      if (chScore > bestChScore) {
        bestChScore = chScore;
        bestNum = numClusters;
      }
      
      //Invoke the garbage collector as java does not do this in some cases.
      Runtime.getRuntime().gc();
    }
    return bestNum;
  }
  
  /**
   * Runs a single iteration of k means.
   * This method assigns the data to the closest centroid and 
   * then sets the new centre of the centroid.
   * This method does not clear the items.
   * @param data - The data to run an iteration with.
   * @param clusters - The clusters to assign data to.
   */
  public void runIteration(Position[] data, Centroid[] clusters) {
    //Assign each position to the correct cluster
    //This is completed in parallel as there is likely to be many positions.
    Arrays.stream(data).parallel()
    .forEach(currPos -> assignPosition(currPos, clusters));
    
    //Set the new centre for each cluster after all of the data has been assigned.
    //Set the centres in parallel. The benefits of this will occur if there are many clusters.
    Arrays.stream(clusters).parallel()
    .forEach(Centroid::setCentre);
  }
  
  /**
   * Method for initialising a specified number of clusters using the data given.
   * This will use a distortion function that measures the within cluster variance
   * to try and find the best randomly initialised clusters.
   * @param data - the data used to help initialise the clusters.
   * @param numOfClusters - The number of clusters to be generated.
   * @return - an array containing the generated clusters with their initialised locations.
   */
  public Centroid[] initialiseCentroids(Position[] data, int numOfClusters) {
    //Set the best and min-value variables for storing the initialisation with 
    //the lowest within cluster variation.
    Centroid[] best = new Centroid[numOfClusters];
    Double minValue = Double.MAX_VALUE;
    Random random = new Random();
    //This currently operates using a fixed number of iterations,
    //However, this could be updated to a custom value.
    for (int i = 0; i < 100; i++) {
      //Setup a new list that uses the data, and a new array to store the current clusters.
      ArrayList<Position> posList = new ArrayList<>(Arrays.asList(data));
      Centroid[] currClusters = new Centroid[numOfClusters];
      
      //Pick a random item from the list to use as the location for the centroid.
      //Then remove it from the list so it can't be used twice.
      for (int currIndex = 0; currIndex < numOfClusters; currIndex++) {
        int randIndex = random.nextInt(posList.size());
        currClusters[currIndex] = new Centroid("Cluster-" + currIndex, posList.get(randIndex));
        posList.remove(randIndex);
      }
      //Run an iteration that assigns all of the data to a cluster and then centres them.
      this.runIteration(data, currClusters);
      
      //Calculate the within variation, this will want to be minimised.
      Double score = metrics.withinClusterVariance(currClusters);
      
      //Remove all of the assigned positions now the score has been calculated.
      clearCentroids(currClusters);
      
      //Now the items have been cleared, the centroids can potentially be cloned.
      if (score < minValue) {
        minValue = score;
        best = currClusters.clone();
      }
    }
    return best;
  }
  
  /**
   * Check if the k means algorithm has converged by checking if the 
   * locations of the centroids have changed.
   * @param before - the centroids from the previous iteration.
   * @param current - the centroids from the current iteration.
   * @return - Returns if the locations of the centroids have not changed, i.e. it has converged.
   */
  public boolean hasConverged(Centroid[] before, Centroid[] current) {
    //Go through each of the items, see if it is in the current assigned positions.
    //If not, then add one to count of different.
    int totalChanged = 0;
    int dataSize = 0;
    //Convert current to hash map to allow for the order to change.
    HashMap<String, Centroid> currentMap = new HashMap<>();
    for (Centroid cluster : current) {
      currentMap.put(cluster.getID(), cluster);
    }
    for (int i = 0; i < before.length; i++) {
      for (Position pos : before[i].getAssignedPositions()) {
        dataSize++;
        if (!currentMap.get(before[i].getID()).getAssignedPositions().contains(pos)) {
          totalChanged++;
        }
      }
    }
    if (dataSize < 20) {
      return totalChanged == 0;
    } else {
      return totalChanged < (0.05 * dataSize);      
    }
  }
  
  /**
   * Clears the assigned positions from all of the centroids given in the array.
   * @param centroids - centroids to remove all assigned positions from.
   */
  public void clearCentroids(Centroid[] centroids) {
    for (Centroid centroid : centroids) {
      centroid.clearAssignedPositions();
    }
  }

}
