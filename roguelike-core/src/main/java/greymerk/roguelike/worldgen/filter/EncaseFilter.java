package greymerk.roguelike.worldgen.filter;

import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.worldgen.Bounded;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.Shape;

public class EncaseFilter implements IFilter {

  @Override
  public void apply(WorldEditor editor, Theme theme, Bounded box) {
    box.getShape(Shape.RECTSOLID).fill(editor, theme.getPrimary().getWall());
  }
}
