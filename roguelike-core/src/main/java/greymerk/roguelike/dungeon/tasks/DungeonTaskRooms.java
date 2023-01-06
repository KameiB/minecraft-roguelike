package greymerk.roguelike.dungeon.tasks;

import com.github.fnar.util.ReportThisIssueException;

import greymerk.roguelike.dungeon.Dungeon;
import greymerk.roguelike.dungeon.DungeonLevel;
import greymerk.roguelike.dungeon.DungeonNode;
import greymerk.roguelike.dungeon.LevelLayout;
import greymerk.roguelike.dungeon.settings.DungeonSettings;
import greymerk.roguelike.worldgen.WorldEditor;

public class DungeonTaskRooms implements IDungeonTask {

  @Override
  public void execute(WorldEditor editor, Dungeon dungeon, DungeonSettings settings) {
    dungeon.getLevels()
        .forEach(this::generateLevel);
  }

  private void generateLevel(DungeonLevel level) {
    LevelLayout layout = level.getLayout();
    layout.getNodes().stream()
        .filter(node -> !layout.isStartOrEnd(node))
        .forEach(this::safelyGenerate);
  }

  private void safelyGenerate(DungeonNode dungeonNode) {
    try {
      dungeonNode.generate();
    } catch (Exception exception) {
      new ReportThisIssueException(exception).printStackTrace();
    }
  }

}
