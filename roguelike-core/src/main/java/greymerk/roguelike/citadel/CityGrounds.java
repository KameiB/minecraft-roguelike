package greymerk.roguelike.citadel;

import com.github.srwaggon.roguelike.worldgen.SingleBlockBrush;

import java.util.List;
import java.util.Random;

import greymerk.roguelike.dungeon.towers.ITower;
import greymerk.roguelike.dungeon.towers.Tower;
import greymerk.roguelike.theme.Theme;
import greymerk.roguelike.theme.ThemeBase;
import greymerk.roguelike.util.graph.Edge;
import greymerk.roguelike.util.graph.Graph;
import greymerk.roguelike.util.mst.MSTPoint;
import greymerk.roguelike.util.mst.MinimumSpanningTree;
import greymerk.roguelike.worldgen.Cardinal;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectSolid;

public class CityGrounds {

  public static void generate(WorldEditor editor, MinimumSpanningTree mst, ThemeBase theme, Coord pos) {

    Coord start;
    Coord end;

    start = new Coord(pos);
    start.translate(new Coord(Citadel.EDGE_LENGTH * -3, 10, Citadel.EDGE_LENGTH * -3));
    end = new Coord(pos);
    end.translate(new Coord(Citadel.EDGE_LENGTH * 3, 40, Citadel.EDGE_LENGTH * 3));
    RectSolid.newRect(start, end).fill(editor, SingleBlockBrush.AIR, true, true);

    start = new Coord(pos);
    start.translate(new Coord(Citadel.EDGE_LENGTH * -3, 10, Citadel.EDGE_LENGTH * -3));
    end = new Coord(pos);
    end.translate(new Coord(Citadel.EDGE_LENGTH * 3, 20, Citadel.EDGE_LENGTH * 3));
    RectSolid.newRect(start, end).fill(editor, theme.getPrimary().getWall(), true, true);

    start = new Coord(pos);
    start.translate(new Coord(Citadel.EDGE_LENGTH * -2, 20, Citadel.EDGE_LENGTH * -2));
    end = new Coord(pos);
    end.translate(new Coord(Citadel.EDGE_LENGTH * 2, 30, Citadel.EDGE_LENGTH * 2));
    RectSolid.newRect(start, end).fill(editor, theme.getPrimary().getWall(), true, true);

    start = new Coord(pos);
    start.translate(new Coord(Citadel.EDGE_LENGTH * -1, 30, Citadel.EDGE_LENGTH * -1));
    end = new Coord(pos);
    end.translate(new Coord(Citadel.EDGE_LENGTH, 40, Citadel.EDGE_LENGTH));
    RectSolid.newRect(start, end).fill(editor, theme.getPrimary().getWall(), true, true);

    Coord cursor = new Coord(pos);
    cursor.translate(Cardinal.UP, 20);

    for (Edge<MSTPoint> e : mst.getEdges()) {
      start = e.getStart().getPosition();
      start.translate(cursor);
      end = e.getEnd().getPosition();
      end.translate(cursor);
      end.translate(Cardinal.DOWN, 20);
      RectSolid.newRect(start, end).fill(editor, theme.getPrimary().getWall(), true, true);
    }

    Graph<Coord> layout = mst.getGraph();
    List<Coord> towers = layout.getPoints();
    for (Coord c : towers) {
      c.translate(pos);
      Random rand = Citadel.getRandom(editor, c.getX(), c.getZ());
      ITower tower = Tower.get(Tower.values()[rand.nextInt(Tower.values().length)]);
      tower.generate(editor, rand, Theme.values()[rand.nextInt(Theme.values().length)].getThemeBase(), new Coord(c.getX(), 50, c.getZ()));
    }

  }


}
