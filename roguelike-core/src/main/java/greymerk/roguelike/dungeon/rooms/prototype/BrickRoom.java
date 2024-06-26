package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.fnar.minecraft.block.SingleBlockBrush;

import java.util.List;

import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.treasure.TreasureChest;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;

import static greymerk.roguelike.worldgen.Direction.CARDINAL;
import static greymerk.roguelike.worldgen.Direction.UP;

public class BrickRoom extends BaseRoom {

  public BrickRoom(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
    this.wallDist = 4;
  }

  public BaseRoom generate(Coord at, List<Direction> entrances) {

    int x = at.getX();
    int y = at.getY();
    int z = at.getZ();

    // fill air inside
    SingleBlockBrush.AIR.fill(worldEditor, RectSolid.newRect(new Coord(x - 3, y, z - 3), new Coord(x + 3, y + 3, z + 3)));
    SingleBlockBrush.AIR.fill(worldEditor, RectSolid.newRect(new Coord(x - 1, y + 4, z - 1), new Coord(x + 1, y + 4, z + 1)));

    // shell
    RectHollow.newRect(new Coord(x - 4, y - 1, z - 4), new Coord(x + 4, y + 4, z + 4)).fill(worldEditor, primaryWallBrush(), false, true);

    RectSolid.newRect(new Coord(x - 4, y - 1, z - 4), new Coord(x + 4, y - 1, z + 4)).fill(worldEditor, primaryFloorBrush(), false, true);


    Coord cursor = new Coord(x, y, z);
    cursor.translate(UP, 5);
    SingleBlockBrush.AIR.stroke(worldEditor, cursor);
    cursor.translate(UP, 1);
    primaryWallBrush().stroke(worldEditor, cursor);


    for (Direction dir : CARDINAL) {

      // top
      cursor = new Coord(x, y, z);
      cursor.translate(dir, 1);
      cursor.translate(UP, 5);
      primaryStairBrush().setUpsideDown(true).setFacing(dir.reverse()).stroke(worldEditor, cursor, false, true);
      cursor.translate(dir.antiClockwise(), 1);
      primaryWallBrush().stroke(worldEditor, cursor, false, true);

      cursor = new Coord(x, y, z);
      cursor.translate(dir, 2);
      cursor.translate(UP, 4);
      SingleBlockBrush.AIR.stroke(worldEditor, cursor);
      cursor.translate(UP, 1);
      primaryWallBrush().stroke(worldEditor, cursor, false, true);

      // pillar
      cursor = new Coord(x, y, z);
      cursor.translate(dir, 3);
      cursor.translate(dir.antiClockwise(), 3);
      Coord start = cursor.copy();
      cursor.translate(UP, 2);
      Coord end = cursor.copy();
      primaryPillarBrush().fill(worldEditor, RectSolid.newRect(start, end));
      cursor.translate(UP, 1);
      primaryWallBrush().stroke(worldEditor, cursor);

      // pillar stairs
      for (Direction orthogonals : dir.orthogonals()) {
        cursor = new Coord(x, y, z);
        cursor.translate(dir, 3);
        cursor.translate(orthogonals, 2);
        cursor.translate(UP, 3);
        primaryStairBrush().setUpsideDown(true).setFacing(orthogonals.reverse()).stroke(worldEditor, cursor);
      }

      // layer above pillars
      cursor = new Coord(x, y, z);
      cursor.translate(dir, 2);
      cursor.translate(dir.antiClockwise(), 2);
      cursor.translate(UP, 4);
      primaryWallBrush().stroke(worldEditor, cursor, false, true);

      for (Direction orthogonals : dir.orthogonals()) {
        cursor = new Coord(x, y, z);
        cursor.translate(UP, 4);
        cursor.translate(dir, 2);
        cursor.translate(orthogonals, 1);
        primaryStairBrush().setUpsideDown(true).setFacing(orthogonals.reverse()).stroke(worldEditor, cursor, false, true);
      }

      cursor = new Coord(x, y, z);
      cursor.translate(dir, 1);
      cursor.translate(dir.antiClockwise(), 1);
      cursor.translate(UP, 5);
      primaryWallBrush().stroke(worldEditor, cursor, false, true);
    }

    Direction entrance = getEntrance(entrances);
    Coord spawnerCoord = at.copy().translate(entrance, random().nextInt(3) - 1).translate(entrance.right(), random().nextInt(3) - 1);
    generateSpawner(spawnerCoord);
    generateChest(at, entrance.reverse());
    generateDoorways(at, entrances);

    return this;
  }

  private void generateChest(Coord origin, Direction facing) {
    Coord chestLocation = generateChestLocation(origin);
    new TreasureChest(chestLocation, worldEditor)
        .withChestType(getChestTypeOrUse(ChestType.chooseRandomAmong(random(), ChestType.COMMON_TREASURES)))
        .withFacing(facing)
        .withTrap(TreasureChest.shouldBeTrapped(random(), levelSettings.getLevel()))
        .stroke(worldEditor, chestLocation);
  }

}
