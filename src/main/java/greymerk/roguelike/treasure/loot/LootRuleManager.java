package greymerk.roguelike.treasure.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import greymerk.roguelike.treasure.Treasure;
import greymerk.roguelike.treasure.TreasureManager;
import greymerk.roguelike.util.IWeighted;
import greymerk.roguelike.util.WeightedChoice;
import greymerk.roguelike.util.WeightedRandomizer;

public class LootRuleManager {

  private List<LootRule> rules;

  public LootRuleManager() {
    this.rules = new ArrayList<>();
  }

  public LootRuleManager(JsonElement e) throws Exception {
    this.rules = new ArrayList<>();
    JsonArray arr = e.getAsJsonArray();
    for (JsonElement ruleElement : arr) {
      if (ruleElement.isJsonNull()) {
        continue;
      }

      JsonObject rule = ruleElement.getAsJsonObject();

      Treasure type = rule.has("type") ? Treasure.valueOf(rule.get("type").getAsString()) : null;

      if (!rule.has("loot")) {
        continue;
      }
      JsonArray data = rule.get("loot").getAsJsonArray();
      WeightedRandomizer<ItemStack> items = new WeightedRandomizer<>(1);
      for (JsonElement item : data) {
        if (item.isJsonNull()) {
          continue;
        }
        items.add(parseProvider(item.getAsJsonObject()));
      }

      List<Integer> levels = new ArrayList<>();
      JsonElement levelElement = rule.get("level");
      if (levelElement.isJsonArray()) {
        JsonArray levelArray = levelElement.getAsJsonArray();
        for (JsonElement lvl : levelArray) {
          if (lvl.isJsonNull()) {
            continue;
          }
          levels.add(lvl.getAsInt());
        }
      } else {
        levels.add(rule.get("level").getAsInt());
      }

      boolean each = rule.get("each").getAsBoolean();
      int amount = rule.get("quantity").getAsInt();

      for (int level : levels) {
        this.add(type, items, level, each, amount);
      }
    }
  }

  public void add(Treasure treasure, ItemStack itemStack) {
    WeightedChoice<ItemStack> weightedChoice = new WeightedChoice<>(itemStack, 1);
    add(treasure, weightedChoice, 0, true, 1);
  }

  public void add(Treasure type, IWeighted<ItemStack> item, int level, boolean toEach, int amount) {
    add(new LootRule(type, item, level, toEach, amount));
  }

  public void add(LootRule toAdd) {
    this.rules.add(toAdd);
  }

  public void add(LootRuleManager other) {
    if (other == null) {
      return;
    }
    this.rules.addAll(other.rules);
  }

  public void process(Random rand, TreasureManager treasure) {
    this.rules.forEach(rule -> rule.process(rand, treasure));
  }

  private IWeighted<ItemStack> parseProvider(JsonObject lootItem) throws Exception {

    int weight = lootItem.has("weight") ? lootItem.get("weight").getAsInt() : 1;

    if (lootItem.get("data").isJsonObject()) {
      JsonObject data = lootItem.get("data").getAsJsonObject();
      return Loot.get(data, weight);
    }

    JsonArray data = lootItem.get("data").getAsJsonArray();
    WeightedRandomizer<ItemStack> items = new WeightedRandomizer<>(weight);
    for (JsonElement jsonElement : data) {
      if (jsonElement.isJsonNull()) {
        continue;
      }
      items.add(parseProvider(jsonElement.getAsJsonObject()));
    }

    return items;
  }

  @Override
  public String toString() {
    return Integer.toString(this.rules.size());
  }
}
