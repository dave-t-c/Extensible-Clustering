package testextensibleclustering.testdependencies;

import static org.junit.Assert.assertEquals;

import extensibleclustering.dependencies.Centroid;
import extensibleclustering.dependencies.Metrics;
import extensibleclustering.dependencies.Position;
import org.junit.Before;
import org.junit.Test;

/**
 * Class that will be used for testing the metrics class that
 * will then be used for help with K-Means clustering. 
 * @author David Cook
 */
public class TestMetrics {
  
  Metrics testMetrics;
  Centroid testCentroid;
  Centroid duplicateTestCentroid;
  Centroid testChCentroid1dA;
  Centroid testChCentroid1dB;
  Centroid testCentroid2d;
  Centroid testCentroid3d;
  Centroid duplicateTestCentroid3d;
  Position position1dA;
  Position position1dB;
  Position position1dC;
  Position position1dD;
  Position position1dE;
  Position position2dA;
  Position position2dB;
  Position position3dA;
  Position position3dB;
  Position position3dC;
  Position position3dD;
  Position position3dE;
  Position position3dF;
  
  /**
   * Sets up the required variables before execution.
   */
  @Before
  public void setUp() {
    testMetrics = new Metrics();
    testCentroid = new Centroid("Centroid A", new Position("1", new Double[] {1.0}));
    testChCentroid1dA = new Centroid("Centroid CH-1D-A", new Position("2", new Double[] {17.0}));
    testChCentroid1dB = new Centroid("Centroid CH-1D-B", new Position("3", new Double[] {45.0}));
    duplicateTestCentroid = new Centroid("Dup Centroid A", new Position("1", new Double[] {1.0}));
    testCentroid2d = new Centroid("Centroid B", new Position("2", new Double[] {1.0, 1.0}));
    testCentroid3d = new Centroid("Centroid C", new Position("3", new Double[] {1.0, 1.0, 1.0}));
    duplicateTestCentroid3d = new Centroid("Centroid D", 
        new Position("4", new Double[] {1.0, 1.0, 1.0}));
    position1dA = new Position("1dA", new Double[] {1.0});
    position1dB = new Position("1dB", new Double[] {2.0});
    position1dC = new Position("1dC", new Double[] {4.0});
    position1dD = new Position("1dD", new Double[] {17.0});
    position1dE = new Position("1dE", new Double[] {45.0});
    position2dA = new Position("2dA", new Double[] {1.0, 2.0});
    position2dB = new Position("2dB", new Double[] {2.0, 1.0});
    position3dA = new Position("3dA", new Double[] {1.0, 2.0, 3.0});
    position3dB = new Position("3dB", new Double[] {4.0, 5.0, 6.0});
    position3dC = new Position("3dC", new Double[] {7.0, 8.0, 9.0});
    position3dD = new Position("3dD", new Double[] {10.0, 11.0, 12.0});
    position3dE = new Position("3dE", new Double[] {13.0, 14.0, 15.0});
    position3dF = new Position("3dF", new Double[] {16.0, 17.0, 18.0});
    
    
  }
  
  /**
   * Test to try and get the within variance with a single centroid 
   * that has no data assigned.
   * This should return 0.
   */
  @Test
  public void testGetWithinVariance() {
    assertEquals("Could not get correct within cluster variance with no assigned positions",
        0.0, testMetrics.withinClusterVariance(new Centroid[] {testCentroid}), 0.0);
  }
  
  /**
   * Test to try and calculate the within cluster variance with two
   * assignments and a single cluster.
   * This should return the difference between the assigned position and 
   * the centre of cluster, squared. 
   * As the average will be the same as the position, this should be 0.
   */
  @Test
  public void testGetVarianceTwoAssignments() {
    testCentroid.assignPosition(position1dA);
    testCentroid.assignPosition(position1dB);
    assertEquals("Could not get correct within cluster variance with two assigned positions",
        0.5, testMetrics.withinClusterVariance(new Centroid[] {testCentroid}), 0.1);
  }
  
