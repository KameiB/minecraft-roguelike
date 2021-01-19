package greymerk.roguelike.worldgen.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import greymerk.roguelike.worldgen.Coord;

public class RectSolid implements IShape {

  private final Coord start;
  private final Coord end;

  public RectSolid(Coord start, Coord end) {
    this.start = start;
    this.end = end;
  }

  public static RectSolid newRect(Coord start, Coord end) {
    return new RectSolid(start, end);
  }

  @Override
  public List<Coord> get() {
    List<Coord> coords = new ArrayList<>();

    for (Coord c : this) {
      coords.add(c);
    }

    return coords;
  }

  @Nonnull
  @Override
  public Iterator<Coord> iterator() {
    return new RectSolidIterator(this.start, this.end);
  }

  private static class RectSolidIterator implements Iterator<Coord> {

    Coord cursor;
    Coord start;
    Coord end;

    public RectSolidIterator(Coord start, Coord end) {
      this.start = start.copy();
      this.end = end.copy();

      Coord.correct(this.start, this.end);
      cursor = this.start.copy();
    }

    @Override
    public boolean hasNext() {
      return this.cursor.getY() <= this.end.getY();
    }

    @Override
    public Coord next() {

      Coord toReturn = cursor.copy();

      if (cursor.getZ() == end.getZ() && cursor.getX() == end.getX()) {
        cursor = new Coord(start.getX(), cursor.getY(), start.getZ());
        cursor.up();
        return toReturn;
      }

      if (cursor.getX() == end.getX()) {
        cursor = new Coord(start.getX(), cursor.getY(), cursor.getZ());
        cursor.south();
        return toReturn;
      }

      cursor.east();
      return toReturn;

    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
