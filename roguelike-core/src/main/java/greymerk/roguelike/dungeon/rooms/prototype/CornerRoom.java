package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.fnar.minecraft.block.SingleBlockBrush;

import java.util.List;

import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class CornerRoom extends BaseRoom {

  public CornerRoom(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
    this.wallDist = 2;
  }

  @Override
  public BaseRoom generate(Coord at, List<Direction> entrances) {
    createHollowCenter(at);
    createShell(at);
    fillFloor(at);
    createCornerWalls(at);
    createCeiling(at);
    generateDoorways(at, entrances);
    return this;
  }

  private void createCornerWalls(Coord origin) {
    for (Direction dir : Direction.CARDINAL) {
      Coord cursor = origin.copy()
          .translate(dir, 2)
          .translate(dir.antiClockwise(), 2);

      Coord pillarStart = cursor.copy();
      Coord pillarEnd = cursor.copy().up(2);
      primaryPillarBrush().fill(worldEditor, RectSolid.newRect(pillarStart, pillarEnd));

      Coord pillarTop = cursor.copy().up();
      primaryWallBrush().stroke(worldEditor, pillarTop);
    }
  }

  private void createCeiling(Coord origin) {
    SingleBlockBrush.AIR.stroke(worldEditor, origin.copy().up(4));

    primaryWallBrush().stroke(worldEditor, origin.copy().up(5));

    for (Direction dir : Direction.CARDINAL) {
      Coord ceiling = origin.copy()
          .translate(dir, 1)
          .up(4);

      primaryStairBrush()
          .setUpsideDown(true)
          .setFacing(dir.reverse())
          .stroke(worldEditor, ceiling);

      for (Direction orthogonal : dir.orthogonals()) {
        Coord decorativeCeiling = origin.copy()
            .translate(dir, 2)
            .translate(orthogonal, 1)
            .up(3);
        primaryStairBrush()
            .setUpsideDown(true)
            .setFacing(orthogonal.reverse())
            .stroke(worldEditor, decorativeCeiling);
      }
    }
  }

  private void createHollowCenter(Coord origin) {
    Coord hollowAirCorner0 = origin.add(-2, 0, -2);
    Coord hollowAirCorner1 = origin.add(2, 3, 2);
    SingleBlockBrush.AIR.fill(worldEditor, RectSolid.newRect(hollowAirCorner0, hollowAirCorner1));
  }

  private void createShell(Coord origin) {
    Coord roomShellCorner0 = origin.add(-3, -1, -3);
    Coord roomShellCorner1 = origin.add(3, 4, 3);
    RectHollow.newRect(roomShellCorner0, roomShellCorner1).fill(worldEditor, primaryWallBrush(), false, true);
  }

  private void fillFloor(Coord origin) {
    Coord floorCorner0 = origin.add(-3, -1, -3);
    Coord floorCorner1 = origin.add(3, -1, 3);
    RectSolid.newRect(floorCorner0, floorCorner1).fill(worldEditor, primaryFloorBrush(), false, true);
  }

}
