import java.util.*;

public class GraphGenerator {
  public static Graph generateRandomGraph(int numberOfPebbles) {
    Graph res = new Graph(new Pebble(PebbleColor.BLUE));

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
    Graph res = new Graph(new Pebble(PebbleColor.BLUE));
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

  public static PathGraph generateRandomPathGraph(int numberOfPebbles){
    if(numberOfPebbles < 1) throw new IllegalArgumentException("The number of pebbles must be greater than 1 for a path graph");

    PathGraph res = new PathGraph(new Pebble(PebbleColor.BLUE));

    LinkedList<Vertex> vertices = new LinkedList<>();
    Vertex v = new StartVertex(new Pebble(PebbleColor.RED), true);
    v.addPebble(new Pebble(PebbleColor.BLUE));
    vertices.add(v);
    v = new TargetVertex();
    vertices.add(v);

    for (int i = 0; i < numberOfPebbles - 1; i++) {
      Pebble pebble = new Pebble(PebbleColor.RED);
      Vertex st = new StartVertex(pebble);
      Vertex tg = new TargetVertex();
      vertices.add(st);
      vertices.add(tg);
    }

    Collections.shuffle(vertices);

    Iterator<Vertex> iter = vertices.iterator();

    Vertex prev = iter.next();
    Vertex current;
    res.addVertex(prev);
    while(iter.hasNext()){
      current = iter.next();
      prev.addEdge(current, 1);
      res.addVertex(current);
      prev = current;
    }

    return res;
  }

  public static void main(String[] args) {
    Graph g = generateRandomGraph(3);
    //g = generateCompletelyConnectedGraph(3);
    //g = generateRandomPathGraph(4);
    g.show();
  }
}
