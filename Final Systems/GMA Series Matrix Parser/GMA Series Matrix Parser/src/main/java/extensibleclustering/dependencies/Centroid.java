package extensibleclustering.dependencies;

import extensibleclustering.dependencies.Position;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

/**
 * Class for representing a centroid.
 * This class can be used to implement K-Means clustering for example.
 * @author David Cook
 * @version 0.1
 */
public class Centroid {
  
  private Vector<Position> assignedPositions;
  private String id;
  private Position location;
  
  /**
   * Constructor for creating a new centroid.
   * @param id - The id to be assigned. This should be unique.
   * @param location - The initial location for the centroid. 
   */
  public Centroid(String id, Position location) {
    if (id == null || location == null) {
      throw new IllegalArgumentException();
    }
    this.id = id;
    this.location = location;
    assignedPositions = new Vector<Position>();
  }
  
  /**
   * Returns the ID of the centroid.
   * @return - String ID value for the centroid.
   */
  public String getID() {
    return id;
  }

  /**
   * Method for assigning a position to a centroid.
   * If the position given is null, the position will not 
   * be assigned to the centroid.
   * @param position - The position to assign.
   */
  public void assignPosition(Position position) {
    if (position != null 
        && position.getComponents().length == this.location.getComponents().length) {
      this.assignedPositions.add(position);      
    }
  }

  /**
   * Return the Positions that have been assigned to 
   * this centroid.
   * @return - Vector of Positions containing all assigned positions.
   */
  public Vector<Position> getAssignedPositions() {
    return assignedPositions;
  }
  
  /**
   * Clears the assigned positions from the centroid.
   * This can be used after the new centre has been set.
   */
  public void clearAssignedPositions() {
    assignedPositions.clear();
  }

  /**
   * Returns the current location of the centroid.
   * @return - Position instance for the location of the centroid.
   */
  public Position getLocation() {
    return this.location;
  }
  
  /**
   * Method for setting the location of the centroid to the 
   * centre of all of the assigned positions.
   * This can be used in K-Means clustering to change the location of 
   * the centroid after data has been assigned.
   */
  public void setCentre() {
    //If there are no assigned positions, the location of the centroid should
    //not change.
    if (assignedPositions.size() == 0) {
      return;
    }
    
    //Go though each of the items that have been assigned to the centroid.
    //Get the sum total for each dimension.
    //Then divide each dimension by the number of assigned positions.
    Double[] updatedLocation = new Double[this.location.getComponents().length];
    Arrays.fill(updatedLocation, 0.0);
    
    for (Position currPos : this.assignedPositions) {
      for (int component = 0; component < updatedLocation.length; component++) {
        updatedLocation[component] += currPos.getComponents()[component];
      }
    }
    
    //Go through each of the components and calculate the average by dividing
    //the total by the number of assigned positions.
    for (int i = 0; i < updatedLocation.length; i++) {
      updatedLocation[i] /= this.assignedPositions.size();
    }
    
    //Preserve the ID of the position so it is not updated each time.
    this.location = new Position(this.location.getID(), updatedLocation);
    
  }
  
  /**
   * Returns a readable string that contains all of the information relating
   * to the centroid.
   */
  @Override
  public String toString() {
    return "ID: " + this.id + ", Location: {" + this.location.toString()
        + "}, Assigned Positions: {" + this.assignedPositions.toString() + "}";
  }
  
  /**
   * Returns a hash code for the centroid.
   * This uses the id, the location and the assigned positions in 
   * the calculation.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.location, this.assignedPositions);
  }
  
  /**
   * Performs an equality check between the centroid and the 
   * object given as an argument. Returns true if the objects are equal, 
   * false if they are not.
   * @param obj - The object to compare the centroid against.
   * @return - The equality of the centroid and the object. 
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    Centroid otherClusteroid;
    try {
      otherClusteroid = (Centroid) obj;
    } catch (ClassCastException ex) {
      return false;
    }
    return this.id.equals(otherClusteroid.id) 
        && this.location.equals(otherClusteroid.location)
        && this.assignedPositions.equals(otherClusteroid.assignedPositions);
  }
}