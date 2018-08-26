package paroxayte.knightsTour;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import paroxayte.util.*;

public class TourApp extends Application {

  private static final String PROMPT_COLOR = "-fx-prompt-text-fill: gainsboro;";

  private Scene scene;
  private GridPane board;
  private int sz = 8;
  private VBox container = null;
  private TextField szField = null;
  private TextField coordField = null;

  public static void main(String[] args) {
    launch();
  }

  public void start(Stage stage) {
    scene = initScene();
    stage.setScene(scene);
    stage.show();
    bindBoardDems();
  }

  private Scene initScene() {
    return new Scene(initContainer(), 400, 400);
  }

  private VBox initContainer() {
    VBox container = new VBox(15);
    container.getChildren().add((board = initBoard(new GridPane(), sz)));
    container.getChildren().add(initControlArea());
    this.container = container;
    return container;
  }

  private HBox initControlArea() {
    HBox controlArea =new HBox(initSizeField(), initCoordField());
    controlArea.setPadding(new Insets(10));
    return controlArea;
  }

  private TextField initSizeField() {
    TextField szField = new TextField();
    szField.setPromptText(Integer.toString(sz));
    szField.setStyle(PROMPT_COLOR);
    szField.setAlignment(Pos.CENTER_LEFT);
    szField.setOnAction(this::handleSzChange);

    this.szField = szField;
    return szField;
  }

  private TextField initCoordField() {
    TextField coordField = new TextField();
    coordField.setPromptText("(0, 0) where 0 <= x, y < size");
    coordField.setStyle(PROMPT_COLOR);
    coordField.setAlignment(Pos.CENTER_RIGHT);
    coordField.setOnAction(this::handlePointChange);

    this.coordField = coordField;
    return coordField;
  }

  private void handleSzChange(ActionEvent e) {
    board = initBoard(new GridPane(), Integer.valueOf(szField.getText()));
    container.getChildren().remove(0);
    container.getChildren().add(0, board);
    bindBoardDems();
  }

  private GridPane initBoard(GridPane board, int sz) {
    this.sz = sz;
    List<ColumnConstraints> cols = board.getColumnConstraints();
    List<RowConstraints> rows = board.getRowConstraints();
    double pSz = 100d / sz;

    for(int i = 0; i < sz; i++) {
      ColumnConstraints c = new ColumnConstraints();
      //c.setPercentWidth(100);
      cols.add(c);

      for(int j = 0; j < sz; j++) {
        RowConstraints r = new RowConstraints();
        //r.setPercentHeight(100);
        rows.add(r);
      }
    }

    initTiles(board, sz);
    return board;
  }

  private void initTiles(GridPane board, int sz) {
    for(int i = 0; i < sz; i++) {
      for(int j = 0; j < sz; j++) {
        
        Pane tile = new Pane();
        tile.setStyle("-fx-border-style: solid");
        tile.setStyle("-fx-border-color: black");
        board.add(tile, i, j);
      }
    }

  }

  private void bindBoardDems() {
    board.minHeightProperty().bind(scene.heightProperty().multiply(.8));
    board.maxHeightProperty().bind(board.minHeightProperty());
    board.minWidthProperty().bind(scene.widthProperty());
    board.maxWidthProperty().bind(board.minWidthProperty());
    
    List<RowConstraints> rows = board.getRowConstraints();
    List<ColumnConstraints> cols = board.getColumnConstraints();
    
    cols.forEach(this::bindColDem);
    rows.forEach(this::bindRowDem);
  }
  
  private void bindColDem(ColumnConstraints c) {
    double pSz = 1d / sz;
    c.minWidthProperty().bind(board.minWidthProperty().multiply(pSz));
    c.maxWidthProperty().bind(board.minWidthProperty().multiply(pSz));
  }
  
  private void bindRowDem(RowConstraints r) {
    double pSz = 1d / sz;
    r.minHeightProperty().bind(board.minHeightProperty().multiply(pSz));
    r.maxHeightProperty().bind(board.minHeightProperty().multiply(pSz));
  }

  private void handlePointChange(ActionEvent e) {
    clearGrid();
    Point<Integer> input = extractPoint();
    TourBE tour = new TourBE(sz);

    System.out.println("calculating path..");
    List<Point<Double>> pointMap = tour.startTour(input)
      .map(this::toTileMapper)
      .map(this::toSceneMapper)
      .orElseGet(() -> new ArrayList());

      handleResult(pointMap);
  }

  private void clearGrid() {
    List<Node> boardChildren = board.getChildren();
    int listSz = 0;
    while((listSz = boardChildren.size()) > sz * sz) {
      boardChildren.remove(listSz - 1);
    }
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

  private ArrayList<Pane> toTileMapper(UniSet<Point<Integer>> path) {
    ArrayList<Pane> tileMap = new ArrayList<>();
    path.forEach((p) -> tileMap.add((Pane)board.getChildren().get(sz * p.y + p.x)));
    return tileMap;
  }

  private ArrayList<Point<Double>> toSceneMapper(ArrayList<Pane> path) {
    ArrayList<Point<Double>> coordMap = new ArrayList<>();
    path.forEach((p) -> coordMap.add(getTileCenter(p)));
    return coordMap;
  }

  private Point<Double> getTileCenter(Pane tile) {
    Bounds sceneBounds = tile.localToScene(tile.getBoundsInLocal());

    double x = sceneBounds.getMinX() + sceneBounds.getWidth() / 2;
    double y = sceneBounds.getMinY() + sceneBounds.getHeight() / 2;

    return new Point<Double>(x, y);
  }

  private void handleResult(List<Point<Double>> path) {

    if(path.isEmpty()) {
      //todo 
    } else {
      drawLines(path);
    }
  }

  @SuppressWarnings("unchecked")
  private void drawLines(List<Point<Double>> path) {
    System.out.println("drawing result");
    int sz = path.size();
    Group lines = new Group();
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