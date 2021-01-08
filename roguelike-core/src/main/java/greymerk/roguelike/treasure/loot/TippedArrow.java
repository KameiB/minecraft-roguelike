package greymerk.roguelike.treasure.loot;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public class TippedArrow {

  public static ItemStack get(Random rand) {
    return get(rand, 1);
  }

  public static ItemStack get(Random rand, int amount) {
    PotionType type = PotionType.values()[rand.nextInt(PotionType.values().length)];
    return get(type, amount);
  }

  public static ItemStack get(PotionType type) {
    return get(type, 1);
  }

  public static ItemStack get(net.minecraft.potion.PotionType type) {
    return get(type, 1);
  }

  public static ItemStack get(PotionType type, int amount) {

    net.minecraft.potion.PotionType pot = PotionType.getEffect(type, false, false);
    return get(pot, amount);

  }

  public static ItemStack get(net.minecraft.potion.PotionType type, int amount) {
    String id = net.minecraft.potion.PotionType.REGISTRY.getNameForObject(type).toString();

    ItemStack arrow = new ItemStack(Items.TIPPED_ARROW, amount);

    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setString("Potion", id);

    arrow.setTagCompound(nbt);

    return arrow;
  }

  public static ItemStack getHarmful(Random rand, int amount) {
    switch (rand.nextInt(4)) {
      case 0:
        return TippedArrow.get(PotionType.HARM, amount);
      case 1:
        return TippedArrow.get(PotionType.POISON, amount);
      case 2:
        return TippedArrow.get(PotionType.SLOWNESS, amount);
      case 3:
        return TippedArrow.get(PotionType.WEAKNESS, amount);
      default:
        return new ItemStack(Items.ARROW, amount);
    }
  }


}
