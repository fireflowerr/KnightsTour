package paroxayte.knightsTour.fe;

import java.util.List;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import paroxayte.util.Point;


public class Path extends Group{

  private final Scene coordSpace;

  private Path(List<Point<Double>> path, Scene coordSpace) {
    this.coordSpace = coordSpace;
  }

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