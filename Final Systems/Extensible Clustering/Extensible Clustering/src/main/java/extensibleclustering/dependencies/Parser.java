package extensibleclustering.dependencies;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This interface will need to be implemented by all parsers.
 * This allows them all to have a common method that the system can use.
 * @author David Cook
 * @version 0.1
 */
public interface Parser {
  
  /**
   * Parses a given file and returns the data in a Position format that
   * can be interpreted by the program.
   * @param file - The file to parse the data from.
   * @return - An array of Positions that have been parsed from the file.
   * @throws FileNotFoundException - Thrown if the file given cannot be located.
   * @throws IOException - Thrown if there is an error when parsing data from the file.
   */
  public Position[] parseFile(File file) throws FileNotFoundException, IOException;
  
  /**
   * Returns a formatted name for the parser.
   * This is required for displaying to the user when they are picking a parser.
   * @return String - formatted name for the parser.
   */
  public String getName();
  
  /**
   * Returns a formatted description for the parser.
   * This is required so a description can be put alongside the parser.
   * @return String - formatted description for the parser.
   */
  public String getDescription();
  
  /**
   * Returns the supported file extensions the parser supports.
   * The first element of the array list should be the description, 
   * with the rest of the items being the supported extensions, e.g. '.txt'.
   * @return - ArrayList containing the description and supported extensions for the parser.
   */
  public ArrayList<String> getSupportedFileExtensions();

}
