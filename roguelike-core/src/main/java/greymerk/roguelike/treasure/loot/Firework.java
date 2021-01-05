package greymerk.roguelike.treasure.loot;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;

import java.util.Random;

import greymerk.roguelike.util.DyeColor;

public class Firework {

  public static ItemStack get(Random random, int stackSize) {

    ItemStack rocket = new ItemStack(Items.FIREWORKS, stackSize);

    NBTTagCompound tag = new NBTTagCompound();
    NBTTagCompound fireworks = new NBTTagCompound();

    fireworks.setByte("Flight", (byte) (random.nextInt(3) + 1));

    NBTTagList explosion = new NBTTagList();

    NBTTagCompound properties = new NBTTagCompound();
    properties.setByte("Flicker", (byte) (random.nextBoolean() ? 1 : 0));
    properties.setByte("Trail", (byte) (random.nextBoolean() ? 1 : 0));
    properties.setByte("Type", (byte) (random.nextInt(5)));

    int size = random.nextInt(4) + 1;
    int[] colorArr = new int[size];
    for (int i = 0; i < size; ++i) {
      colorArr[i] = DyeColor.HSLToColor(random.nextFloat(), (float) 1.0, (float) 0.5);
    }

    NBTTagIntArray colors = new NBTTagIntArray(colorArr);
    properties.setTag("Colors", colors);

    explosion.appendTag(properties);
    fireworks.setTag("Explosions", explosion);
    tag.setTag("Fireworks", fireworks);

    rocket.setTagCompound(tag);

    return rocket;
  }
}
