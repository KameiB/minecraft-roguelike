package greymerk.roguelike.monster.profiles;

import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

import greymerk.roguelike.monster.IEntity;
import greymerk.roguelike.monster.IMonsterProfile;
import greymerk.roguelike.monster.MobType;
import greymerk.roguelike.treasure.loot.Enchant;
import greymerk.roguelike.treasure.loot.Quality;
import greymerk.roguelike.treasure.loot.Slot;
import greymerk.roguelike.treasure.loot.TippedArrow;
import greymerk.roguelike.treasure.loot.provider.ItemArmour;
import greymerk.roguelike.treasure.loot.provider.ItemWeapon;

public class ProfilePoisonArcher implements IMonsterProfile {

  @Override
  public void addEquipment(World world, Random rand, int level, IEntity mob) {

    mob.setMobClass(MobType.STRAY, false);

    mob.setSlot(EntityEquipmentSlot.OFFHAND, TippedArrow.get(PotionTypes.STRONG_POISON));
    mob.setSlot(EntityEquipmentSlot.MAINHAND, ItemWeapon.getBow(rand, level, Enchant.canEnchant(world.getDifficulty(), rand, level)));

    for (EntityEquipmentSlot slot : new EntityEquipmentSlot[]{
        EntityEquipmentSlot.HEAD,
        EntityEquipmentSlot.CHEST,
        EntityEquipmentSlot.LEGS,
        EntityEquipmentSlot.FEET
    }) {
      ItemStack item = ItemArmour.get(rand, Slot.getSlot(slot), Quality.WOOD);
      Enchant.enchantItem(rand, item, 20);
      ItemArmour.dyeArmor(item, 178, 255, 102); //bright lime green
      mob.setSlot(slot, item);
    }
  }

}
