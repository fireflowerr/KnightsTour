package paroxayte.util;

// Tuple containing a number.
public final class Point <T extends Number> implements Comparable<Point<T>> {
  public T x;
  public T y;

  private double dubX;
  private double dubY;

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