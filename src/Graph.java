import javafx.util.Pair;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;

public class Graph{
  private Set<Vertex> vertices;

  public Graph(){
    vertices = new HashSet<>();
  }

  public Vertex getFirstVertex() {
    return vertices.stream().findFirst().orElse(null);
  }

  public void addVertex(Vertex vertex) {
    vertices.add(vertex);
  }

  public void addVertex(Vertex addedVertex, Set<Vertex> connections) {
    for(Vertex connection : connections){
      if(vertices.contains(connection)) addedVertex.addEdge(connection);
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
  public boolean addConnection(Vertex vertexA, Vertex vertexB) {
    if(!vertices.contains(vertexA) || !vertices.contains(vertexB)) return false;
    if(vertexA.hasEdgeTo(vertexB)) return false;
    if(vertexB.hasEdgeTo(vertexA)) return false;
    vertexA.addEdge(vertexB);
    return true;
  }

  public void show(){
    org.graphstream.graph.Graph vGraph = new SingleGraph("The graph");
    vGraph.setStrict(false);
    vGraph.setAutoCreate(true);

    Set<Edge> addedEdges = new HashSet<>();
    for(Vertex v : vertices){
      v.getEdges().stream().forEach(vert -> {
        Edge newEdge = new Edge(v, vert);
        addedEdges.add(newEdge);
      });
    }

    addedEdges.forEach(edge -> vGraph.addEdge(edge.toString(), edge.getA().toString(), edge.getB().toString()));

    vGraph.display();
  }

}
