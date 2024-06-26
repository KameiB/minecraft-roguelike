package greymerk.roguelike.command.routes;

import com.github.fnar.roguelike.command.CommandContext;
import com.github.fnar.roguelike.command.commands.GenerateCitadelCommand;

import java.util.List;

import greymerk.roguelike.worldgen.Coord;

public class CitadelCommand1_12 extends DungeonCommand1_12 {

  @Override
  public void execute(CommandContext commandContext, List<String> args) {
    Coord coord = parseCoord(commandContext, args);
    new GenerateCitadelCommand(commandContext, coord).run();
  }

}
