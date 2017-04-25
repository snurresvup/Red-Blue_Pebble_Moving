import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
  private Graph graph;

  private Vertex vertex1;
  private Vertex baseVertex;

  @BeforeEach
  public void setup(){
    graph = new Graph();
    vertex1 = new Vertex();
    baseVertex = new Vertex();
    graph.addVertex(baseVertex);
  }

  @Test
  public void shouldBeAbleToAddVertices(){
    assertNotNull(graph.getFirstVertex());
  }

  @Test
  public void shouldBeAbleToAddVertexWithConnection(){
    HashSet<Vertex> connection = new HashSet<>();
    connection.add(baseVertex);
    graph.addVertex(vertex1, connection);
  }

  @Test
  public void shouldBeAbleToAddConnection(){
    graph.addConnection(baseVertex, vertex1);
    assertNotNull(graph.getFirstVertex().getEdges().stream().findAny().orElse(null));
  }
}
