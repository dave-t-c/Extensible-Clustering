package extensibleclustering.dependencies;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for helping with finding the output and plug-ins directories.
 * This can also help people making plug-ins to determine the required 
 * directories exist before they are used.
 * @author David Cook
 */
public class DirectoryHelper {
  
  Path documentsDirectory = Paths.get(System.getProperty("user.home") + File.separator 
      + "Documents");
  
  Path extensibleHome = Paths.get(documentsDirectory.toString() + File.separator
      + "Extensible Clustering");
  
  Path outputDirectory = Paths.get(extensibleHome.toString() + File.separator
      + "Output");
  
  Path pluginsDirectory = Paths.get(extensibleHome.toString() + File.separator
      + "Plugins");
  
  public boolean correctFolderStructureExists() {
    return Files.exists(outputDirectory) && Files.exists(pluginsDirectory);
  }
  
  /**
   * Creates the required directories for the System to function.
   * This will not throw an exception if the directories already exist.
   * It will also create the documents / Extensible Clustering folders
   * if they do not exist.
   */
  public void createRequiredDirectories() {
    try {
      Files.createDirectories(outputDirectory);
      Files.createDirectories(pluginsDirectory);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Returns the output directory for the output directory. 
   * @return Path - Path for the Output Directory.
   */
  public Path getOutputDirectory() {
    return outputDirectory;
  }
  
  /**
   * Returns the Path for the PlugIns directory.
   * @return Path - Location of the PlugIns directory.
   */
  public Path getPlugInDirectory() {
    return pluginsDirectory;
  }

}
