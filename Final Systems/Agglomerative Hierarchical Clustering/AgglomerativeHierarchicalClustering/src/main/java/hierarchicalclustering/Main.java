package hierarchicalclustering;

import extensibleclustering.dependencies.DirectoryHelper;
import extensibleclustering.dependencies.Position;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import parser.SeriesMatrixParser;

public class Main {
  
  /**
   * Main method for running the ahc. 
   * This allows it to be run independently of the main program.
   * @param args - args for the program. These are not used.
   * @throws IOException - Thrown if the file cannot be read.
   * @throws FileNotFoundException - Thrown if the file cannot be found.
   */
  public static void main(String[] args) throws FileNotFoundException, IOException {
    Scanner readIn = new Scanner(System.in);
    System.out.println("Please enter the location of the file to cluster");
    String fileLocation = readIn.nextLine();
    DirectoryHelper dirHelper = new DirectoryHelper();
    dirHelper.createRequiredDirectories();
    readIn.close();
    
    SeriesMatrixParser sp = new SeriesMatrixParser();
    File file = new File(fileLocation);
    Position[] data = sp.parseFile(file);
    
    SingleLinkHierarchicalAggloClustering ahc = new SingleLinkHierarchicalAggloClustering();
    ahc.clusterData(data, file.getName());
    
  }

}
