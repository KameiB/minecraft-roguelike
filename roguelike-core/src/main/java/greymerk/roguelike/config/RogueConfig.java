package greymerk.roguelike.config;


import com.github.fnar.util.Strings;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import greymerk.roguelike.worldgen.VanillaStructure;

import static greymerk.roguelike.dungeon.Dungeon.MOD_ID;

public class RogueConfig {

  private static final Logger logger = LogManager.getLogger(MOD_ID);

  public static final String CONFIG_DIRECTORY = "config/roguelike_dungeons";
  public static final String CONFIG_FILE_NAME = "roguelike.cfg";
  public static final String CONFIG_FILE = CONFIG_DIRECTORY + "/" + CONFIG_FILE_NAME;

  public static boolean testing = false;
  private static ConfigurationMap instance = null;

  public static final RogueConfig BREAK_IF_REQUIRED_MOD_IS_MISSING = new RogueConfig("breakIfRequiredModIsMissing").withValue(true);
  public static final RogueConfig DIMENSIONBL = new RogueConfig("dimensionBL").withValue(new Integer[]{});
  public static final RogueConfig DIMENSIONWL = new RogueConfig("dimensionWL").withValue(new Integer[]{0});
  public static final RogueConfig ENCASE = new RogueConfig("encase").withValue(false);
  public static final RogueConfig FURNITURE = new RogueConfig("furniture").withValue(true);
  public static final RogueConfig GENEROUS = new RogueConfig("generous").withValue(true);
  public static final RogueConfig LOWERLIMIT = new RogueConfig("lowerLimit").withValue(60);
  public static final RogueConfig PRECIOUSBLOCKS = new RogueConfig("preciousBlocks").withValue(true);
  public static final RogueConfig RANDOM = new RogueConfig("random").withValue(false);
  public static final RogueConfig SPAWNBUILTIN = new RogueConfig("doBuiltinSpawn").withValue(true);
  public static final RogueConfig SPAWN_MINIMUM_DISTANCE_FROM_VANILLA_STRUCTURES = new RogueConfig("spawnMinimumDistanceFromVanillaStructures").withValue(50);
  public static final RogueConfig UPPERLIMIT = new RogueConfig("upperLimit").withValue(100);
  public static final RogueConfig VANILLA_STRUCTURES_TO_CHECK_MINIMUM_DISTANCE_FROM = new RogueConfig("vanillaStructuresToCheckMinimumDistanceFrom").withValue(VanillaStructure.getAllAsCommaDelimitedString());
  public static final RogueConfig DUNGEONS_GENERATION_THRESHOLD_CHANCE = new RogueConfig("doorwaysChance").withValue(new Double[]{0.10, 0.10, 0.10, 0.10, 0.10});
  public static final RogueConfig DUNGEONS_SPAWN_ATTEMPTS = new RogueConfig("spawnAttempts").withValue(10);
  public static final RogueConfig DUNGEONS_SPAWN_CHANCE = new RogueConfig("spawnChance").withValue(1.0);
  public static final RogueConfig DUNGEONS_SPAWN_ENABLED = new RogueConfig("doNaturalSpawn").withValue(true);
  public static final RogueConfig DUNGEONS_SPAWN_FREQUENCY = new RogueConfig("spawnFrequency").withValue(10);
  public static final RogueConfig MOBS_DESPAWN_ENABLED = new RogueConfig("mobsDespawn").withValue(true);
  public static final RogueConfig MOBS_ITEMS_DROP_CHANCE = new RogueConfig("looting").withValue(0.085);
  public static final RogueConfig MOBS_ITEMS_TIEFIGHTERS_ENABLED = new RogueConfig("tiefighters").withValue(false);
  public static final RogueConfig MOBS_ITEMS_ENCHANTMENTS_CHANCE = new RogueConfig("mobEnchantedChance").withValue(new Double[]{-1.0, -1.0, -1.0, -1.0, -1.0});
  public static final RogueConfig MOBS_ITEMS_ENCHANTMENTS_LEVELS = new RogueConfig("mobEnchantedLevels").withValue(new Integer[]{-1, -1, -1, -1, -1});
  public static final RogueConfig MOBS_PROFILES_ENABLED = new RogueConfig("rogueSpawners").withValue(true);

  private final String name;
  private String stringValue;
  private Boolean booleanValue;
  private Integer intValue;
  private Double doubleValue;
  private List<Integer> intsValue;
  private List<Double> doublesValue;

  public RogueConfig(String name) {
    this.name = name;
  }

  public Optional<Integer> getIntAtIndexIfNonNegative(int level) {
    Optional<Integer> value = getIntAtIndex(level);
    return value.isPresent() && value.get() >= 0 ? value : Optional.empty();
  }

