package parser;

import extensibleclustering.dependencies.Position;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class will be the entry point for the Parser Proof of Concept Program.
 * This is what will be called when the program is run.
 * @author David Cook
 */
public class Main {
  /**
   * Main method for this class.
   * @param args - Args provided on entry.
   */
  public static void main(String[] args) {
    SeriesMatrixParser parser = new SeriesMatrixParser();
    try {
      System.out.println("Please enter the location of the file that you would like to parse");
      Scanner readIn = new Scanner(System.in);
      String fileLocation = readIn.nextLine();
      readIn.close();
      Position[] parsedPositions = parser.parseFile(new File(fileLocation));
      System.out.println("Parsed " + parsedPositions.length + " positions");
      System.out.println("Each have " + parsedPositions[0].getComponents().length + " components");
      
      
    } catch (FileNotFoundException ex) {
      System.out.println("Error, the File could not be found");
      ex.printStackTrace();
      
    } catch (IOException ex) {
      System.out.println("There were errors when reading the file");
      ex.printStackTrace();
    }
  }
}
