package paroxayte.knightsTour;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.text.Text;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import paroxayte.util.*;

public class TourApp extends Application {

  private static final String PROMPT_COLOR = "-fx-prompt-text-fill: gainsboro;";

  private Scene scene;
  private Grid board;
  private int sz = 8;
  private VBox container = null;
  private TextField szField = null;
  private TextField coordField = null;

  public static void main(String[] args) {
    launch();
  }

  public void start(Stage stage) {
    scene = initScene(stage);
    stage.setScene(scene);
    stage.setTitle("Knights Tour");
    stage.show();
    board.bindGridDems(scene);
    showInstructions();
  }

  private void showInstructions() {
    Alert popUp = new Alert(Alert.AlertType.INFORMATION);
    popUp.setTitle("Instructions");
    popUp.setContentText("The size field in the bottom let controls the board demensions.\n" 
        + "The coord field in the bottom right controls the starting point of the tour " 
        + "where 0 <= x, y < size\n"
        + "Press enter with the text field in focus to submit changes. "
        + "Changes submitted to the coordinate field will start a new tour. "
        + "Depending on the conditions it could take a while!");

    popUp.showAndWait();
  }

  private void failureAlert() {
    Alert popUp = new Alert(Alert.AlertType.INFORMATION);
    popUp.setTitle("FAILED");
    popUp.setContentText("Unable to find solution");

    popUp.showAndWait();
  }

  private Scene initScene(Stage stage) {
    Scene toRet = new Scene(initContainer(), 400, 400);
    stage.minWidthProperty().bind(toRet.heightProperty());
    stage.minHeightProperty().bind(toRet.widthProperty());
    return toRet;
  }

  private VBox initContainer() {
    VBox container = new VBox(15);
    container.getChildren().add((board = new Grid(sz)));
    container.getChildren().add(initControlArea());
    this.container = container;
    return container;
  }

  private GridPane initControlArea() {
    GridPane controlArea = new GridPane();
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(50);
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(50);
    controlArea.getColumnConstraints().addAll(col1, col2);
    controlArea.setPadding(new Insets(10));
    controlArea.add(initSizeField(), 0, 0);
    controlArea.add(initCoordField(), 1, 0);
    return controlArea;
  }

  private HBox initSizeField() {
    HBox szField = new HBox(5);
    szField.setAlignment(Pos.CENTER);
    TextField szInput = new TextField();
    szInput.setPromptText(Integer.toString(sz));
    szInput.setOnAction(this::handleSzChange);

    szField.setStyle(PROMPT_COLOR);
    szField.setAlignment(Pos.CENTER_LEFT);

    GridPane.setFillWidth(szField, false);
    GridPane.setHalignment(szField, HPos.LEFT);

    Text label = new Text("enter size:");

    szField.getChildren().addAll(label, szInput);
    this.szField = szInput;
    return szField;
  }

  private HBox initCoordField() {
    HBox coordField = new HBox(5);
    coordField.setAlignment(Pos.CENTER);
    TextField coordInput = new TextField();
    coordInput.setPromptText("(0, 0)");
    coordInput.setStyle(PROMPT_COLOR);
    coordInput.setOnAction(this::handlePointChange);

    GridPane.setFillWidth(coordField, false);
    GridPane.setHalignment(coordField, HPos.RIGHT);

    Text label = new Text("enter start point:");

    coordField.getChildren().addAll(label, coordInput);
    this.coordField = coordInput;
    return coordField;
  }

  private void handleSzChange(ActionEvent e) {
    sz = Integer.valueOf(szField.getText());
    board = new Grid(sz);
    container.getChildren().remove(0);
    container.getChildren().add(0, board);
    board.bindGridDems(scene);
  }

  private void handlePointChange(ActionEvent e) {
    board.clearGrid();
    Point<Integer> input = extractPoint();
    TourBE tour = new TourBE(sz);
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Point<Double>> pointMap = tour.startTour(input)
      .map(board::toSceneMapper)
      .orElseGet(() -> new ArrayList());

      handleResult(pointMap);
  }

  private Point<Integer> extractPoint() {
    String input = coordField.getText();
    Pattern digit = Pattern.compile("\\d++");
    Matcher parser = digit.matcher(input);
    IllegalArgumentException notFound = new IllegalArgumentException("cannot parse input: " + input);

    int x = 0;
    if(parser.find()) {
      x = Integer.valueOf(parser.group());
    } else {
      throw notFound;
    }

    int y = 0;
    if(parser.find()) {
      y = Integer.valueOf(parser.group());
    } else {
      throw notFound;
    }

    return new Point<Integer>(x, y);
  }

  private void handleResult(List<Point<Double>> path) {

    if(path.isEmpty()) {
      failureAlert();
    } else {
      drawLines(path);
    }
  }

  private void drawLines(List<Point<Double>> path) {
    int sz = path.size();
    Group lines = new Group();

    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Line> groupChildren = (List)lines.getChildren();
    lines.setManaged(false);

    for(int i = 0, j = 1; j < sz; i++, j++) {
      
      Point<Double> start = path.get(i);
      Point<Double> end = path.get(j);

      Line l = new Line(start.x, start.y, end.x, end.y);
      l.setManaged(false);
      groupChildren.add(l);
    }

    board.getChildren().add(lines);
  }

}