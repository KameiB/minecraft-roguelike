package greymerk.roguelike.worldgen.filter;

import java.util.Random;

import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.worldgen.IBounded;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.Shape;

public class EncaseFilter implements IFilter {

  @Override
  public void apply(WorldEditor editor, Random rand, Theme theme, IBounded box) {
    box.getShape(Shape.RECTSOLID).fill(editor, theme.getPrimary().getWall());
  }
}
