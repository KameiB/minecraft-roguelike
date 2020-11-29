package greymerk.roguelike.command.routes;

import net.minecraft.item.ItemStack;

import java.util.List;

import greymerk.roguelike.command.CommandContext;
import greymerk.roguelike.command.CommandRouteBase;
import greymerk.roguelike.treasure.loot.provider.ItemNovelty;
import greymerk.roguelike.util.ArgumentParser;

public class CommandRouteGive extends CommandRouteBase {

  @Override
  public void execute(CommandContext context, List<String> args) {
    ArgumentParser ap = new ArgumentParser(args);

    if (!ap.hasEntry(0)) {
      context.sendInfo("Usage: roguelike give novelty_name");
      return;
    }

    ItemStack item = ItemNovelty.getItemByName(ap.get(0));
    if (item == null) {
      context.sendFailure("No such item");
      return;
    }

    context.give(item);
    context.sendSuccess("Given " + item.getDisplayName());
  }
}
