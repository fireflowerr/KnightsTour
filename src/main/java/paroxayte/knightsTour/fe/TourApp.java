package paroxayte.knightsTour.fe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


/**
 * UI enterance point for Knight's Tour.
 */
public class TourApp extends Application {

  public static void main(String[] args) {
    launch();
  }

  /**
   * Initializes all UI elements.
   */
  public void start(Stage stage) {
    Layout layout = new Layout();
    Scene scene = new Scene(layout, 400, 400); 
    stage.setScene(scene);
    layout.bindToScene(scene);
    new AppController(layout);
    stage.show();
    showInstructions();
  }

  /**
   * Displays a informative pop-up explaining usage of this app.
   */
  private void showInstructions() {
    Alert popUp = new Alert(Alert.AlertType.INFORMATION);
    popUp.setTitle("Instructions");
    popUp.setContentText("The size field in the bottom let controls the board demensions." 
        + "The coord field in the bottom right controls the starting point of the tour " 
        + "where 0 <= x, y < size. "
        + "Press enter with the text field in focus to submit changes. "
        + "Changes submitted to the coordinate field will start a new tour. "
        + "Depending on the conditions it could take a while!");

    popUp.showAndWait();
  }

}