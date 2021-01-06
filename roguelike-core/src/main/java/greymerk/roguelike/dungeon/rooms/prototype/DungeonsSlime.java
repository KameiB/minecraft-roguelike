package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.srwaggon.roguelike.worldgen.SingleBlockBrush;
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

public class DungeonsSlime extends DungeonBase {

  public DungeonsSlime(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
  }

  public DungeonBase generate(Coord origin, List<Cardinal> entrances) {
    ThemeBase theme = levelSettings.getTheme();
    BlockBrush wall = theme.getPrimary().getWall();
    BlockBrush bars = BlockType.IRON_BAR.getBrush();
    BlockBrush liquid = theme.getPrimary().getLiquid();
    StairsBlock stair = theme.getPrimary().getStair();

    Coord start;
    Coord end;
    Coord cursor;

    start = origin.copy();
    end = origin.copy();
    start.translate(new Coord(-8, -1, -8));
    end.translate(new Coord(8, 5, 8));
    RectHollow.newRect(start, end).fill(worldEditor, wall, false, true);

    for (Cardinal dir : Cardinal.DIRECTIONS) {
      cursor = origin.copy();
      cursor.translate(dir, 5);
      cursor.translate(dir.antiClockwise(), 5);
      corner(worldEditor, levelSettings, cursor);

      start = origin.copy();
      start.translate(Cardinal.UP, 4);
      start.translate(dir, 3);
      end = start.copy();
      start.translate(dir.antiClockwise(), 8);
      end.translate(dir.clockwise(), 8);
      RectSolid.newRect(start, end).fill(worldEditor, wall);
      start.translate(dir, 4);
      end.translate(dir, 4);
      RectSolid.newRect(start, end).fill(worldEditor, wall);

    }


    for (Cardinal dir : Cardinal.DIRECTIONS) {
      if (!entrances.contains(dir)) {
        start = origin.copy();
        start.translate(dir, 4);
        end = start.copy();
        end.translate(dir, 2);
        start.translate(dir.antiClockwise(), 3);
        end.translate(dir.clockwise(), 3);
        RectSolid.newRect(start, end).fill(worldEditor, SingleBlockBrush.AIR);
        start.translate(Cardinal.DOWN);
        end.translate(Cardinal.DOWN);
        RectSolid.newRect(start, end).fill(worldEditor, liquid);
        start.translate(Cardinal.DOWN);
        end.translate(Cardinal.DOWN);
        RectSolid.newRect(start, end).fill(worldEditor, wall);

        start = origin.copy();
        start.translate(dir, 3);
        end = start.copy();
        start.translate(dir.antiClockwise(), 2);
        end.translate(dir.clockwise(), 2);
        RectSolid.newRect(start, end).fill(worldEditor, bars);

        cursor = origin.copy();
        cursor.translate(dir, 7);
        wall.stroke(worldEditor, cursor);
        cursor.translate(Cardinal.UP, 2);
        wall.stroke(worldEditor, cursor);
        cursor.translate(Cardinal.DOWN);
        cursor.translate(dir);
        liquid.stroke(worldEditor, cursor);
        for (Cardinal o : dir.orthogonals()) {
          cursor = origin.copy();
          cursor.translate(dir, 7);
          cursor.translate(o);
          stair.setUpsideDown(true).setFacing(o).stroke(worldEditor, cursor);
          cursor.translate(Cardinal.UP);
          wall.stroke(worldEditor, cursor);
          cursor.translate(Cardinal.UP);
          stair.setUpsideDown(false).setFacing(o).stroke(worldEditor, cursor);

        }
      }
    }


    return this;
  }

  private void corner(WorldEditor editor, LevelSettings settings, Coord origin) {
    ThemeBase theme = settings.getTheme();
    BlockBrush wall = theme.getPrimary().getWall();
    BlockBrush pillar = theme.getPrimary().getPillar();
    StairsBlock stair = theme.getPrimary().getStair();
    BlockBrush bars = BlockType.IRON_BAR.getBrush();
    BlockBrush water = theme.getPrimary().getLiquid();


    Coord start;
    Coord end;
    Coord cursor;

    start = origin.copy();
    end = origin.copy();
    start.translate(new Coord(-1, -1, -1));
    end.translate(new Coord(1, -1, 1));
    RectSolid.newRect(start, end).fill(editor, water);

    start = origin.copy();
    end = origin.copy();
    start.translate(new Coord(-1, -2, -1));
    end.translate(new Coord(1, -2, 1));
    RectSolid.newRect(start, end).fill(editor, wall);

    for (Cardinal dir : Cardinal.DIRECTIONS) {
      start = origin.copy();
      start.translate(dir, 2);
      start.translate(dir.antiClockwise(), 2);
      end = start.copy();
      end.translate(Cardinal.UP, 3);
      RectSolid.newRect(start, end).fill(editor, pillar);

      for (Cardinal d : Cardinal.DIRECTIONS) {
        cursor = end.copy();
        cursor.translate(d);
        stair.setUpsideDown(true).setFacing(d).stroke(editor, cursor, true, false);
      }

      start = origin.copy();
      start.translate(dir, 2);
      end = start.copy();
      start.translate(dir.antiClockwise());
      end.translate(dir.clockwise());
      RectSolid.newRect(start, end).fill(editor, bars);

    }
  }

  public int getSize() {
    return 8;
  }
}
