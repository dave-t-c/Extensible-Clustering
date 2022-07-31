package parser;

import clustering.Centroid;
import clustering.KMeansClustering;
import clustering.Position;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
      System.out.println("Clustering.....");
      
      //The required file structure needs to be setup. 
      Path rootDirectory = Paths.get(System.getProperty("user.home") + File.separator 
          + "Documents" + File.separator + "Extensible Clustering");
      if (Files.notExists(rootDirectory)) {
        //Create the Directory
        try {
          Files.createDirectory(rootDirectory);
        } catch (IOException e) {
          e.printStackTrace();
          System.exit(1);
        }
      }
      Path outputDirectory = Paths.get(rootDirectory.toString() 
          + File.separator + "Output");
      if (Files.notExists(outputDirectory)) {
        //Create the output directory;
        try {
          Files.createDirectory(outputDirectory);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      
      
      
      KMeansClustering kmeans = new KMeansClustering();
      long startTime = System.currentTimeMillis();
      Centroid[] result = kmeans.clusterData(parsedPositions, new File(fileLocation).getName());
      System.out.println("Clustered data with " + result.length + " clusters");
      System.out.println("Elapsed time: " 
          + (System.currentTimeMillis() - startTime) + "ms");
      Path outputFile = kmeans.getLastOutputFile();
      System.out.println("Output file can be found at: " + outputFile.toString());
      
      
    } catch (FileNotFoundException ex) {
      System.out.println("Error, the File could not be found");
      ex.printStackTrace();
      
    } catch (IOException ex) {
      System.out.println("There were errors when reading the file");
      ex.printStackTrace();
    }
  }
}
