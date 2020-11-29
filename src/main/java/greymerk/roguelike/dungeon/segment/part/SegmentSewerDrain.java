package greymerk.roguelike.dungeon.segment.part;

import java.util.Random;

import greymerk.roguelike.dungeon.DungeonLevel;
import greymerk.roguelike.theme.ThemeBase;
import greymerk.roguelike.worldgen.Cardinal;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.IStair;
import greymerk.roguelike.worldgen.MetaBlock;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.blocks.BlockType;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class SegmentSewerDrain extends SegmentBase {


  @Override
  protected void genWall(WorldEditor editor, Random rand, DungeonLevel level, Cardinal dir, ThemeBase theme, Coord origin) {

    MetaBlock air = BlockType.get(BlockType.AIR);
    MetaBlock water = BlockType.get(BlockType.WATER_FLOWING);
    IStair stair = theme.getSecondary().getStair();
    MetaBlock bars = BlockType.get(BlockType.IRON_BAR);

    Coord cursor;
    Coord start;
    Coord end;

    Cardinal[] orth = dir.orthogonal();

    start = new Coord(origin);
    start.translate(Cardinal.DOWN);
    end = new Coord(start);
    start.translate(orth[0]);
    end.translate(orth[1]);
    RectSolid.fill(editor, rand, start, end, air);
    start.translate(Cardinal.DOWN);
    end.translate(Cardinal.DOWN);
    RectSolid.fill(editor, rand, start, end, water, false, true);

    start = new Coord(origin);
    start.translate(dir, 2);
    end = new Coord(start);
    start.translate(orth[0]);
    end.translate(orth[1]);
    end.translate(Cardinal.UP, 2);
    RectSolid.fill(editor, rand, start, end, air);
    start.translate(dir);
    end.translate(dir);
    RectSolid.fill(editor, rand, start, end, theme.getPrimary().getWall());

    for (Cardinal o : orth) {
      cursor = new Coord(origin);
      cursor.translate(dir, 2);
      cursor.translate(o);
      stair.setOrientation(o.reverse(), false).set(editor, cursor);
      cursor.translate(Cardinal.UP);
      bars.set(editor, cursor);
      cursor.translate(Cardinal.UP);
      stair.setOrientation(o.reverse(), true).set(editor, cursor);
    }

    start = new Coord(origin);
    start.translate(Cardinal.UP);
    end = new Coord(start);
    end.translate(dir, 5);
    RectSolid.fill(editor, rand, start, end, air);
    water.set(editor, end);

    cursor = new Coord(origin);
    cursor.translate(Cardinal.DOWN);
    cursor.translate(dir);
    air.set(editor, cursor);

  }
}
