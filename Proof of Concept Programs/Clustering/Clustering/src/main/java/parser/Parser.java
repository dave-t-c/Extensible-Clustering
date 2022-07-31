package parser;

import clustering.Position;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This interface will need to be implemented by all parsers.
 * This allows them all to have a common method that the system can use.
 * @author David Cook
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

}
