import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GraphGenerator {
  public static Graph generateRandomGraph(int numberOfPebbles) {
    Graph res = new Graph();

    Random r = new Random();

    Set<Vertex> startVertices = new HashSet<>();
    Set<Vertex> targetVertices = new HashSet<>();


    for (int i = 0; i < numberOfPebbles; i++) {
      Vertex start = new Vertex();
      Vertex target = new Vertex();
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
              vert.addEdge(v);
            }
          });
    }

    return res;
  }

  public static Graph generateCompletelyConnectedGraph(int numberOfPebbles){
    Graph res = new Graph();
    Set<Vertex> vertices = new HashSet<>();
    for (int i = 0; i < numberOfPebbles; i++) {
      Vertex st = new Vertex();
      Vertex tg = new Vertex();
      res.addVertex(st);
      res.addVertex(tg);
      vertices.add(st); vertices.add(tg);
    }
    for(Vertex v: vertices){
      vertices.stream().forEach(vertex -> v.addEdge(vertex));
    }
    return res;
  }

  public static void main(String[] args) {
    Graph g = generateRandomGraph(2);
    g = generateCompletelyConnectedGraph(3);
    g.show();
  }
}
