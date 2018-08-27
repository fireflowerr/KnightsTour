package paroxayte.knightsTour.be;

import java.util.ArrayList;

import paroxayte.knightsTour.util.Point;
import paroxayte.knightsTour.util.UniSet;

class BoardScanner {

  private final int sz;
  private final UniSet<Point<Integer>> coordSpace;
  private final UniSet<Point<Integer>> path = new UniSet<>();

  /**
   * Construct a scanner for a Tour.
   * 
   * @param  tour  The tour to be scanned.
   * @param  init  The initial position.
   */
  BoardScanner(TourBE tour, Point<Integer> init) {
    sz = tour.getSz();
    coordSpace = buildCoordSet();
    path.add(init);
  }

  /**
   * @return  Returns a coordinate set to test against.
   */
  private UniSet<Point<Integer>> buildCoordSet() {
    UniSet<Point<Integer>> coordSpace = new UniSet<>();

    for (int i = 0; i < sz; i++) {
      for (int j = 0; j < sz; j++) {
        coordSpace.add(new Point<Integer>(i, j));
      }
    }
    return coordSpace;
  }


  Point<Integer>[] getAvailable(Point<Integer> p) {
    return getAvailable(p, true);
  }

  /**
   * Searches for valid moves from the current position.
   * 
   * @param  p     The current position.
   * @param  sort  Control structure used for producing ranked results by warnsdorf's rule.
   */
  private Point<Integer>[] getAvailable(Point<Integer> p, boolean sort) {
    ArrayList<Point<Integer>> tmp = new ArrayList<>();
    for (int dX = -2; dX <= 2; dX++) {
      int x2 = dX + p.x;

      if (dX == 0 || x2 < 0 || x2 >= sz) {
        continue;
      }

      for (int dY = -2; dY <= 2; dY++) {
        int y2 = dY + p.y;
        if (dY == 0 || y2 < 0 || y2 >= sz) {
          continue;
        }

        if (dX % 2 == 0 && dY % 2 != 0) {
          tmp.add(new Point<Integer>(x2, y2));
        } else if (dX % 2 != 0 && dY % 2 == 0) {
          tmp.add(new Point<Integer>(x2, y2));
        }

      }
    }
    tmp.removeIf(path::contains);
    if(sort) {
      warnsdorfSort(tmp);
    }
    
    @SuppressWarnings("unchecked")
    Point<Integer>[] toRet = tmp.toArray(new Point[0]);
    return toRet;
  }

  /**
   * Sorts a list of results by the number of valid moves from each position, least to greatest.
   */
  private void warnsdorfSort(ArrayList<Point<Integer>> viable) {
    viable.sort((a, b) -> getAvailable(a, false).length - getAvailable(b, false).length);
  }

  /**
   * @return  True if the movement is valid.
   */
  boolean walkPath(Point<Integer> p) {
    return path.add(p);
  }


  /**
   * Removes the last result. The contract of this method is that the provided point is the last
   * point to have been walked upon.
   */
  void backtrack(Point<Integer> p) {
    path.remove(p);
  }

  /**
   * @return  True if the knight's path contains every coordinate in the coordinate space.
   */
  boolean reachedEnd() {
    return path.equals(coordSpace);
  }

  /**
   * @return  The current path the knight has taken, backtracking excluded.
   */
  UniSet<Point<Integer>> getPath() {
    return path;
  }

}