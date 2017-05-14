import javafx.util.Pair;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

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

      boolean isLeaf = true;
      for(Vertex v : current.getEdges().keySet()){
        if(parent != null && v.equals(parent.getModelee())) continue;
        queue.add(new Pair<>(v, currentSTV));
        isLeaf = false;
      }
      if(isLeaf) leafs.add(currentSTV);
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

  public void removeLeaf(SpanningTreeVertex vertex){
    Set<Edge<SpanningTreeVertex>> neighborhood = getNeighborhood(vertex);

    if(!(neighborhood.size() <= 1)) throw new IllegalArgumentException("Vertex is not a leaf, and cannot be removed");
    edges.removeAll(neighborhood);
    vertices.remove(vertex);
    leafs.remove(vertex);

    Iterator<Edge<SpanningTreeVertex>> iter = neighborhood.iterator();
    if(!iter.hasNext()) return;
    Edge<SpanningTreeVertex> danglingEdge = iter.next();
    SpanningTreeVertex newLeaf = danglingEdge.getA().equals(vertex) ? danglingEdge.getB() : danglingEdge.getA();
    leafs.add(newLeaf);
  }

  private Set<Edge<SpanningTreeVertex>> getNeighborhood(SpanningTreeVertex vertex) {
    return edges.stream().filter(
        edge -> edge.getA().equals(vertex)
             || edge.getB().equals(vertex)
    ).collect(Collectors.toSet());
  }

  public SpanningTreeVertex getLeaf(){
    return leafs.iterator().next();
  }

  public SpanningTreeVertex findClosestPebble(SpanningTreeVertex leaf) {
    return findClosest(leaf, v -> v.getModelee().getPebble(PebbleColor.RED) != null);
  }

  public boolean isEmpty() {
    return vertices.isEmpty();
  }

  public SpanningTreeVertex findClosestEmptyVertex(SpanningTreeVertex leaf) {
    return findClosest(leaf, v-> v.getModelee().getPebble(PebbleColor.RED) == null);
  }

  public SpanningTreeVertex findClosest(SpanningTreeVertex leaf, Predicate<SpanningTreeVertex> predicate){
    Queue<SpanningTreeVertex> queue = new LinkedList<>();
    queue.add(leaf);

    while(!queue.isEmpty()){
      SpanningTreeVertex current = queue.remove();
      if(predicate.test(current)) return current;
      Set<Edge<SpanningTreeVertex>> neighborhood = getNeighborhood(current);
      Iterator<Edge<SpanningTreeVertex>> iterator = neighborhood.iterator();
      while (iterator.hasNext()){
        Edge<SpanningTreeVertex> edge = iterator.next();
        queue.add(edge.getA().equals(current) ? edge.getB() : edge.getB());
      }
    }

    return null;
  }

  public LinkedList<SpanningTreeVertex> getPath(SpanningTreeVertex from, SpanningTreeVertex to, LinkedList<SpanningTreeVertex> accum) {
    if(accum == null) accum = new LinkedList<>();
    if(from.equals(to)) return accum;

    Set<Edge<SpanningTreeVertex>> neighborhood = getNeighborhood(from);

    for(Edge<SpanningTreeVertex> edge : neighborhood){
      SpanningTreeVertex other = edge.getA().equals(from) ? edge.getB() : edge.getA();

      if(accum.getLast() != null && accum.getLast().equals(other)) continue;

      LinkedList<SpanningTreeVertex> nextAccum = new LinkedList<>(accum);
      nextAccum.add(from);
      LinkedList<SpanningTreeVertex> res = getPath(other, to, nextAccum);
      if(res == null) continue;

      return res;
    }

    return null;
  }

  public enum SpanningTreeType {
    MINIMUM, BFS, RANDOM
  }
}
