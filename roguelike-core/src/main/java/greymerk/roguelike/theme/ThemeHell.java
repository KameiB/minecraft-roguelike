package greymerk.roguelike.theme;

import com.github.srwaggon.minecraft.block.BlockType;
import com.github.srwaggon.minecraft.block.normal.StairsBlock;
import com.github.srwaggon.minecraft.block.redstone.DoorBlock;

import greymerk.roguelike.config.RogueConfig;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.BlockWeightedRandom;

public class ThemeHell extends ThemeBase {

  public ThemeHell() {

    BlockWeightedRandom walls = new BlockWeightedRandom();
    walls.addBlock(BlockType.NETHERBRICK.getBrush(), 200);
    walls.addBlock(BlockType.NETHERRACK.getBrush(), 20);
    walls.addBlock(BlockType.ORE_QUARTZ.getBrush(), 20);
    walls.addBlock(BlockType.SOUL_SAND.getBrush(), 15);
    walls.addBlock(BlockType.NETHER_WART_BLOCK.getBrush(), 10);
    walls.addBlock(BlockType.COAL_BLOCK.getBrush(), 5);

    BlockWeightedRandom floor = new BlockWeightedRandom();
    floor.addBlock(walls, 1500);
    floor.addBlock(BlockType.RED_NETHERBRICK.getBrush(), 500);
    floor.addBlock(BlockType.REDSTONE_BLOCK.getBrush(), 50);
    if (RogueConfig.PRECIOUSBLOCKS.getBoolean()) {
      floor.addBlock(BlockType.GOLD_BLOCK.getBrush(), 2);
    }
    if (RogueConfig.PRECIOUSBLOCKS.getBoolean()) {
      floor.addBlock(BlockType.DIAMOND_BLOCK.getBrush(), 1);
    }

    StairsBlock stair = StairsBlock.netherBrick();
    BlockBrush pillar = BlockType.OBSIDIAN.getBrush();

    DoorBlock door = DoorBlock.iron();
    BlockBrush lightstone = BlockType.GLOWSTONE.getBrush();
    BlockBrush liquid = BlockType.LAVA_FLOWING.getBrush();

    this.primary = new BlockSet(floor, walls, stair, pillar, door, lightstone, liquid);

    BlockBrush secondaryWalls = BlockType.RED_NETHERBRICK.getBrush();
    BlockBrush secondaryPillar = BlockType.MAGMA.getBrush();
    this.secondary = new BlockSet(floor, secondaryWalls, stair, secondaryPillar, door, lightstone, liquid);
  }
}
