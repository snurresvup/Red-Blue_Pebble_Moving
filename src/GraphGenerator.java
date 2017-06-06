import java.util.*;

public class GraphGenerator {
  public static GraphImpl generateRandomGraph(int numberOfPebbles, double connectionChance) {
    Pebble bluePebble = new Pebble(PebbleColor.BLUE);
    GraphImpl res = new GraphImpl(bluePebble);

    Random r = new Random();//15467

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

    startVertices.stream().findAny().orElse(null).addPebble(bluePebble);

    Set<Vertex> connectedComponent = new HashSet<>();

    for (Vertex v : allVertices) {
      connectVertexToComponent(v, connectedComponent, connectionChance, r);
    }

    return res;
  }

  private static void connectVertexToComponent(Vertex v, Set<Vertex> connectedComponent, double connectionChance, Random r) {
    boolean done = false;
    while(!done) {
      if(connectedComponent.size() == 0) {
        connectedComponent.add(v);
        done = true;
      }
      for (Vertex vertex : connectedComponent) {
        if (r.nextDouble() <= connectionChance) {
          vertex.addEdge(v, r.nextInt(10) + 1);
          done = true;
        }
      }
    }
    connectedComponent.add(v);
  }

  public static GraphImpl generateCompletelyConnectedGraph(int numberOfPebbles){
    Pebble bluePebble = new Pebble(PebbleColor.BLUE);
    GraphImpl res = new GraphImpl(bluePebble);
    ArrayList<Pebble> pebbles = new ArrayList<>();
    for (int i = 0; i < numberOfPebbles; i++) {
      pebbles.add(new Pebble(PebbleColor.RED));
    }
    Set<Vertex> vertices = new HashSet<>();

    for (int i = 0; i < numberOfPebbles; i++) {
      Vertex st = new StartVertex(pebbles.get(i), i == 0 );
      if(i == 0) st.addPebble(bluePebble);
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

    Pebble bluePebble = new Pebble(PebbleColor.BLUE);
    PathGraph res = new PathGraph(bluePebble);

    LinkedList<Vertex> vertices = new LinkedList<>();
    Vertex v = new StartVertex(new Pebble(PebbleColor.RED), true);
    v.addPebble(bluePebble);
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

  public static PathGraph generateSimplePathGraph(int numberOfPebbles){
    Pebble bluePebble = new Pebble(PebbleColor.BLUE);
    PathGraph res = new PathGraph(bluePebble);

    Vertex prev = new StartVertex(new Pebble(PebbleColor.RED), true);
    prev.addPebble(bluePebble);
    res.addVertex(prev);

    Vertex current;

    for (int i = 0; i < numberOfPebbles - 1; i++) {
      current = new StartVertex(new Pebble(PebbleColor.RED));
      current.addEdge(prev, 1);
      res.addVertex(current);
      prev = current;
    }
    for (int i = 0; i < numberOfPebbles; i++) {
      current = new TargetVertex();
      current.addEdge(prev, 1);
      res.addVertex(current);
      prev = current;
    }

    return res;
  }

  public static void main(String[] args) {
    GraphImpl g = generateRandomGraph(4, 0.3);

    //g.show();
    GraphUtil.outputArrayRepresentationToFile(g,0, 2,"JSON_data");
    g=generateRandomGraph(4,0.3);
    GraphUtil.outputArrayRepresentationToFile(g,1, 2, "JSON_data");
    g=generateRandomGraph(4,0.3);
    GraphUtil.outputArrayRepresentationToFile(g,2, 2, "JSON_data");

    //g.computeAPSPForGraph();

    //APSP.APSPInfo info = g.getGSGraph().getNode(g.getFirstVertex().toString()).getAttribute(APSP.APSPInfo.ATTRIBUTE_NAME);
    //Vertex v = g.getVertices().stream().filter(vert -> !(vert instanceof StartVertex)).findAny().orElse(null);
    //System.out.println(info.getShortestPathTo(v.toString()));
    //System.out.println(info.getLengthTo(v.toString()));
    //g = generateRandomPathGraph(4);
    //g = generateSimplePathGraph(4);
    //g.show();
  }
}
