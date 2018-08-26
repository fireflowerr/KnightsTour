package paroxayte.knightsTour;

import java.util.ArrayList;
import paroxayte.util.*;

class BoardScanner {

  private final int sz;
  private final UniSet<Point<Integer>> coordSpace;
  private final UniSet<Point<Integer>> path = new UniSet<>();

  BoardScanner(int sz, Point<Integer> init) {
    this.sz = sz;
    coordSpace = buildCoordSet();
    path.add(init);
  }

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
    
    return tmp.toArray(new Point[0]);
  }

  private void warnsdorfSort(ArrayList<Point<Integer>> viable) {
    viable.sort((a, b) -> getAvailable(a, false).length - getAvailable(b, false).length);
  }

  boolean walkPath(Point<Integer> p) {
    return path.add(p);
  }

  void backtrack(Point<Integer> p) {
    path.remove(p);
  }

  boolean reachedEnd() {
    return path.equals(coordSpace);
  }

  UniSet<Point<Integer>> getPath() {
    return path;
  }

}