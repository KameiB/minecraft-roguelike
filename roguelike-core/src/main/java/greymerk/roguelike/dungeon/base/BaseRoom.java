package greymerk.roguelike.dungeon.base;

import com.github.fnar.minecraft.block.spawner.MobType;
import com.github.fnar.minecraft.block.spawner.Spawner;
import com.github.fnar.minecraft.block.spawner.SpawnerSettings;
import com.github.fnar.minecraft.worldgen.generatables.Doorways;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import greymerk.roguelike.dungeon.Dungeon;
import greymerk.roguelike.dungeon.rooms.RoomSetting;
import greymerk.roguelike.dungeon.settings.DungeonSettings;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.treasure.TreasureChest;
import greymerk.roguelike.treasure.loot.ChestType;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.Direction;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;
import lombok.EqualsAndHashCode;

import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toList;

@EqualsAndHashCode
public abstract class BaseRoom implements Comparable<BaseRoom> {

  private final RoomSetting roomSetting;
  protected final LevelSettings levelSettings;
  protected final WorldEditor worldEditor;

  public BaseRoom(RoomSetting roomSetting, LevelSettings levelSettings, WorldEditor worldEditor) {
    this.roomSetting = roomSetting;
    this.levelSettings = levelSettings;
    this.worldEditor = worldEditor;
  }

  public List<Coord> chooseRandomLocations(int limit, List<Coord> spaces) {
    shuffle(spaces, worldEditor.getRandom());

    return spaces.stream()
        .limit(limit)
        .collect(toList());
  }

  public abstract BaseRoom generate(Coord origin, List<Direction> entrances);

  protected void generateDoorways(Coord origin, List<Direction> entrances) {
    generateDoorways(origin, entrances, getSize()-2);
  }

  protected void generateDoorways(Coord origin, List<Direction> entrances, int distanceFromOrigin) {
    entrances.forEach(direction ->
        Doorways.generateDoorway(
            worldEditor,
            levelSettings,
            origin.copy().translate(direction, distanceFromOrigin),
            direction));
  }

  protected void generateSpawner(Coord spawnerLocation, MobType... defaultMobs) {
    int difficulty = levelSettings.getDifficulty(spawnerLocation);
    generateSpawner(spawnerLocation, difficulty, defaultMobs);
  }

  private void generateSpawner(Coord spawnerLocation, int difficulty, MobType... defaultMobs) {
    Spawner spawner = chooseSpawner(difficulty, defaultMobs, worldEditor.getRandom());
    generateSpawnerSafe(worldEditor, spawner, spawnerLocation, difficulty);
  }

  private Spawner chooseSpawner(int difficulty, MobType[] defaultMobs, Random random) {
    Optional<SpawnerSettings> roomSpawnerSettings = getSpawnerSettings(roomSetting.getSpawnerId(), difficulty);
    SpawnerSettings levelSpawnerSettings = levelSettings.getSpawnerSettings();
    if (roomSpawnerSettings.isPresent()) {
      return roomSpawnerSettings.get().chooseOneAtRandom(random);
    }
    if (!levelSpawnerSettings.isEmpty()) {
      return levelSpawnerSettings.chooseOneAtRandom(worldEditor.getRandom());
    }
    return MobType.asSpawner(defaultMobs.length > 0 ? defaultMobs : MobType.COMMON_MOBS);
  }

  private static Optional<SpawnerSettings> getSpawnerSettings(String spawnerId, int difficulty) {
    if (spawnerId == null) {
      return Optional.empty();
    }
    DungeonSettings dungeonSettings = null;
    try {
      dungeonSettings = Dungeon.settingsResolver.getByName(spawnerId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (dungeonSettings == null) {
      return Optional.empty();
    }
    SpawnerSettings spawnerSettings = dungeonSettings.getLevelSettings(difficulty).getSpawnerSettings();
    if (spawnerSettings == null || spawnerSettings.isEmpty()) {
       return Optional.empty();
    }
    return Optional.of(spawnerSettings);
  }

  public static void generateSpawnerSafe(WorldEditor editor, Spawner spawner, Coord cursor, int difficulty) {
    try {
      editor.generateSpawner(spawner, cursor, difficulty);
    } catch (Exception e) {
      throw new RuntimeException("Tried to spawn empty spawner", e);
    }
  }

  protected ChestType getChestTypeOrUse(ChestType defaultChestType) {
    return getRoomSetting().getChestType().orElse(defaultChestType);
  }

  protected void generateChests(List<Coord> chestLocations, Direction facing) {
    generateChests(chestLocations, facing, ChestType.COMMON_TREASURES);
  }

  protected void generateChests(List<Coord> chestLocations, Direction facing, ChestType... defaultChestTypes) {
    chestLocations.forEach(chestLocation ->
        generateChest(chestLocation, facing, defaultChestTypes));
  }

  protected void generateChest(Coord cursor, Direction dir, ChestType... defaultChestType) {
    generateChest(cursor, dir, ChestType.chooseRandomAmong(worldEditor.getRandom(), defaultChestType));
  }

  protected Optional<TreasureChest> generateChest(Coord cursor, Direction dir, ChestType defaultChestType) {
    return chest(cursor, dir, defaultChestType)
        .stroke();
  }

  protected Optional<TreasureChest> generateTrappedChest(Coord cursor, Direction dir, ChestType defaultChestType) {
    return chest(cursor, dir, defaultChestType)
        .withTrap(true)
        .stroke();
  }

  protected void generateTrappableChests(List<Coord> chestLocations, Direction facing) {
    generateTrappableChests(chestLocations, facing, ChestType.COMMON_TREASURES);
  }

  protected void generateTrappableChests(List<Coord> chestLocations, Direction facing, ChestType... defaultChestTypes) {
    chestLocations.forEach(chestLocation ->
        generateTrappableChest(chestLocation, facing, defaultChestTypes));
  }

  protected void generateTrappableChest(Coord cursor, Direction dir, ChestType... defaultChestType) {
    generateTrappableChest(cursor, dir, ChestType.chooseRandomAmong(worldEditor.getRandom(), defaultChestType));
  }

  protected Optional<TreasureChest> generateTrappableChest(Coord cursor, Direction dir, ChestType defaultChestType) {
    return chest(cursor, dir, defaultChestType)
        .withTrapBasedOnDifficulty(levelSettings.getDifficulty(cursor))
        .stroke();
  }

  private TreasureChest chest(Coord cursor, Direction dir, ChestType defaultChestType) {
    return new TreasureChest(cursor, worldEditor)
        .withChestType(getChestTypeOrUse(defaultChestType))
        .withFacing(dir);
  }

  public abstract int getSize();

  public boolean validLocation(WorldEditor editor, Direction dir, Coord pos) {

    int size = getSize();
    Coord start = new Coord(pos.getX() - size, pos.getY() - 2, pos.getZ() - size);
    Coord end = new Coord(pos.getX() + size, pos.getY() + 5, pos.getZ() + size);

    for (Coord cursor : new RectHollow(start, end)) {
      if (!editor.isSolidBlock(cursor)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public int compareTo(BaseRoom other) {
    return getSize() - other.getSize();
  }

  protected RoomSetting getRoomSetting() {
    return roomSetting;
  }
}
