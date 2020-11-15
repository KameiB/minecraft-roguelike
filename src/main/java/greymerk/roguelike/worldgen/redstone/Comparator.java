package greymerk.roguelike.worldgen.redstone;

import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.init.Blocks;

import java.util.Random;

import greymerk.roguelike.worldgen.Cardinal;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.MetaBlock;
import greymerk.roguelike.worldgen.WorldEditor;

public class Comparator {

  public static void generate(WorldEditor world, Random rand, Cardinal dir, boolean subtraction, Coord pos) {

    MetaBlock comparator = new MetaBlock(Blocks.UNPOWERED_COMPARATOR);
    comparator.withProperty(BlockRedstoneComparator.FACING, dir.getFacing());
    if (subtraction) {
      comparator.withProperty(BlockRedstoneComparator.MODE, BlockRedstoneComparator.Mode.SUBTRACT);
    } else {
      comparator.withProperty(BlockRedstoneComparator.MODE, BlockRedstoneComparator.Mode.COMPARE);
    }
    comparator.set(world, pos);
  }

}
