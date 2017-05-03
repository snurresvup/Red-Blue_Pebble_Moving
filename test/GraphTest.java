import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
  private Graph graph;

  private Vertex vertex1;
  private Vertex baseVertex;

  @BeforeEach
  public void setup(){
    graph = new Graph();
    vertex1 = new StartVertex(new Pebble(PebbleColor.RED));
    baseVertex = new TargetVertex();
    graph.addVertex(baseVertex);
  }

  @Test
  public void shouldBeAbleToAddVertices(){
    assertNotNull(graph.getFirstVertex());
  }

  @Test
  public void shouldBeAbleToAddVertexWithConnection(){
    HashMap<Vertex,Integer> connection = new HashMap<>();
    connection.put(baseVertex, 1);
    graph.addVertex(vertex1, connection);
  }

  @Test
  public void shouldBeAbleToAddConnection(){
    graph.addConnection(baseVertex, vertex1, 1);
    assertNotNull(graph.getFirstVertex().getEdges().keySet().stream().findAny().orElse(null));
  }
}
