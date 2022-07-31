package testextensibleclustering.testplugins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import extensibleclustering.plugins.PluginType;
import extensibleclustering.plugins.PluginTypeFactory;

/**
 * Class for testing and development of the PluginTypeFactory class.
 * This class will return the correct plug-in type depending on the interface that is 
 * implemented. 
 * @author David Cook
 */
public class TestPluginTypeFactory {
  
  /**
   * Test to try and get the correct PluginType from the Factory.
   * This should return the ParserType.
   */
  @Test
  public void testGetParserType() {
    PluginTypeFactory testFactory = new PluginTypeFactory();
    assertEquals("Could not get the correct plugin type for parser", 
        "ParserType", testFactory.getPluginType("Parser").getClass().getSimpleName());
  }
  
  /**
   * Test to try and see if it is the same instance returned.
   * The memory address should be the same.
   */
  @Test
  public void testGetSameObject() {
   PluginTypeFactory testFactory = new PluginTypeFactory();
   PluginType objectA = testFactory.getPluginType("Parser");
   PluginType objectB = testFactory.getPluginType("Parser");
   assertTrue("Could not determine that the ParserPlugin is a Singleton",
       objectA == objectB);
  }
}