  /**
   * Test to try and calculate within variance with 2d assignments and cluster.
   * This should result in ~1.0, accommodating for minor resolution issues).
   */
  @Test
  public void testGetVariance2D() {
    testCentroid2d.assignPosition(position2dA);
    testCentroid2d.assignPosition(position2dB);
    assertEquals("Could not get correct within cluster variance with 2d positions",
        1.0, 
        testMetrics.withinClusterVariance(new Centroid[] {testCentroid2d}), 0.01);
  }
  
  /**
   * Test to try and calculate the variance with 3d assignment and cluster.
   * This should result in ~54.
   * This will also test if it will work with 3 positions.
   */
  @Test
  public void testGetVariance3D() {
    testCentroid3d.assignPosition(position3dA);
    testCentroid3d.assignPosition(position3dB);
    testCentroid3d.assignPosition(position3dC);
    assertEquals("Could not get correct within cluster variance with 3, 3d positions",
        54.0, 
        testMetrics.withinClusterVariance(new Centroid[] {testCentroid3d}), 0.01);
  }
  
  /**
   * Test to try and get the within variance correct with multiple centroids.
   * This should return 1.0
   */
  @Test
  public void testWithinVarianceMultipleClusters() {
    testCentroid.assignPosition(position1dA);
    testCentroid.assignPosition(position1dB);
    duplicateTestCentroid.assignPosition(position1dA);
    duplicateTestCentroid.assignPosition(position1dB);
    assertEquals("Could not get correct within cluster variance with two centroids",
        1.0, testMetrics.withinClusterVariance(
            new Centroid[] {testCentroid, duplicateTestCentroid}), 0.1);
  }
  
  /**
   * Test to try and get the between cluster variance with 
   * two clusters, with one item assigned.
   * This should return 0.5.
   */
  @Test
  public void testBetweenVariance() {
    testCentroid.assignPosition(position1dA);
    duplicateTestCentroid.assignPosition(position1dB);
    assertEquals("Could not get correct between cluster variance with two centroids",
        0.5, testMetrics.betweenClusterVariance(
            new Centroid[] {testCentroid, duplicateTestCentroid}), 0.1);
  }
  
  /**
   * Test to try and get the between cluster variance with 
   * some different values.
   */
  @Test
  public void testBetweenVarianceDiffValues() {
    testCentroid.assignPosition(position1dC);
    duplicateTestCentroid.assignPosition(position1dD);
    assertEquals("Could not get the correct between variance with differnet values",
        84.5, testMetrics.betweenClusterVariance(
            new Centroid[] {testCentroid, duplicateTestCentroid}), 0.1);
  }
  
  
  /**
   * Test to try and get the between cluster variance with multiple dimensioned data.
   */
  @Test
  public void testBetweenVarianceMultipleDimension() {
    testCentroid3d.assignPosition(position3dA);
    testCentroid3d.assignPosition(position3dB);
    testCentroid3d.assignPosition(position3dC);
    duplicateTestCentroid3d.assignPosition(position3dD);
    duplicateTestCentroid3d.assignPosition(position3dE);
    duplicateTestCentroid3d.assignPosition(position3dF);
    assertEquals("Could not get correct variance between clusters with 3 dimiensions", 
        364.5, testMetrics.betweenClusterVariance(
            new Centroid[] {testCentroid3d, duplicateTestCentroid3d}), 0.1);
  }
  
  /**
   * Test to see what happens if the num of clusters is less than 2.
   * This should return 0.0
   */
  @Test
  public void testBetweenSingleCluster() {
    testCentroid.assignPosition(position1dA);
    assertEquals("Could not handle less than 2 clusters", 
        0.0, testMetrics.betweenClusterVariance(new Centroid[] {testCentroid}), 0.0);
  }
  
