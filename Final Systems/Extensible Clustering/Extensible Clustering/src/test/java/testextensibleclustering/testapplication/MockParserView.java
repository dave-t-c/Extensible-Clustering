package testextensibleclustering.testapplication;

import extensibleclustering.application.ImportedPluginObserver;
import extensibleclustering.application.SceneObserver;
import extensibleclustering.application.StandardViewInterface;
import extensibleclustering.application.StringObserver;
import extensibleclustering.plugins.ImportedPlugins;
import java.io.File;
import java.util.Set;
import javafx.stage.Stage;

public final class MockParserView implements StandardViewInterface {
  
  private static MockParserView instance = new MockParserView();
  
  String parserResult = "";
  File selectedFile = null;
  String selectedName = "";
  String selectedClustering = "";
  String selectedVisualisation = "";
  ImportedPlugins importedPlugins;
  Set<String> parserPluginSet;
  Set<String> clusteringPluginSet;
  
  public static MockParserView getInstance() {
    return instance;
  }

  @Override
  public File getSelectedFile() {
    return selectedFile;
  }

  @Override
  public void setOutputLabel(String result) {
    parserResult = result;
  }
  
  public void setSelectedFile(File file) {
    selectedFile = file;
  }
  
  public void setSelectedClustering(String clusteringName) {
    selectedClustering = clusteringName;
  }
  
  public String getParserResult() {
    return parserResult;
  }
  
  public void setSelectedName(String name) {
    selectedName = name;
  }

  @Override
  public void setParserPluginsSet(Set<String> parsersSet) {
    parserPluginSet = parsersSet;    
  }
  
  @Override 
  public Set<String> getParserPluginsSet() {
    return parserPluginSet;
  }

  @Override
  public String getSelectedParserName() {
    return selectedName;
  }

  @Override
  public void addParserFileObserver(StringObserver parseObserver) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addUpdateParsersObserver(ImportedPluginObserver updateObserver) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setParentStage(Stage newParent) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setButtonActions() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addClusterDataObserver(StringObserver clusteringObserver) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getSelectedClusteringAlgoName() {
    return selectedClustering;
  }

  @Override
  public void setImportedPlugins(ImportedPlugins importedPlugins) {
    this.importedPlugins = importedPlugins;
    setClusteringPluginSet(this.importedPlugins.getImportedClusteringAlgorithms().keySet());
    setParserPluginsSet(this.importedPlugins.getImportedParsers().keySet());
    setVisualisationPluginSet(this.importedPlugins.getImportedVisualisationMethods().keySet());
    
    
  }

  @Override
  public void setClusteringPluginSet(Set<String> clusterSet) {
    clusteringPluginSet = clusterSet;    
  }

  @Override
  public Set<String> getClusteringPluginSet() {
    return clusteringPluginSet;
  }
  
  public void setSelectedVisualisation(String visName) {
    selectedVisualisation = visName;
  }

  @Override
  public String getSelectedVisualisationMethod() {
    return selectedVisualisation;
  }

  @Override
  public void setVisualisationPluginSet(Set<String> visualisationSet) {
    // TODO Auto-generated method stub
  }

  @Override
  public void addVisualisationObserver(SceneObserver visualisationObserver) {
    // TODO Auto-generated method stub
  }
}
