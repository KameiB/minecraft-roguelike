package greymerk.roguelike.theme;

import com.github.srwaggon.roguelike.worldgen.block.BlockType;
import com.github.srwaggon.roguelike.worldgen.block.normal.StairsBlock;
import com.github.srwaggon.roguelike.worldgen.block.normal.Wood;

import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.BlockWeightedRandom;

public class ThemeSpruce extends ThemeBase {

  public ThemeSpruce() {

    BlockWeightedRandom walls = new BlockWeightedRandom();
    walls.addBlock(BlockType.STONE_BRICK.getBrush(), 20);
    BlockBrush cracked = BlockType.STONE_BRICK_CRACKED.getBrush();

    walls.addBlock(cracked, 10);
    walls.addBlock(BlockType.COBBLESTONE.getBrush(), 5);
    walls.addBlock(BlockType.GRAVEL.getBrush(), 1);

    StairsBlock stair = StairsBlock.stoneBrick();

    this.primary = new BlockSet(walls, stair, walls);

    Wood wood = Wood.SPRUCE;
    BlockBrush segmentWall = wood.getPlanks();
    StairsBlock segmentStair = StairsBlock.spruce();

    BlockBrush pillar = wood.getLog();
    this.secondary = new BlockSet(segmentWall, segmentStair, pillar);

  }
}
