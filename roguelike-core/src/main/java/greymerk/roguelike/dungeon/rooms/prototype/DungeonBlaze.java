package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.srwaggon.roguelike.worldgen.block.BlockType;
import com.github.srwaggon.roguelike.worldgen.block.normal.StairsBlock;

import java.util.List;

import greymerk.roguelike.dungeon.base.DungeonBase;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.theme.ThemeBase;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Cardinal;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;
import greymerk.roguelike.worldgen.spawners.MobType;

public class DungeonBlaze extends DungeonBase {


  public DungeonBlaze(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
  }

  public static void genFire(WorldEditor editor, ThemeBase theme, Coord origin) {

    BlockBrush wall = theme.getPrimary().getWall();
    BlockBrush pillar = theme.getPrimary().getPillar();
    StairsBlock stair = theme.getPrimary().getStair();

    Coord cursor;
    Coord start;
    Coord end;

    start = origin.copy();
    end = start.copy();
    end.translate(Cardinal.UP, 2);
    RectSolid.newRect(start, end).fill(editor, BlockType.LAVA_STILL.getBrush());

    for (Cardinal dir : Cardinal.DIRECTIONS) {

      start = origin.copy();
      start.translate(dir);
      start.translate(dir.antiClockwise());
      end = start.copy();
      end.translate(Cardinal.UP, 2);
      RectSolid.newRect(start, end).fill(editor, pillar, true, false);

      cursor = origin.copy();
      cursor.translate(dir);
      stair.setUpsideDown(false).setFacing(dir).stroke(editor, cursor, true, false);
      cursor.translate(Cardinal.UP);
      BlockType.IRON_BAR.getBrush().stroke(editor, cursor);
      cursor.translate(Cardinal.UP);
      stair.setUpsideDown(true).setFacing(dir).stroke(editor, cursor, true, false);

      cursor = origin.copy();
      cursor.translate(Cardinal.UP, 6);
      cursor.translate(dir, 3);

      for (Cardinal o : dir.orthogonals()) {
        Coord c = cursor.copy();
        c.translate(o, 2);
        stair.setUpsideDown(true).setFacing(dir).stroke(editor, c, true, false);
        c.translate(o);
        stair.setUpsideDown(true).setFacing(dir).stroke(editor, c, true, false);
      }

      cursor = origin.copy();
      cursor.translate(Cardinal.UP);
      cursor.translate(dir, 2);

      if (!editor.isAirBlock(cursor)) {
        continue;
      }

      start = origin.copy();
      start.translate(Cardinal.UP, 3);
      start.translate(dir, 2);
      end = start.copy();
      start.translate(dir.antiClockwise(), 2);
      end.translate(dir.clockwise(), 2);
      stair.setUpsideDown(true).setFacing(dir);
      RectSolid.newRect(start, end).fill(editor, stair, true, false);
    }

    start = origin.copy();
    start.translate(Cardinal.UP, 3);
    start.translate(Cardinal.NORTH, 2);
    start.translate(Cardinal.WEST, 2);
    end = origin.copy();
    end.translate(Cardinal.UP, 7);
    end.translate(Cardinal.SOUTH, 2);
    end.translate(Cardinal.EAST, 2);

    RectSolid.newRect(start, end).fill(editor, wall, true, false);

  }

