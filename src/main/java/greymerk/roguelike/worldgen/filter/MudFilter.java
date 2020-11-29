package greymerk.roguelike.worldgen.filter;

import java.util.Random;

import greymerk.roguelike.theme.ThemeBase;
import greymerk.roguelike.worldgen.BlockJumble;
import greymerk.roguelike.worldgen.Cardinal;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.IBounded;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.blocks.BlockType;
import greymerk.roguelike.worldgen.blocks.FlowerPot;
import greymerk.roguelike.worldgen.shapes.Shape;

public class MudFilter implements IFilter {

  @Override
  public void apply(WorldEditor editor, Random rand, ThemeBase theme, IBounded box) {
    for (Coord pos : box.getShape(Shape.RECTSOLID)) {
      if (rand.nextInt(40) != 0) {
        continue;
      }
      if (!validLocation(editor, rand, pos)) {
        continue;
      }
      generate(editor, rand, pos, rand.nextInt(3) + 2);
    }
  }

  private void generate(WorldEditor editor, Random rand, Coord pos, int counter) {
    if (counter <= 0) {
      return;
    }

    for (Cardinal dir : Cardinal.DIRECTIONS) {
      if (rand.nextBoolean()) {
        continue;
      }
      Coord next = new Coord(pos);
      next.translate(dir);
      generate(editor, rand, next, counter - 1);
    }

    if (!validLocation(editor, rand, pos)) {
      return;
    }

    BlockJumble wet = new BlockJumble();
    wet.addBlock(BlockType.get(BlockType.CLAY));
    wet.addBlock(BlockType.get(BlockType.SOUL_SAND));
    wet.addBlock(BlockType.get(BlockType.MYCELIUM));

    BlockJumble dry = new BlockJumble();
    dry.addBlock(BlockType.get(BlockType.DIRT_PODZOL));
    dry.addBlock(BlockType.get(BlockType.DIRT));
    dry.addBlock(BlockType.get(BlockType.DIRT_COARSE));

    switch (counter) {
      case 5:
      case 4:
        BlockType.get(BlockType.DIRT).set(editor, pos);
      case 3:
        if (rand.nextBoolean()) {
          BlockType.get(BlockType.DIRT_COARSE).set(editor, pos);
          break;
        }
      case 2:
        wet.set(editor, rand, pos);
        break;
      case 1:
        if (rand.nextBoolean()) {
          wet.set(editor, rand, pos);
          break;
        }
      default:
        BlockType.get(BlockType.WATER_FLOWING).set(editor, pos);
        return;
    }


    if (rand.nextInt(6) != 0) {
      return;
    }

    BlockJumble plants = new BlockJumble();
    plants.addBlock(FlowerPot.getFlower(FlowerPot.BROWNMUSHROOM));
    plants.addBlock(FlowerPot.getFlower(FlowerPot.REDMUSHROOM));

    Coord cursor = new Coord(pos);
    cursor.translate(Cardinal.UP);
    plants.set(editor, rand, cursor);
  }

  private boolean validLocation(WorldEditor editor, Random rand, Coord pos) {

    if (!editor.getBlock(pos).isOpaqueCube()) {
      return false;
    }

    Coord cursor = new Coord(pos);
    cursor.translate(Cardinal.UP);
    if (!editor.isAirBlock(cursor)) {
      return false;
    }

    cursor.translate(Cardinal.DOWN, 2);
    if (editor.isAirBlock(cursor)) {
      return false;
    }

    cursor.translate(Cardinal.UP);

    for (Cardinal dir : Cardinal.values()) {
      cursor.translate(dir);
      if (!editor.getBlock(pos).isOpaqueCube()) {
        return false;
      }
      cursor.translate(dir.reverse());
    }

    return true;
  }
}
