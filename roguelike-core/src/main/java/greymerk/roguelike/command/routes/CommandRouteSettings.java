package greymerk.roguelike.command.routes;

import com.github.fnar.roguelike.command.ListSettingsCommand;
import com.github.fnar.roguelike.command.ReloadSettingsCommand;

import java.util.List;

import greymerk.roguelike.command.CommandBase;
import greymerk.roguelike.command.CommandContext1_12;
import greymerk.roguelike.command.CommandRouteBase;
import greymerk.roguelike.util.ArgumentParser;

public class CommandRouteSettings extends CommandRouteBase {

  public CommandRouteSettings(CommandBase commandBase) {
    super(commandBase);
  }

  @Override
  public void execute(CommandContext1_12 commandContext, List<String> args) {
    ArgumentParser argumentParser = new ArgumentParser(args);

    if (!argumentParser.hasEntry(0)) {
      commandContext.sendInfo("notif.roguelike.usage_", "roguelike settings [reload | list]");
      return;
    }

    if (argumentParser.match(0, "reload")) {
      new ReloadSettingsCommand(commandContext).run();
    }

    if (argumentParser.match(0, "list")) {
      String namespace = argumentParser.hasEntry(1) ? argumentParser.get(1) : "";
      new ListSettingsCommand(commandContext, namespace).run();
    }
  }

}
