package com.github.fnar.roguelike.command.commands;

import com.github.fnar.roguelike.command.CommandContext;
import com.github.fnar.roguelike.command.exception.NoValidLocationException;

import java.util.Random;

import greymerk.roguelike.dungeon.Dungeon;
import greymerk.roguelike.dungeon.settings.DungeonSettings;
import greymerk.roguelike.dungeon.settings.SettingsContainer;
import greymerk.roguelike.dungeon.settings.SettingsRandom;
import greymerk.roguelike.dungeon.settings.SettingsResolver;
import greymerk.roguelike.dungeon.settings.TestDungeonSettings;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;

public class DungeonCommand extends BaseRoguelikeCommand {

  private final Coord coord;
  private final String settingName;

  public DungeonCommand(CommandContext commandContext, Coord coord, String settingName) {
    super(commandContext);
    this.settingName = settingName;
    this.coord = coord;
  }

  @Override
  public void onRun() throws Exception {
    WorldEditor editor = commandContext.createEditor();
    SettingsContainer settingsContainer = new SettingsContainer(commandContext.getModLoader()).loadFiles();
    SettingsResolver.instance = new SettingsResolver(settingsContainer);
    DungeonSettings dungeonSettings = chooseDungeonSettings(SettingsResolver.instance, settingName, coord, editor);
    generateDungeon(commandContext, coord, editor, dungeonSettings);
  }

  private void generateDungeon(CommandContext context, Coord coord, WorldEditor editor, DungeonSettings dungeonSettings) {
    Dungeon dungeon = new Dungeon(editor, context.getModLoader());
    dungeon.generate(dungeonSettings, coord);
    context.sendSuccess("generateddungeon", String.format("%s at %s.", dungeonSettings.getId(), coord));
  }

  public static DungeonSettings chooseDungeonSettings(SettingsResolver settingsResolver, String settingName, Coord pos, WorldEditor editor) throws NoValidLocationException {
    if (settingName == null) {
      return getAnyValid(settingsResolver, pos, editor);
    } else if (settingName.equals("test")) {
      return getTestSettings(editor, pos);
    } else if (settingName.equals("random")) {
      return getRandomSettings(editor, pos);
    } else {
      return SettingsResolver.instance.resolve(settingName);
    }
  }

  private static DungeonSettings getAnyValid(SettingsResolver settingsResolver, Coord pos, WorldEditor editor) throws NoValidLocationException {
    return settingsResolver.chooseRandom(editor, pos)
        .orElseThrow(() -> new NoValidLocationException(pos));
  }

  private static DungeonSettings getTestSettings(WorldEditor editor, Coord pos) {
    Random random = editor.getRandom();
    random.setSeed(editor.getSeed(pos));
    return new TestDungeonSettings(random);
  }

  private static DungeonSettings getRandomSettings(WorldEditor editor, Coord pos) {
    Random random = editor.getRandom();
    random.setSeed(editor.getSeed(pos));
    return new SettingsRandom(random);
  }

}