  public Optional<Double> getDoubleAtIndexIfNonNegative(int level) {
    Optional<Double> value = getDoubleAtIndex(level);
    return value.isPresent() && value.get() >= 0 ? value : Optional.empty();
  }

  public String getName() {
    return name;
  }

  private static void addEachConfigurationIfMissing(ConfigurationMap configurationMap) {
    if (!configurationMap.containsKey(BREAK_IF_REQUIRED_MOD_IS_MISSING.name)) {
      BREAK_IF_REQUIRED_MOD_IS_MISSING.setBoolean(BREAK_IF_REQUIRED_MOD_IS_MISSING.booleanValue);
    }

    if (!configurationMap.containsKey(DUNGEONS_SPAWN_ENABLED.name)) {
      DUNGEONS_SPAWN_ENABLED.setBoolean(DUNGEONS_SPAWN_ENABLED.booleanValue);
    }
    if (!configurationMap.containsKey(DUNGEONS_SPAWN_FREQUENCY.name)) {
      DUNGEONS_SPAWN_FREQUENCY.setInt(DUNGEONS_SPAWN_FREQUENCY.intValue);
    }
    if (!configurationMap.containsKey(DUNGEONS_SPAWN_CHANCE.name)) {
      DUNGEONS_SPAWN_CHANCE.setDouble(DUNGEONS_SPAWN_CHANCE.doubleValue);
    }
    if (!configurationMap.containsKey(GENEROUS.name)) {
      GENEROUS.setBoolean(GENEROUS.booleanValue);
    }
    if (!configurationMap.containsKey(DIMENSIONWL.name)) {
      DIMENSIONWL.setIntList(DIMENSIONWL.intsValue);
    }
    if (!configurationMap.containsKey(DIMENSIONBL.name)) {
      DIMENSIONBL.setIntList(DIMENSIONBL.intsValue);
    }
    if (!configurationMap.containsKey(PRECIOUSBLOCKS.name)) {
      PRECIOUSBLOCKS.setBoolean(PRECIOUSBLOCKS.booleanValue);
    }

    if (!configurationMap.containsKey(DUNGEONS_GENERATION_THRESHOLD_CHANCE.getName())) {
      DUNGEONS_GENERATION_THRESHOLD_CHANCE.setDoubleList(DUNGEONS_GENERATION_THRESHOLD_CHANCE.doublesValue);
    }

    if (!configurationMap.containsKey(MOBS_DESPAWN_ENABLED.getName())) {
      MOBS_DESPAWN_ENABLED.setBoolean(MOBS_DESPAWN_ENABLED.getBoolean());
    }

    if (!configurationMap.containsKey(MOBS_ITEMS_DROP_CHANCE.getName())) {
      MOBS_ITEMS_DROP_CHANCE.setDouble(MOBS_ITEMS_DROP_CHANCE.doubleValue);
    }

    if (!configurationMap.containsKey(MOBS_ITEMS_ENCHANTMENTS_CHANCE.getName())) {
      MOBS_ITEMS_ENCHANTMENTS_CHANCE.setDoubleList(MOBS_ITEMS_ENCHANTMENTS_CHANCE.doublesValue);
    }

    if (!configurationMap.containsKey(MOBS_ITEMS_ENCHANTMENTS_LEVELS.getName())) {
      MOBS_ITEMS_ENCHANTMENTS_LEVELS.setIntList(MOBS_ITEMS_ENCHANTMENTS_LEVELS.intsValue);
    }

    if (!configurationMap.containsKey(UPPERLIMIT.name)) {
      UPPERLIMIT.setInt(UPPERLIMIT.intValue);
    }
    if (!configurationMap.containsKey(LOWERLIMIT.name)) {
      LOWERLIMIT.setInt(LOWERLIMIT.intValue);
    }
    if (!configurationMap.containsKey(MOBS_PROFILES_ENABLED.name)) {
      MOBS_PROFILES_ENABLED.setBoolean(MOBS_PROFILES_ENABLED.booleanValue);
    }
    if (!configurationMap.containsKey(ENCASE.name)) {
      ENCASE.setBoolean(ENCASE.booleanValue);
    }
    if (!configurationMap.containsKey(FURNITURE.name)) {
      FURNITURE.setBoolean(FURNITURE.booleanValue);
    }
    if (!configurationMap.containsKey(RANDOM.name)) {
      RANDOM.setBoolean(RANDOM.booleanValue);
    }
    if (!configurationMap.containsKey(SPAWNBUILTIN.name)) {
      SPAWNBUILTIN.setBoolean(SPAWNBUILTIN.booleanValue);
    }
    if (!configurationMap.containsKey(SPAWN_MINIMUM_DISTANCE_FROM_VANILLA_STRUCTURES.name)) {
      SPAWN_MINIMUM_DISTANCE_FROM_VANILLA_STRUCTURES.setInt(SPAWN_MINIMUM_DISTANCE_FROM_VANILLA_STRUCTURES.intValue);
    }
    if (!configurationMap.containsKey(DUNGEONS_SPAWN_ATTEMPTS.name)) {
      DUNGEONS_SPAWN_ATTEMPTS.setInt(DUNGEONS_SPAWN_ATTEMPTS.intValue);
    }

    if (!configurationMap.containsKey(VANILLA_STRUCTURES_TO_CHECK_MINIMUM_DISTANCE_FROM.getName())) {
      VANILLA_STRUCTURES_TO_CHECK_MINIMUM_DISTANCE_FROM.setString(VANILLA_STRUCTURES_TO_CHECK_MINIMUM_DISTANCE_FROM.stringValue);
    }
  }

