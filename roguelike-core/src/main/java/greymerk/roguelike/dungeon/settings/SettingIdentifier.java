package greymerk.roguelike.dungeon.settings;

import com.github.fnar.util.Pair;

import java.util.Objects;

public class SettingIdentifier {

  public static final char DELIMITER = ':';
  private final Pair<String, String> identifier;

  public SettingIdentifier(String namespace, String name) {
    this.identifier = new Pair<>(namespace, name);
  }

  public SettingIdentifier(String name) {
    String[] parts = name.split("" + DELIMITER);
    String namespace = parts.length > 1 ? parts[0] : SettingsContainer.DEFAULT_NAMESPACE;
    String uniqueName = parts.length > 1 ? parts[1] : name;
    this.identifier = new Pair<>(namespace, uniqueName);
  }

  public String getNamespace() {
    return identifier.getKey();
  }

  public String getName() {
    return identifier.getValue();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SettingIdentifier that = (SettingIdentifier) o;
    return Objects.equals(identifier, that.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier);
  }

  @Override
  public String toString() {
    return getNamespace() + DELIMITER + getName();
  }
}
