package greymerk.roguelike.dungeon.rooms.prototype;

import com.github.srwaggon.roguelike.worldgen.SingleBlockBrush;
import com.github.srwaggon.roguelike.worldgen.block.BlockType;
import com.github.srwaggon.roguelike.worldgen.block.redstone.LeverBlock;
import com.github.srwaggon.roguelike.worldgen.block.decorative.TorchBlock;
import com.github.srwaggon.roguelike.worldgen.block.redstone.ComparatorBlock;
import com.github.srwaggon.roguelike.worldgen.block.redstone.RepeaterBlock;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;

import greymerk.roguelike.dungeon.base.DungeonBase;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.treasure.loot.Firework;
import greymerk.roguelike.treasure.loot.Loot;
import greymerk.roguelike.util.DyeColor;
import greymerk.roguelike.util.TextFormat;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;

import static com.github.srwaggon.roguelike.worldgen.block.normal.ColoredBlock.stainedHardenedClay;

public class DungeonFirework extends DungeonBase {

  public DungeonFirework(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    super(roomSetting, levelSettings, worldEditor);
  }

  @Override
  public DungeonBase generate(Coord origin, List<Direction> entrances) {

    int x = origin.getX();
    int y = origin.getY();
    int z = origin.getZ();
    BlockBrush breadboard = stainedHardenedClay().setColor(DyeColor.GREEN);

    Coord cursor;
    Coord start;
    Coord end;

    Direction dir = entrances.get(0);
    start = new Coord(x, y, z);
    end = start.copy();
    start.translate(dir.reverse(), 9);
    end.translate(dir, 9);
    start.translate(dir.antiClockwise(), 4);
    end.translate(dir.clockwise(), 4);
    start.translate(Direction.DOWN);
    end.translate(Direction.UP, 3);
    RectHollow.newRect(start, end).fill(worldEditor, stainedHardenedClay().setColor(DyeColor.ORANGE), false, true);

    start = new Coord(x, y, z);
    start.translate(dir.antiClockwise(), 2);
    end = start.copy();
    start.translate(dir.reverse(), 3);
    end.translate(dir, 7);
    end.translate(Direction.UP);
    RectSolid.newRect(start, end).fill(worldEditor, breadboard);

    start.translate(dir.clockwise(), 2);
    end.translate(dir.clockwise(), 2);
    RectSolid.newRect(start, end).fill(worldEditor, breadboard);

    start.translate(dir.clockwise(), 2);
    end.translate(dir.clockwise(), 2);
    RectSolid.newRect(start, end).fill(worldEditor, breadboard);

    cursor = new Coord(x, y, z);
    cursor.translate(dir.antiClockwise(), 2);

    launcher(worldEditor, dir, cursor);
    cursor.translate(dir.clockwise(), 2);
    launcher(worldEditor, dir, cursor);
    cursor.translate(dir.clockwise(), 2);
    launcher(worldEditor, dir, cursor);
    cursor.translate(dir, 6);
    launcher(worldEditor, dir, cursor);
    cursor.translate(dir.antiClockwise(), 2);
    launcher(worldEditor, dir, cursor);
    cursor.translate(dir.antiClockwise(), 2);
    launcher(worldEditor, dir, cursor);

    start = new Coord(x, y, z);
    start.translate(dir, 4);
    end = start.copy();
    start.translate(dir.antiClockwise(), 2);
    end.translate(dir.clockwise(), 2);
    end.translate(dir, 2);
    RectSolid.newRect(start, end).fill(worldEditor, SingleBlockBrush.AIR);

    cursor = new Coord(x, y, z);
    cursor.translate(dir, 2);
    RepeaterBlock.repeater().setFacing(dir).stroke(worldEditor, cursor);
    cursor.translate(dir.antiClockwise(), 2);
    RepeaterBlock.repeater().setFacing(dir).stroke(worldEditor, cursor);
    cursor.translate(dir.clockwise(), 4);
    RepeaterBlock.repeater().setFacing(dir).stroke(worldEditor, cursor);

    cursor = new Coord(x, y, z);
    cursor.translate(dir.reverse(), 3);
    cursor.translate(dir.antiClockwise());
    RepeaterBlock.repeater().setFacing(dir.antiClockwise()).stroke(worldEditor, cursor);
    cursor.translate(dir.clockwise(), 2);
    RepeaterBlock.repeater().setFacing(dir.clockwise()).stroke(worldEditor, cursor);

    BlockBrush wire = BlockType.REDSTONE_WIRE.getBrush();

    start = new Coord(x, y, z);
    start.translate(Direction.DOWN, 2);
    start.translate(dir.clockwise());
    start.translate(dir.reverse(), 2);
    end = start.copy();
    end.translate(dir.antiClockwise(), 5);
    end.translate(dir.reverse(), 5);
    end.translate(Direction.DOWN, 2);
    RectSolid.newRect(start, end).fill(worldEditor, BlockType.COBBLESTONE.getBrush());

    cursor = new Coord(x, y, z);
    cursor.translate(dir.reverse(), 3);
    cursor.translate(Direction.DOWN);
    TorchBlock.redstone().setFacing(Direction.UP).stroke(worldEditor, cursor);
    cursor.translate(Direction.DOWN);
    breadboard.stroke(worldEditor, cursor);
    cursor.translate(dir.antiClockwise());
    TorchBlock.redstone().setFacing(dir.antiClockwise()).stroke(worldEditor, cursor);
    cursor.translate(dir.antiClockwise());
    wire.stroke(worldEditor, cursor);
    cursor.translate(dir.reverse());
    wire.stroke(worldEditor, cursor);
    cursor.translate(dir.reverse());
    wire.stroke(worldEditor, cursor);
    cursor.translate(dir.clockwise());
    wire.stroke(worldEditor, cursor);
    cursor.translate(dir.clockwise());
    wire.stroke(worldEditor, cursor);
    cursor.translate(dir);
    RepeaterBlock.repeater()
        .setDelay(RepeaterBlock.Delay.FOUR)
        .setPowered(true)
        .setFacing(dir)
        .stroke(worldEditor, cursor);
    cursor.translate(Direction.UP);
    cursor.translate(dir.reverse());
    stainedHardenedClay().setColor(DyeColor.RED).stroke(worldEditor, cursor);
    cursor.translate(Direction.UP);
    LeverBlock.lever().setActive(true).setFacing(Direction.UP).stroke(worldEditor, cursor);

    BlockBrush glowstone = BlockType.GLOWSTONE.getBrush();
    cursor = new Coord(x, y, z);
    cursor.translate(dir.reverse(), 5);
    cursor.translate(Direction.UP, 3);
    glowstone.stroke(worldEditor, cursor);
    cursor.translate(dir, 4);
    glowstone.stroke(worldEditor, cursor);
    cursor.translate(dir, 6);
    glowstone.stroke(worldEditor, cursor);

    return this;
  }


