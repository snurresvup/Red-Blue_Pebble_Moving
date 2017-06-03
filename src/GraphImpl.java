import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.SingleNode;
import org.graphstream.ui.graphicGraph.GraphicNode;

import java.util.*;

public class GraphImpl implements Graph {
  private Set<Vertex> vertices;
  private Pebble bluePebble;

  public GraphImpl(Pebble bluePebble){
    this.bluePebble = bluePebble;
    vertices = new HashSet<>();
  }

  public Vertex getFirstVertex() {
    return vertices.stream().findFirst().orElse(null);
  }

  public void addVertex(Vertex vertex) {
    vertices.add(vertex);
  }

  public void addVertex(Vertex addedVertex, Map<Vertex,Integer> connections) {
    for(Map.Entry<Vertex,Integer> connection : connections.entrySet()){
      if(vertices.contains(connection.getKey())) addedVertex.addEdge(connection);
    }
    vertices.add(addedVertex);
  }

  /**
   * Adds a connection between vertexA and vertexB
   * The method only adds the connection if both of the vertices is contained in the graph
   * @param vertexA one endpoint of the connection
   * @param vertexB other endpoint of the connection
   * @return A boolean value indicating whether the connection was added.
   */
  public boolean addConnection(Vertex vertexA, Vertex vertexB, int weight) {
    if(!vertices.contains(vertexA) || !vertices.contains(vertexB)) return false;
    if(vertexA.hasEdgeTo(vertexB)) return false;
    if(vertexB.hasEdgeTo(vertexA)) return false;
    vertexA.addEdge(vertexB, weight);
    return true;
  }

  public void show(){
    System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    org.graphstream.graph.Graph vGraph = new SingleGraph("The graph");
    vGraph.setStrict(false);
    vGraph.setAutoCreate(true);

    String stylesheet =
            "edge {" +
                "text-alignment: above;" +
                "text-style: bold;" +
                "text-size: 16;" +
                "text-background-color: #666;" +
            "}" +
            "node { " +
                "text-style: bold;" +
                "text-size: 16;" +
                "text-background-color: #666;" +
                "size: 50px, 50px;" +
                "fill-mode: plain;" +
                "stroke-mode : plain; " +
                "stroke-width : 20px;" +
                "fill-color: #FFF;" +
            "}" +
            "node.blue { fill-color: blue; }" +
            "node.red { fill-color: red; }" +
            "node.start { stroke-color : #0F0; }" +
            "node.target { stroke-color : #F00; }";

    vGraph.addAttribute("ui.stylesheet", stylesheet);

    for(Vertex v : vertices){
      Node n = vGraph.addNode(v.toString());
      n.addAttribute("ui.label", v.toString());

      if(v instanceof TargetVertex){
        if(v.getPebble(PebbleColor.RED) != null) n.addAttribute("ui.class", "target, red");
        else n.addAttribute("ui.class", "target");
      }

      if(v instanceof StartVertex){
        if(v.getPebble(PebbleColor.RED) != null) {
          n.addAttribute("ui.class", "start, red");
          if(v.getPebble(PebbleColor.BLUE) != null) n.addAttribute("ui.class", "start, blue");
        }
        else n.addAttribute("ui.class", "start");
      }
    }

    Set<Edge<Vertex>> addedEdges = new HashSet<>();
    for(Vertex v : vertices){
      v.getEdges().keySet().forEach(vert -> {
        Edge<Vertex> newEdge = new Edge(v, vert);
        addedEdges.add(newEdge);
      });
    }

    addedEdges.forEach(edge -> {
      org.graphstream.graph.Edge theEdge = vGraph.addEdge(edge.toString(), edge.getA().toString(), edge.getB().toString());
      theEdge.addAttribute("ui.label", edge.getA().getEdges().get(edge.getB()));
    });

    vGraph.display();
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public Pebble getBluePebble() {
    return bluePebble;
  }

  @Override
  public Set<Vertex> getVertices() {
    return vertices;
  }
}
