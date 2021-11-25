package greymerk.roguelike.theme;

import java.util.Optional;

import lombok.ToString;

@ToString
public class Theme {

  protected BlockSet primary;
  protected BlockSet secondary;

  public Theme() {
  }

  public Theme(Theme toCopy) {
    this.primary = new BlockSet(toCopy.getPrimary());
    this.secondary = new BlockSet(toCopy.getSecondary());
  }

  public Theme(BlockSet primary, BlockSet secondary) {
    this.primary = primary;
    this.secondary = secondary;
  }

  public static Theme inherit(Theme parent, Theme child) {
    return new Theme(
        BlockSet.inherit(parent.getPrimary(), child.getPrimary()),
        BlockSet.inherit(parent.getSecondary(), child.getSecondary()));
  }

  public BlockSet getPrimary() {
    return primary;
  }

  public BlockSet getSecondary() {
    return Optional.ofNullable(secondary).orElse(getPrimary());
  }

}