  @Override
  public DungeonBase generate(Coord origin, List<Cardinal> entrances) {

    ThemeBase theme = levelSettings.getTheme();

    BlockBrush wall = theme.getPrimary().getWall();
    StairsBlock stair = theme.getPrimary().getStair();
    BlockBrush pillar = theme.getPrimary().getPillar();

    Coord cursor;
    Coord start;
    Coord end;

    start = origin.copy();
    start.translate(Cardinal.NORTH, 8);
    start.translate(Cardinal.WEST, 8);
    start.translate(Cardinal.DOWN);
    end = origin.copy();
    end.translate(Cardinal.SOUTH, 8);
    end.translate(Cardinal.EAST, 8);
    end.translate(Cardinal.UP, 7);
    RectHollow.newRect(start, end).fill(worldEditor, wall, false, true);

    start = origin.copy();
    start.translate(Cardinal.DOWN);
    end = start.copy();
    start.translate(Cardinal.NORTH, 8);
    start.translate(Cardinal.WEST, 8);
    end.translate(Cardinal.SOUTH, 8);
    end.translate(Cardinal.EAST, 8);
    RectSolid.newRect(start, end).fill(worldEditor, theme.getPrimary().getFloor(), false, true);

    for (Cardinal dir : Cardinal.DIRECTIONS) {
      for (Cardinal orthogonal : dir.orthogonals()) {
        start = origin.copy();
        start.translate(dir, 7);
        start.translate(orthogonal, 2);
        end = start.copy();
        end.translate(Cardinal.UP, 6);
        RectSolid.newRect(start, end).fill(worldEditor, pillar);

        cursor = origin.copy();
        cursor.translate(dir, 8);
        cursor.translate(orthogonal);
        cursor.translate(Cardinal.UP, 2);
        stair.setUpsideDown(true).setFacing(orthogonal.reverse()).stroke(worldEditor, cursor, true, false);

        cursor.translate(dir.reverse());
        cursor.translate(Cardinal.UP);
        stair.setUpsideDown(true).setFacing(orthogonal.reverse()).stroke(worldEditor, cursor);

        start = cursor.copy();
        start.translate(Cardinal.UP);
        end = start.copy();
        end.translate(Cardinal.UP, 3);
        RectSolid.newRect(start, end).fill(worldEditor, pillar);

        cursor.translate(dir.reverse());
        cursor.translate(orthogonal);
        stair.setUpsideDown(true).setFacing(dir.reverse()).stroke(worldEditor, cursor);

        start = cursor.copy();
        start.translate(Cardinal.UP);
        end = start.copy();
        end.translate(Cardinal.UP, 3);
        RectSolid.newRect(start, end).fill(worldEditor, pillar);

        cursor.translate(dir);
        cursor.translate(orthogonal);
        stair.setUpsideDown(true).setFacing(orthogonal).stroke(worldEditor, cursor);

        start = cursor.copy();
        start.translate(Cardinal.UP);
        end = start.copy();
        end.translate(Cardinal.UP, 3);
        RectSolid.newRect(start, end).fill(worldEditor, pillar);

      }

      cursor = origin.copy();
      cursor.translate(dir, 6);
      cursor.translate(dir.antiClockwise(), 6);

      genFire(worldEditor, theme, cursor);

      cursor = origin.copy();
      cursor.translate(Cardinal.UP, 4);
      cursor.translate(dir);
      start = cursor.copy();
      end = cursor.copy();
      end.translate(dir, 6);
      RectSolid.newRect(start, end).fill(worldEditor, wall);
      cursor.translate(dir.antiClockwise());
      wall.stroke(worldEditor, cursor);

      start = end.copy();
      end.translate(Cardinal.UP, 2);
      end.translate(dir.reverse());
      RectSolid.newRect(start, end).fill(worldEditor, wall);

      stair.setUpsideDown(true).setFacing(dir.reverse());

      cursor = end.copy();
      start = cursor.copy();
      start.translate(dir.antiClockwise(), 3);
      end.translate(dir.clockwise(), 3);
      RectSolid.newRect(start, end).fill(worldEditor, wall, true, false);

      start = cursor.copy();
      start.translate(Cardinal.DOWN);
      end = start.copy();
      start.translate(dir.antiClockwise(), 3);
      end.translate(dir.clockwise(), 3);
      RectSolid.newRect(start, end).fill(worldEditor, stair, true, false);

      start = cursor.copy();
      start.translate(dir.reverse());
      end = start.copy();
      start.translate(dir.antiClockwise(), 3);
      end.translate(dir.clockwise(), 3);
      RectSolid.newRect(start, end).fill(worldEditor, stair, true, false);
    }

    start = origin.copy();
    end = origin.copy();
    start.translate(Cardinal.NORTH, 4);
    start.translate(Cardinal.EAST, 4);
    end.translate(Cardinal.SOUTH, 4);
    end.translate(Cardinal.WEST, 4);
    end.translate(Cardinal.DOWN, 4);
    RectHollow.newRect(start, end).fill(worldEditor, wall, false, true);

    start = origin.copy();
    start.translate(Cardinal.DOWN, 2);
    end = start.copy();
    end.translate(Cardinal.DOWN);
    start.translate(Cardinal.NORTH, 3);
    start.translate(Cardinal.EAST, 3);
    end.translate(Cardinal.SOUTH, 3);
    end.translate(Cardinal.WEST, 3);
    RectSolid.newRect(start, end).fill(worldEditor, BlockType.LAVA_FLOWING.getBrush());

    cursor = origin.copy();
    cursor.translate(Cardinal.UP, 4);
    start = cursor.copy();
    start.translate(Cardinal.DOWN);
    start.translate(Cardinal.NORTH);
    start.translate(Cardinal.EAST);
    end = cursor.copy();
    end.translate(Cardinal.UP);
    end.translate(Cardinal.SOUTH);
    end.translate(Cardinal.WEST);
    RectSolid.newRect(start, end).fill(worldEditor, BlockType.OBSIDIAN.getBrush());
    generateSpawner(cursor, MobType.NETHER_MOBS);

    return this;
  }

  public int getSize() {
    return 10;
  }

}
