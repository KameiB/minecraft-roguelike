package greymerk.roguelike.monster;

import com.github.fnar.minecraft.block.spawner.MobType;
import com.github.fnar.minecraft.entity.SlotMapper1_12;
import com.github.fnar.minecraft.item.mapper.ItemMapper1_12;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class EntityMapper1_12 {

  public static Entity map(EntityLiving entityLiving, int level, Random random, int difficulty) {
    Mob mob = applyProfile(entityLiving, level, difficulty, random);

    if (mob == null) {
      return null;
    }

    EntityLiving newEntity = createNewInstance(mob.getMobType(), entityLiving.getEntityWorld());

    newEntity.copyLocationAndAnglesFrom(entityLiving);

    for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
      ItemStack toTrade = entityLiving.getItemStackFromSlot(slot);
      newEntity.setItemStackToSlot(slot, toTrade);
    }

    mob.getItems().forEach((slot, rldItemStack) -> {
      EntityEquipmentSlot equipmentSlot = new SlotMapper1_12().map(slot);
      ItemStack item = new ItemMapper1_12().map(rldItemStack);
      newEntity.setItemStackToSlot(equipmentSlot, item);
    });

    if (newEntity instanceof EntityZombie) {
      ((EntityZombie) newEntity).setChild(entityLiving.isChild() || mob.isChild());
    }

    if (mob.getName() != null) {
      newEntity.setCustomNameTag(mob.getName());
      newEntity.setAlwaysRenderNameTag(true);
    }

    return newEntity;
  }

  private static EntityLiving createNewInstance(MobType mobType, World world) {
    switch (mobType) {
      case BAT:
        return new EntityBat(world);
      case BLAZE:
        return new EntityBlaze(world);
      case CAVESPIDER:
        return new EntityCaveSpider(world);
      case CREEPER:
        return new EntityCreeper(world);
      case DRAGON:
        return new EntityDragon(world);
      case ELDER_GUARDIAN:
        return new EntityElderGuardian(world);
      case ENDERMAN:
        return new EntityEnderman(world);
      case ENDERMITE:
        return new EntityEndermite(world);
      case EVOKER:
        return new EntityEvoker(world);
      case GHAST:
        return new EntityGhast(world);
      case GUARDIAN:
        return new EntityGuardian(world);
      case HUSK:
        return new EntityHusk(world);
      case ILLUSIONER:
        return new EntityIllusionIllager(world);
      case MAGMA_CUBE:
        return new EntityMagmaCube(world);
      case PIGZOMBIE:
        return new EntityPigZombie(world);
      case SHULKER:
        return new EntityShulker(world);
      case SILVERFISH:
        return new EntitySilverfish(world);
      case SKELETON:
        return new EntitySkeleton(world);
      case SLIME:
        return new EntitySlime(world);
      case SPIDER:
        return new EntitySpider(world);
      case STRAY:
        return new EntityStray(world);
      case VEX:
        return new EntityVex(world);
      case VINDICATOR:
        return new EntityVindicator(world);
      case WITCH:
        return new EntityWitch(world);
      case WITHER:
        return new EntityWither(world);
      case WITHERSKELETON:
        return new EntityWitherSkeleton(world);
      case ZOMBIE:
      default:
        return new EntityZombie(world);
    }
  }

  private static Mob applyProfile(EntityLiving entityLiving, int level, int difficulty, Random random) {
    if (entityLiving instanceof EntityCreeper) {
      return new Mob();
    }
    if (entityLiving instanceof EntityEvoker) {
      return new Mob().apply(MonsterProfileType.EVOKER, level, difficulty, random);
    }
    if (entityLiving instanceof EntityHusk) {
      return new Mob().apply(MonsterProfileType.HUSK, level, difficulty, random);
    }
    if (entityLiving instanceof EntitySkeleton) {
      return new Mob().apply(MonsterProfileType.SKELETON, level, difficulty, random);
    }
    if (entityLiving instanceof EntityStray) {
      return new Mob();
    }
    if (entityLiving instanceof EntitySpider) {
      return new Mob();
    }
    if (entityLiving instanceof EntityVindicator) {
      return new Mob().apply(MonsterProfileType.VINDICATOR, level, difficulty, random);
    }
    if (entityLiving instanceof EntityWitch) {
      return new Mob().apply(MonsterProfileType.WITCH, level, difficulty, random);
    }
    if (entityLiving instanceof EntityWitherSkeleton) {
      return new Mob().apply(MonsterProfileType.WITHER_SKELETON, level, difficulty, random);
    }
    if (entityLiving instanceof EntityPigZombie) {
      return new Mob().apply(MonsterProfileType.PIG_ZOMBIE, level, difficulty, random);
    }
    if (entityLiving instanceof EntityZombieVillager) {
      return new Mob().apply(MonsterProfileType.ZOMBIE_VILLAGER, level, difficulty, random);
    }
    if (entityLiving instanceof EntityZombie) {
      return new Mob().apply(MonsterProfileType.ZOMBIE, level, difficulty, random);
    }
    return new Mob();
  }

}
