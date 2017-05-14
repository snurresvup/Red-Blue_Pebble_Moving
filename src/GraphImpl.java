import org.graphstream.graph.implementations.SingleGraph;

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
    org.graphstream.graph.Graph vGraph = new SingleGraph("The graph");
    vGraph.setStrict(false);
    vGraph.setAutoCreate(true);

    Set<Edge> addedEdges = new HashSet<>();
    for(Vertex v : vertices){
      v.getEdges().keySet().forEach(vert -> {
        Edge newEdge = new Edge(v, vert);
        addedEdges.add(newEdge);
      });
    }

    addedEdges.forEach(edge -> vGraph.addEdge(edge.toString(), edge.getA().toString(), edge.getB().toString()));

    vGraph.display();
  }

  public Pebble getBluePebble() {
    return bluePebble;
  }

  @Override
  public Set<Vertex> getVertices() {
    return vertices;
  }
}
