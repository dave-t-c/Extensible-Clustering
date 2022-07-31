package visualisation;

import extensibleclustering.dependencies.Visualisation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

/**
 * Class that implements dendrogram visualisation.
 * This uses the output of the agglomerative clustering plugin.
 * @author David Cook
 */
public class DendrogramVisualisation implements Visualisation {
  
  private HashMap<String, Line> lineMap;
  private Group visualisationGroup;
  private ScrollPane dataPane;
  private double originalHeight = 0.0;
  private double originalWidth = 0.0;
  private double scale = 1.0;
  private final double maxScale = 20.0;
  private final double minScale = 0.01;

  @Override
  public Scene visualiseData(Path dataFile) throws IOException {
    if (dataFile == null || !dataFile.toFile().exists()) {
      throw new IllegalArgumentException("File does not exist");
    }
    
    //Check the file extension matches the '.tsv' requirement.
    String extension = "";
    int lastDot = dataFile.getFileName().toString().lastIndexOf('.');
    if (lastDot != -1) {
      extension = dataFile.getFileName().toString().substring(lastDot + 1);
    }
    
    //If the extension is empty or not tsv, throw an illegal args exception.
    if (extension.equals("") || !extension.equals("tsv")) {
      throw new IllegalArgumentException("Unsupported File Type Given");
    }
    
    try {      
      visualisationGroup = createVisualisation(dataFile);
    } catch (Exception ex) {
      throw new IllegalArgumentException("An error occured when parsing the file given. "
          + "This may be due to an incorrect file layout");
    }
    
    
    
    //Find the original max Y and max X values so they can be used 
    //when transforming the view to zoom in and out.
    double maxX = 0.0;
    double maxY = 0.0;
    for (Node node : visualisationGroup.getChildren()) {
      Bounds nodeBounds = node.getBoundsInLocal();
      if (nodeBounds.getMaxX() > maxX) {
        maxX = nodeBounds.getMaxX();
      }
      
      if (nodeBounds.getMaxY() > maxY) {
        maxY = nodeBounds.getMaxY();
      }
    }
    
    originalHeight = maxY;
    originalWidth = maxX;
    
    
    dataPane = new ScrollPane();
    dataPane.setPadding(new Insets(25, 25, 10, 25));
    dataPane.setContent(visualisationGroup);
    
    GridPane.setHgrow(dataPane, Priority.ALWAYS);
    GridPane.setVgrow(dataPane, Priority.ALWAYS);
    GridPane rootGrid = new GridPane();
    rootGrid.add(dataPane, 0, 0);
    
    //Create the grid to store the zoom buttons
    Button zoomOut = new Button("Zoom Out (-)");
    Button zoomIn = new Button("Zoom In (+)");
    Button resetSelection = new Button("Clear Selection");
    setupButtons(zoomOut, zoomIn, resetSelection);    
    GridPane buttonGrid = new GridPane();
    buttonGrid.add(zoomOut, 0, 0);
    buttonGrid.add(zoomIn, 1, 0);
    buttonGrid.add(resetSelection, 2, 0);
    buttonGrid.setHgap(10.0);
    buttonGrid.setPadding(new Insets(10, 10, 10, 10));
    
    rootGrid.add(buttonGrid, 0, 1);
    
    return new Scene(rootGrid, 1000, 1000);
  }
  
  
  /**
   * Sets the required actions for the buttons given in parameters. 
   * @param zoomOut - Button to use for zooming out on the visualisation.
   * @param zoomIn - Button to use for zooming in on the visualisation.
   * @param resetSelection - Button to use to clear the selected lines.
   */
  private void setupButtons(Button zoomOut, Button zoomIn, Button resetSelection) {
    zoomIn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
          scale /= 0.95;
          scale = Math.min(maxScale, scale);
          transformGroup(scale);
      }
    });
  
  
    zoomOut.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
          scale *= 0.95;
          scale = Math.max(minScale, scale);
          transformGroup(scale);
      }
    });
  
    resetSelection.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
          lineMap.values()
                  .parallelStream()
                  .forEach(line -> line.setStroke(Color.BLACK));
      }
    });

  }
  
  
  /**
   * Transforms the visualisation group by a specified scale.
   * This is used to zoom in and zoom out.
   * @param scale - The scale to transforms the dendrogram by.
   */
  private void transformGroup(double scale) {
    visualisationGroup.setScaleY(scale);
    visualisationGroup.setScaleX(scale);
    
    double translateDelta = (1.0 - scale) / 2.0;
    visualisationGroup.setTranslateX(- translateDelta * originalWidth);
    visualisationGroup.setTranslateY(- translateDelta * originalHeight);
    
    double originalV = dataPane.getVvalue();
    double originalH = dataPane.getHvalue();
    
    dataPane.setContent(new Group(visualisationGroup));
    
    dataPane.setHvalue(originalH);
    dataPane.setVvalue(originalV);
    
  }
  
  /**
   * Calculates the maximum Y value required for the data given.
   * This uses the file and number of data points used to calculate the value,
   * @param dataPath - Path to the data file being used.
   * @param spacingY - The spacing used between each merge.
   * @return - The maximum y value required for the dendrogram.
   * @throws IOException - Thrown if an error occurs when reading the data file.
   */
  private double calculateMaxY(Path dataPath, double spacingY) throws IOException {
    //Calculate the maximum y value required using the number of data entries in the file.
    //The dataPoints + 2 is used to give some extra space at the bottom of the dendrogram.
    BufferedReader dataReader = new BufferedReader(new FileReader(dataPath.toFile()));
    String line = dataReader.readLine();
    while (!line.contains("Number of data points used:")) {
      line = dataReader.readLine();
    }
    
    dataReader.close();
    
    String[] splitLine = line.split(" ");
    int dataPoints = Integer.parseInt(splitLine[splitLine.length - 1]);
    return (dataPoints + 2) * spacingY;  
  }
  
  /**
   * Creates the dendrogram and adds all lines to the returned group.
   * @param dataFile - The file the visualisation is created with.
   * @return - Group - Group containing the created dendrogram.
   * @throws IOException - Thrown if an error occurs when reading the file.
   */
  private Group createVisualisation(Path dataFile) throws IOException {
    Group gp = new Group();
    String fileLine;
    String[] splitLine;
    double currX = 10;
    double initialY = 0;
    double spacingX = 30;
    double spacingY = 30;
    double currY = 50.0;
    double maxY = calculateMaxY(dataFile, spacingY);
    try (BufferedReader readFile = new BufferedReader(new FileReader(dataFile.toFile()))) {
      //Read through the data section until the next section
      while (!readFile.readLine().equals("Position ID\tLocation")){}

      //Create a Set of the IDs that exist in the data
      lineMap = new HashMap<>();
      
      while ((fileLine = readFile.readLine()) != null) {
        splitLine = fileLine.split("\t");
          
        Line line = new Line(currX, initialY, currX, maxY);
        Tooltip t = new Tooltip(splitLine[0]);
        t.setFont(new Font(20));
        Tooltip.install(line, t);
        line.setStrokeWidth(10);
        lineMap.put(splitLine[0], line);
        currX += spacingX;
      }
      

      
      for (HashMap.Entry<String, Line> entry : lineMap.entrySet()) {
        Label idLabel = new Label(entry.getKey());
        idLabel.setRotate(90.0);
        idLabel.layoutXProperty().bind(entry.getValue().startXProperty().subtract(50));
        idLabel.layoutYProperty().bind(entry.getValue().startYProperty().subtract(75));
        gp.getChildren().addAll(entry.getValue(), idLabel);
      }
    }
    
    
    try (BufferedReader readFile = new BufferedReader(new FileReader(dataFile.toFile()))) {
      //Go though the file again instead of storing all of the previous instructions in memory.
      //This should decrease memory usage.
      
      while (!readFile.readLine().equals("Height\tMerged ID A\tMerged ID B")){}
      
      fileLine = readFile.readLine();

      while (!fileLine.equals("Data Points Used")) {
        splitLine = fileLine.split("\t");
         
        String idA = splitLine[1];
        String idB = splitLine[2];
          
        
          
        //Use index of to get the item in group.getchildren()
        //Make a new line with the merged IDs
        Line mergedLine = new Line();
        mergedLine.setStrokeWidth(10);
        Line connector = new Line();
        connector.setStrokeWidth(10);
          
        Line lineA = lineMap.get(idA);
        Line lineB = lineMap.get(idB);
        //Set the end of the two existing lines to the current y.
        lineA.setEndY(currY);
        lineB.setEndY(currY);
          
        //Create the line that connects the two
        connector.setStartX(lineA.getStartX());
        connector.setEndX(lineB.getStartX());
        gp.getChildren().add(connector);
          
        //Set the height
        connector.setStartY(currY);
        connector.setEndY(currY);
          
        //Set the x value of the new line to be the avg of the two x vals.
        double avgX = (lineA.getStartX() + lineB.getStartX()) / 2;
        mergedLine.setStartX(avgX);
        mergedLine.setEndX(avgX);
          
        //Set the start y to the end of the merged lines.
        mergedLine.setStartY(lineA.getEndY());
        mergedLine.setEndY(maxY);
          
        //Add the new line to the map and the
        String combinedID = idA + "::" + idB;
        Tooltip t = new Tooltip(combinedID);
        t.setFont(new Font(20));
        Tooltip.install(mergedLine, t);
        lineMap.put(combinedID, mergedLine);
        gp.getChildren().add(mergedLine);
          
        //Inc y
        currY += spacingY;
          
        fileLine = readFile.readLine();
      }
      
      //This iterates through each line, and sets the on click event.
      //When this occurs, every line that contains the ID of the line clicked, 
      //will be highlighted in blue, as this allows the user to find all lines with the 
      //same ID in the dendrogram.
      for (Map.Entry<String, Line> entry: lineMap.entrySet()) {
        entry.getValue().setOnMousePressed(mouseEvent -> {
          //ID is the key
          String lineID = entry.getKey();
          //Go through all of the entries
          lineMap.entrySet()
                    .parallelStream()
                    .filter(entry1 -> entry1.getKey().contains(lineID))
                    .forEach(entry1 -> entry1.getValue().setStroke(Color.BLUE));
            
        });
      }
      
      return gp;
      
    }
    
    
  }

  @Override
  public String getName() {
    return "Agglomerative Dendrogram";
  }

  
  @Override
  public String getDescription() {
    return "Creates a dendrogram from an agglomerative hierarchical "
        + "clustering result to visualise where clusters are merged. This supports "
        + "the Agglomerative Hierarchical Clustering Plugin Output";
  }
}
