package paroxayte.knightsTour.fe;

import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * UI layout for knight's tour.
 */
class Layout extends VBox {

  private static final int DEFAULT_SZ = 8;
  private static final String PROMPT_COLOR = "-fx-prompt-text-fill: gainsboro;";

  private final TextField szField;
  private final TextField coordField;
  private Grid board;


  Layout() {
    super(15);
    szField = initSzField();
    coordField = initCoordField();
    getChildren().add((board = new Grid(DEFAULT_SZ)));
    getChildren().add(initControlArea());
  }

  /**
   * Replace the current board with one of a new size.
   * 
   * @param  sz  The size of the new board.
   */
  void replaceBoard(int sz) {
    List<Node> children = getChildren();
    children.remove(0);
    children.add(0, (board = new Grid(sz)));
  }

  /**
   * Binds the layout elements to the scene for correct resizing functionality.
   */
  void bindToScene(Scene scene) {
    board.bindGridDems(scene);
  }

  Grid getBoard() {
    return board;
  }

  TextField getSzField() {
    return szField;
  }

  TextField getCoordField() {
    return coordField;
  }

  private GridPane initControlArea() {
    GridPane controlArea = new GridPane();
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(50);
    ColumnConstraints col2 = new ColumnConstraints();
    col2.setPercentWidth(50);
    controlArea.getColumnConstraints().addAll(col1, col2);
    controlArea.setPadding(new Insets(10));
    controlArea.add(initSzArea(), 0, 0);
    controlArea.add(initCoordArea(), 1, 0);
    return controlArea;
  }

  private TextField initSzField() {
    TextField szField = new TextField();
    szField.setPromptText(Integer.toString(DEFAULT_SZ));
    szField.setStyle(PROMPT_COLOR);
    return szField;
  }

  private HBox initSzArea() {
    HBox szArea = new HBox(5);
    szArea.setAlignment(Pos.CENTER);
    szArea.setStyle(PROMPT_COLOR);
    szArea.setAlignment(Pos.CENTER_LEFT);

    Text label = new Text("enter size:");

    szArea.getChildren().addAll(label, szField);
    return szArea;
  }

  private TextField initCoordField() {
    TextField coordField = new TextField();
    coordField.setPromptText("(0, 0)");
    coordField.setStyle(PROMPT_COLOR);
    return coordField;
  }

  private HBox initCoordArea() {
    HBox coordArea = new HBox(5);
    coordArea.setAlignment(Pos.CENTER);
    GridPane.setFillWidth(coordArea, false);
    GridPane.setHalignment(coordArea, HPos.RIGHT);

    Text label = new Text("enter start point:");

    coordArea.getChildren().addAll(label, coordField);
    return coordArea;
  }

}