package greymerk.roguelike.dungeon.segment.part;

import com.github.fnar.minecraft.block.BlockType;
import com.github.fnar.minecraft.block.SingleBlockBrush;
import com.github.fnar.minecraft.block.normal.StairsBlock;
import com.github.fnar.minecraft.material.Wood;

import java.util.Optional;

import greymerk.roguelike.dungeon.DungeonLevel;
import greymerk.roguelike.dungeon.base.BaseRoom;
import greymerk.roguelike.dungeon.base.SecretsSetting;
import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class SegmentSewerDoor extends SegmentBase {

  @Override
  protected void genWall(WorldEditor editor, DungeonLevel level, Direction dir, Theme theme, Coord origin) {

    StairsBlock stair = getSecondaryStairs(theme);
    BlockBrush bars = BlockType.IRON_BAR.getBrush();
    BlockBrush water = BlockType.WATER_FLOWING.getBrush();
    BlockBrush leaves = Wood.SPRUCE.getLeaves();
    BlockBrush glowstone = getSecondaryLightBlock(theme);

    Direction[] orthogonal = dir.orthogonals();

    Coord cursor = origin.copy();
    cursor.down();
    bars.stroke(editor, cursor);
    Coord start = cursor.copy();
    Coord end = start.copy();
    start.translate(orthogonal[0]);
    end.translate(orthogonal[1]);
    stair.setUpsideDown(true).setFacing(orthogonal[0]).stroke(editor, start);
    stair.setUpsideDown(true).setFacing(orthogonal[1]).stroke(editor, end);
    cursor = origin.copy();
    cursor.down();
    bars.stroke(editor, cursor);
    start.down();
    end.down();
    RectSolid.newRect(start, end).fill(editor, water);

    cursor = origin.copy();
    cursor.up(3);
    bars.stroke(editor, cursor);
    cursor.up();
    leaves.stroke(editor, cursor, false, true);
    cursor.translate(dir);
    water.stroke(editor, cursor, false, true);
    cursor.translate(dir);
    glowstone.stroke(editor, cursor, false, true);

    cursor = origin.copy();
    cursor.translate(dir, 2);
    start = cursor.copy();
    start.translate(orthogonal[0], 1);
    end = cursor.copy();
    end.translate(orthogonal[1], 1);
    end.up(2);
    SingleBlockBrush.AIR.fill(editor, RectSolid.newRect(start, end));

    SecretsSetting secrets = level.getSettings().getSecrets();
    Optional<BaseRoom> room = generateSecret(secrets, editor, level.getSettings(), dir, origin.copy());

    start.translate(dir, 1);
    end.translate(dir, 1);
    RectSolid.newRect(start, end).fill(editor, getSecondaryWall(theme), false, true);

    cursor.up(2);
    for (Direction d : orthogonal) {
      Coord c = cursor.copy();
      c.translate(d, 1);
      stair.setUpsideDown(true).setFacing(d.reverse());
      stair.stroke(editor, c);
    }

    if (room.isPresent()) {
      cursor = origin.copy();
      cursor.translate(dir, 3);
      getSecondaryDoor(theme).setFacing(dir.reverse()).stroke(editor, cursor);
    }
  }

}
