package paroxayte.knightsTour;

import java.util.Optional;
import paroxayte.util.*;

public class TourBE {

  private final int sz;

  public TourBE(int sz) {
    this.sz = sz;
  }

  public Optional<UniSet<Point<Integer>>> startTour(Point<Integer> init) {
    BoardScanner scanner = new BoardScanner(sz, init);

    return tour(init, scanner) ? Optional.of(scanner.getPath()) : Optional.empty();
  }

  private boolean tour(Point<Integer> pos, BoardScanner scanner) {
    if (scanner.reachedEnd()) {
      return true;
    }

    for (Point<Integer> p : scanner.getAvailable(pos)) {

      if (scanner.walkPath(p)) {
        if(tour(p, scanner)) {
          return true;
        } else {
          scanner.backtrack(p);
        }
      }

    }
    return false; // catch empty result case
  }

  public static void main(String[] args) {
    TourBE testTour = new TourBE(8);
    Optional<UniSet<Point<Integer>>> result = testTour.startTour(new Point<Integer>(0, 0));
    result.ifPresent(System.out::println);
  }

}