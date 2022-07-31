package dnaseqparser;

import extensibleclustering.dependencies.Parser;
import extensibleclustering.dependencies.Position;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Implements a DNA Sequence parser that converts GenBank .seq files with a character
 * set of 'acgt' to instances of the Position class that can be used to cluster the data.
 * @author David Cook
 */
public class DnaSeqParser implements Parser {
  
  /**
   * Generates all of the combinations of the specified length from
   * a given char set.
   * @param set - The set of chars to generate the combinations from.
   * @param prefix - The current string to use.
   * @param length - The length the combinations should be, e.g. 3.
   * @param generatedSet - The set to add the generated combinations to.
   */
  private void generateAllCombinations(char[] set, String prefix, int length,
      Set<String> generatedSet) {
    if (length == 0) {
      generatedSet.add(prefix);
      return;
    }
    
    for (int i = 0; i < set.length; ++i) {
      String newPrefix = prefix + set[i];
      generateAllCombinations(set, newPrefix, length - 1, generatedSet);
    }
  }

  @Override
  public Position[] parseFile(File file) throws FileNotFoundException, IOException {
    //Check that the file is not null before using it.
    if (file == null) {
      throw new IllegalArgumentException("File provided was null");
    }
    
    //Get and check the file extension.
    String extension = "";
    String fileName = file.getName();
    int lastDot = fileName.lastIndexOf('.');
    if (lastDot >= 0 || lastDot != -1) {
      extension = fileName.substring(lastDot + 1);
    }
    
    if (!extension.equals("seq")) {
      throw new IllegalArgumentException("File type is not supported");
    }
    
    
    
    //Generate the combinations of the alphabet {A,C,G,T} that are length 3.
    HashSet<String> permSet = new HashSet<>();
    generateAllCombinations(new char[]{'a', 'c', 'g', 't'}, "", 3, permSet);    
    BufferedReader readFile = new BufferedReader(new FileReader(file));
    String currentLine;
    currentLine = readFile.readLine();
    HashMap<String, String> dataMap = new HashMap<>();
    Vector<Position> positionsVector = new Vector<>();
    while (currentLine != null) {
      if (currentLine.contains("LOCUS")) {
        //Get the ID. This will be the second entry in the array when the line is split by spaces.
        String[] splitLine = currentLine.split(" ");
        String id = splitLine[7];
        dataMap.put(id, parseSequence(readFile));
      }
      currentLine = readFile.readLine();
    }
    
    //Iterate through the 
    dataMap.entrySet()
            .parallelStream()
            .forEach(entry -> {
              //Get the string to the correct length
              //Get the mod 3 result to see how many there should be.
              int remainder = entry.getValue().length() % 3;
  
              //Trim the remainder off the end.
              StringBuilder remainingTriplets = new StringBuilder(
                  entry.getValue().substring(0, entry.getValue().length() - remainder));
  
              ArrayList<String> splitList = new ArrayList<>();
              //Split the remaining triplets up into 3s.
              while (remainingTriplets.length() != 0) {
                splitList.add(remainingTriplets.substring(0, 3));
                remainingTriplets.delete(0, 3);
              }
              
              //All of the sets have now been split up into 3s. 
              //The frequency of these out of the data length can now be calculated.
              int dataCount = splitList.size();
              //Gather the frequency of each.
              HashMap<String, Double> freqMap = new HashMap<>();
              //Add all of the items in the perm set to the map.
              permSet.forEach(s -> freqMap.put(s, 0.0));
              //Gather the frequency of each.
              
              //Go through each of the sets of 3, and calculate the total occurrences.
              for (String key : splitList) {
                try {
                  freqMap.put(key, freqMap.get(key) + 1);
                } catch (NullPointerException ex) {
                  throw new IllegalArgumentException("The file given contained an "
                      + "invalid character not in 'a,c,g,t'");
                }
              }
  
              //Go through each entry in the map and divide it by the length of the triplets.
              for (String freqKey : freqMap.keySet()) {
                freqMap.put(freqKey, freqMap.get(freqKey) / dataCount);
              }
  
              //Now there is a map with the % of each triplet, 
              //these can be converted to a double array, then converted to a position,
              //and added to the vector.
  
              //Iterate thru the map and add the values to the list.
              ArrayList<Double> components = new ArrayList<>(freqMap.values());
              
              //Create a new position and add it to the vector.
              positionsVector.add(new Position(entry.getKey(), components.toArray(new Double[0])));
            });
    
    //Manually invoke garbage collection to unnecessary memory usage, 
    System.gc();
    return positionsVector.toArray(new Position[0]);
  }
  
  private String parseSequence(BufferedReader readFile) throws IOException {
    String currentLine;
    currentLine = readFile.readLine();
    while (!currentLine.contains("ORIGIN")) {
      currentLine = readFile.readLine();
    }
    
    //We are now at the start of the data.
    //A string builder will be used for this, 
    //as string concatenation of this will be very inefficient.
    StringBuilder sb = new StringBuilder();
  
    /* Last line of data is '//' */
    String currentData = "";
    currentData = readFile.readLine();
    while (!currentData.equals("//")) {
      //Handle this line, and add it to the string builder.
      //Remove all of the spaces from the line
      currentData = currentData.replaceAll("\\s+", "");
      
      //Remove all digits from the string as these are not required.
      currentData = currentData.replaceAll("\\d", "");
      
      //Can now append it to the string builder.
      sb.append(currentData);
      
      currentData = readFile.readLine();
    }
    
    return sb.toString();
  }

  @Override
  public String getName() {
    return "GenBank DNA .Seq";
  }

  @Override
  public String getDescription() {
    return "Parses GenBank .seq files containing DNA Sequences";
  }

  @Override
  public ArrayList<String> getSupportedFileExtensions() {
    return new ArrayList<String>(Arrays.asList(
        "GenBank Sequence Files (*.seq)",
        "*.seq"
        ));
  }
}
