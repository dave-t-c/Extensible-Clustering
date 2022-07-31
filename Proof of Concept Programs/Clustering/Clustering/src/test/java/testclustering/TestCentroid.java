package testclustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import clustering.Centroid;
import clustering.Position;
import java.util.Arrays;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;

/**
 * Class that will be used for testing and development of the 
 * Centroid class.
 * @author David Cook
 */
public class TestCentroid {
  
  Centroid testCentroid;
  Centroid duplicateCentroid;
  Centroid testCentroidDiffID;
  Centroid testCentroidDiffLocation;
  Centroid testCentroidDiffAssigned;
  Centroid diffCentroid;
  Position positionA;
  Position positionB;
  Position positionC;
  Position positionD;
  
  /**
   * Set up the required variables between each test.
   */
  @Before
  public void setUp() {
    testCentroid = new Centroid("Example", new Position("1", new Double[] {0.0}));
    testCentroidDiffID = new Centroid("Other", new Position("1", new Double[] {0.0}));
    testCentroidDiffLocation = new Centroid("Example", new Position("1", new Double[] {1.0}));
    testCentroidDiffAssigned = new Centroid("Example", new Position("1", new Double[] {0.0}));
    duplicateCentroid = new Centroid("Example", new Position("1", new Double[] {0.0}));
    diffCentroid = new Centroid("Other", new Position("2", new Double[] {1.0}));
    positionA = new Position("A", new Double[]{1.0, 2.0});
    positionB = new Position("B", new Double[] {2.0, 1.0});
    positionC = new Position("C", new Double[] {1.0});
    positionD = new Position("D", new Double[] {2.0});
  }

  /**
   * Test to try and create a new Centroid and
   * get the correct ID.
   */
  @Test
  public void testCreateNewCentroid() {
    assertEquals("Could not get correct ID from Centroid", "Example", testCentroid.getID());
  }
  
  /**
   * Test to try and get a different ID from a Centroid.
   */
  @Test
  public void testGetDifferentID() {
    assertEquals("Could not get different ID from Centroid", "Other", diffCentroid.getID());
  }
  
  /**
   * Test to try and assign a position to this Centroid. 
   * This should mean the size of the list of assigned positions should be 1.
   * This should also be the 
   */
  @Test
  public void testAddPosition() {
    testCentroid.assignPosition(positionC);
    assertEquals("Could not get a List of length 1 after assigning 1 position",
        1, testCentroid.getAssignedPositions().size());
    
  }
  
  /**
   * Test to try and get a specific position from the getAssignedPosition method.
   * The first item should be the only item added.
   */
  @Test
  public void testGetAddedPosition() {
    testCentroid.assignPosition(positionC);
    assertTrue("Could not get the added position", 
        positionC.equals(testCentroid.getAssignedPositions().get(0)));
  }
  
  /**
   * Test to try and assign a different position to the Centroid.
   * The index 0 of the list should contain this position.
   */
  @Test
  public void testGetDifferentAddedPosition() {
    testCentroid.assignPosition(positionD);
    assertTrue("Could not get a different position from Centroid",
        positionD.equals(testCentroid.getAssignedPositions().get(0)));
  }
  
  /**
   * Test to add multiple Positions to a Centroid.
   * The length of the returned list should be 2, as 2 positions 
   * have been added.
   * The list should also contain both positionA and positionB.
   */
  @Test
  public void testAddMultiplePositions() {
    testCentroid.assignPosition(positionC);
    testCentroid.assignPosition(positionD);
    Vector<Position> returnedList = testCentroid.getAssignedPositions();
    assertTrue("Could not get added position C from Centroid",
        returnedList.contains(positionC));
    assertTrue("Could not get added position D from Centroid",
        returnedList.contains(positionD));
    assertEquals("Could not get a list containing the correct number of Positions",
        2, returnedList.size());
  }
  
  /**
   * Test to try and clear the assigned items. 
   * When the assigned items are retrieved, the size of the list should 
   * be zero.
   */
  @Test
  public void testClearAssignedPositions() {
    testCentroid.assignPosition(positionA);
    testCentroid.clearAssignedPositions();
    assertTrue("Could not clear the assigned items from the Centroid",
        testCentroid.getAssignedPositions().isEmpty());  
  }
  
  /**
   * Test to try and assign a null position.
   * If a null position is given, the size of the assigned positions
   * should not change as the position should not be added.
   */
  @Test
  public void testAssignNullPosition() {
    testCentroid.assignPosition(null);
    assertEquals("Could not handle a null position correctly",
        0, testCentroid.getAssignedPositions().size());
  }
  
  /**
   * Test to try and get the initial location from the Centroid.
   * This should return the same position that was given to it in
   * the constructor.
   */
  @Test
  public void testGetInitialLocation() {
    assertEquals("Could not get the correct initial location from the Centroid.",
        new Position("1", new Double[] {0.0}), testCentroid.getLocation());
  }
  
  /**
   * Test to try and get the initial location from the Centroid
   * when the initial location has changed.
   */
  @Test
  public void testGetDifferentLocation() {
    assertEquals("Could not get the correct different initial location from the Centroid",
        new Position("2", new Double[] {1.0}), diffCentroid.getLocation());
  }
  
  /**
   * Test to try and give the ID as null to the constructor.
   * This should throw an illegal argument exception.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testNullID() {
    new Centroid(null, positionA);
  }
  
  /**
   * Test to try and give a null position to the constructor.
   * This should throw an IllegalArgumentException.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testNullPosition() {
    new Centroid("1", null);
  }
  
  /**
   * Test to try and centre the Centroid based on the items assigned.
   * As there have been no items assigned, the cluster should not move.
   */
  @Test
  public void testCentreNoAssignments() {
    Position initialLocation = testCentroid.getLocation();
    testCentroid.setCentre();
    assertEquals("Could not set the centre when no items have been assigned.",
        initialLocation, testCentroid.getLocation());
  }
  
