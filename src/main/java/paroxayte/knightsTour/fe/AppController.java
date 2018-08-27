package paroxayte.knightsTour.fe;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import paroxayte.knightsTour.be.TourBE;
import paroxayte.knightsTour.util.Point;


/**
 * Controls all side effects emmited as a result of user interaction.
 */
class AppController {

  private final Layout appLayout;

  /**
   * Constructs a controller. On creation, binds itself to a provided layout.
   */
  AppController(Layout appLayout) {
    this.appLayout = appLayout;
    appLayout.getSzField().setOnAction(this::handleSzChange);
    appLayout.getCoordField().setOnAction(this::handlePointChange);
  }

  /**
   * Handles changes submitted to szField.
   */
  private void handleSzChange(ActionEvent e) {
    int sz = Integer.valueOf(appLayout.getSzField().getText().trim());
    appLayout.replaceBoard(sz);
    appLayout.bindToScene(appLayout.getScene());
  }

  /**
   * Handles changes submitted to coordField.
   */
  private void handlePointChange(ActionEvent e) {
    Grid board = appLayout.getBoard();
    board.clearGrid();
    Point<Integer> input = extractPoint();
    TourBE tour = new TourBE(board.getSz());
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Point<Double>> pointMap = tour.startTour(input)
      .map(board::toSceneMapper)
      .orElseGet(() -> new ArrayList());

      handleResult(pointMap);
  }

  /**
   * @return  Coordinate extracted from user input.
   */
  private Point<Integer> extractPoint() {
    String input = appLayout.getCoordField().getText();
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

  /**
   * Examines the result of a knight's tour and reacts appropriately.
   */
  private void handleResult(List<Point<Double>> coordPath) {

    if(coordPath.isEmpty()) {
      failureAlert();
    } else {
      Path path = Path.draw(coordPath, appLayout.getScene());
      appLayout.getBoard().getChildren().add(path);
    }
  }

  /**
   * Displays a pop-up informing user that search was unsuccessful.
   */
  private void failureAlert() {
    Alert popUp = new Alert(Alert.AlertType.INFORMATION);
    popUp.setTitle("FAILED");
    popUp.setContentText("Unable to find solution");
    popUp.showAndWait();
  }

}