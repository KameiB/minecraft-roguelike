package greymerk.roguelike.command.routes;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import greymerk.roguelike.command.CommandContext1_12;
import greymerk.roguelike.command.CommandRouteBase;
import greymerk.roguelike.command.routes.exception.NoValidLocationException;
import greymerk.roguelike.command.routes.exception.SettingNameNotFoundException;
import greymerk.roguelike.dungeon.Dungeon;
import greymerk.roguelike.dungeon.settings.DungeonSettings;
import greymerk.roguelike.dungeon.settings.SettingsContainer;
import greymerk.roguelike.dungeon.settings.SettingsRandom;
import greymerk.roguelike.dungeon.settings.SettingsResolver;
import greymerk.roguelike.dungeon.settings.TestDungeonSettings;
import greymerk.roguelike.util.ArgumentParser;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;

public class CommandRouteDungeon extends CommandRouteBase {

  public CommandRouteDungeon(greymerk.roguelike.command.CommandBase commandBase) {
    super(commandBase);
  }

  public Coord getLocation(CommandContext1_12 context, List<String> args) {
    ArgumentParser argumentParser = new ArgumentParser(args);
    if (argumentParser.match(0, "here") || argumentParser.match(0, "nearby")) {
      Coord coord = context.getPos();
      return new Coord(coord.getX(), 0, coord.getZ());
    }
    try {
      int x = commandBase.parseInt(argumentParser.get(0));
      int z = commandBase.parseInt(argumentParser.get(1));
      return new Coord(x, 0, z);
    } catch (RuntimeException e) {
      context.sendFailure("invalidcoords", "X Z");
      throw (e);
    }
  }

  @Override
  public void execute(CommandContext1_12 context, List<String> args) {
    ArgumentParser argumentParser = new ArgumentParser(args);
    if (!argumentParser.hasEntry(0)) {
      context.sendInfo("notif.roguelike.usage_", "roguelike dungeon {X Z | here} [setting]");
      return;
    }
    String settingName = getSettingName(argumentParser);

    try {
      Coord pos = getLocation(context, args);
      WorldEditor editor = context.createEditor();
      DungeonSettings dungeonSettings = chooseDungeonSettings(settingName, pos, editor, context);
      generateDungeon(context, pos, editor, dungeonSettings);
    } catch (Exception e) {
      context.sendFailure(e);
    }
  }

  private DungeonSettings chooseDungeonSettings(String settingName, Coord pos, WorldEditor editor, CommandContext1_12 commandContext) throws Exception {
    if (settingName == null) {
      return resolveAnyCustomDungeonSettings(pos, editor, commandContext);
    } else if (settingName.equals("test")) {
      return resolveTestDungeon(editor, pos, commandContext);
    } else if (settingName.equals("random")) {
      return resolveRandomDungeon(editor, pos, commandContext);
    } else {
      return resolveNamedDungeonSettings(settingName, commandContext);
    }
  }

  private DungeonSettings resolveAnyCustomDungeonSettings(Coord pos, WorldEditor editor, CommandContext1_12 commandContext) throws Exception {
    SettingsContainer settingsContainer = new SettingsContainer(commandContext.getModLoader());
    settingsContainer.loadFiles();
    SettingsResolver settingsResolver = new SettingsResolver(settingsContainer);

    Optional<DungeonSettings> dungeonSettings = settingsResolver.chooseDungeonSetting(editor, pos);
    return dungeonSettings
        .orElseThrow(() -> new NoValidLocationException(pos));
  }

  private DungeonSettings resolveTestDungeon(WorldEditor editor, Coord pos, CommandContext1_12 commandContext) throws Exception {
    SettingsContainer settingsContainer = new SettingsContainer(commandContext.getModLoader());
    settingsContainer.loadFiles();
    Dungeon.settingsResolver = new SettingsResolver(settingsContainer);

    Random random = editor.getRandom();
    random.setSeed(editor.getSeed(pos));
    return new TestDungeonSettings(random);
  }

  private DungeonSettings resolveRandomDungeon(WorldEditor editor, Coord pos, CommandContext1_12 commandContext) throws Exception {
    SettingsContainer settingsContainer = new SettingsContainer(commandContext.getModLoader());
    settingsContainer.loadFiles();
    Dungeon.settingsResolver = new SettingsResolver(settingsContainer);

    Random random = editor.getRandom();
    random.setSeed(editor.getSeed(pos));
    return new SettingsRandom(random);
  }

  private DungeonSettings resolveNamedDungeonSettings(String settingName, CommandContext1_12 commandContext) throws Exception {
    SettingsContainer settingsContainer = new SettingsContainer(commandContext.getModLoader());
    settingsContainer.loadFiles();
    SettingsResolver settingsResolver = new SettingsResolver(settingsContainer);

    DungeonSettings dungeonSettings = settingsResolver.getByName(settingName);
    return Optional.ofNullable(dungeonSettings)
        .orElseThrow(() -> new SettingNameNotFoundException(settingName));
  }

  private void generateDungeon(CommandContext1_12 context, Coord coord, WorldEditor editor, DungeonSettings dungeonSettings) {
    Dungeon dungeon = new Dungeon(editor, context.getModLoader());
    dungeon.generate(dungeonSettings, coord);
    context.sendSuccess("generateddungeon", String.format("%s at %s.", dungeonSettings.getId(), coord));
  }

  private String getSettingName(ArgumentParser argumentParser) {
    int index = argumentParser.match(0, "here")
        || argumentParser.match(0, "nearby")
        ? 1
        : 2;
    return argumentParser.get(index);
  }
}
