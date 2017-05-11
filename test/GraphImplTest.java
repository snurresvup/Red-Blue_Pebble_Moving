import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

public class GraphImplTest {
  private GraphImpl graph;

  private Vertex vertex1;
  private Vertex baseVertex;

  @Before
  public void setup(){
    graph = new GraphImpl(new Pebble(PebbleColor.BLUE));
    vertex1 = new StartVertex(new Pebble(PebbleColor.RED));
    baseVertex = new TargetVertex();
    graph.addVertex(baseVertex);
    graph.addVertex(vertex1);
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
