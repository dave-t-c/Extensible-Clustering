package testclustering;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import clustering.IncomparableComponentsException;
import clustering.Position;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for creating / developing the position class.
 * @author David Cook
 */
public class TestPosition {
  
  private Position testPosition;
  private Position duplicatePosition;
  private Position diffID;
  private Position diffComponents;
  private Position positionSingleDimension;
  private Position emptyTwoDimensionPosition;
  private Position nonEmptyTwoDimensionPosition;
  
  /**
   * Setup the variables before they are used in each test.
   * @throws Exception - thrown if the classes cannot be constructed.
   */
  @Before
  public void setUp() throws Exception {
    testPosition = new Position("1", new Double[] {0.0});
    duplicatePosition = new Position("1", new Double[] {0.0});
    diffID = new Position("2", new Double[] {0.0});
    diffComponents = new Position("1", new Double[] {1.2, 2.1, 3.0});
    positionSingleDimension = new Position("1", new Double[] {1.0});
    emptyTwoDimensionPosition = new Position("1", new Double[] {0.0, 0.0});
    nonEmptyTwoDimensionPosition = new Position("2", new Double[] {1.0, 1.0});
  }
  
  /**
   * Test to try and create a new position, the returned ID should be the same
   * as that given to the constructor.
   */
  @Test
  public void testCreateNewPosition() {
    assertEquals("Could not create new Position with same ID", "1", testPosition.getID());
  }
  
  /**
   * Test to create a position with a different ID.
   * The ID should be the same as in the constructor.
   */
  @Test
  public void testCreateDiffPositionID() {
    assertEquals("Could not create position with different ID", "2", diffID.getID());
  }
  
  /**
   * Test to try and get the Double array given to the constructor.
   * The test should return the same array given the constructor.
   */
  @Test
  public void testGetConstructorArray() {
    assertArrayEquals("Could not get the same array as given to the constructor", 
        new Double[] {0.0}, testPosition.getComponents());
  }
  
  /**
   * Test to get a different array from getComponents.
   * This should return the same array that is given 
   * to the constructor.
   */
  @Test
  public void testGetDiffComponents() {
    assertArrayEquals("Could not get a different array from get components method.",
        new Double[] {1.2, 2.1, 3.0}, diffComponents.getComponents());
  }
  
  /**
   * Test to try and get the distance between two positions.
   * As this is the same position it should return 0.
   * @throws IncomparableComponentsException -  Thrown if the components are of different length.
   */
  @Test
  public void testGetDistance() throws IncomparableComponentsException {
    assertEquals("Could not get the distance between two positions",
        0.0, testPosition.getDistance(testPosition), 0.0);
  }
  
  /**
   * Test to try and get the distance between a position at 0.0, and 1.0.
   * These positions are all single dimensions. 
   * The difference between the two should be 1.0;
   * As Sqrt((1.0-0.0)^2) == 1.0
   * @throws IncomparableComponentsException -  Thrown if the components are of different length.
   */
  @Test
  public void testGetDistanceSingleDimension() throws IncomparableComponentsException {
    assertEquals("Could not get correct distnace between single dimension positions",
        1.0, testPosition.getDistance(positionSingleDimension), 0.0);
  }
  
  /**
   * Test to work out the distance between two 2d Positions.
   * This should use both of the components in each position to calculate the 
   * euclidean distance.
   * @throws IncomparableComponentsException -  Thrown if the components are of different length.
   */
  @Test
  public void testGetDistance2Dimensions() throws IncomparableComponentsException {
    assertEquals("Could not get the correct distance betwene 2D positions",
        Math.sqrt(2), emptyTwoDimensionPosition.getDistance(nonEmptyTwoDimensionPosition), 0.0);
  }
  
  /**
   * Test to try and get the distances between positions with different dimensions.
   * An IncomparableComponentsException should be thrown. This exception will be thrown when 
   * The distances cannot be compared because they have a different dimensionality.
   * @throws IncomparableComponentsException - Thrown as the components length does not match.
   */
  @Test (expected = IncomparableComponentsException.class)
  public void testThrowException() throws IncomparableComponentsException {
    testPosition.getDistance(nonEmptyTwoDimensionPosition);
  }
  
