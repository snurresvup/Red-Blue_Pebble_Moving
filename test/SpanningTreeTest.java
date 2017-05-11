import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpanningTreeTest {
  private Graph originalGraph;
  private PathGraph originalPathGraph;
  private Vertex root;

  @Before
  public void setup(){
    originalGraph = GraphGenerator.generateCompletelyConnectedGraph(2);
    originalPathGraph = GraphGenerator.generateRandomPathGraph(20);
    root = originalGraph.getFirstVertex();
  }

  @Test
  public void shouldProduceATree(){
    SpanningTree st = new SpanningTree(originalGraph, SpanningTree.SpanningTreeType.BFS);
    assertNotNull(st);
    assertEquals(3, st.getEdges().size());
    assertEquals(4, st.getVertices().size());
  }

  @Test
  public void shouldProduceAPathForPathInput(){
    SpanningTree st = new SpanningTree(originalPathGraph, SpanningTree.SpanningTreeType.BFS);
    assertNotNull(st);
    assertEquals(40, st.getVertices().size());
    assertEquals(39, st.getEdges().size());
  }
}
