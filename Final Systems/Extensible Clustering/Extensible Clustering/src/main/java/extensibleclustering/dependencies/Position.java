package extensibleclustering.dependencies;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class for storing a position.
 * This will handle storing different dimensions of data.
 * This will also have an ID to identify the position.
 * @author David Cook
 * @version 0.1
 */
public class Position {
  
  private String id = "";
  private Double[] components;
  
  /**
   * Creates a new instance of the position class.
   * @param id - ID for the new position.
   * @param components - The components for the new position.
   */
  public Position(String id, Double[] components) {
    if (id == null || components == null) {
      throw new IllegalArgumentException();
    }
    this.id = id;
    this.components = components.clone();
  }
  
  /**
   * Returns the ID for this position.
   * @return String - ID for this position.
   */
  public String getID() {
    return id;
  }
  
  /**
   * Return the components for this position.
   * @return Double[] - The components for this position.
   */
  public Double[] getComponents() {
    return components;
  }
  
  /**
   * Calculates the euclidean distance between two positions.
   * @param otherPosition - Position to get the distance between.
   * @return Double - Euclidean distance between the two positions.
   * @throws IncomparableComponentsException - Thrown if the Positions cannot be compared.
   */
  public Double getDistance(Position otherPosition) throws IncomparableComponentsException {
    if (otherPosition == null || this.components.length != otherPosition.getComponents().length) {
      throw new IncomparableComponentsException();
    }
    Double total = 0.0;
    Double[] otherPositionComponents = otherPosition.getComponents();
    for (int i = 0; i < components.length; i++) {
      total += Math.pow((components[i] - otherPositionComponents[i]), 2);
    }
    return Math.sqrt(total);
  }
  
  /**
   * Generates a hash code for the Position Object.
   * @return int - hash code for this Position.
   */
  @Override
  public int hashCode() {
    /* This method requires the use of both Objects.hash and Arrays.hashCode
     * as Objects.hash does not handle Arrays well and generates different hash codes 
     * for the same array, for example, {0.0} and {0.0} generate different hash codes.
     */
    return Objects.hash(id) + Arrays.hashCode(components);
  }
  
  /**
   * Returns the values of the Position in a formatted String.
   * @return String - formatted values of the Position.
   */
  @Override
  public String toString() {
    return "ID: " + id + ", Components: " + Arrays.toString(components);
  }
  
  /**
   * Returns the equality of an Object and this Position.
   * @param obj - Object to compare the Position against.
   * @return boolean - Result of the equality test between the Object and Position.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    Position otherPos;
    try {
      otherPos = (Position) obj;
    } catch (ClassCastException ex) {
      return false;
    }
    return id.equals(otherPos.getID()) && Arrays.equals(components, otherPos.getComponents());
  }
  
  /**
   * Checks if the components of two Positions are equal.
   * @param other - The Position to compare against.
   * @return - Result of the equality check between the components.
   */
  public boolean equalComponents(Position other) {
    return Arrays.equals(components, other.getComponents());
  }
}
