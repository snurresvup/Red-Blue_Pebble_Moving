import com.sun.org.apache.regexp.internal.RE;
import javafx.util.Pair;
import scala.Int;
import scala.util.regexp.Base;

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
      case MINIMUM:
        constructMinimumSpanningTreePrim(origin);
        break;
      case BFS:
        constructBFSSpanningTree(origin.getFirstVertex());
        break;
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
      leafs.add(currentSTV);

      if(parent != null) {
        edges.add(new Edge<>(currentSTV, parent));
        if(parent.getModelee().getEdges().size() > 1) leafs.remove(parent);
      }

      for(Vertex v : current.getEdges().keySet()){
        if(parent != null && v.equals(parent.getModelee())) continue;
        if(vertices.contains(new SpanningTreeVertex(v))) continue;
        queue.add(new Pair<>(v, currentSTV));
      }

    }
  }

  private void constructMinimumSpanningTreePrim(Graph origin) {
    leafs = new HashSet<>();
    vertices = new HashSet<>();
    edges = new HashSet<>();

    Map<SpanningTreeVertex, Integer> c = new HashMap<>();
    Set<SpanningTreeVertex> q = new HashSet<>();
    Map<SpanningTreeVertex, Edge<SpanningTreeVertex>> e = new HashMap<>();

    for(Vertex vertex : origin.getVertices()){
      SpanningTreeVertex sV = new SpanningTreeVertex(vertex);
      c.put(sV, Integer.MAX_VALUE);
      q.add(sV);
    }

    while(!q.isEmpty()) {
      SpanningTreeVertex v = null;
      for (SpanningTreeVertex vertex : q) {
        if (v == null || c.get(vertex) < c.get(v)) v = vertex;
      }
      q.remove(v);
      vertices.add(v);
      leafs.add(v);
      if (e.get(v) != null) {
        edges.add(e.get(v));

        SpanningTreeVertex parent = e.get(v).getOther(v);
        boolean parentIsLeaf = edges.stream().filter(edge -> edge.contains(parent)).count() <= 1;
        if(!parentIsLeaf) {
          leafs.remove(parent);
        }
      }

      for (Map.Entry<Vertex, Integer> w : v.getModelee().getEdges().entrySet()) {
        SpanningTreeVertex sTVW = new SpanningTreeVertex(w.getKey());
        Edge<SpanningTreeVertex> vw = new Edge<>(v, sTVW);
        if (q.contains(sTVW) && c.get(sTVW) > w.getValue()) {
          c.put(sTVW, w.getValue());
          e.put(sTVW, vw);
        }
      }
    }
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
    if(vertex.getModelee().getPebble(PebbleColor.BLUE) != null && leafs.size() > 1) throw new IllegalArgumentException("Please panic, you have removed the blue pebble");

    if(!(neighborhood.size() <= 1)){
      throw new IllegalArgumentException("Vertex is not a leaf, and cannot be removed. Leafs: " + leafs + " removed leaf: " + vertex.getModelee());
    }
    edges.removeAll(neighborhood);
    vertices.remove(vertex);
    leafs.remove(vertex);

    Iterator<Edge<SpanningTreeVertex>> iter = neighborhood.iterator();
    if(!iter.hasNext()) return;
    Edge<SpanningTreeVertex> danglingEdge = iter.next();
    SpanningTreeVertex newLeaf = danglingEdge.getOther(vertex);
    long nEdges = edges.stream().filter(e -> e.contains(newLeaf)).count();
    if(nEdges == 1) leafs.add(newLeaf);
  }

  public Set<Edge<SpanningTreeVertex>> getNeighborhood(SpanningTreeVertex vertex) {
    return edges.stream().filter(
        edge -> edge.getA().equals(vertex)
             || edge.getB().equals(vertex)
    ).collect(Collectors.toSet());
  }

  public SpanningTreeVertex getLeaf(){
    return leafs.iterator().next();
  }

  public SpanningTreeVertex getPrioritizedLeaf() {
    SpanningTreeVertex tEmpty = null;
    int tEmptyWeight = 0;
    SpanningTreeVertex sRed = null;
    int sRedWeight = 0;

    for(SpanningTreeVertex stv : leafs){
      if(stv.getModelee() instanceof TargetVertex && stv.getModelee().getPebble(PebbleColor.RED) != null) return stv;
      if(stv.getModelee() instanceof StartVertex && stv.getModelee().getPebble(PebbleColor.RED) == null) return stv;

      Edge<SpanningTreeVertex> edge = getEdgeToLeaf(stv);
      if(stv.getModelee() instanceof TargetVertex){
        if(tEmpty == null || getWeightOfEdge(edge) < tEmptyWeight) {
          tEmpty = stv;
          tEmptyWeight = getWeightOfEdge(edge);
        }
      } else {
        if(sRed == null || getWeightOfEdge(edge) < sRedWeight) {
          sRed = stv;
          sRedWeight = getWeightOfEdge(edge);
        }
      }
    }

    return tEmpty == null ? sRed : tEmpty;
  }

  private int getWeightOfEdge(Edge<SpanningTreeVertex> edge){
    return edge.getA().getModelee().getEdges().get(edge.getB().getModelee());
  }

  public Edge<SpanningTreeVertex> getEdgeToLeaf(SpanningTreeVertex leaf){
    return edges.stream().filter(edge -> edge.contains(leaf)).findAny().orElse(null);
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
    PriorityQueue<Pair<SpanningTreeVertex, Integer>> queue = new PriorityQueue<>(10, new Comparator<Pair<SpanningTreeVertex, Integer>>() {
      @Override
      public int compare(Pair<SpanningTreeVertex, Integer> o1, Pair<SpanningTreeVertex, Integer> o2) {
        return o1.getValue() - o2.getValue();
      }
    });
    queue.add(new Pair<>(leaf,0));

    while(!queue.isEmpty()){
      Pair<SpanningTreeVertex, Integer> current = queue.remove();
      if(predicate.test(current.getKey())) return current.getKey();

      Set<Edge<SpanningTreeVertex>> neighborhood = new HashSet<>();//getNeighborhood(current.getKey());
      for(Map.Entry<Vertex, Integer> entry : current.getKey().getModelee().getEdges().entrySet()){
        neighborhood.add(new Edge<>(current.getKey(), new SpanningTreeVertex(entry.getKey())));
      }
      Iterator<Edge<SpanningTreeVertex>> iterator = neighborhood.iterator();

      while (iterator.hasNext()){
        Edge<SpanningTreeVertex> edge = iterator.next();
        if(!vertices.contains(edge.getOther(current.getKey()))) continue;
        Pair<SpanningTreeVertex, Integer> p = queue.stream().filter(pair -> pair.getKey().equals(edge.getOther(current.getKey()))).findAny().orElse(null);
        int potentialValue = current.getKey().getModelee().getEdges().get(edge.getOther(current.getKey()).getModelee()) + current.getValue();
        if(p == null || p.getValue() > potentialValue){
          if(p != null) {
            queue.remove(p);
          }
          queue.add(new Pair<>(edge.getOther(current.getKey()), potentialValue));
        }
      }
    }

    return null;
  }

  public LinkedList<SpanningTreeVertex> getPath(SpanningTreeVertex from, SpanningTreeVertex to, LinkedList<SpanningTreeVertex> accum) {
    if(accum == null) accum = new LinkedList<>();
    if(from.equals(to)) {
      accum.add(to);
      return accum;
    }

    Set<Edge<SpanningTreeVertex>> neighborhood = getNeighborhood(from);

    for(Edge<SpanningTreeVertex> edge : neighborhood){
      if(!accum.isEmpty() && accum.getLast() != null && accum.getLast().equals(edge.getOther(from))) continue;

      LinkedList<SpanningTreeVertex> nextAccum = new LinkedList<>(accum);
      nextAccum.add(from);
      LinkedList<SpanningTreeVertex> res = getPath(edge.getOther(from), to, nextAccum);
      if(res == null) continue;

      return res;
    }

    return null;
  }

  public boolean noVertexNeedsWork() {
    for(SpanningTreeVertex spv : vertices){
      if ((spv.getModelee() instanceof TargetVertex && spv.getModelee().getPebble(PebbleColor.RED) == null)
          || (spv.getModelee() instanceof StartVertex && spv.getModelee().getPebble(PebbleColor.RED) != null))
        return false;
    }
    return true;
  }

  public enum SpanningTreeType {
    MINIMUM, BFS, RANDOM
  }
}
