package com.github.srwaggon.minecraft.item;

import com.github.srwaggon.minecraft.EffectType;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionUtils;

import greymerk.roguelike.treasure.loot.PotionForm;
import greymerk.roguelike.treasure.loot.PotionType;

public class PotionMapper1_12 {

  public static ItemStack map(RldItemStack rldItemStack) {
    RldItem item = rldItemStack.getItem();
    return map((Potion) item);
  }

  public static ItemStack map(Potion potion) {
    ItemPotion itemPotion = map(potion.getForm());
    ItemStack itemStack = new ItemStack(itemPotion);
    net.minecraft.potion.PotionType data = map(potion.getType(), potion.isAmplified(), potion.isExtended());
    ItemStack potionWithData = PotionUtils.addPotionToItemStack(itemStack, data);
    potion.getEffects()
        .forEach(effect ->
            addCustomEffect(potionWithData, effect.getType(), effect.getAmplification(), effect.getDuration()));
    return potionWithData;
  }

  private static void addCustomEffect(ItemStack itemStack, EffectType type, int amplifier, int duration) {
    final int ticksPerSecond = 20;
    final String CUSTOM = "CustomPotionEffects";

    NBTTagCompound tag = itemStack.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      itemStack.setTagCompound(tag);
    }

    NBTTagCompound toAdd = new NBTTagCompound();
    toAdd.setByte("Id", (byte) type.getEffectID());
    toAdd.setByte("Amplifier", (byte) (amplifier));
    toAdd.setInteger("Duration", duration * ticksPerSecond);
    toAdd.setBoolean("Ambient", true);

    NBTTagList effects = tag.getTagList(CUSTOM, 10);
    effects.appendTag(toAdd);
    tag.setTag(CUSTOM, effects);
    itemStack.setTagCompound(tag);
  }


  private static ItemPotion map(PotionForm potionForm) {
    return potionForm == PotionForm.REGULAR ? Items.POTIONITEM
        : potionForm == PotionForm.SPLASH ? Items.SPLASH_POTION
            : Items.LINGERING_POTION;
  }

  public static net.minecraft.potion.PotionType map(
      PotionType effect,
      boolean isAmplified,
      boolean isExtended
  ) {
    switch (effect) {
      case HEALING:
        return isAmplified ? PotionTypes.STRONG_HEALING
            : PotionTypes.HEALING;
      case HARMING:
        return isAmplified ? PotionTypes.STRONG_HARMING
            : PotionTypes.HARMING;
      case REGENERATION:
        return isExtended ? PotionTypes.LONG_REGENERATION
            : isAmplified ? PotionTypes.STRONG_REGENERATION
                : PotionTypes.REGENERATION;
      case POISON:
        return isExtended ? PotionTypes.LONG_POISON
            : isAmplified ? PotionTypes.STRONG_POISON
                : PotionTypes.POISON;
      case STRENGTH:
        return isExtended ? PotionTypes.LONG_STRENGTH
            : isAmplified ? PotionTypes.STRONG_STRENGTH
                : PotionTypes.STRENGTH;
      case WEAKNESS:
        return isExtended ? PotionTypes.LONG_WEAKNESS
            : PotionTypes.WEAKNESS;
      case SLOWNESS:
        return isExtended ? PotionTypes.LONG_SLOWNESS
            : PotionTypes.SLOWNESS;
      case SWIFTNESS:
        return isExtended ? PotionTypes.LONG_SWIFTNESS
            : isAmplified ? PotionTypes.STRONG_SWIFTNESS
                : PotionTypes.SWIFTNESS;
      case FIRE_RESISTANCE:
        return isExtended ? PotionTypes.LONG_FIRE_RESISTANCE
            : PotionTypes.FIRE_RESISTANCE;
      case INVISIBILITY:
        return isExtended ? PotionTypes.LONG_INVISIBILITY
            : PotionTypes.INVISIBILITY;
      case LEAPING:
        return isExtended ? PotionTypes.LONG_LEAPING
            : PotionTypes.LEAPING;
      case NIGHT_VISION:
        return isExtended ? PotionTypes.LONG_NIGHT_VISION
            : PotionTypes.NIGHT_VISION;
      case WATER_BREATHING:
        return isExtended ? PotionTypes.LONG_WATER_BREATHING
            : PotionTypes.WATER_BREATHING;
      case LUCK:
//        return PotionTypes.LUCK; // introduced in 1.9. Not sure why I can't find it in Forge.
      case LEVITATION:
//        return PotionTypes.LEVITATION; // introduced in 1.9. Not sure why I can't find it in Forge.
      case TURTLE_MASTER:
//        return PotionTypes.TURTLE_MASTER; // introduced in 1.13
      case SLOW_FALLING:
//        return PotionTypes.SLOW_FALLING; // introduced in 1.13
      case AWKWARD:
      default:
        return PotionTypes.AWKWARD;
    }
  }
}