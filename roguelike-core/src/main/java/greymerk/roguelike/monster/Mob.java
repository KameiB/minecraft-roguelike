package greymerk.roguelike.monster;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.github.fnar.minecraft.Difficulty;
import com.github.fnar.minecraft.block.spawner.MobType;
import com.github.fnar.minecraft.entity.Slot;
import com.github.fnar.minecraft.item.Armour;
import com.github.fnar.minecraft.item.ArmourType;
import com.github.fnar.minecraft.item.Arrow;
import com.github.fnar.minecraft.item.RldBaseItem;
import com.github.fnar.minecraft.item.RldItem;
import com.github.fnar.minecraft.item.RldItemStack;
import com.github.fnar.minecraft.item.ToolType;
import com.github.fnar.minecraft.item.WeaponType;
import com.github.fnar.roguelike.loot.special.armour.SpecialArmour;
import com.github.fnar.roguelike.loot.special.tools.SpecialTool;
import com.github.fnar.roguelike.loot.special.weapons.SpecialBow;
import com.github.fnar.roguelike.loot.special.weapons.SpecialSword;
import com.github.fnar.util.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import greymerk.roguelike.config.RogueConfig;
import greymerk.roguelike.treasure.loot.Shield;
import greymerk.roguelike.treasure.loot.provider.LootItem;
import greymerk.roguelike.treasure.loot.provider.SpecialtyLootItem;

import static greymerk.roguelike.treasure.loot.Equipment.rollQuality;

public class Mob {

  private boolean isChild = false;
  private final Map<Slot, RldItemStack> items = Maps.newEnumMap(Slot.class);
  private String name;
  private MobType mobType = MobType.ZOMBIE;

  public Mob withRandomEquipment(int level, Random random) {
    RldBaseItem mainhand = chooseMainhand(random, level);
    if (mainhand != null) {
      equipMainhand(mainhand.asStack());
    }

    RldBaseItem offhand = chooseOffhand(random);
    if (offhand != null) {
      equipOffhand(offhand.asStack());
    }

    equip(Slot.FEET, chooseArmourItem(random, level, ArmourType.BOOTS).asStack());
    equip(Slot.LEGS, chooseArmourItem(random, level, ArmourType.LEGGINGS).asStack());
    equip(Slot.CHEST, chooseArmourItem(random, level, ArmourType.CHESTPLATE).asStack());
    equip(Slot.HEAD, chooseArmourItem(random, level, ArmourType.HELMET).asStack());

    return this;
  }

  public boolean isChild() {
    return isChild;
  }

  public Map<Slot, RldItemStack> getItems() {
    return items;
  }

  public String getName() {
    return name;
  }

  public void equip(Slot slot, RldItemStack rldItemStack) {
    items.put(slot, rldItemStack);
  }

