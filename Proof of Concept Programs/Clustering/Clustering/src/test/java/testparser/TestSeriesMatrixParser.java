package testparser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import clustering.Position;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import parser.Parser;
import parser.SeriesMatrixParser;

/**
 * Test class for development / testing of the SeriesMatrixParser class.
 * @author David Cook
 */


/*
 * The file 'GSE4014-GPL32_series_matrix.txt' and 
 * 'Modified-GSE4014-GPL32_series_matrix.txt' are from the 
 * Gene Expression Omnibus, providided by the NCBI.
 * 
 * This file can be found here:
 * "https://ftp.ncbi.nlm.nih.gov/geo/series/GSE4nnn/GSE4014/matrix/"
 * With the file being used ending with GPL32.
 * 
 * This file was accessed on 28/09/2020.
 */

public class TestSeriesMatrixParser {
  String baseDirectoryPath;
  File singlePositionFile;
  File diffSinglePositionFile;
  File twoPositionFile;
  File multiplePositionFile;
  File largeModifiedGeoFile;
  File unmodifiedGeoFile;
  SeriesMatrixParser testParser;
  
  Position correctPosOne;
  Position correctPosTwo;
  Position correctPosThree;
  Position correctPosFour;
  Position correctPosFive;
  
  /**
   * Sets up the required files and variables for each test.
   * @throws Exception - Thrown if an error occurs setting up a variable.
   */
  @Before
  public void setUp() throws Exception {
    testParser = new SeriesMatrixParser();
    
    baseDirectoryPath = "src" + File.separator + "test" + File.separator 
        + "resources" + File.separator + "SeriesMatrixParser";
    singlePositionFile = new File(baseDirectoryPath + File.separator 
        + "SinglePositionMatrix.txt");
    diffSinglePositionFile = new File(baseDirectoryPath + File.separator 
        + "SinglePositionMatrixDiff.txt");
    twoPositionFile = new File(baseDirectoryPath + File.separator 
        + "TwoPositionMatrix.txt");  
    multiplePositionFile = new File(baseDirectoryPath + File.separator
        + "MultiplePositionMatrix.txt");
    largeModifiedGeoFile = new File(baseDirectoryPath + File.separator
        + "Modified-GSE4014-GPL32_series_matrix.txt");
    unmodifiedGeoFile = new File(baseDirectoryPath + File.separator 
        + "GSE4014-GPL32_series_matrix.txt");
    
    correctPosOne = new Position("\"1\"", new Double[] {1.05, 2.05, 3.05});
    correctPosTwo = new Position("\"2\"", new Double[] {4.05, 5.05, 6.05});
    correctPosThree = new Position("\"3\"", new Double[] {4.05, 5.05, 6.05});
    correctPosFour = new Position("\"4\"", new Double[] {4.05, 5.05, 6.05});
    correctPosFive = new Position("\"5\"", new Double[] {4.05, 5.05, 6.05});
  }
  
  /**
   * Test to create a new SeriesMatrixParser.
   * This should be an instance of the Parser interface.
   */
  @Test
  public void testCreateParser() {
    assertTrue("Could not get SeriesMatrixParser to implement the Parser interface",
        testParser instanceof Parser);
  }
  
  /**
   * Test to try and get the parser to read a single lined file.
   * This will include a single position, on a single line.
   * This file will not include any of the meta-info before the data.
   * It will include the !series_matrix_table_begin tag and end tag however.
   * Like series matrix files, the file will be tab separated.
   * @throws FileNotFoundException - Thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when using the IO.
   */
  @Test
  public void testReadSinglePosition() throws FileNotFoundException, IOException {
    Position[] result = testParser.parseFile(singlePositionFile);
    Position[] correctResult = new Position[] {new Position("\"1\"", 
        new Double[] {1.05, 2.05, 2.05})};
    assertArrayEquals("Could not get correct Position from a file with only one Position",
        correctResult, result);
  }
  
  /**
   * Test to try and get the parser to read a single lined file 
   * that has different data.
   * This should return the data in the different file.
   * @throws FileNotFoundException - Thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when using the IO.
   */
  @Test
  public void testReadDiffSinglePosition() throws FileNotFoundException, IOException {
    Position[] result = testParser.parseFile(diffSinglePositionFile);
    Position[] correctResult = new Position[] {new Position("\"2\"", 
        new Double[] {5.05, 4.05, 3.05})};
    assertArrayEquals("Could not get correct different Position from a file with only one position",
        correctResult, result);
  }
  
  /**
   * Test to try and get the parser to read a file with two lines in.
   * This should return an array with 2 positions in.
   * @throws FileNotFoundException - Thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when using the IO.
   */
  @Test
  public void testReadTwoLineFile() throws FileNotFoundException, IOException {
    Position[] result = testParser.parseFile(twoPositionFile);
    Position[] correctResult = new Position[] {correctPosOne, correctPosTwo};
    assertArrayEquals("Could not get correct Positions from a multiple line file",
        correctResult, result);
  }
  
  /**
   * Test to try and see if the parser can read a file with many lines in.
   * This should return an array with the correct number of Positions in.
   * This test will use 5, as it is fairly easy to verify.
   * @throws FileNotFoundException - Thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when using the IO.
   */
  @Test
  public void testReadMultiplePositionFile() throws FileNotFoundException, IOException {
    Position[] result = testParser.parseFile(multiplePositionFile);
    Position[] correctResult = new Position[] 
        { correctPosOne, correctPosTwo, correctPosThree, correctPosFour, correctPosFive};
    assertArrayEquals("Could not get correct Positions from a five lined file",
        correctResult, result);
  }
  
  /**
   * Test to try and see if the parser can handle larger files.
   * This will use a modified version of GSE4014-GPL32_series_matrix.txt.
   * This was downloaded from the Gene Expression Omnibus.
   * This has also had the meta data at the start removed, as the parser does 
   * not currently handle that.
   * This also involves removing the  ID Ref line.
   * This should return an array with 12654 positions in.
   * @throws FileNotFoundException - Thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when using the IO.
   */
  @Test
  public void testParserLargeFile() throws FileNotFoundException, IOException {
    assertEquals("Could not get correct length of array from large file",
        12654, testParser.parseFile(largeModifiedGeoFile).length);
  }
  
  /**
   * Test to see if the Parser can handle a file with meta data in. 
   * This will use an unmodified version of GSE4014-GPL32_series_matrix.txt.
   * This will test to see if the parser can handle metadata, as this will be in nearly 
   * all of these files.
   * @throws FileNotFoundException - Thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when using the IO.
   */
  @Test
  public void testParserHandleMetadata() throws FileNotFoundException, IOException {
    assertEquals("Could not get the correct number of positions from a file with metadata",
        12654, testParser.parseFile(unmodifiedGeoFile).length);
  }
  
  /**
   * See how the parser handles being given a location of a file that does not exist.
   * This should throw a file not found exception.
   * @throws FileNotFoundException - Should be thrown as the file should not be found.
   * @throws IOException - Thrown if an error occurs when using the IO.
   */
  @Test (expected = FileNotFoundException.class)
  public void testFileDoesNotExist() throws FileNotFoundException, IOException {
    testParser.parseFile(new File(""));
  }
  
  /**
   * Test to see what happens if null is given.
   * This should throw a file not found exception.
   * @throws FileNotFoundException - Should be thrown as the file should not be found.
   * @throws IOException - Thrown if an error occurs when using the IO.
   */
  @Test (expected = FileNotFoundException.class)
  public void testParserNull() throws FileNotFoundException, IOException {
    testParser.parseFile(null);
  }

}
