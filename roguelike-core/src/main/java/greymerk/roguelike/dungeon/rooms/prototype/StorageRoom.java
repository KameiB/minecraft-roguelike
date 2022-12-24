package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.fnar.minecraft.block.SingleBlockBrush;
import com.github.fnar.minecraft.block.normal.StairsBlock;

import java.util.ArrayList;
import java.util.List;

import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class StorageRoom extends BaseRoom {

  public StorageRoom(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
  }

  private void pillarTop(Coord cursor) {
    StairsBlock stair = secondaryStairs();
    for (Direction dir : Direction.CARDINAL) {
      cursor.translate(dir, 1);
      stair.setUpsideDown(true).setFacing(dir).stroke(worldEditor, cursor, true, false);
      cursor.translate(dir.reverse(), 1);
    }
  }

  private void pillar(Coord base, int height) {
    Coord top = base.copy();
    top.up(height);
    RectSolid.newRect(base, top).fill(worldEditor, pillars());
  }

  @Override
  public BaseRoom generate(Coord origin, List<Direction> entrances) {
    List<Coord> chestSpaces = new ArrayList<>();

    Direction front = getEntrance(entrances);

    generateCavity(origin, front);
    generateFloor(origin, front);
    generateCeiling(origin, front);

    for (Direction dir : Direction.CARDINAL) {
      for (Direction orthogonals : dir.orthogonals()) {

        Coord cursor = origin.copy()
            .up(3)
            .translate(dir, 2)
            .translate(orthogonals, 2);
        pillarTop(cursor);
        cursor.translate(dir, 3).translate(orthogonals, 3);
        pillarTop(cursor);
        Coord start = cursor.copy();

        cursor.down().translate(dir, 1);
        pillarTop(cursor);

        Coord end = cursor.copy().down(3).translate(dir, 1).translate(orthogonals, 1);
        RectSolid.newRect(start, end).fill(worldEditor, walls());

        cursor = origin.copy().translate(dir, 2).translate(orthogonals, 2);
        pillar(cursor, 4);

        cursor.translate(dir, 4);
        pillar(cursor, 3);

        cursor.up(2);
        pillarTop(cursor);

        cursor.up(1);
        cursor.translate(dir.reverse(), 1);
        pillarTop(cursor);

        cursor.translate(dir.reverse(), 3);
        pillarTop(cursor);

        generateWall(origin, dir, orthogonals);

        cursor = origin.copy();
        cursor.translate(dir, 6);
        cursor.translate(orthogonals, 3);
        StairsBlock stair = secondaryStairs();
        stair.setUpsideDown(true).setFacing(dir.reverse());
        stair.stroke(worldEditor, cursor);
        cursor.translate(orthogonals, 1);
        stair.stroke(worldEditor, cursor);
        cursor.up(1);
        chestSpaces.add(cursor.copy());
        cursor.translate(orthogonals.reverse(), 1);
        chestSpaces.add(cursor.copy());

        start = origin.copy();
        start.down();
        start.translate(dir, 3);
        start.translate(orthogonals, 3);
        end = start.copy();
        end.translate(dir, 3);
        end.translate(orthogonals, 1);
        RectSolid.newRect(start, end).fill(worldEditor, secondaryFloors());

        cursor = origin.copy();
        cursor.translate(dir, 5);
        cursor.translate(orthogonals, 5);
        pillar(cursor, 4);

      }
    }

    Coord.randomFrom(chestSpaces, 2, random())
        .forEach(coord -> generateChest(coord, coord.dirTo(origin).reverse(), ChestType.SUPPLIES_TREASURES));

    generateDoorways(origin, entrances, getSize() - 3);

    return this;
  }

  private void generateWall(Coord origin, Direction dir, Direction orthogonals) {
    Coord start = origin.copy().translate(dir, 6).up(3);
    Coord end = start.copy().translate(orthogonals, 5);
    RectSolid.newRect(start, end).fill(worldEditor, walls());

    start.translate(dir, 1);
    end.translate(dir, 1).down(3);
    RectSolid.newRect(start, end).fill(worldEditor, walls(), false, true);
  }

  private void generateCavity(Coord origin, Direction front) {
    int size = getSize() - 4; // 6
    RectSolid roomRect = RectSolid.newRect(
        origin.copy().translate(front, size).translate(front.left(), size).down(),
        origin.copy().translate(front.reverse(), size).translate(front.right(), size).up(getCeilingHeight())
    );
    SingleBlockBrush.AIR.fill(worldEditor, roomRect);
  }

  private void generateFloor(Coord origin, Direction front) {
    RectSolid floorRect = RectSolid.newRect(
        origin.copy().translate(front, getSize()).translate(front.left(), getSize()).down(),
        origin.copy().translate(front.reverse(), getSize()).translate(front.right(), getSize()).down()
    );
    floors().fill(worldEditor, floorRect);
  }

  private void generateCeiling(Coord origin, Direction front) {
    BlockBrush wall = walls();
    RectSolid ceilingRect = RectSolid.newRect(
        origin.copy().translate(front, getSize()).translate(front.left(), getSize()).up(getCeilingHeight()),
        origin.copy().translate(front.reverse(), getSize()).translate(front.right(), getSize()).up(getCeilingHeight())
    );
    wall.fill(worldEditor, ceilingRect);
  }

  private int getCeilingHeight() {
    return 4;
  }

  @Override
  public int getSize() {
    return 10;
  }
}
