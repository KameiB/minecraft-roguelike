package greymerk.roguelike.monster.profiles;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

import greymerk.roguelike.monster.IEntity;
import greymerk.roguelike.monster.IMonsterProfile;
import greymerk.roguelike.monster.MonsterProfile;
import greymerk.roguelike.treasure.loot.Enchant;
import greymerk.roguelike.treasure.loot.Shield;
import greymerk.roguelike.treasure.loot.provider.ItemNovelty;
import greymerk.roguelike.treasure.loot.provider.ItemWeapon;

public class ProfileSwordsman implements IMonsterProfile {

  @Override
  public void addEquipment(World world, Random rand, int level, IEntity mob) {
    ItemStack weapon = rand.nextInt(20) == 0
        ? ItemNovelty.getItem(ItemNovelty.VALANDRAH)
        : ItemWeapon.getSword(rand, level, Enchant.canEnchant(world.getDifficulty(), rand, level));

    mob.setSlot(EntityEquipmentSlot.MAINHAND, weapon);
    mob.setSlot(EntityEquipmentSlot.OFFHAND, Shield.get(rand));
    MonsterProfile.TALLMOB.getMonsterProfile().addEquipment(world, rand, level, mob);
  }

}
