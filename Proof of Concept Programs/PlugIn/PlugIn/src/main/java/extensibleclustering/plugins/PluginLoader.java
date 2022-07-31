package extensibleclustering.plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  
  public PluginLoader() {
    //Setup the initial locations for the folders.
    //This needs to create the folder as otherwise when they are read an error will occur.
    //This also works as a fall back if for some reason the folders have not been created.
    documentsDirectory = Paths.get(System.getProperty("user.home") + File.separator 
        + "Documents");
    if (Files.notExists(documentsDirectory)) {
      try {
        Files.createDirectory(documentsDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    
    programHomeDirectory = Paths.get(documentsDirectory.toString()
        + File.separator + "Extensible Clustering");
    if (Files.notExists(programHomeDirectory)) {
      try {
        Files.createDirectory(programHomeDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    
    pluginsDirectory = Paths.get(programHomeDirectory.toString() 
        + File.separator + "Plugins");
    if (Files.notExists(pluginsDirectory)) {
      try {
        Files.createDirectory(pluginsDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    }
    
    pluginTypeFactory = new PluginTypeFactory();
    supportedInterfaces = new HashSet<>();
    supportedInterfaces.add("Parser");
  }
  
  public ImportedPlugins loadPlugins() {
   ImportedPlugins plugins = new ImportedPlugins();
   //Iterate through all of the jar files in a directory
   File[] directoryFiles = pluginsDirectory.toFile().listFiles();
   for(File file : directoryFiles) {
     //Get the extension of the file, for it to be considered to be imported, 
     //It must be a '.jar' file.
     //Use the index of the last dot to check. 
     int lastDot = file.getName().lastIndexOf('.');
     String fileExtension = file.getName().substring(lastDot + 1);
     if(!fileExtension.equals("jar")) {
       continue;
     }
     
     //Load the file as a jar entry
     JarFile currJar;
     try {
       currJar = new JarFile(file.getAbsolutePath());
       Enumeration<JarEntry> jarFiles = currJar.entries();
       //Make a new set of urls to load in. This specifies the type of file.
       URL[] urls = { new URL("jar:file:" + file.getAbsolutePath() + "!/") };
       URLClassLoader classLoader = URLClassLoader.newInstance(urls);
       while(jarFiles.hasMoreElements()) {
         //Make a new jar entry using this element.
         JarEntry entry = jarFiles.nextElement();
         
         //Skip directories or files that do not end in 'class'.
         if(entry.isDirectory() || !entry.getName().endsWith(".class")) {
           continue;
         }
         
         //Get the class name - -6 is used to handle .class
         String className = entry.getName().substring(0, entry.getName().length() - 6);
         className = className.replace('/', '.');
         
         Class<?> clazz = classLoader.loadClass(className);
         
         //Check to see the class file implements any of the supported interfaces.
         boolean containsSupportedInterface = false;
         for(Class<?> currInterface : clazz.getInterfaces()) {
           containsSupportedInterface = containsSupportedInterface 
               || supportedInterfaces.contains(currInterface.getSimpleName());
         }
         
         //If it does not contain the supported interface, then move on to the next jar entry.
         if(!containsSupportedInterface) {
           continue;
         }
         
         //Get the name from the class.
         Object instance = clazz.newInstance();
         Method getName = clazz.getDeclaredMethod("getName");
         Object objName = getName.invoke(instance);
         String plugInName = String.class.cast(objName);
         
         //Find the first implemented interface that is supported, and get its simple
         //name so that it can be given to the factory.
         String interfaceName = "";
         for(Class<?> currInterface : clazz.getInterfaces()) {
           if(supportedInterfaces.contains(currInterface.getSimpleName())) {
             interfaceName = currInterface.getSimpleName();
             break;
           }
         }
         
         PluginType pluginType = pluginTypeFactory.getPluginType(interfaceName);
         pluginType.storePlugin(plugins, plugInName, clazz);
         
       }
       
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
   
   return plugins;
  }

}
