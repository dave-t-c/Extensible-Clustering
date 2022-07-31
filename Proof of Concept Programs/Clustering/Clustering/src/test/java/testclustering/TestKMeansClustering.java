package testclustering;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import clustering.Centroid;
import clustering.ClusteringAlgorithm;
import clustering.KMeansClustering;
import clustering.Metrics;
import clustering.Position;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Class for testing and creating the K Means 
 * Clustering Class.
 * @author David Cook
 */
public class TestKMeansClustering {
  
  Metrics testMetrics;
  KMeansClustering testClustering;
  Position testPosition1dA;
  Position testPosition1dB;
  Position testPosition1dC;
  Position testPosition1dD;
  Position testPosition1dE;
  Centroid testCentroid1dA;
  Centroid testCentroid1dAdiffPos;
  Centroid testCentroid1dB;
  Centroid testCentroid1dC;
  Centroid testCentroid1dD;
  static Path rootDirectory;
  static Path documentsDirectory;
  static Path outputDirectory;
  
  /**
   * Method that is run before any of the tests are executed.
   * This should set up the folder structure required for testing
   * the output files.
   */
  @BeforeClass
  public static void initialSetUp() {
    //This is where the directories need to be initialised.
    documentsDirectory = Paths.get(System.getProperty("user.home") + File.separator 
        + "Documents");
    if (Files.notExists(documentsDirectory)) {
      //Create the Directory
      try {
        Files.createDirectory(documentsDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    
    rootDirectory = Paths.get(documentsDirectory.toString()
        + File.separator + "Extensible Clustering");
    if (Files.notExists(rootDirectory)) {
      //Create the Directory
      try {
        Files.createDirectory(rootDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    outputDirectory = Paths.get(rootDirectory.toString() 
        + File.separator + "Output");
    if (Files.notExists(outputDirectory)) {
      //Create the output directory;
      try {
        Files.createDirectory(outputDirectory);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Setup the required variables before each test.
   */
  @Before
  public void setUp() {
    testMetrics = new Metrics();
    testClustering = new KMeansClustering();
    testPosition1dA = new Position("1dA", new Double[] {1.0});
    testPosition1dB = new Position("1dB", new Double[] {1.1});
    testPosition1dC = new Position("1dC", new Double[] {35.0});
    testPosition1dD = new Position("1dD", new Double[] {50.0});
    testPosition1dE = new Position("1dE", new Double[] {75.0});
    testCentroid1dA = new Centroid("1dA", new Position("1dA", new Double[] {25.0}));
    testCentroid1dAdiffPos = new Centroid("1dA", new Position("1dA", new Double[] {24.0}));
    testCentroid1dB = new Centroid("1dB", new Position("1dB", new Double[] {75.0}));
    testCentroid1dC = new Centroid("1dC", new Position("1dC", new Double[] {1.0}));
    testCentroid1dD = new Centroid("1dD", new Position("1dD", new Double[] {35.0}));    
  }
  
  /**
   * Perform these actions after each of the tests to ensure that 
   * there is only one file in the directory.
   * This avoids problems with using a time value in the file,
   * which will help the user find the output quicker.
   */
  @After
  public void tearDown() {
    //Clear the files from the output folder.
    try {
      Files.walk(outputDirectory)
      .filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Tear down the directory structure that was set up for the tests.
   */
  @AfterClass
  public static void finalTearDown() {
    //Delete the folder structure created in the initial set up method.
    try {
      Files.walk(rootDirectory)
      .filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
      Files.deleteIfExists(outputDirectory);
      Files.deleteIfExists(rootDirectory);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Test to try and create a new instance of the K Means
   * clustering class. This should be an instance of the 
   * ClusteringAlgorithm interface.
   */
  @Test
  public void testCreateKMeansClustering() {
    assertTrue("Could not create K Means Clustering as a Clustering algorith instance",
        testClustering instanceof ClusteringAlgorithm);
  }
  
  /**
   * Test to see if the k means implementation can return the correct
   * number of centroids. 
   * This will involve using the CH score metric, and determing which will be
   * best, between 2 clusters and n -1 clusters.
   * This will use 2 positions very similar, and one very far away.
   */
  @Test
  public void testGetCorrectNumOfClusters() {
    Position[] arrPos = new Position[] {testPosition1dA, testPosition1dB, testPosition1dC};
    
    //Determine the correct number of clusters, this should be 2,
    //so an array with 2 items should be returned.
    //Verify this is the
    
    assertEquals("Could not get correct number of clusteroids from KMeans",
        2, testClustering.clusterData(arrPos, "").length);
  }
  
  /**
   * Test to try and assign a Position to a cluster.
   * The cluster should have a single position assigned, and it should be the value
   * that was added.
   */
  @Test
  public void testAssignPosition() {
    Centroid[] centroidArr = new Centroid[] {testCentroid1dA, testCentroid1dB};
    testClustering.assignPosition(testPosition1dC, centroidArr);
    assertTrue("Could not assign position to correct cluster",
        testCentroid1dA.getAssignedPositions().contains(testPosition1dC));
  }
  
  /**
   * Test to try and assign a different position to a cluster.
   * This should assign a position to centroid1dB.
   */
  @Test
  public void testAssignDiffPosition() {
    Centroid[] centroidArr = new Centroid[] {testCentroid1dA, testCentroid1dB};
    testClustering.assignPosition(testPosition1dE, centroidArr);
    assertTrue("Could not assign diff position to correct cluster",
        testCentroid1dB.getAssignedPositions().contains(testPosition1dE));
  }
  
  /**
   * Test to see if the assignPosition method can handle a null position.
   * Nothing should be added to either of the centroids.
   */
  @Test
  public void testAssignNullPosition() {
    Centroid[] centroidArr = new Centroid[] {testCentroid1dA, testCentroid1dB};
    testClustering.assignPosition(null, centroidArr);
    assertTrue("Could not handle null for centroid A",
        testCentroid1dA.getAssignedPositions().isEmpty());
    assertTrue("Could not handle null for centroid B",
        testCentroid1dB.getAssignedPositions().isEmpty());
  }
  
  /**
   * Test to see how assignPosition handles null Centroids.
   * This should not cause an error.
   */
  @Test
  public void testAssignNullCentroids() {
    testClustering.assignPosition(testPosition1dA, null);
  }
  
  private int calcNumOfClusters(Position[] posArr) {
    Double bestChScore = Double.MIN_VALUE;
    int bestNum = -1;
    //This will use random initialisation
    for (int i = 2; i < posArr.length; i++) {
      //Initiate the clusters for up to and including i clusters.
      Centroid[] clusterArr = new Centroid[i];
      for (int j = 0; j < i; j++) {
        Centroid cluster = new Centroid(j + " centroid", posArr[j]);
        clusterArr[j] = cluster;
      }
      
      //Assign all the positions.
      for (Position currPos : posArr) {
        testClustering.assignPosition(currPos, clusterArr);
      }
      
      //Then calculate the ch value
      Double chScore = testMetrics.calinskiHarabasz(clusterArr, 5);
      if (chScore > bestChScore) {
        bestChScore = chScore;
        bestNum = i;
      }
    }
    return bestNum;
  }
  
  /**
   * Test to try and get the correct number of required clusters with
   * 5 pieces of data.
   * This should be the same as the CH metric calculates.
   */
  @Test
  public void testGetNumClustersFivePositions() {    
    //Make the positions array
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC, testPosition1dD, testPosition1dE};
    
    //Now we have the best num, we need to compare it to the result of the CH method.
    assertEquals("Could not get correct num of clusters for 5 positions",
        calcNumOfClusters(posArr), testClustering.getNumOfClusters(posArr));
  }
  
  /**
   * Test to try and get the correct number of required clusters with 3 pieces of data.
   * This should return a different value to that of the previous test.
   */
  @Test
  public void testGetDiffNumOfClusters() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    
    
    assertEquals("Could not get correct num of clusters with 3 pieces of data",
        calcNumOfClusters(posArr), testClustering.getNumOfClusters(posArr));
  }
  
  /**
   * Test to try and run a single iteration with a specified number of clusters
   * and positions.
   * This should assign the first and second positions to the first cluster,
   * and the third to the second cluster.
   */
  @Test
  public void testRunIteration() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    Centroid[] centroidArr = new Centroid[] {testCentroid1dC, testCentroid1dD}; 
    testClustering.runIteration(posArr, centroidArr);
    assertTrue("Could not configure centroid a expected",
        testCentroid1dC.getAssignedPositions().contains(testPosition1dA)
        && testCentroid1dC.getAssignedPositions().contains(testPosition1dB));
    assertTrue("Could not configure centroid b as expected",
        testCentroid1dD.getAssignedPositions().contains(testPosition1dC));
  }
  
  /**
   * Test to see if the centre of the cluster is set in the 
   * runIteration method.
   */
  @Test
  public void testRunIterationSetCentre() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    Centroid[] centroidArr = new Centroid[] {testCentroid1dC, testCentroid1dD}; 
    testClustering.runIteration(posArr, centroidArr);
    assertArrayEquals("Could not get correct centre for centroid a",
        new Double[]{1.05}, testCentroid1dC.getLocation().getComponents());    
    assertArrayEquals("Could not get correct centre for centroid b",
        new Double[] {35.0}, testCentroid1dD.getLocation().getComponents());
  }
  
  /**
   * Test to try and assign a different number of positions
   * with a different number of clusters.
   */
  @Test
  public void testRunIterationDiffClusters() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC, testPosition1dE};
    Centroid[] centroidArr = new Centroid[] {testCentroid1dC, testCentroid1dD, testCentroid1dB}; 
    testClustering.runIteration(posArr, centroidArr);
    assertTrue("Could not assign different data to a centroid with runIteration method",
        testCentroid1dB.getAssignedPositions().contains(testPosition1dE));
  }
  
  /**
   * Test to see if the initialiseClusters method 
   * can succesfully initialise 2 clusters, with 
   * the centres being 1.05 and 35.0
   */
  @Test
  public void testInitialiseCentroidsTwoClusters() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    Centroid[] result = testClustering.initialiseCentroids(posArr, 2);
    boolean containsValues = hasExpectedValues(35.0, 1.05, result);   
    assertTrue("Could not get the initialised centroids to contain the expected locations",
        containsValues);
    assertTrue("Could not get initialised centroids of expected length", 
        result.length == 2);
  }
  
  /**
   * Method for finding if a centroid array has the 2 specified locations.
   * These are 1d locations.
   * @param value1 - position 1 to compare.
   * @param value2 - position 2 to compare.
   * @param result - The centroid array to check.
   * @return - If the array contains value1 and value2 as a position.
   */
  public boolean hasExpectedValues(Double value1, Double value2, Centroid[] result) {
    boolean hasExpectedA = false;
    boolean hasExpectedB = false;
    for (Centroid currCentroid : result) {
      if (Arrays.equals(currCentroid.getLocation().getComponents(), new Double[] {value1})) {
        hasExpectedA = true;
      } else if (Arrays.equals(currCentroid.getLocation().getComponents(), new Double[] {value2})) {
        hasExpectedB = true;
      }
    }
    return hasExpectedA && hasExpectedB;
  }
  
  
  /**
   * Test to try and get the expected location for different positions.
   */
  @Test
  public void testInitialiseCentroidsDiffPositions() {
    Position[] posArr = new Position[] {testPosition1dB, testPosition1dC, 
        testPosition1dD};
    Centroid[] result = testClustering.initialiseCentroids(posArr, 2);
    //Expected locations for this are 1.1 and 42.5
    boolean containsValue = hasExpectedValues(1.1, 42.5, result);
    assertTrue("Could not get the expected locations initialiseCentroids with diff values",
        containsValue);    
  }
  
  /**
   * Test to try and get the name of the K Means algorithm.
   * This is so it can be displayed to the user.
   */
  @Test
  public void testGetName() {
    assertEquals("Could not get the expected name",
        "K-Means Clustering", testClustering.getName());
  }
  
  /**
   * Test to try and see if a array of centroids 
   * has converged.
   * This will be used to determine if the K means clustering can terminate.
   * This should return true as the assigned positions have not changed clusters.
   */
  @Test
  public void testHasConverged() {
    Centroid[] beforeArr = new Centroid[] {testCentroid1dA, testCentroid1dB};
    Centroid[] afterArr = new Centroid[] {testCentroid1dA, testCentroid1dB};
    testCentroid1dA.assignPosition(testPosition1dA);
    testCentroid1dB.assignPosition(testPosition1dB);
    assertTrue("Could not assert that identical centroid arrays have converged",
        testClustering.hasConverged(beforeArr, afterArr));
  }
  
  /**
   * Test to try and see if a centroid that has its location changed has converged.
   * This should return false.s
   */
  @Test
  public void testHasConvergedDiffClusters() {
    Centroid[] beforeArr = new Centroid[] {testCentroid1dA, testCentroid1dB};
    Centroid[] afterArr = new Centroid[] {testCentroid1dAdiffPos, testCentroid1dB};
    testCentroid1dA.assignPosition(testPosition1dA);
    testCentroid1dB.assignPosition(testPosition1dB);
    assertFalse("Could not assert that different located centroid arrays have not converged",
        testClustering.hasConverged(beforeArr, afterArr));
  }
  
  /**
   * Test to try and get the correct number of clusters from the k means implementation. 
   * This should return 4 clusters.
   */
  @Test
  public void testGetCorrectNumOfClustersDiffPositions() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC, testPosition1dD, testPosition1dE};
    assertEquals("Could not get correct number of clusters with diff positions from k means",
        4, testClustering.clusterData(posArr, "").length);
  }
  
  /**
   * Test to try and get expected cluster output from the clusterData methtod.
   * This should include 2 centroids with the correct location.
   */
  @Test
  public void testGetCorrectOutput() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    Centroid[] result = testClustering.clusterData(posArr, "");
    //Check that the locations are correct.
    assertTrue("Could not get the expected result from clustering with 2 cluster",
        hasExpectedValues(1.05, 35.0, result));
  }
  
  /**
   * Test to try and get the count of positions assigned to clusters.
   * This should be the same as those given.
   */
  @Test
  public void testGetCorrectAssignedNum() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    Centroid[] result = testClustering.clusterData(posArr, "");
    int total = 0;
    for (Centroid cluster : result) {
      total += cluster.getAssignedPositions().size();
    }
    assertEquals("Could not get equal num of positions out as put in",
        posArr.length, total);
  }
  
  /**
   * Test to see if there are no items assigned to the initialised clusters.
   * There should not be any positions assigned.
   */
  @Test
  public void testInitialiseClustersNoneAssigned() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    Centroid[] result = testClustering.initialiseCentroids(posArr, 2);
    for (Centroid cluster : result) {
      assertTrue("Could not have initialised cluster to not have assigned positions",
          cluster.getAssignedPositions().size() == 0);
    }
  }
  
  /**
   * Test to see if a clearClusterPositions method can remove all of the items from the given
   * centroid array.
   * All of them should have an empty list for their assigned.
   */
  @Test
  public void testClearClusters() {
    Centroid[] centroidArr = new Centroid[] {testCentroid1dA, testCentroid1dB};
    testCentroid1dA.assignPosition(testPosition1dA);
    testCentroid1dB.assignPosition(testPosition1dB);
    testClustering.clearCentroids(centroidArr);
    for (Centroid cluster : centroidArr) {
      assertTrue("Could not clear assigned positions from centroid",
          cluster.getAssignedPositions().isEmpty());
    }
  }
  
  /**
   * Test to try and get the output file that contains the results from the clustering.
   * This should be in a 'clustering output' folder.
   */
  @Test
  public void testOututDirectoryExists() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    testClustering.clusterData(posArr, "");
    
    //There should be a file in the output directory.
    assertEquals("Could not get an output file from k means clustering",
        1, outputDirectory.toFile().listFiles().length);
  }
  
