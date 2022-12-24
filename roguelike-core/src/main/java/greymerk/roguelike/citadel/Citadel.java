package greymerk.roguelike.citadel;

import java.util.Random;

import greymerk.roguelike.theme.Themes;
import greymerk.roguelike.util.mst.MinimumSpanningTree;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;

public class Citadel {

  public static final int EDGE_LENGTH = 17;

  public static void generate(WorldEditor editor, int x, int z) {

    Random rand = editor.getRandom();

    MinimumSpanningTree mst = new MinimumSpanningTree(rand, 7, EDGE_LENGTH);
    //mst.generate(world, rand, new MetaBlock(Blocks.glowstone), new Coord(x, 100, z));

    CityGrounds.generate(editor, mst, Themes.OAK.getThemeBase(), new Coord(x, 50, z));
  }

}
