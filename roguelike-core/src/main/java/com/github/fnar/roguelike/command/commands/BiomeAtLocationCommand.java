package com.github.fnar.roguelike.command.commands;

import com.github.fnar.roguelike.command.CommandContext;

public class BiomeAtLocationCommand extends BiomeAtPlayerCommand {

  public BiomeAtLocationCommand(CommandContext commandContext) {
    super(commandContext);
  }

  @Override
  public void onRun() {
    sendBiomeDetails(commandContext, commandContext.getArgumentAsCoord("position"));
  }

}