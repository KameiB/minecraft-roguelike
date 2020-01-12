package greymerk.roguelike.dungeon.settings.builtin;

import greymerk.roguelike.dungeon.base.DungeonFactory;
import greymerk.roguelike.dungeon.base.DungeonRoom;
import greymerk.roguelike.dungeon.base.SecretFactory;
import greymerk.roguelike.dungeon.segment.Segment;
import greymerk.roguelike.dungeon.segment.SegmentGenerator;
import greymerk.roguelike.dungeon.settings.DungeonSettings;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.dungeon.settings.SettingIdentifier;
import greymerk.roguelike.dungeon.settings.SettingsContainer;
import greymerk.roguelike.dungeon.settings.TowerSettings;
import greymerk.roguelike.dungeon.settings.base.SettingsBase;
import greymerk.roguelike.dungeon.towers.Tower;
import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.worldgen.filter.Filter;

import static com.google.common.collect.Lists.newArrayList;
import static net.minecraftforge.common.BiomeDictionary.Type.FOREST;

public class SettingsForestTheme extends DungeonSettings {

  public static final SettingIdentifier ID = new SettingIdentifier(SettingsContainer.BUILTIN_NAMESPACE, "forest");

  public SettingsForestTheme() {
    super(ID);
    getInherit().add(SettingsBase.ID);
    getCriteria().setBiomeTypes(newArrayList(FOREST));
    setTowerSettings(new TowerSettings(Tower.ROGUE, Theme.TOWER));

    for (int i = 0; i < 5; ++i) {

      LevelSettings level = new LevelSettings();
      SecretFactory secrets;
      DungeonFactory rooms;
      SegmentGenerator segments;


      switch (i) {
        case 0:
          rooms = new DungeonFactory();
          rooms.addRandom(DungeonRoom.CORNER, 8);
          rooms.addRandom(DungeonRoom.BRICK, 3);
          rooms.addSingle(DungeonRoom.CAKE);
          rooms.addSingle(DungeonRoom.DARKHALL);
          rooms.addSingle(DungeonRoom.LIBRARY);
          level.setRooms(rooms);
          secrets = new SecretFactory();
          secrets.addRoom(DungeonRoom.SMITH);
          secrets.addRoom(DungeonRoom.BEDROOM, 2);
          level.setSecrets(secrets);
          level.setTheme(Theme.SPRUCE.getThemeBase());
          segments = new SegmentGenerator(Segment.ARCH);
          segments.add(Segment.DOOR, 8);
          segments.add(Segment.LAMP, 2);
          segments.add(Segment.WHEAT, 3);
          segments.add(Segment.FLOWERS, 2);
          segments.add(Segment.INSET, 1);
          segments.add(Segment.PLANT, 2);
          segments.add(Segment.SHELF, 1);
          segments.add(Segment.CHEST, 1);
          level.setSegments(segments);
          break;
        case 1:
          rooms = new DungeonFactory();
          rooms.addSingle(DungeonRoom.MUSIC);
          rooms.addSingle(DungeonRoom.PIT);
          rooms.addSingle(DungeonRoom.LAB);
          rooms.addSingle(DungeonRoom.SLIME, 2);
          rooms.addRandom(DungeonRoom.CORNER, 10);
          rooms.addRandom(DungeonRoom.BRICK, 3);
          level.setRooms(rooms);
          level.setTheme(Theme.DARKHALL.getThemeBase());
          segments = new SegmentGenerator(Segment.ARCH);
          segments.add(Segment.DOOR, 10);
          segments.add(Segment.FLOWERS, 2);
          segments.add(Segment.INSET, 2);
          segments.add(Segment.PLANT, 2);
          segments.add(Segment.SHELF, 2);
          segments.add(Segment.CHEST, 1);
          level.setSegments(segments);
          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          break;
        default:
          break;
      }


      getLevels().put(i, level);
    }
    getLevels().get(3).addFilter(Filter.VINE);
  }
}