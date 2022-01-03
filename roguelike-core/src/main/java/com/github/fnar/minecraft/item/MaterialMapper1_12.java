package com.github.fnar.minecraft.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MaterialMapper1_12 extends BaseItemMapper1_12<Material> {
  @Override
  public Class<Material> getClazz() {
    return Material.class;
  }

  @Override
  public ItemStack map(Material item) {
    return new ItemStack(mapItemOrThrow(item));
  }

  private Item mapItemOrThrow(Material item) {
    switch (item.getMaterialType()) {
      case BONE:
        return Items.BONE;
      case BOOK:
        return Items.BOOK;
      case CLAY_BALL:
        return Items.CLAY_BALL;
      case COAL:
        return Items.COAL;
      case DIAMOND:
        return Items.DIAMOND;
      case EMERALD:
        return Items.EMERALD;
      case FEATHER:
        return Items.FEATHER;
      case FLINT:
        return Items.FLINT;
      case GOLD_INGOT:
        return Items.GOLD_INGOT;
      case GOLD_NUGGET:
        return Items.GOLD_NUGGET;
      case IRON_INGOT:
        return Items.IRON_INGOT;
      case IRON_NUGGET:
        return Items.IRON_NUGGET;
      case LEATHER:
        return Items.LEATHER;
      case PAPER:
        return Items.PAPER;
      case SLIME_BALL:
        return Items.SLIME_BALL;
      case SNOWBALL:
        return Items.SNOWBALL;
      case STICK:
        return Items.STICK;
      case STRING:
        return Items.STRING;
      case WHEAT:
        return Items.WHEAT;
    }
    throw new CouldNotMapItemException(item);
  }
}
