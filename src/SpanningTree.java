import javafx.util.Pair;

import java.util.*;

public class SpanningTree {
  private Set<SpanningTreeVertex> leafs;
  private Set<SpanningTreeVertex> vertices;
  private Set<Edge<SpanningTreeVertex>> edges;
  private Graph origin;

  public SpanningTree(Graph origin, SpanningTreeType type){
    this.origin = origin;

    switch (type){
      case MINIMUM: constructMinimumSpanningTreePrim(origin);
      case BFS: constructBFSSpanningTree(origin.getFirstVertex());
    }
  }

  private void constructBFSSpanningTree(Vertex root) {
    leafs = new HashSet<>();
    vertices = new HashSet<>();
    edges = new HashSet<>();

    buildBFSSpanningTree(root);
  }

  private void buildBFSSpanningTree(Vertex root) {
    Queue<Pair<Vertex, SpanningTreeVertex>> queue = new LinkedList<>();
    queue.add(new Pair<>(root, null));

    while (!queue.isEmpty()){
      Pair<Vertex, SpanningTreeVertex> element = queue.remove();
      Vertex current = element.getKey();
      SpanningTreeVertex parent = element.getValue();

      SpanningTreeVertex currentSTV = new SpanningTreeVertex(current);
      if(vertices.contains(currentSTV)) continue;
      vertices.add(currentSTV);

      if(parent != null) edges.add(new Edge<>(currentSTV, parent));

      for(Vertex v : current.getEdges().keySet()){
        if(parent != null && v.equals(parent.getModelee())) continue;
        queue.add(new Pair<>(v, currentSTV));
      }
    }
  }

  private void constructMinimumSpanningTreePrim(Graph origin) {
    Vertex root = origin.getFirstVertex();

  }

  public Graph getOrigin() {
    return origin;
  }

  public SpanningTreeVertex getFirstVertex() {
    return vertices.stream().findFirst().orElse(null);
  }

  public Set<SpanningTreeVertex> getVertices() {
    return vertices;
  }

  public Set<Edge<SpanningTreeVertex>> getEdges() {
    return edges;
  }

  public enum SpanningTreeType {
    MINIMUM, BFS, RANDOM
  }
}
