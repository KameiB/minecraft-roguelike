package greymerk.roguelike.dungeon.segment.part;

import com.github.fnar.minecraft.block.SingleBlockBrush;

import greymerk.roguelike.dungeon.DungeonLevel;
import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.BlockJumble;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class SegmentCave extends SegmentBase {

  @Override
  protected void genWall(WorldEditor editor, DungeonLevel level, Direction dir, Theme theme, Coord origin) {
    BlockBrush wall = getPrimaryWalls(theme);
    BlockJumble fill = new BlockJumble();
    fill.addBlock(SingleBlockBrush.AIR);
    fill.addBlock(wall);

    Direction[] orthogonals = dir.orthogonals();

    Coord cursor = origin.copy();

    Coord start = cursor.copy();
    start.up(2);
    start.translate(dir);
    Coord end = start.copy();
    start.translate(orthogonals[0]);
    end.translate(orthogonals[1]);
    RectSolid.newRect(start, end).fill(editor, fill);
    start.translate(dir);
    end.translate(dir);
    RectSolid.newRect(start, end).fill(editor, fill);
    start.down();
    RectSolid.newRect(start, end).fill(editor, fill);
    start.down();
    RectSolid.newRect(start, end).fill(editor, fill);

  }
}
