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

  public SettingsResolver(SettingsContainer settingsContainer) {
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
      DungeonSettings dungeonSettings = settingsContainer.get(id);
      return processInheritance(dungeonSettings);
    } catch (Exception e) {
      Minecraft.getMinecraft().player.sendChatMessage(Arrays.toString(e.getStackTrace()));
      throw new RuntimeException(e);
//      throw new RuntimeException("Malformed Setting ID String: " + name);
    }
  }

  public DungeonSettings processInheritance(DungeonSettings dungeonSettings) {
    DungeonSettings accumulatedInheritedSettings = dungeonSettings.getInherit().stream()
        .map(settingsContainer::get)
        .map(this::processInheritance)
        .reduce(new DungeonSettings(), (accumulation, toInherit) -> toInherit.inherit(accumulation));

    return dungeonSettings.inherit(accumulatedInheritedSettings);
  }

  private Optional<DungeonSettings> chooseOneBuiltinSettingAtRandom(WorldEditor editor, Coord coord) {
    if (!RogueConfig.getBoolean(RogueConfig.SPAWNBUILTIN)) {
      return empty();
    }
    return chooseRandomValid(editor, coord, settingsContainer.getBuiltinSettings());
  }

  private Optional<DungeonSettings> chooseRandomCustomDungeonIfPossible(WorldEditor editor, Coord coord) {
    return chooseRandomValid(editor, coord, settingsContainer.getCustomSettings());
  }

  private Optional<DungeonSettings> chooseRandomValid(WorldEditor editor, Coord coord, Collection<DungeonSettings> settings) {
    Collection<DungeonSettings> inflatedSettings = settings.stream()
        .map(this::processInheritance)
        .collect(toList());
    List<DungeonSettings> validSettings = filterValid(inflatedSettings, editor, coord);
    WeightedRandomizer<DungeonSettings> settingsRandomizer = newWeightedRandomizer(validSettings);
    return ofNullable(settingsRandomizer.get(editor.getRandom(coord)));
  }

  private List<DungeonSettings> filterValid(Collection<DungeonSettings> settings, WorldEditor editor, Coord coord) {
    return settings.stream()
        .filter(DungeonSettings::isExclusive)
        .filter(isValid(editor, coord))
        .collect(Collectors.toList());
  }

  private WeightedRandomizer<DungeonSettings> newWeightedRandomizer(List<DungeonSettings> dungeonSettings) {
    WeightedRandomizer<DungeonSettings> settingsRandomizer = new WeightedRandomizer<>();
    dungeonSettings.stream()
        .map(setting -> new WeightedChoice<>(setting, setting.getCriteria().getWeight()))
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
