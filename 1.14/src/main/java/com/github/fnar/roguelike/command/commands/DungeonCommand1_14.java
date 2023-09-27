package com.github.fnar.roguelike.command.commands;

import com.github.fnar.roguelike.command.ContextHolder1_14;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;

import greymerk.roguelike.worldgen.Coord;

public class DungeonCommand1_14 {

  public static final String ARG_SETTINGS_NAME = "settingsName";
  public static final String ARG_POSITION = "position";

  public static LiteralArgumentBuilder<CommandSource> dungeonCommand() {
    return Commands.literal("dungeon")
        .then(Commands.literal("here")
            .executes(DungeonCommand1_14::generateDungeon)
            .then(Commands.argument(ARG_SETTINGS_NAME, StringArgumentType.string())
                .executes(DungeonCommand1_14::generateDungeon)))
        .then(Commands.literal("nearby")
            .executes(DungeonCommand1_14::generateDungeon)
            .then(Commands.argument(ARG_SETTINGS_NAME, StringArgumentType.string())
                .executes(DungeonCommand1_14::generateDungeon)))
        .then(Commands.argument(ARG_POSITION, BlockPosArgument.blockPos())
            .executes(DungeonCommand1_14::generateDungeon)
            .then(Commands.argument(ARG_SETTINGS_NAME, StringArgumentType.string())
                .executes(DungeonCommand1_14::generateDungeon)));
  }

  private static int generateDungeon(CommandContext<CommandSource> context) {
    com.github.fnar.roguelike.command.CommandContext commandContext = new com.github.fnar.roguelike.command.CommandContext(new ContextHolder1_14(context));
    Coord coord = commandContext.getArgumentAsCoord(ARG_POSITION).orElse(null);
    String settingName = commandContext.getArgument(ARG_SETTINGS_NAME).orElse(null);
    new DungeonCommand(commandContext, coord, settingName).run();
    return 0;
  }

}
