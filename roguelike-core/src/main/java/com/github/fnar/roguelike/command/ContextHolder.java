package com.github.fnar.roguelike.command;

import java.util.Optional;

import greymerk.roguelike.dungeon.settings.SettingIdentifier;
import greymerk.roguelike.worldgen.Coord;

public interface ContextHolder {

  Optional<String> getArgument(int argumentIndex);

  Optional<String> getArgument(String argumentName);

  Optional<Coord> getArgumentAsCoord(int argumentIndex);

  Optional<Coord> getArgumentAsCoord(String argumentName);

  Optional<Coord> getArgumentAsXZCoord(int argumentIndex);

  CommandSender getCommandSender();

  Optional<SettingIdentifier> getArgumentAsSettingIdentifier(String argumentName);

}
