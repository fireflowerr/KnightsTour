package paroxayte.knightsTour.fe;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import paroxayte.util.Point;
import paroxayte.util.UniSet;


/**
 * A n x n grid.
 */
public class Grid extends GridPane {

  private final int sz;

  public Grid(int sz) {
    super();
    this.sz = sz;
    initGrid();
  }

  private void initGrid() {
    List<ColumnConstraints> cols = getColumnConstraints();
    List<RowConstraints> rows = getRowConstraints();

    for(int i = 0; i < sz; i++) {
      ColumnConstraints c = new ColumnConstraints();
      cols.add(c);

      for(int j = 0; j < sz; j++) {
        RowConstraints r = new RowConstraints();
        rows.add(r);
      }
    }

    initTiles();
  }

  private void initTiles() {
    for(int i = 0; i < sz; i++) {
      for(int j = 0; j < sz; j++) {
        
        Pane tile = new Pane();
        tile.setStyle("-fx-border-style: solid");
        tile.setStyle("-fx-border-color: black");
        add(tile, i, j);
      }
    }

  }

  /**
   * Binds a grid to the top of a provided scene, leaving a bottom section exposed.
   * 
   * @param  scene  The scene to be bound to.
   */
  public void bindGridDems(Scene scene) {
    minHeightProperty().bind(scene.heightProperty().multiply(.8));
    maxHeightProperty().bind(minHeightProperty());
    minWidthProperty().bind(scene.widthProperty());
    maxWidthProperty().bind(minWidthProperty());
    
    List<RowConstraints> rows = getRowConstraints();
    List<ColumnConstraints> cols = getColumnConstraints();
    
    cols.forEach(this::bindColDem);
    rows.forEach(this::bindRowDem);
  }
  
  private void bindColDem(ColumnConstraints c) {
    double pSz = 1d / sz;
    c.minWidthProperty().bind(minWidthProperty().multiply(pSz));
    c.maxWidthProperty().bind(minWidthProperty().multiply(pSz));
  }
  
  private void bindRowDem(RowConstraints r) {
    double pSz = 1d / sz;
    r.minHeightProperty().bind(minHeightProperty().multiply(pSz));
    r.maxHeightProperty().bind(minHeightProperty().multiply(pSz));
  }

  /**
   * Clears all elements from this Grid.
   */
  public void clearGrid() {
    List<Node> children = getChildren();
    int listSz = 0;
    while((listSz = children.size()) > sz * sz) {
      children.remove(listSz - 1);
    }
  }
  

  /**
   * Helper method that maps a set of Point coordinates to the respective cell.
   * 
   * @param  path  The set of coordinates to be mapped.
   */
  private ArrayList<Pane> toTileMapper(UniSet<Point<Integer>> path) {
    ArrayList<Pane> tileMap = new ArrayList<>();
    path.forEach((p) -> tileMap.add((Pane)getChildren().get(sz * p.x + p.y))); //This will retrieve a GridPane child node at the coordinates (x, y)
    return tileMap;
  }

  /**
   * Maps a set of Point coordinates to the center of the cell at each coordinate in the Scene's coordinate space.
   */
  public ArrayList<Point<Double>> toSceneMapper(UniSet<Point<Integer>> raw) {
    ArrayList<Pane> path = toTileMapper(raw);
    ArrayList<Point<Double>> coordMap = new ArrayList<>();
    path.forEach((p) -> coordMap.add(getTileCenter(p)));
    return coordMap;
  }

  /**
   * Helper method that returns the center of a provided grid cell in the Scene's coordinate space.
   */
  private Point<Double> getTileCenter(Pane tile) {
    Bounds sceneBounds = tile.localToScene(tile.getBoundsInLocal());

    double x = sceneBounds.getMinX() + sceneBounds.getWidth() / 2;
    double y = sceneBounds.getMinY() + sceneBounds.getHeight() / 2;

    return new Point<Double>(x, y);
  }

  public int getSz() {
    return sz;
  }

}