  private void launcher(WorldEditor editor, Direction dir, Coord pos) {
    Coord cursor = pos.copy();
    BlockType.REDSTONE_WIRE.getBrush().stroke(editor, cursor);
    cursor.translate(dir.reverse());
    BlockType.REDSTONE_WIRE.getBrush().stroke(editor, cursor);
    cursor.translate(dir.reverse());
    RepeaterBlock.repeater().setFacing(dir).stroke(editor, cursor);
    cursor.translate(dir.reverse());
    cursor.translate(Direction.UP);

    BlockType.DROPPER.getBrush().setFacing(Direction.UP).stroke(editor, cursor);
    for (int i = 0; i < 8; ++i) {
      ItemStack stick = new ItemStack(Items.STICK, 1);
      Loot.setItemName(stick, Integer.toString(i));
      Loot.setItemLore(stick, "Random logic unit", TextFormat.DARKGRAY);
      editor.setItem(cursor, i, stick);
    }
    editor.setItem(cursor, 8, new ItemStack(Items.WOODEN_HOE));

    cursor.translate(Direction.UP);
    BlockType.HOPPER.getBrush().setFacing(Direction.DOWN).stroke(editor, cursor);
    cursor.translate(dir);
    ComparatorBlock.comparator()
        .setFacing(dir)
        .stroke(editor, cursor);
    cursor.translate(dir);
    BlockType.REDSTONE_WIRE.getBrush().stroke(editor, cursor);
    cursor.translate(dir);
    BlockType.REDSTONE_WIRE.getBrush().stroke(editor, cursor);
    cursor.translate(dir);

    Coord top = new Coord(pos.getX(), 80, pos.getZ());
    while (top.getY() > pos.getY()) {
      top.translate(Direction.DOWN);
      if (editor.isSolidBlock(top)) {
        break;
      }
    }

    if (top.getY() >= 100) {
      return;
    }

    Coord start = cursor.copy();
    start.translate(Direction.UP);


    start.translate(dir);
    Coord end = start.copy();

    BlockBrush breadboard = stainedHardenedClay().setColor(DyeColor.GREEN);


    boolean torch = false;
    while (end.getY() < top.getY()) {
      if (torch) {
        TorchBlock.redstone().setFacing(Direction.UP).stroke(editor, cursor);
      } else {
        breadboard.stroke(editor, cursor);
      }
      torch = !torch;
      cursor.translate(Direction.UP);
      end.translate(Direction.UP);
    }

    if (torch) {
      cursor.translate(Direction.DOWN);
    }

    BlockType.DISPENSER.getBrush().setFacing(Direction.UP).stroke(editor, cursor);
    for (int i = 0; i < 9; i++) {
      editor.setItem(cursor, i, Firework.get(editor.getRandom(), 16 + editor.getRandom().nextInt(16)));
    }

    cursor.translate(Direction.UP);
    BlockBrush cob = BlockType.COBBLESTONE.getBrush();
    RectSolid.newRect(start, end).fill(editor, cob);
    start.translate(dir.reverse(), 2);
    end.translate(dir.reverse(), 2);
    RectSolid.newRect(start, end).fill(editor, cob);
    start.translate(dir);
    end.translate(dir);
    Coord above = end.copy();
    above.translate(Direction.UP, 10);
    for (Coord c : new RectSolid(cursor, above)) {
      if (editor.isSolidBlock(c)) {
        SingleBlockBrush.AIR.stroke(editor, c);
      }
    }
    start.translate(dir.antiClockwise());
    end.translate(dir.antiClockwise());
    RectSolid.newRect(start, end).fill(editor, cob);
    start.translate(dir.clockwise(), 2);
    end.translate(dir.clockwise(), 2);
    RectSolid.newRect(start, end).fill(editor, cob);
  }


  @Override
  public int getSize() {
    return 10;
  }

  @Override
  public boolean validLocation(WorldEditor editor, Direction dir, Coord pos) {
    Coord start;
    Coord end;

    start = pos.copy();
    end = start.copy();
    start.translate(dir.reverse(), 9);
    end.translate(dir, 9);
    Direction[] orthogonal = dir.orthogonals();
    start.translate(orthogonal[0], 5);
    end.translate(orthogonal[1], 5);
    start.translate(Direction.DOWN);
    end.translate(Direction.UP, 3);

    for (Coord c : new RectHollow(start, end)) {
      if (editor.isAirBlock(c)) {
        return false;
      }
    }

    return true;
  }

}
