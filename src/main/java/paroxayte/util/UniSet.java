package paroxayte.util;

import java.util.LinkedHashSet;

/**
 * Modified hashset to provide equality under .equals(...) as opposed to
 * referential equality.
 */
@SuppressWarnings("serial")
public class UniSet<E> extends LinkedHashSet<E> {

  @Override
  public boolean contains(Object o) {
    return stream().anyMatch((p) -> p.equals(o));
  }

  @Override
  public boolean add(E e) {
    if (contains(e)) {
      return false;
    } else {
      return super.add(e);
    }
  }
}