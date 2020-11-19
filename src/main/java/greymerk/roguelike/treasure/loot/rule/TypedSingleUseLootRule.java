package greymerk.roguelike.treasure.loot.rule;

import net.minecraft.item.ItemStack;

import greymerk.roguelike.treasure.Treasure;
import greymerk.roguelike.treasure.TreasureManager;
import greymerk.roguelike.util.IWeighted;

public class TypedSingleUseLootRule implements ILootRule {
  private final Treasure treasureType;
  private final IWeighted<ItemStack> item;
  private final int level;
  private final int amount;

  public TypedSingleUseLootRule(Treasure treasureType, IWeighted<ItemStack> item, int level, int amount) {
    this.treasureType = treasureType;
    this.item = item;
    this.level = level;
    this.amount = amount;
  }

  @Override
  public void process(TreasureManager treasureManager) {
    treasureManager.addItem(treasureType, level, item, amount);
  }
}
