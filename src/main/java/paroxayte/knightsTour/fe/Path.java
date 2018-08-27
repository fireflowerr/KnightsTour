package paroxayte.knightsTour.fe;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import paroxayte.util.Point;


/**
 * A path drawn from an ordered coordinate set.
 */
public class Path extends Group {

  private final Scene coordSpace;

  private Path(List<Point<Double>> path, Scene coordSpace) {
    this.coordSpace = coordSpace;
  }

  /**
   * Draws and returns a path from a provided ordered coordinate set.
   * 
   * @param  path        The path to be drawn.
   * @param  coordSpace  The scene which the path will be bound to.
   * @return             The path to be returned.
   */
  public static Path draw(List<Point<Double>> path, Scene coordSpace) {
    Path lines = new Path(path, coordSpace);
    int sz = path.size();

    @SuppressWarnings({"unchecked", "rawtypes"})
    List<Line> groupChildren = (List)lines.getChildren();
    lines.setManaged(false);

    for(int i = 0, j = 1; j < sz; i++, j++) {
      
      Point<Double> start = path.get(i);
      Point<Double> end = path.get(j);

      Line l = lines.createBoundLine(start, end);
      l.setManaged(false);
      groupChildren.add(l);
    }

    return lines;
  } 

  /**
   * Creates a line bound to a relative position in a scene to allow for propper resizability.
   * 
   * @param  start  The start coordinate of the line.
   * @param  end    The end coordinate of the line.
   * @return        A line bound to a certain scene.
   */
  private Line createBoundLine(Point<Double> start, Point<Double> end) {
    double sceneWidth = coordSpace.getWidth();
    double sceneHeight = coordSpace.getHeight();

    double xIRatio = start.x / sceneWidth;
    double yIRatio = start.y / sceneHeight;
    double xFRatio = end.x / sceneWidth;
    double yFRatio = end.y / sceneHeight;

    Line l = new Line();
    l.startXProperty().bind(coordSpace.widthProperty().multiply(xIRatio));
    l.startYProperty().bind(coordSpace.heightProperty().multiply(yIRatio));
    l.endXProperty().bind(coordSpace.widthProperty().multiply(xFRatio));
    l.endYProperty().bind(coordSpace.heightProperty().multiply(yFRatio));

    return l;
  }

}