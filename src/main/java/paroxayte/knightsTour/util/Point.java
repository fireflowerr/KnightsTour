package paroxayte.knightsTour.util;

// Tuple containing a number.
public final class Point <T extends Number> implements Comparable<Point<T>> {
  public final T x;
  public final T y;

  private final double dubX;
  private final double dubY;

  public Point(T x, T y) {
    this.x = x;
    this.y = y;
    dubX = x.doubleValue();
    dubY = y.doubleValue();
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  @Override
  public boolean equals(Object b) {
    if (b.getClass().equals(Point.class)) {
      
      @SuppressWarnings("unchecked")
      Point<T> other = ((Point<T>) b);
      return dubX == other.dubX && dubY == other.dubY;
    }
    return false;
  }

  @Override
  public int compareTo(Point<T> o) {
    if (dubX == o.dubX) {
      return dubY == o.dubY ? 0 : dubY < o.dubY ? -1 : 1;
    } else {
      return dubX < o.dubX ? -1 : 1;
    }
  }
}