package com.github.fnar.minecraft.block.normal;

import com.github.fnar.minecraft.block.BlockType;
import com.github.fnar.minecraft.block.SingleBlockBrush;

import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.BlockJumble;

public class InfestedBlock extends SingleBlockBrush {

  private Stone stone = Stone.STONE;

  public InfestedBlock() {
    super(BlockType.INFESTED_BLOCK);
  }

  public InfestedBlock setStone(Stone stone) {
    this.stone = stone;
    return this;
  }

  public Stone getStone() {
    return stone;
  }

  public static BlockBrush getJumble() {
    Stone[] silverfishBlocks = new Stone[]{
        Stone.COBBLE,
        Stone.STONEBRICK,
        Stone.STONEBRICK_MOSSY,
        Stone.STONEBRICK_CRACKED
    };
    BlockJumble jumble = new BlockJumble();
    for (Stone stone : silverfishBlocks) {
      jumble.addBlock(InfestedBlock.infestedBlock().setStone(stone));
    }
    return jumble;
  }

  public static InfestedBlock infestedBlock() {
    return new InfestedBlock();
  }

  @Override
  public InfestedBlock copy() {
    InfestedBlock copy = new InfestedBlock();
    copy.setFacing(getFacing());
    copy.setStone(stone);
    return copy;
  }
}
