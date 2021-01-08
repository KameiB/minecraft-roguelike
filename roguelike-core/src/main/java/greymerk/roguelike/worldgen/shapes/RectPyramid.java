package greymerk.roguelike.worldgen.shapes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.Coord;

public class RectPyramid implements IShape {

  private Coord start;
  private Coord end;

  public RectPyramid(Coord start, Coord end) {
    this.start = start.copy();
    this.end = end.copy();
  }

  @Nonnull
  @Override
  public Iterator<Coord> iterator() {
    return new SquarePyramidIterator(start, end);
  }

  @Override
  public List<Coord> get() {
    List<Coord> shape = new ArrayList<>();
    for (Coord pos : this) {
      shape.add(pos);
    }
    return shape;
  }

  private class SquarePyramidIterator implements Iterator<Coord> {

    Coord start;
    Coord diff;
    Coord cursor;
    Direction dir;
    double thetaX;
    double thetaZ;

    public SquarePyramidIterator(Coord start, Coord end) {
      this.start = start.copy();
      Coord s = start.copy();
      Coord e = end.copy();
      Coord.correct(s, e);

      cursor = new Coord(0, 0, 0);
      dir = Direction.NORTH;

      diff = e.copy();
      diff.translate(-s.getX(), -s.getY(), -s.getZ());

      double hx = Math.sqrt(Math.pow(diff.getX(), 2) + Math.pow(diff.getY(), 2));
      thetaX = Math.acos((double) diff.getY() / hx);

      double hz = Math.sqrt(Math.pow(diff.getZ(), 2) + Math.pow(diff.getY(), 2));
      thetaZ = Math.acos((double) diff.getY() / hz);
    }

    @Override
    public boolean hasNext() {
      return cursor.getY() < diff.getY();
    }

    @Override
    public Coord next() {

      Coord toReturn = start.copy();
      toReturn.translate(Direction.UP, cursor.getY());
      if (dir == Direction.NORTH || dir == Direction.SOUTH) {
        toReturn.translate(dir.antiClockwise(), cursor.getX());
        toReturn.translate(dir, cursor.getZ());
      } else {
        toReturn.translate(dir, cursor.getX());
        toReturn.translate(dir.antiClockwise(), cursor.getZ());
      }

      if (dir != Direction.NORTH) {
        dir = dir.antiClockwise();
        return toReturn;
      }

      cursor.translate(Direction.SOUTH);

      if (inRange(cursor)) {
        dir = dir.antiClockwise();
        return toReturn;
      }

      cursor = new Coord(cursor.getX(), cursor.getY(), 0);


      cursor.translate(Direction.EAST);

      if (inRange(cursor)) {
        dir = dir.antiClockwise();
        return toReturn;
      }

      cursor = new Coord(0, cursor.getY(), cursor.getZ());
      cursor.translate(Direction.UP);
      dir = dir.antiClockwise();
      return toReturn;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    private boolean inRange(Coord pos) {
      int y = diff.getY() - cursor.getY();

      if (!(cursor.getX() < Math.tan(thetaX) * y)) {
        return false;
      }

      return cursor.getZ() < Math.tan(thetaZ) * y;

    }

  }

}