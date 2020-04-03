package greymerk.roguelike.dungeon.settings.builtin;

import greymerk.roguelike.dungeon.base.DungeonRoom;
import greymerk.roguelike.dungeon.base.RoomsSetting;
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
import static net.minecraftforge.common.BiomeDictionary.Type.MOUNTAIN;

public class SettingsMountainTheme extends DungeonSettings {

  public static final SettingIdentifier ID = new SettingIdentifier(SettingsContainer.BUILTIN_NAMESPACE, "mountain");

  public SettingsMountainTheme() {
    super(ID);
    getInherit().add(SettingsBase.ID);
    getSpawnCriteria().setBiomeTypes(newArrayList(MOUNTAIN));
    setTowerSettings(new TowerSettings(Tower.ENIKO, Theme.OAK));

    Theme[] themes = {Theme.ENIKO, Theme.ENIKO2, Theme.SEWER, Theme.MOSSY, Theme.NETHER};

    for (int i = 0; i < 5; ++i) {
      LevelSettings level = new LevelSettings();
      level.setTheme(themes[i].getThemeBase());

      if (i == 0) {
        level.setScatter(16);
        level.setRange(60);
        level.setNumRooms(10);

        RoomsSetting factory;

        factory = new RoomsSetting();
        factory.add(DungeonRoom.LIBRARY.newSingleRoomSetting());
        factory.add(DungeonRoom.FIRE.newSingleRoomSetting());
        DungeonRoom.ENIKO.newRandomRoomSetting(10);
        DungeonRoom.CORNER.newRandomRoomSetting(3);
        level.setRooms(factory);

        SecretFactory secrets = new SecretFactory();
        secrets.addRoom(DungeonRoom.BEDROOM, 2);
        secrets.addRoom(DungeonRoom.SMITH);
        level.setSecrets(secrets);

        SegmentGenerator segments = new SegmentGenerator(Segment.ARCH);
        segments.add(Segment.DOOR, 7);
        segments.add(Segment.ANKH, 2);
        segments.add(Segment.PLANT, 3);
        segments.add(Segment.LAMP, 1);
        segments.add(Segment.FLOWERS, 1);
        level.setSegments(segments);
      }

      if (i == 1) {
        level.setScatter(16);
        level.setRange(80);
        level.setNumRooms(20);

        RoomsSetting factory;
        factory = new RoomsSetting();
        factory.add(DungeonRoom.FIRE.newSingleRoomSetting());
        factory.add(DungeonRoom.MESS.newSingleRoomSetting());
        factory.add(DungeonRoom.LIBRARY.newSingleRoomSetting());
        factory.add(DungeonRoom.LAB.newSingleRoomSetting());
        factory.add(DungeonRoom.ENIKO.newRandomRoomSetting(10));
        factory.add(DungeonRoom.CORNER.newRandomRoomSetting(3));
        level.setRooms(factory);

        SecretFactory secrets = new SecretFactory();
        secrets.addRoom(DungeonRoom.ENCHANT);
        level.setSecrets(secrets);

      }

      if (i == 2) {
        level.setDifficulty(4);

        SegmentGenerator segments = new SegmentGenerator(Segment.SEWERARCH);
        segments.add(Segment.SEWER, 7);
        segments.add(Segment.SEWERDRAIN, 4);
        segments.add(Segment.SEWERDOOR, 2);
        level.setSegments(segments);

        RoomsSetting factory;
        factory = new RoomsSetting();
        factory.add(DungeonRoom.BRICK.newRandomRoomSetting(4));
        factory.add(DungeonRoom.SLIME.newRandomRoomSetting(7));
        factory.add(DungeonRoom.CORNER.newRandomRoomSetting(3));
        factory.add(DungeonRoom.SPIDER.newRandomRoomSetting(2));
        factory.add(DungeonRoom.PIT.newRandomRoomSetting(2));
        factory.add(DungeonRoom.PRISON.newRandomRoomSetting(3));
        level.setRooms(factory);
      }

      getLevels().put(i, level);
    }
    getLevels().get(3).addFilter(Filter.VINE);
  }
}
