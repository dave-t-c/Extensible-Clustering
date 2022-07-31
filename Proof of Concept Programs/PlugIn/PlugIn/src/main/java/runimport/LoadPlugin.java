package runimport;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import extensibleclustering.dependencies.Position;
import extensibleclustering.plugins.ImportedPlugins;
import extensibleclustering.plugins.PluginLoader;

public class LoadPlugin {
  /**
   * Setup the file / folder structure.
   */
  public static void main(String[] args) {
    Path documentsDirectory = Paths.get(System.getProperty("user.home") + File.separator 
        + "Documents");
    
    if (Files.notExists(documentsDirectory)) {
      //Create the Directory
      try {
        Files.createDirectory(documentsDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    
    Path rootDirectory = Paths.get(documentsDirectory.toString()
        + File.separator + "Extensible Clustering");
    if (Files.notExists(rootDirectory)) {
      //Create the Directory
      try {
        Files.createDirectory(rootDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    Path pluginDirectory = Paths.get(rootDirectory.toString() 
        + File.separator + "Plugins");
    if (Files.notExists(pluginDirectory)) {
      //Create the output directory;
      try {
        Files.createDirectory(pluginDirectory);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    //Move in the parser from the main resources.
    Path rootResources = Paths.get("src" + File.separator + "main" + File.separator 
        + "resources" + File.separator);
    Path standardJar = Paths.get(rootResources + File.separator + "parser.jar");
    Path destStandardJar = Paths.get(pluginDirectory + File.separator + "parser.jar");
    try {
      Files.copy(standardJar, destStandardJar, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    PluginLoader pluginLoader = new PluginLoader();
    ImportedPlugins importedPlugins = pluginLoader.loadPlugins();
    
    //Display the names of the imported parsers.
    System.out.println("Parsers imported: " + importedPlugins.getImportedParsers().keySet().toString());
    
    //Try and invoke the parser on a sample file. 
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
    Path geoFile = Paths.get(rootResources + File.separator 
        + "GSE4014-GPL32_series_matrix.txt");
  
    //Use the 'Gene Micro-Array Series Matrix Parser' to parse a file.
    try {
      Class<?> clazz = importedPlugins.getImportedParsers().get("Gene Micro-Array Series Matrix Parser");
      Object instance = clazz.newInstance();
      Method parseFile = clazz.getDeclaredMethod("parseFile", File.class);
      Object result = parseFile.invoke(instance, geoFile.toFile());
      Position[] data = Position[].class.cast(result);
      System.out.println("Parsed " + data.length + " positions from file");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  
  }

}
