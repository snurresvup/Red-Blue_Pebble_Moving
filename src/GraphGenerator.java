import java.util.*;

public class GraphGenerator {
  public static Graph generateRandomGraph(int numberOfPebbles) {
    Graph res = new Graph();

    Random r = new Random();

    ArrayList<Pebble> pebbles = new ArrayList<>();
    for (int i = 0; i < numberOfPebbles; i++) {
      pebbles.add(new Pebble(PebbleColor.RED));
    }

    Set<Vertex> startVertices = new HashSet<>();
    Set<Vertex> targetVertices = new HashSet<>();


    for (int i = 0; i < numberOfPebbles; i++) {
      Vertex start = new StartVertex(pebbles.get(i));
      Vertex target = new TargetVertex();
      startVertices.add(start);
      targetVertices.add(target);
      res.addVertex(start);
      res.addVertex(target);
    }

    Set<Vertex> allVertices = new HashSet<>(startVertices);
    allVertices.addAll(targetVertices);

    for (Vertex v : allVertices) {
      allVertices.stream().forEach(
          vert -> {
            if (r.nextBoolean()) {
              vert.addEdge(v, r.nextInt(10));
            }
          });
    }

    return res;
  }

  public static Graph generateCompletelyConnectedGraph(int numberOfPebbles){
    Graph res = new Graph();
    ArrayList<Pebble> pebbles = new ArrayList<>();
    for (int i = 0; i < numberOfPebbles; i++) {
      pebbles.add(new Pebble(PebbleColor.RED));
    }
    Set<Vertex> vertices = new HashSet<>();

    for (int i = 0; i < numberOfPebbles; i++) {
      Vertex st = new StartVertex(pebbles.get(i));
      Vertex tg = new TargetVertex();
      res.addVertex(st);
      res.addVertex(tg);
      vertices.add(st); vertices.add(tg);
    }
    for(Vertex v: vertices){
      vertices.stream().forEach(vertex -> v.addEdge(vertex, 1));
    }
    return res;
  }

  public static void main(String[] args) {
    Graph g = generateRandomGraph(2);
    g = generateCompletelyConnectedGraph(3);
    g.show();
  }
}
