package greymerk.roguelike.util.mst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import greymerk.roguelike.util.graph.Edge;
import greymerk.roguelike.util.graph.Graph;
import greymerk.roguelike.worldgen.BlockBrush;
import greymerk.roguelike.worldgen.Coord;
import greymerk.roguelike.worldgen.WorldEditor;
import greymerk.roguelike.worldgen.shapes.RectHollow;

public class MinimumSpanningTree {

  List<MSTPoint> points = new ArrayList<>();
  Set<Edge<MSTPoint>> mstEdges = new HashSet<>();

  public MinimumSpanningTree(Random rand, int size, int edgeLength) {
    this(rand, size, edgeLength, new Coord(0, 0, 0));
  }

  public MinimumSpanningTree(Random rand, int size, int edgeLength, Coord origin) {

    generatePoints(rand, size, edgeLength, origin);

    ArrayList<Edge<MSTPoint>> edges = generateAllPossibleEdges();

    Collections.sort(edges);

    for (Edge<MSTPoint> edge : edges) {
      MSTPoint start = edge.getStart();
      MSTPoint end = edge.getEnd();

      if (find(start) == find(end)) {
        continue;
      }
      union(start, end);
      mstEdges.add(edge);
    }

  }

  private void generatePoints(Random rand, int size, int edgeLength, Coord origin) {
    int offset = size / 2 * edgeLength;

    for (int i = 0; i < size; ++i) {

      Coord temp = origin.copy();
      temp.north(offset);
      temp.west(offset);
      temp.south(edgeLength * i);

      for (int j = 0; j < size; ++j) {
        points.add(new MSTPoint(temp.copy(), rand));
        temp.east(edgeLength);
      }
    }
  }

  private ArrayList<Edge<MSTPoint>> generateAllPossibleEdges() {
    ArrayList<Edge<MSTPoint>> edges = new ArrayList<>();
    for (MSTPoint p : points) {
      for (MSTPoint o : points) {
        if (p.equals(o)) {
          continue;
        }
        edges.add(new Edge<>(p, o, p.distance(o)));
      }
    }
    return edges;
  }


  private void union(MSTPoint a, MSTPoint b) {
    MSTPoint root1 = find(a);
    MSTPoint root2 = find(b);
    if (root1 == root2) {
      return;
    }

    if (root1.getRank() > root2.getRank()) {
      root2.setParent(root1);
    } else {
      root1.setParent(root2);
      if (root1.getRank() == root2.getRank()) {
        root2.incRank();
      }
    }
  }

  private MSTPoint find(MSTPoint p) {
    if (p.getParent() == p) {
      return p;
    }
    p.setParent(find(p.getParent()));
    return p.getParent();
  }

  public void generate(WorldEditor editor, BlockBrush blocks, Coord pos) {

    for (Edge<MSTPoint> e : this.mstEdges) {

      Coord start = e.getStart().getPosition();
      start.translate(pos);
      Coord end = e.getEnd().getPosition();
      end.translate(pos);

      RectHollow.newRect(start, end).fill(editor, blocks);
    }
  }

  public List<Edge<MSTPoint>> getEdges() {
    return new ArrayList<>(this.mstEdges);
  }

  public Graph<Coord> getGraph() {
    Graph<Coord> layout = new Graph<>();
    for (Edge<MSTPoint> e : this.mstEdges) {
      Coord start = e.getStart().getPosition();
      Coord end = e.getEnd().getPosition();
      layout.addEdge(new Edge<>(start, end, start.distance(end)));
    }

    return layout;
  }
}
