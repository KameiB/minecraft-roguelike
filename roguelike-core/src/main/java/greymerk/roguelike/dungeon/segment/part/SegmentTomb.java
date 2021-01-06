package greymerk.roguelike.dungeon.segment.part;

import com.github.srwaggon.roguelike.worldgen.SingleBlockBrush;
import com.github.srwaggon.roguelike.worldgen.block.BlockType;
import com.github.srwaggon.roguelike.worldgen.block.normal.StairsBlock;

import java.util.List;
import java.util.Random;

import greymerk.roguelike.dungeon.Dungeon;
import greymerk.roguelike.dungeon.DungeonLevel;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.theme.ThemeBase;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import greymerk.roguelike.worldgen.shapes.RectSolid;
import greymerk.roguelike.worldgen.spawners.MobType;
import greymerk.roguelike.worldgen.spawners.SpawnerSettings;

public class SegmentTomb extends SegmentBase {

  private static void tomb(WorldEditor editor, Random rand, LevelSettings level, ThemeBase theme, Direction dir, Coord pos) {

    Coord cursor;
    Coord start;
    Coord end;

    Direction[] orthogonals = dir.orthogonals();
    start = pos.copy();
    start.translate(dir, 3);
    end = start.copy();
    start.translate(orthogonals[0]);
    end.translate(orthogonals[1]);
    end.translate(Direction.UP, 3);
    end.translate(dir, 3);
    List<Coord> box = new RectHollow(start, end).get();

    // make sure the box is solid wall
    for (Coord c : box) {
      if (!editor.isSolidBlock(c)) {
        return;
      }
    }

    RectHollow.newRect(start, end).fill(editor, theme.getPrimary().getWall());
    if (!(rand.nextInt(3) == 0)) {
      return;
    }
    cursor = pos.copy();
    cursor.translate(Direction.UP);
    cursor.translate(dir, 4);
    SpawnerSettings spawners = level.getSpawners().isEmpty()
        ? MobType.newSpawnerSetting(MobType.UNDEAD_MOBS)
        : level.getSpawners();
    spawners.generateSpawner(editor, cursor, level.getDifficulty(cursor));
    cursor.translate(dir);
    editor.getTreasureChestEditor().createChest(Dungeon.getLevel(cursor.getY()), cursor, false, ChestType.chooseRandomType(rand, ChestType.COMMON_TREASURES));
  }

  @Override
  protected void genWall(WorldEditor editor, Random rand, DungeonLevel level, Direction dir, ThemeBase theme, Coord origin) {

    StairsBlock stair = theme.getPrimary().getStair();

    Coord cursor = origin.copy();
    Coord start;
    Coord end;

    Direction[] orthogonals = dir.orthogonals();

    cursor.translate(dir, 2);
    start = cursor.copy();
    start.translate(orthogonals[0], 1);
    end = cursor.copy();
    end.translate(orthogonals[1], 1);
    end.translate(Direction.UP, 2);
    RectSolid.newRect(start, end).fill(editor, SingleBlockBrush.AIR);

    start.translate(dir, 1);
    end.translate(dir, 1);
    RectSolid.newRect(start, end).fill(editor, theme.getSecondary().getWall(), false, true);

    cursor.translate(Direction.UP, 2);
    for (Direction d : orthogonals) {
      Coord c = cursor.copy();
      c.translate(d, 1);
      stair.setUpsideDown(true).setFacing(d.reverse());
      stair.stroke(editor, c);
    }

    tomb(editor, rand, level.getSettings(), theme, dir, origin.copy());

    cursor = origin.copy();
    cursor.translate(Direction.UP);
    cursor.translate(dir, 3);
    BlockType.QUARTZ.getBrush().stroke(editor, cursor);
  }
}
