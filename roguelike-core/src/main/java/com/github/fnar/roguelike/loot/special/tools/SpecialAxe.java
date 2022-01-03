package com.github.fnar.roguelike.loot.special.tools;

import com.github.fnar.minecraft.item.RldItem;
import com.github.fnar.minecraft.item.ToolType;

import java.util.Random;

import greymerk.roguelike.treasure.loot.Quality;
import greymerk.roguelike.treasure.loot.provider.ToolQualityOddsTable;

public class SpecialAxe extends SpecialTool {

  public SpecialAxe(Random random, int level) {
    this(random, ToolQualityOddsTable.rollToolQuality(random, level));
  }

  public SpecialAxe(Random random, Quality quality) {
    withQuality(quality);
    if (quality == Quality.IRON) {
      withName("Woodland Hatchet");
    } else {
      withName(quality.getDescriptor() + " Axe");
    }
    withRldItem(getItem());
    withToolEnchantments(random);
    withCommonEnchantments(random);
  }

  private RldItem getItem() {
    return ToolType.AXE.asItem().withQuality(quality);
  }

}
