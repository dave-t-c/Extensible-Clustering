package parser;

import extensibleclustering.dependencies.Parser;
import extensibleclustering.dependencies.Position;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class for parsing Gene Micro-array data in a series matrix format.
 * @author David Cook
 */
public class SeriesMatrixParser implements Parser {

  /**
   * Parses a specified Series Matrix file into Positions that can be used for clustering.
   * @param file - The specified file to be parsed.
   * @return - An array of positions that can then be processed.
   * @throws FileNotFoundException - Thrown if the file cannot be found.
   * @throws IOException - Thrown if an error occurs when reading from the file.
   */
  @Override
  public Position[] parseFile(File file) throws FileNotFoundException, IOException {
    
    if (file == null || !file.exists()) {
      throw new FileNotFoundException();
    }
    
    //Find the extension of the file
    //Initialising to empty string means if there is no file extension it will avoid a NPE.
    String extension = "";
    int indexOfLastDot = file.getName().lastIndexOf('.');
    if (indexOfLastDot > 0) {
      extension = file.getName().substring(indexOfLastDot + 1);
    }
    
    //The supported files for this parser end in .txt, so a pdf for example should be rejected.
    if (!extension.equals("txt")) {
      throw new UnsupportedOperationException("This file type is not supported with this parser");
    }
    
    List<Position> posList = new ArrayList<>();
    
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      
      //Skip to the !series_matrix_table_begin line
      String line = reader.readLine();
      while (line != null && !line.equals("!series_matrix_table_begin")) {
        line = reader.readLine();
      }
      
      if (line == null) {
        throw new UnsupportedOperationException("This file type is not supported with this parser");
      }
      
      //Go onto the next line as it will be the start of the data.
      line = reader.readLine();
      
      String deliminator = "\\t";
      
      //Skip this next line if it is an ID ref line.
      //This line continues "ID_Ref" in the first column.
      if (line.split(deliminator)[0].equals("\"ID_REF\"")) {
        line = reader.readLine();
      }
      
      //This loop will continue until it reaches the end of the file
      while (line != null) {
        //The parser will stop reading if it hits a '!', as this normally 
        //denotes a comment of the start of '!series_matrix_table_end'.
        if (line.charAt(0) == '!') {
          break;
        }
        //Split the line and convert it to an array.
        //The first column is skipped because it has the ID in.
        String[] splitLine = line.split(deliminator);
        Double[] components = Arrays.stream(splitLine)
            .skip(1)
            .map(Double::valueOf)
            .toArray(Double[]::new);
        
        String id = splitLine[0];
        
        posList.add(new Position(id, components));
        line = reader.readLine();
      }    
      
    }
    
    return posList.toArray(new Position[posList.size()]);
  }

  /**
   * Returns a formatted string of the name of the parser.
   * In this case: Gene Micro-Array Series Matrix Parser.
   * @return String - Formatted name for this Parser. 
   */
  @Override
  public String getName() {
    return "Gene Micro-Array Series Matrix Parser";
  }

  /**
   * Returns a formatted description of this parser.
   * @return String - Formatted Description of the Series Matrix Parser.
   */
  @Override
  public String getDescription() {
    return "Parses Gene Micro-Array Series Matrix Files";
  }

  /**
   * Returns the supported file extensions for this parser.
   * The first index is the description, the second is the file type.
   */
  @Override
  public ArrayList<String> getSupportedFileExtensions() {
    return new ArrayList<String>(Arrays.asList(
        "txt files (*.txt)",
        "*.txt"
        ));
  }
}

