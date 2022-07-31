package parser;

import clustering.Position;
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
    
    if (file == null) {
      throw new FileNotFoundException();
    }
    
    BufferedReader reader = new BufferedReader(new FileReader(file));
    
    String deliminator = "\\t";
    List<Position> posList = new ArrayList<>();
      
    //Skip to the !series_matrix_table_begin line
    String line = reader.readLine();
    while (!line.equals("!series_matrix_table_begin")) {
      line = reader.readLine();
    }
    
    //Go onto the next line as it will be the start of the data.
    line = reader.readLine();
    
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
    
    reader.close();
    
    return posList.toArray(new Position[posList.size()]);
  }
}