  public void setChild(boolean isChild) {
    this.isChild = isChild;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void equipArmor(Random random, int level, Color color, int difficulty) {
    Arrays.stream(ArmourType.values())
        .filter(armourType -> !armourType.equals(ArmourType.HORSE))
        .forEach(armourType ->
            equip(armourType.asSlot(),
                createArmor(random, level, armourType, color, Difficulty.fromInt(difficulty))));
  }

  private RldItemStack createArmor(Random random, int level, ArmourType armourType, Color color, Difficulty difficulty) {
    return rollForSpecial(random, level, difficulty)
        ? SpecialArmour.createArmour(random, armourType, rollQuality(random, level)).complete()
        : armourType
            .asItem()
            .withQuality(rollQuality(random, level))
            .withColor(color)
            .plzEnchantAtLevel(getEnchantmentLevel(random, level, difficulty))
            .asStack();
  }

  public void equipSword(Random random, int level, Difficulty difficulty) {
    equipMainhand(createSword(random, level, difficulty));
  }

  private static RldItemStack createSword(Random random, int level, Difficulty difficulty) {
    return rollForSpecial(random, level, difficulty)
        ? SpecialSword.newSpecialSword(random, rollQuality(random, level))
        : WeaponType.SWORD.asItem()
            .withQuality(rollQuality(random, level))
            .plzEnchantAtLevel(getEnchantmentLevel(random, level, difficulty))
            .asStack();
  }

  public void equipBow(Random random, int level, Difficulty difficulty) {
    equipMainhand(createBow(random, level, difficulty));
  }

  private RldItemStack createBow(Random random, int level, Difficulty difficulty) {
    return rollForSpecial(random, level, difficulty)
        ? SpecialBow.newSpecialBow(random, rollQuality(random, level))
        : WeaponType.BOW
            .asItem()
            .withQuality(rollQuality(random, level))
            .plzEnchantAtLevel(getEnchantmentLevel(random, level, difficulty))
            .asStack();
  }

  public void equipTool(Random random, int level, Difficulty difficulty) {
    equipMainhand(createTool(random, level, difficulty));
  }

  private static RldItemStack createTool(Random random, int level, Difficulty difficulty) {
    return rollForSpecial(random, level, difficulty)
        ? SpecialTool.createTool(random, rollQuality(random, level))
        : ToolType.random(random)
            .asItem()
            .withQuality(rollQuality(random, level))
            .plzEnchantAtLevel(getEnchantmentLevel(random, level, difficulty))
            .asStack();
  }

  private static boolean rollForSpecial(Random random, int level, Difficulty difficulty) {
    return SpecialtyLootItem.rollForSpecial(random) && rollForEnchanted(random, level, difficulty);
  }

  private static int getEnchantmentLevel(Random random, int level, Difficulty difficulty) {
    return rollForEnchanted(random, level, difficulty)
        ? RogueConfig.getMobItemsEnchantmentsLevelOverride(level).orElse(LootItem.getEnchantmentLevel(random, level))
        : 0;
  }

  public static boolean rollForEnchanted(Random random, int level, Difficulty difficulty) {
    double roll = random.nextDouble();
    return roll < getEnchantmentChance(level, difficulty);
  }

  private static Double getEnchantmentChance(int level, Difficulty difficulty) {
    return RogueConfig.getMobItemsEnchantmentsChanceOverride(level)
        .orElseGet(() -> .05 + getEnchantmentChanceGrowth(difficulty) * level);
  }

  private static double getEnchantmentChanceGrowth(Difficulty difficulty) {
    switch (difficulty) {
      default:
      case PEACEFUL:
        return 0.0;
      case EASY:
        return .01;
      case NORMAL:
        return .02;
      case DIFFICULT:
        return .03;
    }
  }

  public void equipArrows(Arrow arrow) {
    equipOffhand(arrow.asStack());
  }

  public void equipShield(Random rand) {
    equipOffhand(Shield.get(rand));
  }

  public void equipOffhand(RldItemStack item) {
    equip(Slot.OFFHAND, item);
  }

  public void equipMainhand(RldItemStack itemStack) {
    equip(Slot.MAINHAND, itemStack);
  }

  public Mob apply(MonsterProfileType monsterProfile, int level, int difficulty, Random random) {
    return monsterProfile.apply(this, level, difficulty, random);
  }

  private static RldBaseItem chooseMainhand(Random random, int level) {
    return random.nextBoolean()
        ? random.nextBoolean()
        ? null
        : ToolType.randomAmong(random, someTools()).asItem().withQuality(rollQuality(random, level))
        : WeaponType.random(random).asItem().withQuality(rollQuality(random, level));
  }

  private RldBaseItem chooseOffhand(Random random) {
    return random.nextBoolean() ? new com.github.fnar.minecraft.item.Shield() : null;
  }

  private static ArrayList<ToolType> someTools() {
    return Lists.newArrayList(
        ToolType.AXE,
        ToolType.HOE,
        ToolType.PICKAXE,
        ToolType.SHOVEL
    );
  }

  private static Armour chooseArmourItem(Random random, int level, ArmourType armourItem) {
    return armourItem.asItem().withQuality(rollQuality(random, level));
  }

  public RldItem getMainhand() {
    return Optional.ofNullable(items.get(Slot.MAINHAND)).map(RldItemStack::getItem).orElse(null);
  }

  public RldItem getOffhand() {
    return Optional.ofNullable(items.get(Slot.OFFHAND)).map(RldItemStack::getItem).orElse(null);
  }

  public RldItem getBoots() {
    return Optional.ofNullable(items.get(Slot.FEET)).map(RldItemStack::getItem).orElse(null);
  }

  public RldItem getLeggings() {
    return Optional.ofNullable(items.get(Slot.LEGS)).map(RldItemStack::getItem).orElse(null);
  }

  public RldItem getChestplate() {
    return Optional.ofNullable(items.get(Slot.CHEST)).map(RldItemStack::getItem).orElse(null);
  }

  public RldItem getHelmet() {
    return Optional.ofNullable(items.get(Slot.HEAD)).map(RldItemStack::getItem).orElse(null);
  }

  public void setMobType(MobType mobType) {
    this.mobType = mobType;
  }

  public MobType getMobType() {
    return mobType;
  }

  public Mob withMobType(MobType mobType) {
    this.setMobType(mobType);
    return this;
  }

  @Override
  public String toString() {
    return "Mob{" +
        "isChild=" + isChild +
        ", items=" + items +
        ", name='" + name + '\'' +
        ", mobType=" + mobType +
        '}';
  }

}
