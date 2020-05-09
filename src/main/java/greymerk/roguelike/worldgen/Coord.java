package greymerk.roguelike.worldgen;

import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class Coord {

  private int x;
  private int y;
  private int z;

  public Coord(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Coord(Coord toClone) {
    x = toClone.x;
    y = toClone.y;
    z = toClone.z;
  }

  public Coord(BlockPos bp) {
    x = bp.getX();
    y = bp.getY();
    z = bp.getZ();
  }

  // Arranges two coords so that the they create a positive cube.
  // used in fill routines.
  public static void correct(Coord one, Coord two) {

    int temp;

    if (two.x < one.x) {
      temp = two.x;
      two.x = one.x;
      one.x = temp;
    }

    if (two.y < one.y) {
      temp = two.y;
      two.y = one.y;
      one.y = temp;
    }

    if (two.z < one.z) {
      temp = two.z;
      two.z = one.z;
      one.z = temp;
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public Coord newCoord(Cardinal dir) {
    return new Coord(this).translate(dir, 1);
  }

  public Coord translate(Cardinal dir, int amount) {
    switch (dir) {
      case EAST:
        x += amount;
        return this;
      case WEST:
        x -= amount;
        return this;
      case UP:
        y += amount;
        return this;
      case DOWN:
        y -= amount;
        return this;
      case NORTH:
        z -= amount;
        return this;
      case SOUTH:
        z += amount;
        return this;
    }
    return this;
  }

  public Coord translate(int x, int y, int z) {
    this.x += x;
    this.y += y;
    this.z += z;
    return this;
  }

  public Coord translate(Coord other) {
    return translate(other.x, other.y, other.z);
  }

  public Coord translate(Cardinal dir) {
    translate(dir, 1);
    return this;
  }

  public double distance(Coord other) {
    double side1 = Math.abs(getX() - other.getX());
    double side2 = Math.abs(getZ() - other.getZ());

    return Math.sqrt((side1 * side1) + (side2 * side2));
  }

  public Cardinal dirTo(Coord other) {
    int xdiff = other.x - x;
    int ydiff = other.y - y;
    int zdiff = other.z - z;

    if (
        Math.abs(ydiff) > Math.abs(xdiff)
            && Math.abs(ydiff) > Math.abs(zdiff)) {
      return Cardinal.UP;
    }


    if (Math.abs(xdiff) < Math.abs(zdiff)) {
      if (zdiff < 0) {
        return Cardinal.NORTH;
      } else {
        return Cardinal.SOUTH;
      }
    } else {
      if (xdiff < 0) {
        return Cardinal.WEST;
      } else {
        return Cardinal.EAST;
      }
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    String toReturn = "";
    toReturn += "x: " + x + " ";
    toReturn += "y: " + y + " ";
    toReturn += "z: " + z;
    return toReturn;
  }

  @Override
  public boolean equals(Object o) {
    Coord other = (Coord) o;

    if (x != other.x) {
      return false;
    }
    if (y != other.y) {
      return false;
    }
    return z == other.z;
  }

  public BlockPos getBlockPos() {
    return new BlockPos(x, y, z);
  }
}
