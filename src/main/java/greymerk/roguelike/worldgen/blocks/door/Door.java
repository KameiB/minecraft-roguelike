package greymerk.roguelike.worldgen.blocks.door;

import com.google.gson.JsonElement;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;

import greymerk.roguelike.worldgen.Cardinal;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.MetaBlock;
import greymerk.roguelike.worldgen.WorldEditor;

public class Door implements IDoor {

  MetaBlock block;

  public Door(MetaBlock block) {
    this.block = block;
  }

  public Door(DoorType type) {
    block = DoorType.get(type);
  }

  public Door(JsonElement e) {
    block = new MetaBlock(e);
  }

  public static void generate(WorldEditor editor, Coord pos, Cardinal dir, DoorType type) {
    MetaBlock door = DoorType.get(type);
    generate(editor, door, pos, dir, false);
  }

  public static void generate(WorldEditor editor, MetaBlock door, Coord pos, Cardinal dir, boolean open) {
    Coord cursor = new Coord(pos);
    MetaBlock doorBase = setProperties(door, false, dir, open, false);
    doorBase.set(editor, cursor);
    cursor.translate(Cardinal.UP);
    MetaBlock doorTop = setProperties(door, true, dir, open, false);
    doorTop.set(editor, cursor);
  }

  private static MetaBlock setProperties(MetaBlock doorblock, boolean top, Cardinal dir, boolean open, boolean hingeLeft) {

    IBlockState door = doorblock.getBlock().getDefaultState();
    door = door.withProperty(BlockDoor.HALF, top ? BlockDoor.EnumDoorHalf.UPPER : BlockDoor.EnumDoorHalf.LOWER);
    door = door.withProperty(BlockDoor.FACING, dir.getFacing());
    door = door.withProperty(BlockDoor.OPEN, open);
    door = door.withProperty(BlockDoor.HINGE, hingeLeft ? BlockDoor.EnumHingePosition.LEFT : BlockDoor.EnumHingePosition.RIGHT);

    return new MetaBlock(door);
  }

  @Override
  public void generate(WorldEditor editor, Coord pos, Cardinal dir) {
    Door.generate(editor, block, pos, dir, false);
  }

  @Override
  public void generate(WorldEditor editor, Coord pos, Cardinal dir, boolean open) {
    Door.generate(editor, block, pos, dir, open);
  }

}
