package greymerk.roguelike.theme;

import com.github.srwaggon.roguelike.worldgen.block.normal.StairsBlock;

import greymerk.roguelike.worldgen.BlockWeightedRandom;
import com.github.srwaggon.roguelike.worldgen.block.BlockType;

public class ThemeBling extends ThemeBase {

  public ThemeBling() {
    primary = new BlockSet(createWalls(),
        StairsBlock.quartz(),
        BlockType.LAPIS_BLOCK.getBrush());
    secondary = primary;
  }

  private BlockWeightedRandom createWalls() {
    BlockWeightedRandom walls = new BlockWeightedRandom();
    walls.addBlock(BlockType.IRON_BLOCK.getBrush(), 10);
    walls.addBlock(BlockType.GOLD_BLOCK.getBrush(), 3);
    walls.addBlock(BlockType.EMERALD_BLOCK.getBrush(), 10);
    walls.addBlock(BlockType.DIAMOND_BLOCK.getBrush(), 20);
    return walls;
  }
}
