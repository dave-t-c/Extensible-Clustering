package clustering;

import java.util.Arrays;
import java.util.Vector;

public class Metrics {
  
  /**
   * Calculate the variance within the clusters.
   * This requires the clusters to have assigned data, 
   * as otherwise this will return 0.
   * @param clusters - The clusters to calculate the total within variance for.
   * @return - Total within cluster variance for all of the given clusters.
   */
  public Double withinClusterVariance(Centroid[] clusters) {
    //Check to see if the clusters are null.
    if (clusters == null) {
      return 0.0;
    }
    
    //For each cluster, we need to calculate the average assigned
    //For each position in that cluster, we need to find the distance between
    //the position and the average of the cluster. This value is then squared.
    //The total of these values is then added to the total sum for all clusters.
    
    //This method needs to be run after the data has been clustered, but before the centre 
    //has been set.
    
    Double totalSum = 0.0;
    //For each cluster calculate the average.
    for (Centroid currCluster : clusters) {
      //Calculate the average position.
      //This is the same as setting the new centre of the cluster, so we can set the 
      //the centre, then get the location of the cluster.
      currCluster.setCentre();
      
      Double clusterTotal = 0.0;
      
      //For each of the assigned positions, get the distance between the centre
      //and the position, square this value and add to cluster total.
      for (Position currPos : currCluster.getAssignedPositions()) {
        Double distanceBetween = 0.0;
        try {
          distanceBetween = currCluster.getLocation().getDistance(currPos);
        } catch (IncomparableComponentsException e) {
          e.printStackTrace();
        }
        clusterTotal += Math.pow(distanceBetween, 2);
      }
      
      //Once we have completed it for each assigned position, we can add the clusterTotal
      //To the totalSum, and move on to the next cluster.
      totalSum += clusterTotal;
    }
    
    return totalSum;
  }
  
  /**
   * Method that calculates the between cluster variance.
   * This requires at least 2 clusters.
   * @param clusters - The clusters to calculate the between variance for.
   * @return - The total between cluster variance.
   */
  public Double betweenClusterVariance(Centroid[] clusters) {
    
    //Check to see if the clusters given are null.
    if (clusters == null) {
      return 0.0;
    }
    
    //First we need to find the average position, of all of the pieces of data
    //that are present, i.e. assigned to clusters.
    //to do this, we can iterate over each position and get the total for each dimension.
    
    //Calculate the average position of all of the pieces of data.
    //This can then be transformed into a position.
    int numOfData = 0;
    Double[] totalOfData = new Double[clusters[0].getLocation().getComponents().length];
    Arrays.fill(totalOfData, 0.0);
    
    for (Centroid cluster : clusters) {
      Vector<Position> assignedPositions = cluster.getAssignedPositions();
      numOfData += assignedPositions.size();
      
      //Want to go through each positions components, and add to total.
      for (Position currPos : assignedPositions) {
        Double[] components = currPos.getComponents();
        for (int i = 0; i < components.length; i++) {
          totalOfData[i] += components[i];
        }
      }
    }
    
    //Now we have gone through all of the positions, 
    //we can calculate the average for each dimension.
    
    //We can also return 0.0, if there are no pieces of data.
    if (numOfData == 0) {
      return 0.0;
    }
    
    for (int i = 0; i < totalOfData.length; i++) {
      totalOfData[i] /= numOfData;
    }
    
    //We can now create a position class with this location.
    Position averagePos = new Position("Data Average", totalOfData);
    
    Double totalB = 0.0;
    for (Centroid cluster : clusters) {
      Double clusterB = 0.0;
      cluster.setCentre();
      
      try {
        //Get the distance between the centroid and the average location
        clusterB = averagePos.getDistance(cluster.getLocation());
      } catch (IncomparableComponentsException e) {
        e.printStackTrace();
      }
      
      //Square this distance, then multiply it by the count of data assigned to the cluster.
      clusterB = Math.pow(clusterB, 2);
      clusterB *= cluster.getAssignedPositions().size();
      
      //Then add this value to the total.
      totalB += clusterB;
    }
    return totalB;
    
  }

  /**
   * Calculate the CH score for given clusters and num of positions.
   * @param clusters - clusters to use in the calculation. K is determined from the array.
   * @param numPositions - Number of positions in the data.
   * @return - The CH score for these number of clusters and positions.
   */
  public Double calinskiHarabasz(Centroid[] clusters, Integer numPositions) {
    if (clusters == null || numPositions == null || numPositions <= clusters.length) {
      return 0.0;
    }
    
    //We need to calculate the top and bottom halves of the equation.
    //Top = between score / number of clusters - 1
    Double top = betweenClusterVariance(clusters) / (clusters.length - 1);
    
    //Bottom = within variance / (number of positions - number of clusters)
    Double bottom = withinClusterVariance(clusters) / (numPositions - clusters.length);
    
    return (top / bottom);
  }

}
