package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.fnar.minecraft.block.BlockType;
import com.github.fnar.minecraft.block.SingleBlockBrush;

import java.util.List;

import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.treasure.TreasureChest;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.BlockWeightedRandom;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;

public class NetherBrickRoom extends BaseRoom {

  public NetherBrickRoom(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
    this.wallDist = 5;
    this.ceilingHeight = 4;
  }

  @Override
  protected void generateCeiling(Coord at, List<Direction> entrances) {
    ceilingBrush().fill(worldEditor, at.newRect(getWallDist()).up(getCeilingHeight()));
  }

  private BlockWeightedRandom ceilingBrush() {
    BlockWeightedRandom ceilingBrush = new BlockWeightedRandom();
    ceilingBrush.addBlock(BlockType.FENCE_NETHER_BRICK.getBrush(), 10);
    ceilingBrush.addBlock(SingleBlockBrush.AIR, 5);
    return ceilingBrush;
  }

  @Override
  protected void generateDecorations(Coord at, List<Direction> entrances) {
    Coord coord = generateChestLocation(at);
    new TreasureChest(coord, worldEditor)
        .withChestType(getChestTypeOrUse(ChestType.chooseRandomAmong(random(), ChestType.COMMON_TREASURES)))
        .withFacing(getEntrance(entrances))
        .withTrap(TreasureChest.shouldBeTrapped(random(), levelSettings.getLevel()))
        .stroke(worldEditor, coord);
    generateSpawners(at);
  }

  private void generateSpawners(Coord at) {
    int dist = getWallDist();
    for (Direction card : Direction.cardinals()) {
      Coord spawnerCoord = at.copy().translate(card, dist).translate(card.left(), dist).up(random().nextInt(2));
      generateSpawner(spawnerCoord);
    }
  }

}
