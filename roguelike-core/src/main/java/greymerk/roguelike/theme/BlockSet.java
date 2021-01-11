package greymerk.roguelike.theme;

import com.github.srwaggon.minecraft.block.BlockType;
import com.github.srwaggon.minecraft.block.normal.StairsBlock;
import com.github.srwaggon.minecraft.block.redstone.DoorBlock;

import greymerk.roguelike.worldgen.BlockBrush;

import static java.util.Optional.ofNullable;

public class BlockSet {

  private BlockBrush floor;
  private BlockBrush walls = BlockType.STONE_BRICK.getBrush();
  private StairsBlock stair = StairsBlock.stoneBrick();
  private BlockBrush pillar;
  private DoorBlock door = DoorBlock.oak();
  private BlockBrush lightBlock = BlockType.GLOWSTONE.getBrush();
  private BlockBrush liquid = BlockType.WATER_FLOWING.getBrush();

  public BlockSet() {

  }

  public BlockSet(
      BlockBrush floor,
      BlockBrush walls,
      StairsBlock stair,
      BlockBrush pillar,
      DoorBlock door,
      BlockBrush lightBlock,
      BlockBrush liquid
  ) {
    this.floor = floor;
    this.walls = walls;
    this.stair = stair;
    this.pillar = pillar;
    this.door = door;
    this.lightBlock = lightBlock;
    this.liquid = liquid;
  }

  public BlockSet(
      BlockBrush floor,
      BlockBrush walls,
      StairsBlock stair,
      BlockBrush pillar,
      DoorBlock door
  ) {
    this(
        floor,
        walls,
        stair,
        pillar,
        door,
        BlockType.GLOWSTONE.getBrush(),
        BlockType.WATER_FLOWING.getBrush()
    );
  }

  public BlockSet(
      BlockBrush floor,
      BlockBrush walls,
      StairsBlock stair,
      BlockBrush pillar
  ) {
    this(
        floor,
        walls,
        stair,
        pillar,
        DoorBlock.oak()
    );
  }

  public BlockSet(
      BlockBrush walls,
      StairsBlock stair,
      BlockBrush pillar
  ) {
    this(
        walls,
        walls,
        stair,
        pillar
    );
  }

  static BlockSet inherit(
      BlockSet parentBlockSet,
      BlockSet childBlockSet
  ) {
    if (parentBlockSet == null && childBlockSet == null) {
      return new BlockSet();
    }
    if (parentBlockSet == null) {
      return childBlockSet;
    }
    if (childBlockSet == null) {
      return parentBlockSet;
    }
    return new BlockSet(
        ofNullable(childBlockSet.getFloor()).orElse(parentBlockSet.getFloor()),
        ofNullable(childBlockSet.getWall()).orElse(parentBlockSet.getWall()),
        ofNullable(childBlockSet.getStair()).orElse(parentBlockSet.getStair()),
        ofNullable(childBlockSet.getPillar()).orElse(parentBlockSet.getPillar()),
        ofNullable(childBlockSet.getDoor()).orElse(parentBlockSet.getDoor()),
        ofNullable(childBlockSet.getLightBlock()).orElse(parentBlockSet.getLightBlock()),
        ofNullable(childBlockSet.getLiquid()).orElse(parentBlockSet.getLiquid()));
  }

  public BlockBrush getWall() {
    return walls;
  }

  public StairsBlock getStair() {
    return stair;
  }

  public BlockBrush getPillar() {
    return ofNullable(pillar).orElse(getWall());
  }

  public BlockBrush getFloor() {
    return ofNullable(floor).orElse(getWall());
  }

  public DoorBlock getDoor() {
    return door;
  }

  public BlockBrush getLightBlock() {
    return lightBlock;
  }

  public BlockBrush getLiquid() {
    return liquid;
  }
}
