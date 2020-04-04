package greymerk.roguelike.dungeon.rooms;

import com.google.gson.JsonObject;

import greymerk.roguelike.dungeon.base.RoomType;

import static greymerk.roguelike.dungeon.base.RoomType.valueOf;
import static greymerk.roguelike.dungeon.settings.level.LevelsParser.parseLevelsIfPresent;
import static java.lang.String.format;

public class RoomSettingParser {

  public static final String NAME_KEY = "name";
  public static final String ROOM_FREQUENCY = "type";
  public static final String COUNT_KEY = "count";
  public static final String WEIGHT_KEY = "weight";

  public static RoomSetting parse(JsonObject roomSettingJson) throws Exception {
    return new RoomSetting(
        parseName(roomSettingJson),
        parseSpawnerId(roomSettingJson),
        parseRoomFrequency(roomSettingJson),
        parseWeight(roomSettingJson),
        parseCount(roomSettingJson),
        parseLevelsIfPresent(roomSettingJson));
  }

  private static RoomType parseName(JsonObject entry) throws Exception {
    String name = entry.has(NAME_KEY)
        ? entry.get(NAME_KEY).getAsString().toUpperCase()
        : "NAME MISSING";
    try {
      return valueOf(name);
    } catch (IllegalArgumentException e) {
      throw new Exception(format("No such room with name %s", name));
    }
  }

  private static String parseRoomFrequency(JsonObject entry) {
    return entry.get(ROOM_FREQUENCY).getAsString().toLowerCase();
  }

  private static int parseCount(JsonObject roomSettingJson) {
    return roomSettingJson.has(COUNT_KEY)
        ? roomSettingJson.get(COUNT_KEY).getAsInt()
        : 1;
  }

  private static int parseWeight(JsonObject entry) {
    return entry.has(WEIGHT_KEY)
        ? entry.get(WEIGHT_KEY).getAsInt()
        : 1;
  }

  private static String parseSpawnerId(JsonObject roomSettingJson) {
    return roomSettingJson.has("spawnerId")
        ? roomSettingJson.get("spawnerId").getAsString()
        : null;
  }
}