  /**
   * Test to try and create a new Position with a null ID and null components array.
   * This should throw an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullConstructor() throws IllegalArgumentException {
    new Position(null, null);
  }
  
  /**
   * Test to try and get the distance between a valid position and a null position.
   * This should throw an IncomparableComponents exception as they cannot be compared 
   * if one of them is null.
   * @throws IncomparableComponentsException - Thrown as the position to compare against is null.
   */
  @Test(expected = IncomparableComponentsException.class)
  public void testNullGetDistance() throws IncomparableComponentsException {
    testPosition.getDistance(null);
  }
  
  /**
   * Test to try and get the same hash code for two identical Position objects.
   * The hash code should be equal as the ID and the components are the same.
   */
  @Test
  public void testGetIdenticalHashCode() {
    assertEquals("Could not get equal hash code for equal position objects", 
        testPosition.hashCode(), duplicatePosition.hashCode());
  }
  
  /**
   * Test to try and get a hash code for a position with a different ID 
   * and different components.
   * This should be a different from a position with a different ID and different components.
   */
  @Test
  public void testGetHashCodeDiffPositioin() {
    assertTrue("Could not get a different hash code for a different position",
        testPosition.hashCode() != nonEmptyTwoDimensionPosition.hashCode());
  }
  
  /**
   * Test to get a formatted string from the 
   * position class.
   * This should be in the format: 
   *  ID: 1, Components: [0.0]
   */
  @Test
  public void testGetFormattedPositionString() {
    assertEquals("Could not get the correctly formmatted string from Position class",
        "ID: 1, Components: [0.0]", testPosition.toString());
  }
  
  /**
   * Test to get a different string from the position class.
   * This should contain the different ID and the different components.
   */
  @Test
  public void testGetDifferentString() {
    assertEquals("Could not get a different formatted String from Position class", 
        "ID: 2, Components: [1.0, 1.0]", nonEmptyTwoDimensionPosition.toString());
  }
  
  /**
   * Test to try and see if two positions with the same ID and same components are equal.
   * This should return true as they are both the same.
   */
  @Test
  public void testGetIdenticalPositionsEqual() {
    assertTrue("Could not get identical positions to be equal",
        testPosition.equals(duplicatePosition));
  }
  
  /**
   * Test to try and see if a Position with different components is equal.
   * It should not be equal.
   */
  @Test
  public void testGetUnequalComponents() {
    assertFalse("Could not get Positions with different components to not be equal",
        testPosition.equals(diffComponents));
  }
  
  
  /**
   * Test to try and see if a position with the same components but different ID is equal.
   * This should return false.
   */
  @Test
  public void testGetUnequalID() {
    assertFalse("Could not get positions with same components but different ID to be unequal",
        testPosition.equals(diffID));
  }
  
  /**
   * Test to see if a position and null are equal. This should return false.
   */
  @Test
  public void testNullEquals() {
    assertFalse("Could not make null and a Position unequal",
        testPosition.equals(null));
  }
  
  /**
   * Test to see if a position and a string are equal.
   * This should return false, as a String does not have the required ID or Components.
   * It can also not be cast to a Position class.
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEqualsString() {
    assertFalse("Could not make a String and a Position to be unequal",
        testPosition.equals(""));
  }
  
  /**
   * Test to try and see if the components of two Positions are equal.
   * This should return true, as the components are the same.
   */
  @Test
  public void testEqualComponents() {
    assertTrue("Could not get Positions with equal components to be equal",
        testPosition.equalComponents(duplicatePosition));
  }
  
  /**
   * Test to try and see if Positions with different components
   * have equal components.
   * This should return false.
   */
  @Test
  public void testUnequalComponents() {
    assertFalse("Could not get Position with different components to have unequal components",
        testPosition.equalComponents(diffComponents));
  }
}
