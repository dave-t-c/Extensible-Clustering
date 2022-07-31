package testdnaseqparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import dnaseqparser.DnaSeqParser;
import extensibleclustering.dependencies.Parser;
import extensibleclustering.dependencies.Position;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for testing and development of the Dna Sequence Parser.
 * @author David Cook
 */
public class TestDnaSeqParser {
  
  private static Path downloadBat;
  private static Path downloadSh;
  private static Path rootTestResources;
  private static Path testGenBankSample;
  private static Path testGenBankDiffSample;
  private static Path testGenBankWrongChars;
  private static Path testEmptyPdf;
  private DnaSeqParser testParser;
  
  /**
   * Downloads the required test data files before all tests are run.
   * @throws Exception - May be thrown downloading test files.
   */
  @BeforeClass
  public static void initialSetup() throws Exception {
    //Download the required files.
    //First get the resources of the files.
    rootTestResources = Paths.get("src" + File.separator + "test" + File.separator 
        + "resources");
    
    downloadBat = Paths.get(rootTestResources.toString() + File.separator 
        + "DownloadTestResources.bat");
    
    downloadSh = Paths.get(rootTestResources.toString() + File.separator + "DownloadTestData.sh");
    
    //Check if the os is windows.
    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
      //Run the batch file.
      ProcessBuilder processBuilder = new ProcessBuilder(); 
      processBuilder.command("cmd", "/c", downloadBat.toString());
      Process p = processBuilder.start();
      p.waitFor();
    } else {
      //Run the shell script on either macos / linux.
      ProcessBuilder processBuilder = new ProcessBuilder(); 
      processBuilder.command("bash", "-c", downloadSh.toString());
      Process p = processBuilder.start();
      p.waitFor();
    }
    
    testGenBankSample = Paths.get(rootTestResources.toString() + File.separator + "gbbct455.seq");
    
    testGenBankDiffSample = Paths.get(rootTestResources.toString() 
        + File.separator + "gbbct400.seq");
    
    testGenBankWrongChars = Paths.get(rootTestResources.toString() 
        + File.separator + "gbbct101.seq");
    
    testEmptyPdf = Paths.get(rootTestResources.toString() 
        + File.separator + "empty-pdf.pdf");
    
    assertTrue("Could not download GenBank 455 test file", testGenBankSample.toFile().exists());
    assertTrue("Could not download GenBank 400 test file", testGenBankDiffSample.toFile().exists());
    assertTrue("Could not download GenBank 101 test file", testGenBankWrongChars.toFile().exists());
  }
  
  @Before
  public void setUp() throws Exception {
    testParser = new DnaSeqParser();
  }
  
  /**
   * Delete the copied in test files after the tests have been completed.
   * @throws Exception - Thrown if either of the files cannot be deleted.
   */
  @AfterClass
  public static void finalTearDown() throws Exception {
    Files.deleteIfExists(testGenBankSample);
    Files.deleteIfExists(testGenBankDiffSample);
    Files.deleteIfExists(testGenBankWrongChars);
  }
  
  /**
   * Test to try and get an instance of the DNA Sequence Parser.
   * This should implement the Parser Interface from the extensible clustering dependencies.
   */
  @Test
  public void testImplementsParserInterface() {
    assertTrue("Could not get parser to implement correct interface",
        testParser instanceof Parser);
  }
  
  /**
   * Test to try and get the name of the parsing method.
   * This should detail what is supported.
   */
  @Test
  public void testGetParserName() {
    assertEquals("Could not get the expected parser name",
        "GenBank DNA .Seq", testParser.getName());
  }
  
  /**
   * Test to try and get the description of the parsing method.
   * This should include more detail about the supported file types.
   */
  @Test
  public void testGetParserDescription() {
    assertEquals("Could not get the expected parser description",
        "Parses GenBank .seq files containing DNA Sequences", testParser.getDescription());
  }
  
  /**
   * Test to try and get the expected array list to show the supported files.
   */
  @Test
  public void testGetExpectedSupportedFiles() {
    ArrayList<String> returnedList = testParser.getSupportedFileExtensions();
    assertEquals("Could not get the expected list length for file extensions",
        2, returnedList.size());
    assertEquals("Could not get the expected supported description",
        "GenBank Sequence Files (*.seq)", returnedList.get(0));
    assertEquals("Could not get the expected file types",
        "*.seq", returnedList.get(1));
  }
  
  /**
   * Test to try and parse a DNA Sequence file. 
   * This should return a Position array that contains nine positions, each of which 
   * are 64 dimensions.
   * @throws IOException - May be thrown if an error occurs while reading the file.
   * @throws FileNotFoundException  - May occur if the file cannot be found.
   */
  @Test
  public void testParseSampleGenBank() throws FileNotFoundException, IOException {
    Position[] result = testParser.parseFile(testGenBankSample.toFile());
    assertEquals("Could not get the expected length of result when parsing genbank file",
        9, result.length);
    for (Position pos : result) {
      assertEquals("Could not get expected dimensions for Position in parsed genbank file",
          64, pos.getComponents().length);
    }
  }
  
  /**
   * Test to try and parse a different DNA sequence file.
   * This should return a Position array that contains two positions, all of which 
   * are 64 dimensions.
   * @throws IOException  - May occur if the file cannot be read.
   * @throws FileNotFoundException  - May occur if the file is not found for the test.
   */
  @Test
  public void testParseDiffGenBank() throws FileNotFoundException, IOException {
    Position[] result = testParser.parseFile(testGenBankDiffSample.toFile());
    assertEquals("Could not get the expected length of result when parsing diff genbank file",
        2, result.length);
    for (Position pos : result) {
      assertEquals("Could not get expected dimensions for Position in parsed genbank file",
          64, pos.getComponents().length);
    }
  }
  
  /**
   * Test to try and parse a null file. This should throw an Illegal Argument Exception.
   * @throws FileNotFoundException - Will be thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when reading the file.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testParseNullFile() throws FileNotFoundException, IOException {
    testParser.parseFile(null);
  }
  
  /**
   * Test to try and parse an unsupported file type. 
   * This should throw an illegal args exception.
   * @throws FileNotFoundException - Will be thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when reading the file.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testParseUnsupportedFileType() throws FileNotFoundException, IOException {
    testParser.parseFile(testEmptyPdf.toFile());
  }
  
  /**
   * Test to try and parse a file with invalid chars. 
   * This should throw an illegal args exception.
   * @throws FileNotFoundException - Will be thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when reading the file.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testParseIllegalChars() throws FileNotFoundException, IOException {
    testParser.parseFile(testGenBankWrongChars.toFile());
  }
}