  private static void init() {

    if (testing) {
      return;
    }

    // make sure file exists
    File configDir = new File(CONFIG_DIRECTORY);
    if (!configDir.exists()) {
      configDir.mkdir();
    }

    File cfile = new File(CONFIG_FILE);

    if (!cfile.exists()) {
      try {
        cfile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // read in configs
    try {
      instance = new ConfigurationMap();
      ConfigFile.read(instance, CONFIG_FILE);
    } catch (Exception e) {
      e.printStackTrace();
    }

    addEachConfigurationIfMissing(instance);

    try {
      ConfigFile.write(instance, CONFIG_FILE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void reload(boolean force) {
    if (instance == null || force) {
      init();
    }
  }

  public String getString() {
    if (testing) {
      return stringValue;
    }
    return Optional.ofNullable(instance.get(name, stringValue))
        .orElse(stringValue);
  }

  private void setString(String defaultStringValue) {
    reload(false);
    instance.put(name, defaultStringValue);
  }

  public boolean getBoolean() {
    if (testing) {
      return booleanValue;
    }
    reload(false);
    return instance.GetBoolean(name, booleanValue);
  }

  public void setBoolean(Boolean value) {
    reload(false);
    instance.put(name, value ? "true" : "false");
  }

  public int getInt() {
    if (testing) {
      return intValue;
    }
    reload(false);
    return instance.getInteger(name, intValue);
  }

  public void setInt(int value) {
    reload(false);
    instance.put(name, Integer.toString(value));
  }

  public double getDouble() {
    if (testing) {
      return doubleValue;
    }
    reload(false);
    return instance.getDouble(name, doubleValue);
  }

  public void setDouble(double value) {
    reload(false);
    instance.put(name, Double.toString(value));
  }

  @SuppressWarnings("unchecked")
  public List<Integer> getIntList() {
    if (testing) {
      return intsValue;
    }
    reload(false);
    return instance.getIntegers(name, intsValue);
  }

  public void setIntList(List<Integer> value) {
    reload(false);
    instance.put(name, StringUtils.join(value, ","));
  }

  public Optional<Integer> getIntAtIndex(int index) {
    List<Integer> values = getIntList();
    if (index >= values.size()) {
      return Optional.empty();
    }
    return Optional.of(values.get(index));
  }

  @SuppressWarnings("unchecked")
  public List<Double> getDoubleList() {
    if (testing) {
      return doublesValue;
    }
    reload(false);
    return instance.getDoubles(name, doublesValue);
  }

  public void setDoubleList(List<Double> value) {
    reload(false);
    instance.put(name, StringUtils.join(value.toArray(new Double[]{}), ","));
  }

  public Optional<Double> getDoubleAtIndex(int index) {
    List<Double> values = getDoubleList();
    if (index >= values.size()) {
      return Optional.empty();
    }
    return Optional.of(values.get(index));
  }

  public static Set<VanillaStructure> vanillaStructuresToCheckDistanceTo() {

    return Strings.splitCommas(VANILLA_STRUCTURES_TO_CHECK_MINIMUM_DISTANCE_FROM.getString()).stream()
        .map(str -> str.toUpperCase(Locale.ENGLISH))
        .map(structureName -> {
          try {
            return VanillaStructure.valueOf(structureName);
          } catch (IllegalArgumentException exception) {
            logger.error("Invalid value for config `vanillaStructuresToCheckMinimumDistanceFrom`: \"{}\". Skipping...", structureName);
          }
          return null;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  public RogueConfig withValue(String value) {
    this.stringValue = value;
    return this;
  }

  public RogueConfig withValue(boolean value) {
    this.booleanValue = value;
    return this;
  }

  public RogueConfig withValue(int value) {
    this.intValue = value;
    return this;
  }

  public RogueConfig withValue(double value) {
    this.doubleValue = value;
    return this;
  }

  public RogueConfig withValue(Integer[] value) {
    this.intsValue = Arrays.asList(value);
    return this;
  }

  public RogueConfig withValue(Double[] value) {
    this.doublesValue = Arrays.asList(value);
    return this;
  }

}
