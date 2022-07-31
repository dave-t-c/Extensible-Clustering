package runimport;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Class that loads the class that will execute the plug-in loader.
 * @author David Cook
 */
public class EntryPoint {
  public static void main(String[] args) throws Exception{
    URL loaderExample = LoadPlugin.class.getResource("LoadPlugin.class");
    URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {loaderExample});
    Class<?> loaderClass = Class.forName("runimport.LoadPlugin", true, classLoader);
    Method main = loaderClass.getMethod("main", String[].class);
    main.invoke(null, (Object) args);
  }
  

}
