package greymerk.roguelike.dungeon.segment.part;

import com.github.fnar.minecraft.block.BlockType;
import com.github.fnar.minecraft.block.normal.StairsBlock;

import greymerk.roguelike.dungeon.DungeonLevel;
import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;

public class SegmentNetherArch extends SegmentBase {

  @Override
  protected void genWall(WorldEditor editor, DungeonLevel level, Direction dir, Theme theme, Coord origin) {

    StairsBlock stair = getSecondaryStairs(theme);
    stair.setUpsideDown(true).setFacing(dir.reverse());
    BlockBrush pillar = getSecondaryPillar(theme);

    boolean hasLava = editor.getRandom().nextInt(5) == 0;


    for (Direction orthogonals : dir.orthogonals()) {
      Coord cursor = origin.copy();
      cursor.translate(dir, 1);
      cursor.translate(orthogonals, 1);
      cursor.up(2);
      stair.stroke(editor, cursor);

      cursor = origin.copy();
      cursor.translate(dir, 2);
      cursor.translate(orthogonals, 1);
      pillar.stroke(editor, cursor);
      cursor.up(1);
      pillar.stroke(editor, cursor);
    }

    BlockBrush fence = BlockType.FENCE_NETHER_BRICK.getBrush();
    BlockBrush lava = BlockType.LAVA_FLOWING.getBrush();

    Coord cursor = origin.copy();
    cursor.translate(dir, 2);
    fence.stroke(editor, cursor);
    cursor.up(1);
    fence.stroke(editor, cursor);

    if (hasLava) {
      cursor.translate(dir, 1);
      lava.stroke(editor, cursor);
      cursor.down();
      lava.stroke(editor, cursor);
    }
  }
}
