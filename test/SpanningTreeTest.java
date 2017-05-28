import org.junit.Before;
import org.junit.Test;
import scala.collection.parallel.ParIterableLike;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SpanningTreeTest {
  private Graph originalGraph;
  private Graph originalRandomGraph;
  private PathGraph originalPathGraph;
  private Vertex root;

  @Before
  public void setup(){
    originalGraph = GraphGenerator.generateCompletelyConnectedGraph(2);
    originalPathGraph = GraphGenerator.generateRandomPathGraph(20);
    originalRandomGraph = GraphGenerator.generateRandomGraph(2,0.2);
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

  @Test
  public void shouldGenerateMinSpanTree() throws InterruptedException {
    SpanningTree st = new SpanningTree(originalRandomGraph, SpanningTree.SpanningTreeType.MINIMUM);
    originalRandomGraph.show();

    Thread.sleep(2000);

    System.out.println(st);
    RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(originalRandomGraph, true, true);
  }

  @Test
  public void constructWrongMinSpanTree() throws InterruptedException {
    Pebble bluePebble = new Pebble(PebbleColor.BLUE);
    GraphImpl res = new GraphImpl(bluePebble);

    Vertex start1 = new StartVertex(new Pebble(PebbleColor.RED));
    Vertex start2 = new StartVertex(new Pebble(PebbleColor.RED));
    Vertex target1 = new TargetVertex();
    Vertex target2 = new TargetVertex();
    res.addVertex(start1);
    res.addVertex(target1);
    res.addVertex(start2);
    res.addVertex(target2);

    start1.addPebble(bluePebble);

    start1.addEdge(target1,3);
    start1.addEdge(target2,1);
    start2.addEdge(target1,1);
    start2.addEdge(target2,8);
    target1.addEdge(target2,6);

    Thread.sleep(3000);
    res.show();

    Thread.sleep(3000);

    SpanningTree spanningTree = new SpanningTree(res, SpanningTree.SpanningTreeType.MINIMUM);
    System.out.println("TEST");
  }
}