  /**
   * Test to try and get the first few lines from the output file.
   * This should include:
   * Line 1: Name of clustering used.
   * Line 2: Time of completion
   * Line 3: Number of clusters
   * Line 4: Number of data items processed.
   */
  @Test
  public void testOutputFileData() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    testClustering.clusterData(posArr, "");
    File output = outputDirectory.toFile().listFiles()[0];
    try {
      Scanner readFile = new Scanner(output);
      assertEquals("Could not get type of clustering used",
          "Type of clustering: K-Means Clustering", readFile.nextLine());
      assertTrue("Could not get the time of completion", 
          readFile.nextLine().contains("Time Completed: "));
      assertEquals("Could not get the correct number of clusters",
          "Number of clusters generated: 2", readFile.nextLine());
      assertEquals("Could not get the correct number of data points",
          "Number of data points used: 3", readFile.nextLine());
      readFile.close();
    } catch (FileNotFoundException e) {
      fail("Output file not found");
      e.printStackTrace();
    }
  }
  
  /**
   * Test to try and cluster some different data and get different metrics.
   */
  @Test
  public void testOutputDiffMetrics() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC, testPosition1dD, testPosition1dE};
    int expectedClusters = calcNumOfClusters(posArr);
    testClustering.clusterData(posArr, "");
    File output = outputDirectory.toFile().listFiles()[0];
    try {
      Scanner readFile = new Scanner(output);
      //Skip the first 2 lines
      readFile.nextLine();
      readFile.nextLine();
      assertEquals("Could not get the expected number of clusters",
          "Number of clusters generated: " + expectedClusters, readFile.nextLine());
      assertEquals("Could not get the expected length of data",
          "Number of data points used: 5", readFile.nextLine());
      readFile.close();
    } catch (FileNotFoundException e) {
      fail("Output file not found");
      e.printStackTrace();
    }
  } 
  
  /**
   * Test to try and get the name of the file that has been clustered.
   * This will be useful for displaying which file this data has been taken from.
   */
  @Test
  public void testGetUsedFileName() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    testClustering.clusterData(posArr, "Example-Data.txt");
    File output = outputDirectory.toFile().listFiles()[0];
    try {
      Scanner readFile = new Scanner(output);
      //Skip the first 4 lines
      for (int i = 0; i < 4; i++) {
        readFile.nextLine();
      }
      assertEquals("Could not get the expected file name from output",
          "File used: Example-Data.txt", readFile.nextLine());
      readFile.close();
    } catch (FileNotFoundException e) {
      fail("Could not find output file");
      e.printStackTrace();
    }
  }
  
  /**
   * Test to try and get a different file name from the output file.
   */
  @Test
  public void testGetDiffFileName() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    testClustering.clusterData(posArr, "Other-Data.txt");
    File output = outputDirectory.toFile().listFiles()[0];
    try {
      Scanner readFile = new Scanner(output);
      //Skip the first 4 lines
      for (int i = 0; i < 4; i++) {
        readFile.nextLine();
      }
      assertEquals("Could not get the diff file name",
          "File used: Other-Data.txt", readFile.nextLine());
      readFile.close();
    } catch (FileNotFoundException e) {
      fail("Could not find output file");
      e.printStackTrace();
    }
  }
  
  /**
   * Test to try and get the clusters from k means clustering.
   */
  @Test
  public void testGetOutputClusters() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    testClustering.clusterData(posArr, "Other-Data.txt");
    File output = outputDirectory.toFile().listFiles()[0];
    try {
      Scanner readFile = new Scanner(output);
      //Skip the first 5 lines
      for (int i = 0; i < 5; i++) {
        readFile.nextLine();
      }
      //The next line should contain "Clusters" to denote the section.
      assertEquals("Could not get the section of the file",
          "Clusters", readFile.nextLine());
      
      //Line after should be the column headings
      //These are: Cluster ID, Num of assigned positions, location. 
      assertEquals("Could not get the correct line of column headings", 
          "Cluster ID\tNum of assigned positions\tLocation", readFile.nextLine());
      
      //Next two lines should be the clusters. These should start with ID: Cluster-0 and Cluster-1.
      assertTrue("Could not get the expected ID of the cluster",
          readFile.nextLine().contains("Cluster-0"));
      assertTrue("Could not get the expected ID of the next cluster",
          readFile.nextLine().contains("Cluster-1"));      
      readFile.close();
    } catch (FileNotFoundException e) {
      fail("Could not find the output file");
      e.printStackTrace();
    }
  }
  
  /**
   * Test to try and get the positions from the outputData.
   * This should denote the section of the file, and for each cluster,
   * have the assigned cluster and the positions location.
   */
  @Test
  public void testGetPositionsFromOutputData() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    testClustering.clusterData(posArr, "Data.txt");
    File output = outputDirectory.toFile().listFiles()[0];
    try {
      Scanner readFile = new Scanner(output);
      //Skip the initial data and clusters section.
      //This should be 9 lines
      for (int i = 0; i < 9; i++) {
        readFile.nextLine();
      }
      //The first line should contain "Data Points" to show the section that is being used.
      assertEquals("Could not get the correct section header for data section",
          "Data Points Used", readFile.nextLine());
      
      //The second line should contain the column headers. There are:
      //Position ID, Assigned Cluster, Location
      assertEquals("Could not get the correct section headings for data section",
          "Position ID\tAssigned Cluster\tLocation", readFile.nextLine());
      
      //Check that the first expected position location, 1.0 is in the next line.
      assertTrue("Could not get the correct expected position from output data",
          readFile.nextLine().contains("Cluster-0"));
      
      readFile.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Test to try and get the last file output. 
   * This should not be null, and should be in the output directory.
   */
  @Test
  public void testGetLastOutputFile() {
    Position[] posArr = new Position[] {testPosition1dA, testPosition1dB, 
        testPosition1dC};
    testClustering.clusterData(posArr, "Data.txt");
    Path lastOutput = testClustering.getLastOutputFile();
    assertTrue("Could not get the path of the file to be in an output directory",
        lastOutput.toString().contains("Output"));
  }
  
  /**
   * Test to try and get a description from K-Means clustering.
   * This should describe what the algorithm does and how.
   */
  @Test
  public void testGetKMeansDescription() {
    assertEquals("Could not get a description from k-means",
        "Splits a data set into K Clusters. For more info, please see the javadoc.",
        testClustering.getDescription());
  }
  
  /**
   * Test to try and cluster data where there is less than 2 positions given.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testGetClusterForNoData() {
    Position[] posArr = new Position[] {};
    testClustering.clusterData(posArr, "Example.txt");
    //This should throw an illegal argument exeception.
  }
}