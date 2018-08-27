package paroxayte.knightsTour.be;

import java.util.Optional;
import java.util.Scanner;

import paroxayte.util.Point;
import paroxayte.util.UniSet;

/**
 * A TUI knight's tour solver.
 */
public class TourBE {

  private final int sz;

  /**
   * Construct a tour
   * 
   * @param  sz  The size of the board to be toured.
   */
  public TourBE(int sz) {
    this.sz = sz;
  }

  /**
   * Attemps to solve the tour for a given initial position.
   * 
   * @param  init  The starting coordinate.
   * @return       If solution was successful, return an ordered mapping of the knight's moves. 
   *                Else return an empty Optional.
   */
  public Optional<UniSet<Point<Integer>>> startTour(Point<Integer> init) {
    if(!inBounds(init)) {
      throw new IllegalArgumentException("error: point " + init + " not in bounds.");
    }

    BoardScanner scanner = new BoardScanner(this, init);

    return tour(init, scanner) ? Optional.of(scanner.getPath()) : Optional.empty();
  }

  /**
   * Heuristic backtracking algorithm. 
   */
  private boolean tour(Point<Integer> pos, BoardScanner scanner) {
    if (scanner.reachedEnd()) {
      return true;
    }

    for (Point<Integer> p : scanner.getAvailable(pos)) {

      scanner.walkPath(p);
      if(tour(p, scanner)) {
        return true;
      } else {
        scanner.backtrack(p);
      }  

    }
    return false; // catch empty result case
  }

  private boolean inBounds(Point<Integer> p) {
    return p.x >= 0 && p.y >= 0 && p.x < sz && p.y < sz;
  }

  public static void main(String[] args) {
    Scanner stdn = new Scanner(System.in);

    System.out.println("enter sz: ");
    int sz = stdn.nextInt();
    System.out.println("enter x: ");
    int x = stdn.nextInt();
    System.out.println("enter y: ");
    int y = stdn.nextInt();

    TourBE testTour = new TourBE(sz);
    Optional<UniSet<Point<Integer>>> result = testTour.startTour(new Point<Integer>(x, y));
    result.ifPresent(System.out::println);
    stdn.close();
  }

  public int getSz() {
    return sz;
  }

}