  /**
   * Test to see what happens if the clusters given to between variance are 
   * null.
   * This should return 0.0
   */
  @Test
  public void testBetweenNullClusters() {
    assertEquals("Could not handle null clusters for between variance",
        0.0, testMetrics.betweenClusterVariance(null), 0.0);
  }
  
  /**
   * Test to see if withinVariance can handle null as a value for
   * clusters.
   * This should return 0.0
   */
  @Test
  public void testWithinNullClusters() {
    assertEquals("Could not handle null clusters for within variances",
        0.0, testMetrics.withinClusterVariance(null), 0.0);
  }
  
  /**
   * Test to see how a centroid with no data is handled.
   * This should return 0.0
   */
  @Test
  public void testBetweenNoData() {
    assertEquals("Could not handle a clusteroid with no data",
        0.0, testMetrics.betweenClusterVariance(new Centroid[] {testCentroid}), 0.0);
  }
  
  /**
   * Test to calculate the CH Index when n less than or equal to k.
   * This should return 0.0
   */
  @Test
  public void testChNLessThanK() {
    testCentroid.assignPosition(position1dA);
    assertEquals("Could not get correct CH index with n <= K", 
        0.0, testMetrics.calinskiHarabasz(
            new Centroid[] {testCentroid, duplicateTestCentroid}, 1), 0.0);
  }
  
  /**
   * Test to calculate the CH index with 1d.
   * This will calculate the score for 2 clusters.
   */
  @Test
  public void testCalcCh1Dimension() {
    testCentroid.assignPosition(position1dA);
    testCentroid.assignPosition(position1dB);
    testCentroid.assignPosition(position1dC);
    testChCentroid1dA.assignPosition(position1dD);
    
    //Calculate the between and within score.
    Centroid[] clusters = new Centroid[] {testCentroid, testChCentroid1dA};
    Double between = testMetrics.betweenClusterVariance(clusters);
    Double within = testMetrics.withinClusterVariance(clusters);
    
    //We are using k = 2, n = 4 for this calculation.
    Double top = (between / (1));
    Double bottom = (within / (2));
    
    assertEquals("Could not get expected CH index for 2 clusters",
        (top / bottom), testMetrics.calinskiHarabasz(clusters, 4), 0.01);
  }
  
  /**
   * Test to calculate CH score for 3 clusters.
   * This will calculate it for 3 clusters and should return a different value.
   */
  @Test
  public void testCalcChDiffValues() {
    testCentroid.assignPosition(position1dA);
    testCentroid.assignPosition(position1dB);
    testCentroid.assignPosition(position1dC);
    testChCentroid1dA.assignPosition(position1dD);
    testChCentroid1dB.assignPosition(position1dE);
    
    Centroid[] clusters = new Centroid[] {testCentroid, testChCentroid1dA, testChCentroid1dB};
    Double between = testMetrics.betweenClusterVariance(clusters);
    Double within = testMetrics.withinClusterVariance(clusters);
    
    //We are using k = 3, n = 5 for this calculation.
    Double top = (between / (2));
    Double bottom = (within / (2));
    
    assertEquals("Could not get expected CH index for 3 clusters",
        (top / bottom), testMetrics.calinskiHarabasz(clusters, 5), 0.01);
  }
  
  /**
   * Test to see how CH score method handles null Clusters.
   * This should return 0.0
   */
  @Test
  public void testCalcChNullClusters() {
    assertEquals("CH Score method could not handle null value",
        0.0, testMetrics.calinskiHarabasz(null, 1), 0.0);
  }
  
  /**
   * Test to see  how CH method handles null value for num of positions.
   * This should return 0.0
   */
  @Test
  public void testCalcChNullNumPositions() {
    assertEquals("CH Score method could not handle null num of positions",
        0.0, testMetrics.calinskiHarabasz(new Centroid[] {testCentroid}, null), 0.0);
  }

}
