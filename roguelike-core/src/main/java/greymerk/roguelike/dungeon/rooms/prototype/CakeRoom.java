package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.fnar.minecraft.block.BlockType;
import com.github.fnar.minecraft.block.SingleBlockBrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class CakeRoom extends BaseRoom {

  private int width;
  private int length;

  public CakeRoom(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
  }

  @Override
  public BaseRoom generate(Coord origin, List<Direction> entrances) {
    int x = origin.getX();
    int y = origin.getY();
    int z = origin.getZ();
    width = random().nextInt(2) + 2;
    length = random().nextInt(2) + 3;
    int height = 3;

    RectSolid.newRect(
        new Coord(x - width, y, z - length),
        new Coord(x + width, y + height, z + length))
        .fill(worldEditor, SingleBlockBrush.AIR);

    RectHollow.newRect(
        origin.copy().west(width + 1).north(length + 1).down(),
        origin.copy().east(width + 1).south(length + 1).up(height + 1)
    ).fill(worldEditor, floors(), false, true);

    RectSolid.newRect(new Coord(x - width, y, z - length), new Coord(x - width, y + height, z - length)).fill(worldEditor, pillars());
    RectSolid.newRect(new Coord(x - width, y, z + length), new Coord(x - width, y + height, z + length)).fill(worldEditor, pillars());
    RectSolid.newRect(new Coord(x + width, y, z - length), new Coord(x + width, y + height, z - length)).fill(worldEditor, pillars());
    RectSolid.newRect(new Coord(x + width, y, z + length), new Coord(x + width, y + height, z + length)).fill(worldEditor, pillars());

    lights().stroke(worldEditor, new Coord(x - width + 1, y + height + 1, z - length + 1));
    lights().stroke(worldEditor, new Coord(x - width + 1, y + height + 1, z + length - 1));
    lights().stroke(worldEditor, new Coord(x + width - 1, y + height + 1, z - length + 1));
    lights().stroke(worldEditor, new Coord(x + width - 1, y + height + 1, z + length - 1));

    placeCake(origin, pillars());

    generateChest(generateChestLocation(origin), getEntrance(entrances).reverse(), ChestType.FOOD);

    generateDoorways(origin, entrances);
    return this;
  }

  @Override
  protected Coord generateChestLocation(Coord origin) {
    Direction dir0 = Direction.randomCardinal(random());
    Direction dir1 = dir0.orthogonals()[random().nextBoolean() ? 0 : 1];
    return origin.copy()
        .translate(dir0, width)
        .translate(dir1, length);
  }

  public void placeCake(Coord origin, BlockBrush pillar) {
    Coord cakeStand = origin.copy();
    pillar.stroke(worldEditor, cakeStand);
    BlockType.CAKE.getBrush().stroke(worldEditor, cakeStand.up());
  }

  public int getSize() {
    return 6;
  }

}
