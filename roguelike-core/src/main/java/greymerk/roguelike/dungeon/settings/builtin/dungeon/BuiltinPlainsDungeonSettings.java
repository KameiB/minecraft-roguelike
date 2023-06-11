package greymerk.roguelike.dungeon.settings.builtin.dungeon;

import com.github.fnar.util.Strings;

import java.util.Collections;

import greymerk.roguelike.dungeon.base.RoomType;
import greymerk.roguelike.dungeon.base.RoomsSetting;
import greymerk.roguelike.dungeon.base.SecretsSetting;
import greymerk.roguelike.dungeon.settings.DungeonSettings;
import greymerk.roguelike.dungeon.settings.LevelSettings;
import greymerk.roguelike.dungeon.settings.SettingIdentifier;
import greymerk.roguelike.dungeon.settings.SettingsContainer;
import greymerk.roguelike.dungeon.settings.TowerSettings;
import greymerk.roguelike.dungeon.settings.builtin.BuiltinBaseSettings;
import greymerk.roguelike.dungeon.towers.TowerType;
import greymerk.roguelike.theme.Themes;
import greymerk.roguelike.worldgen.filter.Filter;

import static com.google.common.collect.Lists.newArrayList;
import static net.minecraftforge.common.BiomeDictionary.Type.PLAINS;

public class BuiltinPlainsDungeonSettings extends DungeonSettings {

  public static final SettingIdentifier ID = new SettingIdentifier(SettingsContainer.BUILTIN_NAMESPACE, "grassland");

  public BuiltinPlainsDungeonSettings() {

    super(ID);
    setExclusive(true);
    getInherit().add(BuiltinBaseSettings.ID);
    getCriteria().setBiomeTypes(Strings.asStrings(Collections.singleton(newArrayList(PLAINS))));

    setTowerSettings(new TowerSettings(TowerType.HOUSE, Themes.HOUSE));

    for (int level = 0; level < DungeonSettings.MAXIMUM_COUNT_OF_LEVELS; level++) {

      LevelSettings levelSettings = getLevelSettings(level);
      SecretsSetting secrets = new SecretsSetting();
      RoomsSetting rooms = new RoomsSetting();

      switch (level) {
        case 0:
          secrets.add(RoomType.BEDROOM.newSingleRoomSetting());
          secrets.add(RoomType.SMITH.newSingleRoomSetting());
          secrets.add(RoomType.FIREWORK.newSingleRoomSetting());
          levelSettings.setSecrets(secrets);
          break;
        case 1:
          secrets.add(RoomType.BTEAM.newSingleRoomSetting());
          rooms.add(RoomType.MUSIC.newSingleRoomSetting());
          rooms.add(RoomType.PIT.newSingleRoomSetting());
          rooms.add(RoomType.MESS.newSingleRoomSetting());
          rooms.add(RoomType.LAB.newSingleRoomSetting());
          rooms.add(RoomType.CORNER.newRandomRoomSetting(10));
          rooms.add(RoomType.BRICK.newRandomRoomSetting(3));
          levelSettings.setSecrets(secrets);
          levelSettings.setRooms(rooms);
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
      getLevelSettings().put(level, levelSettings);
    }

    getLevelSettings().get(3).addFilter(Filter.VINE);
  }


}
