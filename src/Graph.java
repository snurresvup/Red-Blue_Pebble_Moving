import java.util.Map;
import java.util.Set;

public interface Graph {
  Vertex getFirstVertex();

  void addVertex(Vertex vertex);

  void addVertex(Vertex addedVertex, Map<Vertex,Integer> connections);

  /**
   * Adds a connection between vertexA and vertexB
   * The method only adds the connection if both of the vertices is contained in the graph
   * @param vertexA one endpoint of the connection
   * @param vertexB other endpoint of the connection
   * @return A boolean value indicating whether the connection was added.
   */
  boolean addConnection(Vertex vertexA, Vertex vertexB, int weight);

  void show();

  Pebble getBluePebble();

  Set<Vertex> getVertices();
}
