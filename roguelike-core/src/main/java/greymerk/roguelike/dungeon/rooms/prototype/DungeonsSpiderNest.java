package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.fnar.minecraft.block.BlockType;
import com.github.fnar.minecraft.block.SingleBlockBrush;
import com.github.fnar.minecraft.block.spawner.MobType;

import java.util.List;

import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.BlockWeightedRandom;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class DungeonsSpiderNest extends BaseRoom {
  int originX;
  int originY;
  int originZ;
  byte dungeonHeight;
  int dungeonLength;
  int dungeonWidth;

  public DungeonsSpiderNest(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
    dungeonHeight = 2;
    dungeonLength = 3;
    dungeonWidth = 3;
  }

  public BaseRoom generate(Coord origin, List<Direction> entrances) {

    originX = origin.getX();
    originY = origin.getY();
    originZ = origin.getZ();

    BlockWeightedRandom webs = new BlockWeightedRandom();
    webs.addBlock(BlockType.WEB.getBrush(), 3);
    webs.addBlock(SingleBlockBrush.AIR, 1);

    for (int blockX = originX - dungeonLength - 1; blockX <= originX + dungeonLength + 1; blockX++) {
      for (int blockZ = originZ - dungeonWidth - 1; blockZ <= originZ + dungeonWidth + 1; blockZ++) {
        for (int blockY = originY + dungeonHeight; blockY >= originY - dungeonHeight; blockY--) {

          int x = Math.abs(blockX - originX);
          int z = Math.abs(blockZ - originZ);

          int clearHeight = Math.max(x, z);

          if (blockY == originY) {
            webs.stroke(worldEditor, new Coord(blockX, blockY, blockZ));
          }
          if (clearHeight < 1) {
            clearHeight = 1;
          }
          if (Math.abs(blockY - originY) > clearHeight) {
            continue;
          }

          if (worldEditor.getRandom().nextInt(clearHeight) == 0) {
            webs.stroke(worldEditor, new Coord(blockX, blockY, blockZ));
          } else if (worldEditor.getRandom().nextInt(5) == 0) {
            BlockType.GRAVEL.getBrush().stroke(worldEditor, new Coord(blockX, blockY, blockZ));
          }
        }
      }
    }

    final Coord cursor = new Coord(originX, originY, originZ);
    generateSpawner(cursor, MobType.CAVESPIDER);

    List<Coord> spaces = new RectSolid(
        new Coord(originX - dungeonLength, originY - 1, originZ - dungeonWidth),
        new Coord(originX + dungeonLength, originY + 1, originZ + dungeonWidth)
    ).get();
    List<Coord> chestLocations = chooseRandomLocations(1 + worldEditor.getRandom().nextInt(3), spaces);
    generateChests(chestLocations, entrances.get(0), ChestType.UNCOMMON_TREASURES);
    return this;
  }

  public int getSize() {
    return 4;
  }
}
