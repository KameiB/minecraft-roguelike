package greymerk.roguelike.dungeon.segment.part;

import com.github.srwaggon.minecraft.block.BlockType;
import com.github.srwaggon.minecraft.block.SingleBlockBrush;
import com.github.srwaggon.minecraft.block.normal.StairsBlock;

import java.util.Random;

import greymerk.roguelike.dungeon.DungeonLevel;
import greymerk.roguelike.theme.ThemeBase;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class SegmentNetherLava extends SegmentBase {

  @Override
  protected void genWall(WorldEditor editor, Random rand, DungeonLevel level, Direction dir, ThemeBase theme, Coord origin) {

    StairsBlock stair = theme.getSecondary().getStair();
    BlockBrush lava = BlockType.LAVA_FLOWING.getBrush();

    Coord start;
    Coord end;
    Coord cursor;


    cursor = origin.copy();
    cursor.translate(dir, 2);
    SingleBlockBrush.AIR.stroke(editor, cursor);
    cursor.up(1);
    SingleBlockBrush.AIR.stroke(editor, cursor);
    cursor = origin.copy();
    cursor.translate(dir, 5);
    boolean isAir = editor.isAirBlock(cursor);
    BlockBrush wall = theme.getSecondary().getWall();

    for (Direction orthogonals : dir.orthogonals()) {
      start = origin.copy();
      start.translate(dir, 3);
      end = start.copy();
      start.translate(orthogonals, 1);
      start.up(2);
      end.down();
      if (!isAir) {
        RectSolid.newRect(start, end).fill(editor, SingleBlockBrush.AIR);
        lava.stroke(editor, start);
        start.translate(orthogonals.reverse(), 1);
        lava.stroke(editor, start);
      }

      cursor = origin.copy();
      cursor.translate(dir, 2);

      stair.setUpsideDown(false).setFacing(orthogonals.reverse());
      cursor.translate(orthogonals, 1);
      stair.stroke(editor, cursor);

      stair.setUpsideDown(true).setFacing(orthogonals.reverse());
      cursor.up(1);
      stair.stroke(editor, cursor);

      cursor.up(1);
      wall.stroke(editor, cursor);
      cursor.translate(orthogonals.reverse(), 1);
      wall.stroke(editor, cursor);
    }

  }

}
