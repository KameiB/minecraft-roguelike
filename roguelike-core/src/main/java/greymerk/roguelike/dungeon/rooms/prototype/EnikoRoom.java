package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.fnar.minecraft.block.SingleBlockBrush;
import com.github.fnar.minecraft.block.normal.StairsBlock;

import java.util.List;

import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.treasure.TreasureChest;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;

import static com.github.fnar.minecraft.block.spawner.MobType.COMMON_MOBS;


public class EnikoRoom extends BaseRoom {

  public EnikoRoom(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
    this.wallDist = 6;
  }

  private static void pillar(WorldEditor editor, Theme theme, Coord origin) {
    StairsBlock stair = theme.getPrimary().getStair();
    BlockBrush pillar = theme.getPrimary().getPillar();
    Coord start;
    Coord end;
    Coord cursor;

    start = origin.copy();
    end = start.copy();
    end.up(3);
    RectSolid.newRect(start, end).fill(editor, pillar);
    for (Direction dir : Direction.CARDINAL) {
      cursor = end.copy();
      cursor.translate(dir);
      stair.setUpsideDown(true).setFacing(dir).stroke(editor, cursor, true, false);
    }
  }

  @Override
  public BaseRoom generate(Coord at, List<Direction> entrances) {
    Coord start = at.copy();
    Coord end = at.copy();
    start.translate(new Coord(6, -1, 6));
    end.translate(new Coord(-6, 4, -6));
    RectHollow.newRect(start, end).fill(worldEditor, primaryWallBrush(), false, true);

    start = at.copy();
    end = at.copy();
    start.translate(new Coord(6, 4, 6));
    end.translate(new Coord(-6, 5, -6));
    RectSolid.newRect(start, end).fill(worldEditor, secondaryWallBrush(), false, true);

    start = at.copy();
    end = at.copy();
    start.translate(new Coord(3, 4, 3));
    end.translate(new Coord(-3, 4, -3));
    SingleBlockBrush.AIR.fill(worldEditor, RectSolid.newRect(start, end));

    start = at.copy();
    end = at.copy();
    start.translate(new Coord(-3, -1, -3));
    end.translate(new Coord(3, -1, 3));
    primaryFloorBrush().fill(worldEditor, RectSolid.newRect(start, end));

    for (Direction dir : Direction.CARDINAL) {
      Coord cursor = at.copy();
      cursor.translate(dir, 5);
      for (Direction o : dir.orthogonals()) {
        Coord c = cursor.copy();
        c.translate(o, 2);
        pillar(worldEditor, theme(), c);

        c = cursor.copy();
        c.translate(o, 3);
        primaryStairBrush().setUpsideDown(true).setFacing(dir.reverse()).stroke(worldEditor, c);
        c.translate(o);
        primaryStairBrush().setUpsideDown(true).setFacing(dir.reverse()).stroke(worldEditor, c);
      }

      cursor.translate(dir.antiClockwise(), 5);
      pillar(worldEditor, theme(), cursor);

      if (entrances.contains(dir)) {
        start = at.copy();
        start.down();
        end = start.copy();
        start.translate(dir.antiClockwise());
        end.translate(dir.clockwise());
        end.translate(dir, 6);
        primaryFloorBrush().fill(worldEditor, RectSolid.newRect(start, end));
      }
    }

    generateSpawner(at, COMMON_MOBS);
    Coord coord = generateChestLocation(at.copy().up());
    new TreasureChest(coord, worldEditor)
        .withChestType(getChestTypeOrUse(ChestType.chooseRandomAmong(random(), ChestType.COMMON_TREASURES)))
        .withFacing(getEntrance(entrances))
        .withTrap(false)
        .stroke(worldEditor, coord);

    return this;
  }

  @Override
  protected Coord generateChestLocation(Coord origin) {
    Direction dir0 = Direction.randomCardinal(random());
    Direction dir1 = dir0.orthogonals()[random().nextBoolean() ? 0 : 1];
    return origin.copy()
        .translate(dir0, 5)
        .translate(dir1, 3 + (random().nextBoolean() ? 1 : 0));
  }

}
