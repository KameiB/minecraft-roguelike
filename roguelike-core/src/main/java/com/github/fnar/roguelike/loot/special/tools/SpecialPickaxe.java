package com.github.fnar.roguelike.loot.special.tools;

import com.github.fnar.minecraft.item.RldItem;
import com.github.fnar.minecraft.item.ToolType;

import java.util.Random;

import greymerk.roguelike.treasure.loot.Equipment;
import greymerk.roguelike.treasure.loot.Quality;

public class SpecialPickaxe extends SpecialTool {

  public SpecialPickaxe(Random random, int level) {
    this(random, Equipment.rollQuality(random, level));
  }

  public SpecialPickaxe(Random random, Quality quality) {
    withQuality(quality);
    withName(quality.getDescriptor() + " Pick");
    withRldItem(getItem());
    withToolEnchantments(random);
    withCommonEnchantments(random);
  }

  private RldItem getItem() {
    return ToolType.PICKAXE.asItem().withQuality(quality);
  }

}