  /**
   * Test to try and centre the Centroid after one position has been assigned.
   * The new location of the centroid should be equal to that of the position.
   */
  @Test
  public void testGetCentreSingleAssignment() {
    testCentroid.assignPosition(positionC);
    testCentroid.setCentre();
    assertTrue("Could not get correct location after setting centre with single position",
        Arrays.equals(positionC.getComponents(), testCentroid.getLocation().getComponents()));
  }
  
  /**
   * Test to try and centre the Centroid after two positions have been assigned.
   * The location should be the average of each position.
   */
  @Test
  public void testGetCentreTwoAssignments() {
    testCentroid.assignPosition(positionC);
    testCentroid.assignPosition(positionD);
    testCentroid.setCentre();
    assertTrue("Could not get correct location after setting centre with two positions",
        Arrays.equals(new Double[]{1.5}, testCentroid.getLocation().getComponents()));
  }
  
  /**
   * Test to try and generate a readable string string from a Centroid.
   * This should contain the ID, the location, and the Assigned Positions.
   */
  @Test
  public void testGetReadableString() {
    testCentroid.assignPosition(positionC);
    assertEquals("Could not get the expected readable string from Centroid class",
        "ID: Example, Location: {ID: 1, Components: [0.0]}, "
        + "Assigned Positions: {[ID: C, Components: [1.0]]}",
        testCentroid.toString());
  }
  
  /**
   * Test to get a different formatted string from the Centroid class.
   * This should contain the different ID and Location of the cluster.
   */
  @Test
  public void testGetDiffFormattedString() {
    diffCentroid.assignPosition(positionC);
    assertEquals("Could not get the expected readable string from Centroid class",
        "ID: Other, Location: {ID: 2, Components: [1.0]}, "
        + "Assigned Positions: {[ID: C, Components: [1.0]]}",
        diffCentroid.toString());
  }
  
  /**
   * Test to try and get a hash code from two identical Centroids.
   * The hash codes should be the same.
   */
  @Test
  public void testGetIdenticalHashCodes() {
    assertTrue("Could not get equal hash code for equal Centroids",
        testCentroid.hashCode() == duplicateCentroid.hashCode());
  }
  
  /**
   * Test to try and get a different hash code for Centroids with different IDs.
   */
  @Test
  public void testHashCodeDiffID() {
    assertTrue("Could not get hash codes for Centroids with different IDs",
        testCentroid.hashCode() != testCentroidDiffID.hashCode());
  }
  
  /**
   * Test to try and get a different hash code for Centroids with 
   * a different location.
   */
  @Test
  public void testHashCodeDiffLocation() {
    assertTrue("Could not get hash code for Centroids with different locations",
        testCentroid.hashCode() != testCentroidDiffLocation.hashCode());
  }
  
  /**
   * Test to try and get a different hash code for a Centroid with 
   * different assigned positions.
   * This should generate a different hash code.
   */
  @Test
  public void testHashCodeDiffAssignedPositions() {
    testCentroidDiffAssigned.assignPosition(positionC);
    assertTrue("Could not get different hash codes for Centroids with diff assigned positions",
        testCentroid.hashCode() != testCentroidDiffAssigned.hashCode());
  }
  
  /**
   * Test to try and see if two identical Centroids are equal.
   * This should return true.
   */
  @Test
  public void testIdenticalCentroidsEqual() {
    assertTrue("Could not get duplicate Centroids to be equal",
        testCentroid.equals(duplicateCentroid));
  }
  
  /**
   * Test to try and see if Centroids with a different id are equal.
   * This should return false.
   */
  @Test
  public void testDiffIDsEqual() {
    assertFalse("Could not determine Centroids with different IDs are unequal",
        testCentroid.equals(testCentroidDiffID));
  }
  
  /**
   * Test to try and see if Centroids with a different location are equal.
   * This should return false.
   */
  @Test
  public void testDiffLocationIsEqual() {
    assertFalse("Could not get Centroids with different location to be unequal",
        testCentroid.equals(testCentroidDiffLocation));
  }
  
  /**
   * Test to see if Centroids with different assigned positions are equal.
   * This should return false.
   */
  @Test
  public void testDiffAssignedEqual() {
    duplicateCentroid.assignPosition(positionC);
    assertFalse("Could not get Centroids with different assigned positions to be uneqaul",
        testCentroid.equals(duplicateCentroid));
  }
  
  /**
   * Test to try and see how Centroid handles being given a different object that cannot be cast
   * to a Centroid.
   * This should return false.
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEqualsDiffObjectType() {
    assertFalse("Could not get a Centroid to be uneqaul with a string",
        testCentroid.equals("A"));
  }
  
  /**
   * Test to try and see if null is equal to a Centroid.
   * This should return false.
   */
  @Test
  public void testEqualsNull() {
    assertFalse("Could not get a Centroid and null to be unequal",
        testCentroid.equals(null));
  }
  
  /**
   * Test to try and assign a position to a Centroid
   * when the position does not have the same dimensionality as the Centroid.
   * This should not be assigned.
   */
  @Test
  public void testAssignDifferentDimensionality() {
    testCentroid.assignPosition(positionA);
    assertEquals("Could not avoid assigning a position with different dimensionality",
        0, testCentroid.getAssignedPositions().size());
  }
}
