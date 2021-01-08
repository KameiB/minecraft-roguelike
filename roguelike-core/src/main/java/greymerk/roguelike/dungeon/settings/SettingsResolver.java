package greymerk.roguelike.dungeon.settings;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import net.minecraft.client.Minecraft;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import greymerk.roguelike.config.RogueConfig;
import greymerk.roguelike.util.WeightedChoice;
import greymerk.roguelike.util.WeightedRandomizer;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class SettingsResolver {

  private final SettingsContainer settingsContainer;

  public SettingsResolver(
      SettingsContainer settingsContainer
  ) {
    this.settingsContainer = settingsContainer;
  }

  public static SettingsResolver initSettingsResolver() throws Exception {
    File settingsDirectoryFile = new File(RogueConfig.configDirName + "/settings");

    if (settingsDirectoryFile.exists() && !settingsDirectoryFile.isDirectory()) {
      throw new Exception("Settings directory is a file");
    }

    if (!settingsDirectoryFile.exists()) {
      settingsDirectoryFile.mkdir();
    }

    Map<String, String> fileByName = collectSettingsFiles(settingsDirectoryFile);
    SettingsContainer settings = new SettingsContainer();
    settings.put(fileByName);
    return new SettingsResolver(settings);
  }

  private static Map<String, String> collectSettingsFiles(File settingsDirectory) {
    List<File> files = listFilesRecursively(settingsDirectory);
    return mapContentByFilename(files);
  }

  private static List<File> listFilesRecursively(File settingsDirectory) {
    File[] files = settingsDirectory.listFiles();
    return ofNullable(files).isPresent()
        ? newArrayList(files).stream()
        .flatMap(file -> file.isDirectory() ? listFilesRecursively(file).stream() : Lists.newArrayList(file).stream())
        .filter(file -> FilenameUtils.getExtension(file.getName()).equals("json"))
        .collect(toList())
        : emptyList();
  }

  private static Map<String, String> mapContentByFilename(List<File> files) {
    return files.stream()
        .collect(toMap(File::getName, SettingsResolver::getFileContent));
  }

  private static String getFileContent(File file) {
    try {
      return Files.toString(file, Charsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Error reading file : " + file.getName());
    }
  }

  public DungeonSettings getAnyCustomDungeonSettings(WorldEditor editor, Coord coord) {
    return chooseRandomCustomDungeonIfPossible(editor, coord)
        .orElseGet(() -> chooseOneBuiltinSettingAtRandom(editor, coord)
            .orElse(null));
  }

  public DungeonSettings getByName(String name) {
    try {
      SettingIdentifier id = new SettingIdentifier(name);
      if (settingsContainer.contains(id)) {
        DungeonSettings setting = new DungeonSettings(settingsContainer.get(id));
        DungeonSettings byName = processInheritance(setting);
        if (byName != null) {
          // todo: Remove SettingsBlank. This is data and should be treated like a constant instance
          // todo: also consider eliminating usage of the merge constructor here.
          // todo: also consider eliminating usage of the copy constructor here.
          return new DungeonSettings(new SettingsBlank(), byName);
        }
      }
      return null;
    } catch (Exception e) {
      Minecraft.getMinecraft().player.sendChatMessage(Arrays.toString(e.getStackTrace()));
      throw new RuntimeException(e);
//      throw new RuntimeException("Malformed Setting ID String: " + name);
    }
  }

  public DungeonSettings processInheritance(
      DungeonSettings dungeonSettings
  ) {
    return dungeonSettings.getInherits().stream()
        .peek(this::throwIfNotFound)
        .map(settingsContainer::get)
        .reduce(dungeonSettings, this::inherit);
  }

  private void throwIfNotFound(SettingIdentifier settingIdentifier) {
    if (!settingsContainer.contains(settingIdentifier)) {
      throw new RuntimeException("Setting not found: " + settingIdentifier.toString());
    }
  }

  private DungeonSettings inherit(DungeonSettings child, DungeonSettings parent) {
    return new DungeonSettings(processInheritance(parent), child);
  }

  private Optional<DungeonSettings> chooseOneBuiltinSettingAtRandom(WorldEditor editor, Coord coord) {
    if (!RogueConfig.getBoolean(RogueConfig.SPAWNBUILTIN)) {
      return empty();
    }
    WeightedRandomizer<DungeonSettings> settingsRandomizer = newWeightedRandomizer(getValidBuiltinSettings(editor, coord));

    if (settingsRandomizer.isEmpty()) {
      return empty();
    }
    Random random = editor.getRandom();
    DungeonSettings randomSetting = settingsRandomizer.get(random);
    DungeonSettings builtin = processInheritance(randomSetting);
    return ofNullable(builtin);
  }

  private List<DungeonSettings> getValidBuiltinSettings(WorldEditor editor, Coord coord) {
    return filterValid(settingsContainer.getBuiltinSettings(), editor, coord);
  }

  private List<DungeonSettings> filterValid(Collection<DungeonSettings> builtinSettings, WorldEditor editor, Coord coord) {
    return builtinSettings.stream()
        .filter(isValid(editor, coord))
        .filter(DungeonSettings::isExclusive)
        .collect(Collectors.toList());
  }

  private Optional<DungeonSettings> chooseRandomCustomDungeonIfPossible(
      WorldEditor editor,
      Coord coord
  ) {
    List<DungeonSettings> validCustomSettings = filterValid(settingsContainer.getCustomSettings(), editor, coord);
    WeightedRandomizer<DungeonSettings> settingsRandomizer = newWeightedRandomizer(validCustomSettings);
    Random random = editor.getRandom();
    return ofNullable(settingsRandomizer.get(random))
        .map(this::processInheritance);
  }

  private WeightedRandomizer<DungeonSettings> newWeightedRandomizer(List<DungeonSettings> dungeonSettings) {
    WeightedRandomizer<DungeonSettings> settingsRandomizer = new WeightedRandomizer<>();
    dungeonSettings.stream()
        .map(setting -> new WeightedChoice<>(setting, setting.getSpawnCriteria().getWeight()))
        .forEach(settingsRandomizer::add);
    return settingsRandomizer;
  }

  private Predicate<DungeonSettings> isValid(WorldEditor editor, Coord pos) {
    return setting -> setting.isValid(editor, pos);
  }

  public String toString(String namespace) {
    return settingsContainer.getByNamespace(namespace).stream()
        .map(DungeonSettings::getId)
        .map(SettingIdentifier::toString)
        .collect(joining(" "));
  }

  @Override
  public String toString() {
    return settingsContainer.toString();
  }
}