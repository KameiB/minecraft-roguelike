package greymerk.roguelike.monster;

import com.github.fnar.forge.minecraft.entity.EntityMagmaCube;
import com.github.fnar.forge.minecraft.entity.EntitySlime;
import com.github.fnar.minecraft.block.spawner.MobType;
import com.github.fnar.minecraft.entity.SlotMapper1_12;
import com.github.fnar.minecraft.item.CouldNotMapItemException;
import com.github.fnar.minecraft.item.mapper.ItemMapper1_12;
import com.github.fnar.roguelike.Roguelike;

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
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
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

public class EntityProfiler1_12 {

  public static EntityLiving applyProfile(EntityLiving oldEntity, int level, Random random, int difficulty) {
    Mob mob = applyProfile(oldEntity, level, difficulty, random);

    if (mob == null) {
      return null;
    }

    EntityLiving newEntity = createNewInstance(mob.getMobType(), oldEntity.getEntityWorld());

    if (mob.getName() != null) {
      newEntity.setCustomNameTag(mob.getName());
      newEntity.setAlwaysRenderNameTag(true);
    }

    if (newEntity instanceof EntityZombie) {
      ((EntityZombie) newEntity).setChild(oldEntity.isChild() || mob.isChild());
    }

    if (mob.isEquippable()) {
      for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
        ItemStack toTrade = oldEntity.getItemStackFromSlot(slot);
        newEntity.setItemStackToSlot(slot, toTrade);
      }

      mob.getItems().forEach((slot, rldItemStack) -> {
        EntityEquipmentSlot equipmentSlot = new SlotMapper1_12().map(slot);
        try {
          ItemStack item = new ItemMapper1_12().map(rldItemStack);
          newEntity.setItemStackToSlot(equipmentSlot, item);
        } catch (CouldNotMapItemException e) {
          Roguelike.LOGGER.info(e);
        }
      });
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
      case ELDER_GUARDIAN:
        return new EntityElderGuardian(world);
      case ENDERMAN:
        return new EntityEnderman(world);
      case ENDERMITE:
        return new EntityEndermite(world);
      case ENDER_DRAGON:
        return new EntityDragon(world);
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
        EntityMagmaCube entityMagmaCube = new EntityMagmaCube(world);
        entityMagmaCube.setSlimeSize(randomSlimeSize(), true);
        return entityMagmaCube;
      case SHULKER:
        return new EntityShulker(world);
      case SILVERFISH:
        return new EntitySilverfish(world);
      case SKELETON:
        return new EntitySkeleton(world);
      case SLIME:
        EntitySlime entitySlime = new EntitySlime(world);
        entitySlime.setSlimeSize(randomSlimeSize(), true);
        return entitySlime;
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
      case WITHER_SKELETON:
        return new EntityWitherSkeleton(world);
      case ZOMBIE:
        return new EntityZombie(world);
      case ZOMBIE_PIGMAN:
        return new EntityPigZombie(world);
      case ZOMBIE_VILLAGER:
        return new EntityZombieVillager(world);
      default:
        return new EntityZombie(world);
    }
  }

  private static int randomSlimeSize() {
    return 1 << new Random().nextInt(3);
  }

  private static Mob applyProfile(EntityLiving entityLiving, int level, int difficulty, Random random) {
    if (entityLiving instanceof EntityBlaze) {
      return new Mob().withMobType(MobType.BLAZE);
    }
    if (entityLiving instanceof EntityCaveSpider) {
      return new Mob().withMobType(MobType.CAVESPIDER);
    }
    if (entityLiving instanceof EntityCreeper) {
      return new Mob().withMobType(MobType.CREEPER);
    }
    if (entityLiving instanceof EntityEnderman) {
      return new Mob().withMobType(MobType.ENDERMAN);
    }
    if (entityLiving instanceof EntityEndermite) {
      return new Mob().withMobType(MobType.ENDERMITE);
    }
    if (entityLiving instanceof EntityEvoker) {
      return new Mob().apply(MonsterProfileType.EVOKER, level, difficulty, random);
    }
    if (entityLiving instanceof EntityGhast) {
      return new Mob().withMobType(MobType.GHAST);
    }
    if (entityLiving instanceof EntityGuardian) {
      return new Mob().withMobType(MobType.GUARDIAN);
    }
    if (entityLiving instanceof EntityHusk) {
      return new Mob().apply(MonsterProfileType.HUSK, level, difficulty, random);
    }
    if (entityLiving instanceof EntityIllusionIllager) {
      return new Mob().withMobType(MobType.ILLUSIONER);
    }
    if (entityLiving instanceof EntityMagmaCube) {
      return new Mob().withMobType(MobType.MAGMA_CUBE);
    }
    if (entityLiving instanceof EntityShulker) {
      return new Mob().withMobType(MobType.SHULKER);
    }
    if (entityLiving instanceof EntitySilverfish) {
      return new Mob().withMobType(MobType.SILVERFISH);
    }
    if (entityLiving instanceof EntitySkeleton) {
      return new Mob().apply(MonsterProfileType.SKELETON, level, difficulty, random);
    }
    if (entityLiving instanceof EntitySlime) {
      return new Mob().withMobType(MobType.SLIME);
    }
    if (entityLiving instanceof EntitySpider) {
      return new Mob().withMobType(MobType.SPIDER);
    }
    if (entityLiving instanceof EntityStray) {
      return new Mob().withMobType(MobType.STRAY);
    }
    if (entityLiving instanceof EntityVex) {
      return new Mob().withMobType(MobType.VEX);
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
    return null;
  }

}
