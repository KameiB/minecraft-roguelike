package greymerk.roguelike.dungeon.towers;

import com.github.srwaggon.minecraft.block.BlockType;
import com.github.srwaggon.minecraft.block.SingleBlockBrush;
import com.github.srwaggon.minecraft.block.decorative.VineBlock;

import java.util.Random;

import greymerk.roguelike.theme.ThemeBase;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.BlockJumble;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class HoleTower implements ITower {

  @Override
  public void generate(WorldEditor editor, Random rand, ThemeBase theme, Coord origin) {

    BlockBrush blocks = theme.getPrimary().getWall();
    Coord floor = Tower.getBaseCoord(editor, origin);

    Coord start = floor.copy()
        .north()
        .east()
        .up(3);

    Coord end = origin.copy()
        .south()
        .west();
    RectSolid.newRect(start, end).fill(editor, SingleBlockBrush.AIR);

    start.north(2)
        .east(2);
    end.south(2)
        .west(2)
        .up();
    RectSolid.newRect(start, end)
        .fill(editor, getRubble(blocks), false, true)
        .fill(editor, VineBlock.vine());
  }

  public BlockJumble getRubble(BlockBrush blocks) {
    BlockJumble rubble = new BlockJumble();
    rubble.addBlock(blocks);
    rubble.addBlock(SingleBlockBrush.AIR);
    rubble.addBlock(BlockType.DIRT.getBrush());
    rubble.addBlock(BlockType.DIRT_COARSE.getBrush());
    rubble.addBlock(BlockType.STONE_SMOOTH.getBrush());
    return rubble;
  }

}
