package com.github.fnar.minecraft.item.mapper;

import com.github.fnar.minecraft.block.CouldNotMapBlockException;
import com.github.fnar.minecraft.block.SingleBlockBrush;
import com.github.fnar.minecraft.item.BlockItem;
import com.github.fnar.minecraft.item.CouldNotMapItemException;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockItemMapper1_12 extends BaseItemMapper1_12<BlockItem> {

  @Override
  public Class<BlockItem> getClazz() {
    return BlockItem.class;
  }

  @Override
  public ItemStack map(BlockItem blockItem) throws CouldNotMapItemException {
    SingleBlockBrush brush = blockItem.getBlockType().getBrush();
    try {
      Block block = com.github.fnar.minecraft.block.BlockMapper1_12.map(brush).getBlock();
      Item item = Item.getItemFromBlock(block);
      return addEnchantmentNbtTags(blockItem, item);
    } catch (CouldNotMapBlockException e) {
      throw new CouldNotMapItemException(blockItem, e);
    }
  }
}
