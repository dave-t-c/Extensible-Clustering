package extensibleclustering.plugins;

import extensibleclustering.dependencies.DirectoryHelper;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {
  Path documentsDirectory;
  Path programHomeDirectory;
  Path pluginsDirectory;
  PluginTypeFactory pluginTypeFactory;
  HashSet<String> supportedInterfaces;
  DirectoryHelper directoryHelper;
  
  /**
   * Constructor for the PluginLoader. 
   * This ensures that the plug-ins directory exists so they can be imported.
   */
  public PluginLoader() {    
    //Use the DirectoryHelper class to see if the required folder structure exists.
    //If not, it will be created.
    directoryHelper = new DirectoryHelper();
    if (!directoryHelper.correctFolderStructureExists()) {
      directoryHelper.createRequiredDirectories();
    }
    
    pluginsDirectory = directoryHelper.getPlugInDirectory();
    
    pluginTypeFactory = new PluginTypeFactory();
    supportedInterfaces = new HashSet<>();
    supportedInterfaces.add("Parser");
    supportedInterfaces.add("ClusteringAlgorithm");
    supportedInterfaces.add("Visualisation");
  }
  
  /**
   * Loads plug-ins into an instance of the ImportedPlugins Class.
   * @return ImportedPlugins - Contains the Plug-ins that have been imported. 
   */
  public ImportedPlugins loadPlugins() {
    ImportedPlugins plugins = new ImportedPlugins();
    //Iterate through all of the jar files in a directory
    File[] directoryFiles = pluginsDirectory.toFile().listFiles();
    for (File file : directoryFiles) {
      //Get the extension of the file, for it to be considered to be imported, 
      //It must be a '.jar' file.
      //Use the index of the last dot to check. 
      int lastDot = file.getName().lastIndexOf('.');
      String fileExtension = file.getName().substring(lastDot + 1);
      if (!fileExtension.equals("jar")) {
        continue;
      }
     
      //Load the file as a jar entry
      try (JarFile currJar = new JarFile(file.getAbsolutePath())) {
        Enumeration<JarEntry> jarFiles = currJar.entries();
        //Make a new set of urls to load in. This specifies the type of file.
        URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
        URLClassLoader classLoader = URLClassLoader.newInstance(urls);
        while (jarFiles.hasMoreElements()) {
          //Make a new jar entry using this element.
          JarEntry entry = jarFiles.nextElement();
         
          //Skip directories or files that do not end in 'class'.
          if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
            continue;
          }
         
          //Get the class name - -6 is used to handle .class
          String className = entry.getName().substring(0, entry.getName().length() - 6);
          className = className.replace('/', '.');
         
          Class<?> clazz = classLoader.loadClass(className);
         
          //Check to see the class file implements any of the supported interfaces.
          boolean containsSupportedInterface = false;
          for (Class<?> currInterface : clazz.getInterfaces()) {
            containsSupportedInterface = containsSupportedInterface 
               || supportedInterfaces.contains(currInterface.getSimpleName());
          }
         
          //If it does not contain the supported interface, then move on to the next jar entry.
          if (!containsSupportedInterface) {
            continue;
          }
         
          //Get the name and description from the class.
          Object instance = clazz.newInstance();
          Method getName = clazz.getDeclaredMethod("getName");
          Object objName = getName.invoke(instance);
          String pluginName = String.class.cast(objName);
          
          Method getDescription = clazz.getDeclaredMethod("getDescription");
          Object objDescription = getDescription.invoke(instance);
          String pluginDescription = String.class.cast(objDescription);
          
          plugins.getImportedPluginDescriptions().put(pluginName, pluginDescription);
         
          //Find the first implemented interface that is supported, and get its simple
          //name so that it can be given to the factory.
          String interfaceName = "";
          for (Class<?> currInterface : clazz.getInterfaces()) {
            if (supportedInterfaces.contains(currInterface.getSimpleName())) {
              interfaceName = currInterface.getSimpleName();
              break;
            }
          }
         
          PluginType pluginType = pluginTypeFactory.getPluginType(interfaceName);
          pluginType.storePlugin(plugins, pluginName, clazz);
        }
       
        //Close the class loader after the jar have been read. It can then
        //be re-initialised for the next jar.
        classLoader.close();
       
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return plugins;
  }

}
