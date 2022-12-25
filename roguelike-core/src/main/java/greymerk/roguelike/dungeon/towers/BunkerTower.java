package greymerk.roguelike.dungeon.towers;

import com.github.fnar.minecraft.block.BlockType;
import com.github.fnar.minecraft.block.SingleBlockBrush;
import com.github.fnar.minecraft.block.normal.StairsBlock;
import com.github.fnar.roguelike.worldgen.generatables.SpiralStairStep;

import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.util.DyeColor;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;

import static com.github.fnar.minecraft.block.normal.ColoredBlock.stainedGlassPane;

public class BunkerTower extends Tower {

  public BunkerTower(WorldEditor worldEditor, Theme theme) {
    super(worldEditor, theme);
  }

  @Override
  public void generate(Coord dungeon) {
    Coord origin = TowerType.getBaseCoord(editor, dungeon);
    origin.up();

    StairsBlock stair = getPrimaryStair();
    BlockBrush window = stainedGlassPane().setColor(DyeColor.GRAY);

    Coord start = origin.copy();
    Coord end = start.copy();
    start.down();
    start.north(5);
    start.east(5);
    end.south(5);
    end.west(5);
    end.up(4);
    RectHollow.newRect(start, end).fill(editor, getPrimaryWall());

    start = new Coord(origin.getX(), dungeon.getY() + 10, origin.getZ());
    end = origin.copy();
    end.down();
    start.north(5);
    start.east(5);
    end.south(5);
    end.west(5);
    getPrimaryWall().fill(editor, RectSolid.newRect(start, end));

    for (Direction dir : Direction.CARDINAL) {
      start = origin.copy();
      start.translate(dir, 5);
      end = start.copy();
      start.translate(dir.antiClockwise());
      end.translate(dir.clockwise());
      start = new Coord(start.getX(), dungeon.getY() + 10, start.getZ());
      end.up(3);
      getPrimaryWall().fill(editor, RectSolid.newRect(start, end));
      end.down();
      end.translate(dir);
      start.translate(dir);
      getPrimaryWall().fill(editor, RectSolid.newRect(start, end));
      end.down();
      end.translate(dir);
      start.translate(dir);
      getPrimaryWall().fill(editor, RectSolid.newRect(start, end));
    }

    Coord cursor;
    for (Direction dir : Direction.CARDINAL) {
      cursor = origin.copy();
      cursor.translate(dir, 5);
      cursor.translate(dir.antiClockwise(), 5);
      start = new Coord(origin.getX(), dungeon.getY() + 10, origin.getZ());
      start.translate(dir, 6);
      start.translate(dir.antiClockwise(), 6);
      end = origin.copy();
      end.translate(dir, 6);
      end.translate(dir.antiClockwise(), 6);
      end.up(2);
      getPrimaryWall().fill(editor, RectSolid.newRect(start, end));
      start.translate(dir);
      start.translate(dir.antiClockwise());
      end.down();
      end.translate(dir);
      end.translate(dir.antiClockwise());
      getPrimaryWall().fill(editor, RectSolid.newRect(start, end));


      for (Direction o : dir.orthogonals()) {
        start = new Coord(origin.getX(), dungeon.getY() + 10, origin.getZ());
        start.translate(dir, 5);
        start.translate(o, 5);
        end = origin.copy();
        end.translate(dir, 5);
        end.translate(o, 5);
        end.up(2);
        end.translate(o, 2);
        getPrimaryWall().fill(editor, RectSolid.newRect(start, end));
      }
    }

    for (Direction dir : Direction.CARDINAL) {
      stair.setUpsideDown(false).setFacing(dir);
      for (Direction o : dir.orthogonals()) {
        start = origin.copy();
        start.translate(dir, 6);
        start.translate(o, 6);
        start.up(3);
        end = start.copy();
        end.translate(o.reverse());
        stair.fill(editor, RectSolid.newRect(start, end));
        start.down();
        start.translate(dir);
        start.translate(o);
        end = start.copy();
        end.translate(o.reverse(), 2);
        stair.fill(editor, RectSolid.newRect(start, end));
      }
    }

    for (Direction dir : Direction.CARDINAL) {
      cursor = origin.copy();
      cursor.up(3);
      cursor.translate(dir, 6);
      stair.setUpsideDown(false).setFacing(dir).stroke(editor, cursor);
      for (Direction o : dir.orthogonals()) {
        Coord c = cursor.copy();
        c.translate(o);
        stair.setUpsideDown(false).setFacing(dir).stroke(editor, c);
      }
      cursor.down();
      cursor.translate(dir);
      stair.setUpsideDown(false).setFacing(dir).stroke(editor, cursor);
      for (Direction o : dir.orthogonals()) {
        Coord c = cursor.copy();
        c.translate(o);
        stair.setUpsideDown(false).setFacing(dir).stroke(editor, c);
      }
    }

    for (Direction dir : Direction.CARDINAL) {
      cursor = origin.copy();
      cursor.up(4);
      cursor.translate(dir, 5);
      start = cursor.copy();
      end = start.copy();
      start.translate(dir.antiClockwise(), 5);
      end.translate(dir.clockwise(), 5);
      stair.setUpsideDown(false).setFacing(dir).fill(editor, RectSolid.newRect(start, end));
    }

    for (Direction dir : Direction.CARDINAL) {
      cursor = origin.copy();
      cursor.up(5);
      cursor.translate(dir, 4);
      stair.setUpsideDown(false).setFacing(dir).stroke(editor, cursor);
      for (Direction o : dir.orthogonals()) {
        Coord c = cursor.copy();
        c.translate(o);
        getPrimaryPillar().stroke(editor, c);
        c.translate(o);
        stair.setUpsideDown(false).setFacing(dir).stroke(editor, c);
        c.translate(o);
        getPrimaryPillar().stroke(editor, c);
      }
      cursor.up();
      window.stroke(editor, cursor);
      for (Direction o : dir.orthogonals()) {
        Coord c = cursor.copy();
        c.translate(o);
        getPrimaryPillar().stroke(editor, c);
        c.translate(o);
        window.stroke(editor, c);
        c.translate(o);
        getPrimaryPillar().stroke(editor, c);
      }
      cursor.up();
      start = cursor.copy();
      end = start.copy();
      start.translate(dir.antiClockwise(), 3);
      end.translate(dir.clockwise(), 3);
      stair.setUpsideDown(false).setFacing(dir).fill(editor, RectSolid.newRect(start, end));
      start.translate(dir.reverse());
      end.translate(dir.reverse());
      stair.setUpsideDown(true).setFacing(dir.reverse()).fill(editor, RectSolid.newRect(start, end));
      start.up();
      end.up();
      start.translate(dir.clockwise());
      end.translate(dir.antiClockwise());
      stair.setUpsideDown(false).setFacing(dir).fill(editor, RectSolid.newRect(start, end));
      stair.setUpsideDown(false).setFacing(dir.antiClockwise()).stroke(editor, start);
      stair.setUpsideDown(false).setFacing(dir.clockwise()).stroke(editor, end);
      start.translate(dir.reverse());
      end.translate(dir.reverse());
      start.up();
      end.up();
      start.translate(dir.clockwise());
      end.translate(dir.antiClockwise());
      stair.setUpsideDown(false).setFacing(dir).fill(editor, RectSolid.newRect(start, end));
      stair.setUpsideDown(false).setFacing(dir.antiClockwise()).stroke(editor, start);
      stair.setUpsideDown(false).setFacing(dir.clockwise()).stroke(editor, end);
    }

    cursor = origin.copy();
    cursor.up(8);
    start = cursor.copy();
    end = cursor.copy();
    start.north(2);
    start.east(2);
    end.south(2);
    end.west(2);
    getPrimaryWall().fill(editor, RectSolid.newRect(start, end));
    cursor.up();
    start = cursor.copy();
    end = cursor.copy();
    start.north();
    start.east();
    end.south();
    end.west();
    getPrimaryWall().fill(editor, RectSolid.newRect(start, end));

    for (Direction dir : Direction.CARDINAL) {
      start = origin.copy();
      start.up(3);
      start.translate(dir, 4);
      end = start.copy();
      start.translate(dir.antiClockwise(), 4);
      end.translate(dir.clockwise(), 4);
      stair.setUpsideDown(true).setFacing(dir.reverse()).fill(editor, RectSolid.newRect(start, end));
    }

    for (Direction dir : Direction.CARDINAL) {
      start = origin.copy();
      start.translate(dir, 4);
      start.translate(dir.antiClockwise(), 4);
      end = start.copy();
      end.up(3);
      getPrimaryPillar().fill(editor, RectSolid.newRect(start, end));
    }

    for (Direction dir : Direction.CARDINAL) {
      start = origin.copy();
      start.up(5);
      start.translate(dir, 3);
      start.translate(dir.antiClockwise(), 3);
      end = start.copy();
      end.up(2);
      getPrimaryPillar().fill(editor, RectSolid.newRect(start, end));
    }

    for (Direction dir : Direction.CARDINAL) {
      Direction[] orthogonals = dir.orthogonals();
      cursor = origin.copy();
      cursor.up(2);
      cursor.translate(dir, 5);
      stair.setUpsideDown(true).setFacing(dir.reverse()).stroke(editor, cursor);
      cursor.up();
      BlockType.REDSTONE_BLOCK.getBrush().stroke(editor, cursor);
      cursor.translate(dir.reverse());
      BlockType.REDSTONE_LAMP_LIT.getBrush().stroke(editor, cursor);
      cursor.translate(dir.reverse());
      stair.setUpsideDown(true).setFacing(dir.reverse()).stroke(editor, cursor);
      for (Direction o : orthogonals) {
        Coord c = cursor.copy();
        c.translate(o);
        stair.setUpsideDown(true).setFacing(dir.reverse()).stroke(editor, c);
        c.translate(dir);
        stair.setUpsideDown(true).setFacing(o).stroke(editor, c);
      }
    }

    for (Direction dir : Direction.CARDINAL) {
      start = origin.copy();
      start.translate(dir, 5);
      end = start.copy();
      end.up();
      end.translate(dir, 3);
      SingleBlockBrush.AIR.fill(editor, RectSolid.newRect(start, end));

      cursor = start.copy();
      for (Direction o : dir.orthogonals()) {
        start = cursor.copy();
        start.translate(o, 2);
        start.up();
        end = start.copy();
        end.translate(o);
        stair.setUpsideDown(false).setFacing(dir).fill(editor, RectSolid.newRect(start, end));
        start.up();
        end.up();
        window.fill(editor, RectSolid.newRect(start, end));
        start.down(2);
        end.down(2);
        start.translate(dir.reverse());
        end.translate(dir.reverse());
        getPrimaryWall().fill(editor, RectSolid.newRect(start, end));
        start.translate(dir.reverse());
        end.translate(dir.reverse());
        stair.setUpsideDown(false).setFacing(dir.reverse()).fill(editor, RectSolid.newRect(start, end));
      }

      cursor = origin.copy();
      cursor.translate(dir, 3);
      for (Direction o : dir.orthogonals()) {
        Coord c = cursor.copy();
        c.translate(o);
        stair.setUpsideDown(false).setFacing(o.reverse()).stroke(editor, c);
        c.translate(dir);
        stair.setUpsideDown(false).setFacing(o.reverse()).stroke(editor, c);
      }
    }

    int height = origin.getY() - dungeon.getY() + 5;
    SpiralStairStep.newStairSteps(editor).withHeight(height).withStairs(stair).withPillar(getPrimaryPillar()).generate(dungeon);
  }
}
