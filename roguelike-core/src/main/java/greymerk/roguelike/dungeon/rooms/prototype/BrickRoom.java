package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.srwaggon.minecraft.block.SingleBlockBrush;
import com.github.srwaggon.minecraft.block.normal.StairsBlock;

import java.util.ArrayList;
import java.util.List;

import greymerk.roguelike.dungeon.Dungeon;
import greymerk.roguelike.dungeon.base.DungeonBase;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.theme.ThemeBase;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;

import static greymerk.roguelike.worldgen.Direction.CARDINAL;
import static greymerk.roguelike.worldgen.Direction.UP;

public class BrickRoom extends DungeonBase {

  public BrickRoom(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
  }

  public DungeonBase generate(Coord origin, List<Direction> entrances) {

    int x = origin.getX();
    int y = origin.getY();
    int z = origin.getZ();

    ThemeBase theme = levelSettings.getTheme();

    StairsBlock stair = theme.getPrimary().getStair();
    BlockBrush blocks = theme.getPrimary().getWall();
    BlockBrush pillar = theme.getPrimary().getPillar();

    // fill air inside
    RectSolid.newRect(new Coord(x - 3, y, z - 3), new Coord(x + 3, y + 3, z + 3)).fill(worldEditor, SingleBlockBrush.AIR);
    RectSolid.newRect(new Coord(x - 1, y + 4, z - 1), new Coord(x + 1, y + 4, z + 1)).fill(worldEditor, SingleBlockBrush.AIR);

    // shell
    RectHollow.newRect(new Coord(x - 4, y - 1, z - 4), new Coord(x + 4, y + 4, z + 4)).fill(worldEditor, blocks, false, true);

    RectSolid.newRect(new Coord(x - 4, y - 1, z - 4), new Coord(x + 4, y - 1, z + 4)).fill(worldEditor, theme.getPrimary().getFloor(), false, true);

    Coord start;
    Coord end;
    Coord cursor;


    cursor = new Coord(x, y, z);
    cursor.translate(UP, 5);
    SingleBlockBrush.AIR.stroke(worldEditor, cursor);
    cursor.translate(UP, 1);
    blocks.stroke(worldEditor, cursor);

    // Chests
    List<Coord> potentialChestLocations = new ArrayList<>();

    for (Direction dir : CARDINAL) {

      // top
      cursor = new Coord(x, y, z);
      cursor.translate(dir, 1);
      cursor.translate(UP, 5);
      stair.setUpsideDown(true).setFacing(dir.reverse());
      stair.stroke(worldEditor, cursor, false, true);
      cursor.translate(dir.antiClockwise(), 1);
      blocks.stroke(worldEditor, cursor, false, true);

      cursor = new Coord(x, y, z);
      cursor.translate(dir, 2);
      cursor.translate(UP, 4);
      SingleBlockBrush.AIR.stroke(worldEditor, cursor);
      cursor.translate(UP, 1);
      blocks.stroke(worldEditor, cursor, false, true);

      // pillar
      cursor = new Coord(x, y, z);
      cursor.translate(dir, 3);
      cursor.translate(dir.antiClockwise(), 3);
      start = cursor.copy();
      cursor.translate(UP, 2);
      end = cursor.copy();
      RectSolid.newRect(start, end).fill(worldEditor, pillar);
      cursor.translate(UP, 1);
      blocks.stroke(worldEditor, cursor);

      // pillar stairs
      for (Direction orthogonals : dir.orthogonals()) {
        cursor = new Coord(x, y, z);
        cursor.translate(dir, 3);
        cursor.translate(orthogonals, 2);
        cursor.translate(UP, 3);
        stair.setUpsideDown(true).setFacing(orthogonals.reverse());
        stair.stroke(worldEditor, cursor);
      }

      // layer above pillars
      cursor = new Coord(x, y, z);
      cursor.translate(dir, 2);
      cursor.translate(dir.antiClockwise(), 2);
      cursor.translate(UP, 4);
      blocks.stroke(worldEditor, cursor, false, true);

      for (Direction orthogonals : dir.orthogonals()) {
        cursor = new Coord(x, y, z);
        cursor.translate(UP, 4);
        cursor.translate(dir, 2);
        cursor.translate(orthogonals, 1);
        stair.setUpsideDown(true).setFacing(orthogonals.reverse());
        stair.stroke(worldEditor, cursor, false, true);
      }

      cursor = new Coord(x, y, z);
      cursor.translate(dir, 1);
      cursor.translate(dir.antiClockwise(), 1);
      cursor.translate(UP, 5);
      blocks.stroke(worldEditor, cursor, false, true);

      for (Direction orthogonals : dir.orthogonals()) {
        cursor = origin.copy();
        cursor.translate(dir, 3);
        cursor.translate(orthogonals, 2);
        potentialChestLocations.add(cursor);
      }
    }

    List<Coord> chestLocations = chooseRandomLocations(1, potentialChestLocations);
    int level = Dungeon.getLevel(origin.getY());
    ChestType chestType = getRoomSetting().getChestType()
        .orElse(ChestType.chooseRandomAmong(worldEditor.getRandom(cursor), ChestType.COMMON_TREASURES));
    worldEditor.getTreasureChestEditor().createChests(chestLocations, false, level, chestType);

    generateSpawner(origin);
    return this;
  }

  public int getSize() {
    return 6;
  }
}
