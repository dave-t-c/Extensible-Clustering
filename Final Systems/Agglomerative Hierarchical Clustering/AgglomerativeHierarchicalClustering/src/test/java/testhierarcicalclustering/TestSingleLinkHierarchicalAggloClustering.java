package testhierarcicalclustering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import extensibleclustering.dependencies.Centroid;
import extensibleclustering.dependencies.ClusteringAlgorithm;
import extensibleclustering.dependencies.IncomparableComponentsException;
import extensibleclustering.dependencies.Position;
import hierarchicalclustering.SingleLinkHierarchicalAggloClustering;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for the development of the single link hierarchical clustering
 * algorithm.
 * @author David Cook
 */
public class TestSingleLinkHierarchicalAggloClustering {
  
  private SingleLinkHierarchicalAggloClustering testClustering;
  private ArrayList<Centroid> clusterList;
  private Centroid centroidA;
  private Centroid centroidB;
  private Position[] data;
  private static Path documentsDirectory;
  private static Path rootDirectory;
  private static Path outputDirectory;
  
  /**
   * Set up the required directories before the class is run.
   */
  @BeforeClass
  public static void initialSetUp() {
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
   * Set up the variables before each test so they are reset.
   */
  @Before
  public void setUp() {
    testClustering = new SingleLinkHierarchicalAggloClustering();
    clusterList = new ArrayList<>();
    centroidA = new Centroid("1", new Position("1", new Double[] {0.5}));
    centroidB = new Centroid("2", new Position("2", new Double[] {0.6}));
    centroidA.assignPosition(new Position("1a", new Double[] {0.5}));
    centroidB.assignPosition(new Position("2a", new Double[] {0.6}));
    data = new Position[2];
    data[0] = new Position("1", new Double[] {1.0});
    data[1] = new Position("2", new Double[] {2.0});
  }
  
  
  /**
   * Remove all of the files that have been created during the test.
   */
  @After
  public void tearDown() {
    try {
      Files.walk(outputDirectory)
      .filter(Files::isRegularFile).map(Path::toFile).forEach(File::delete);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Remove all of the files and folders created after the tests have been run.
   */
  @AfterClass
  public static void finalTearDown() {
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
   * Test to try and see if the hierarchical clustering implements the 
   * clustering algorithm interface. It should do this so it can be 
   * detected by the extensible clustering program.
   */
  @Test
  public void testImplementsClusteringInterface() {
    assertTrue("Could not get the clustering class to implement the clustering algorithm interface",
        testClustering instanceof ClusteringAlgorithm);
  }
  
  /**
   * Test to try and cluster a data array with 2 positions in.
   * This should return an array with 1 cluster in, as all hierarchical clustering 
   * operations return a single cluster.
   */
  @Test
  public void testGetCorrectReturnedSize() {
    assertEquals("Could not get the expected returned array size from clustered data",
        1, testClustering.clusterData(data, "Example").length);
  }
  
  /**
   * Test to make sure the correct number of positions are assigned to the final cluster.
   * This should be the same as the num of pieces of data given.
   */
  @Test
  public void testGetCorrectNumOfAssignedPositions() {
    assertEquals("Could not get the expected num of assigned data to final cluster",
        2, testClustering.clusterData(data, "Example")[0].getAssignedPositions().size());
  }
  
  /**
   * Test to try and get the name of the clustering algorithm.
   * This should return the expected name of 
   * 'Single Link Agglomerative Hierarchical Clustering'
   */
  @Test
  public void testGetExpectedName() {
    assertEquals("Could not get the expected name from hierarchical clustering",
        "Single Link Agglomerative Hierarchical Clustering", testClustering.getName());
  }
  
  /**
   * Test to try and get the expected description for hierarchical clustering.
   * This should include a warning that it will take a long time on large data sets.
   */
  @Test
  public void testGetExpectedDescription() {
    assertEquals("Could not get the expecte description from hierarchical clustering",
        "Performs Single-Link Agglomerative Hierarchical Clustering on this data set."
        + " This will cluster data from individual pieces of data down to a single cluster"
        + " WARNING, this does not scale well to large data sets so will take a long time to run",
        testClustering.getDescription());
  }
  
  /**
   * Test to try and perform a single iteration on the data.
   * This should merge two clusters, and the list size should be 1 after the two 
   * clusters have been merged.
   * @throws IncomparableComponentsException - may be thrown if an error occurs with position.
   */
  @Test
  public void testPerformIterationMergeTwoClusters() throws IncomparableComponentsException {
    clusterList.add(centroidA);
    clusterList.add(centroidB);
    testClustering.performIteration(clusterList, new HashMap<String, Double>(), 1);
    assertEquals("Could not get the expected cluster list size after performing iteration",
        1, clusterList.size());
  }
  
  /**
   * Test to check the merged IDs after merging the two clusters.
   * This should be "1,2" and the cluster should have two positions assigned. 
   * @throws IncomparableComponentsException - may be thrown if an error occurs with position.
   */
  @Test
  public void testMergeClustersCheckIDs() throws IncomparableComponentsException {
    clusterList.add(centroidA);
    clusterList.add(centroidB);
    testClustering.performIteration(clusterList, new HashMap<String, Double>(), 1);
    assertEquals("Could not get expected ID from merged cluster",
        "1::2", clusterList.get(0).getID());
    assertEquals("Could not get the expected num of assigned positions in the cluster",
        2, clusterList.get(0).getAssignedPositions().size());
  }
  
  /**
   * Test to try and have 3 centroids with assigned positions.
   * This should merge the 1 and 3 clusters.
   * @throws IncomparableComponentsException - may be thrown if an error occurs with position.
   */
  @Test
  public void testMergeThreeClustersCloses() throws IncomparableComponentsException {
    Centroid centroidC = new Centroid("3", new Position("3", new Double[] {0.54}));
    centroidC.assignPosition(new Position("3a", new Double[] {0.54}));
    clusterList.add(centroidA);
    clusterList.add(centroidB);
    clusterList.add(centroidC);
    testClustering.performIteration(clusterList, new HashMap<String, Double>(), 1);
    assertEquals("Could not get the expected IDs to be merged",
        "1::3", clusterList.get(clusterList.size() - 1).getID());
  }
  
  /**
   * Test to try and see if the distances are added to a hash map.
   * There should only be 1 entry and it should be between keys 1a and 2a. 
   * @throws IncomparableComponentsException - may be thrown if an error occurs with position.
   */
  @Test
  public void testPerformIterationStoreDistance() throws IncomparableComponentsException {
    clusterList.add(centroidA);
    clusterList.add(centroidB);
    HashMap<String, Double> distancesMap = new HashMap<>();
    testClustering.performIteration(clusterList, distancesMap, 1);
    assertEquals("Could not get the expected distances key size", 1, distancesMap.keySet().size());
    assertTrue("Could not get the expected key in distnances map", 
        distancesMap.containsKey("1a->2a"));
  }
  
  
  /**
   * Test to try and cluster data and get the last output file. 
   * This should return a file that exists.
   */
  @Test
  public void testClusterDataGetFileExists() {
    testClustering.clusterData(data, "Example");
    Path lastOutput = testClustering.getLastOutputFile();
    assertTrue("Could not get the last output file to exist after clustering",
        lastOutput.toFile().exists());
  }
  
  /**
   * Test to try and get the output file name.
   * This should include the name of the clustering method that was used.
   */
  @Test
  public void testGetOututFileNameContainsClusteringName() {
    testClustering.clusterData(data, "Example");
    Path lastOutput = testClustering.getLastOutputFile();
    assertTrue("Could not get the output file name to include the name of the clustering used",
        lastOutput.getFileName().toString().contains(testClustering.getName()));
  }
  
  /**
   * Test to try and get the first few lines of the output file. 
   * This will all need to be entered 
   */
  @Test
  public void testGetFileMetaLines() {
    testClustering.clusterData(data, "Example-Data.txt");
    Path lastOutput = testClustering.getLastOutputFile();
    try {
      Scanner readFile = new Scanner(lastOutput.toFile());
      assertEquals("Could not get type of clustering used",
          "Type of clustering: Single Link Agglomerative "
          + "Hierarchical Clustering", readFile.nextLine());
      assertTrue("Could not get the time started", 
          readFile.nextLine().contains("Time Started: "));
      assertEquals("Could not get the correct number of data points",
          "Number of data points used: 2", readFile.nextLine());
      assertEquals("Could not get the expected file name from output",
          "File used: Example-Data.txt", readFile.nextLine());
      readFile.close();
    } catch (FileNotFoundException ex) {
      fail("Ouptut file not found");
      ex.printStackTrace();
    }
  }
  
  /**
   * Test to try and get the height, and the IDs that are merged. 
   * This should be prefaced by a line saying what is in the section.
   */
  @Test
  public void testGetClusteringOutputInFile() {
    testClustering.clusterData(data, "Other-Data.txt");
    Path lastOutput = testClustering.getLastOutputFile();
    try {
      Scanner readFile = new Scanner(lastOutput.toFile());
      //Skip the first 4 lines
      for (int i = 0; i < 4; i++) {
        readFile.nextLine();
      }
      //Read the heading line.
      assertEquals("Could not get the expected section heading line for merged IDs",
          "Merged IDs and Height Merged At", readFile.nextLine());
      
      //The next line should be the column ids.
      assertEquals("Could not get the expected column headings",
          "Height\tMerged ID A\tMerged ID B", readFile.nextLine());
      readFile.close();
    } catch (FileNotFoundException ex) {
      fail("Ouptut file not found");
      ex.printStackTrace();
    }
  }
  
  
  /**
   * Test to try and get the expected data at the end of the file.
   * This should be after the info on what has been merged. 
   * It should contain the data of each position.
  */
  @Test
  public void testGetExpectedDataAtEndOfFile() {
    testClustering.clusterData(data, "Example-Data.txt");
    Path lastOutput = testClustering.getLastOutputFile();
    try {
      Scanner readFile = new Scanner(lastOutput.toFile());
      //Skip the first 7 lines
      for (int i = 0; i < 7; i++) {
        readFile.nextLine();
      }
      assertEquals("Could not get the expected section heading for Data used",
          "Data Points Used", readFile.nextLine());
      
      assertEquals("Could not get the expected headings for data section",
          "Position ID\tLocation", readFile.nextLine());
      
      assertEquals("Could not get the expected first data point",
          "1\t[1.0]", readFile.nextLine());
      readFile.close();
    } catch (FileNotFoundException ex) {
      fail("Ouptut file not found");
      ex.printStackTrace();
    }
  }
}
