
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class PebbleSolverTest {
  private PathGraph pathGraph;
  private Vertex a,b,c,d;

  private PathGraph pathGraph2;
  private StartVertex sa, sb, sc, sd;
  private TargetVertex ta, tb, tc, td;
  private Pebble bluePebble;
  private Pebble pebbleA;
  private Pebble pebbleB;

  @Before
  public void setup(){
    bluePebble = new Pebble(PebbleColor.BLUE);
    pathGraph = new PathGraph(bluePebble);
    pebbleA = new Pebble(PebbleColor.RED);
    pebbleB = new Pebble(PebbleColor.RED);
    a = new StartVertex(pebbleA);
    b = new StartVertex(pebbleB);
    c = new TargetVertex();
    d = new TargetVertex();

    a.addPebble(bluePebble);

    a.addEdge(b,1);
    b.addEdge(c,14);
    c.addEdge(d,1);

    pathGraph.addVertex(a);
    pathGraph.addVertex(b);
    pathGraph.addVertex(c);
    pathGraph.addVertex(d);

    Pebble pA = new Pebble(PebbleColor.RED), pB = new Pebble(PebbleColor.RED), pC = new Pebble(PebbleColor.RED), pD = new Pebble(PebbleColor.RED);
    Pebble bluePebble2 = new Pebble(PebbleColor.BLUE);
    pathGraph2 = new PathGraph(bluePebble2);
    sa = new StartVertex(pA);
    sb = new StartVertex(pB, true);
    bluePebble2.setCurrentVertex(sb);
    sb.addPebble(bluePebble2);

    sc = new StartVertex(pC);
    sd = new StartVertex(pD);

    ta = new TargetVertex();
    tb = new TargetVertex();
    tc = new TargetVertex();
    td = new TargetVertex();

    ta.addEdge(sa,1);
    sa.addEdge(tb, 1);
    tb.addEdge(sb,1);
    sb.addEdge(tc,1);
    tc.addEdge(sc,1);
    sc.addEdge(td,1);
    td.addEdge(sd,1);

    pathGraph2.addVertex(ta);
    pathGraph2.addVertex(sa);
    pathGraph2.addVertex(tb);
    pathGraph2.addVertex(sb);
    pathGraph2.addVertex(tc);
    pathGraph2.addVertex(sc);
    pathGraph2.addVertex(td);
    pathGraph2.addVertex(sd);

  }

  @Test
  public void shouldFetchFirstVertexOnPath(){
    assertTrue(d.equals(pathGraph.getFirstVertex()) || a.equals(pathGraph.getFirstVertex()));
  }

  @Test
  public void shouldCalculateCorrectAssignment(){
    Map<StartVertex, TargetVertex> res = PebbleSolver.computeAssignmentOnPath(pathGraph);
    assertEquals(c,res.get(a));
    assertEquals(d,res.get(b));
  }

  @Test
  public void shouldCalculateASolution(){
    RBPMSolution solution = PebbleSolver.computeSolution(pathGraph);
    assertNotNull(solution);
  }

  @Test
  public void shouldCalculateCorrectSolution(){
    RBPMSolution solution = PebbleSolver.computeSolution(pathGraph);
    assertNotNull(solution);
    assertEquals(10, solution.size());
    assertEquals(a, solution.get(0).getFrom());
    assertEquals(b, solution.get(0).getTo());
    assertFalse(solution.get(0).isCarrying());

    assertEquals(b, solution.get(1).getFrom());
    assertEquals(c, solution.get(1).getTo());
    assertTrue(solution.get(1).isCarrying());

    assertEquals(c, solution.get(2).getFrom());
    assertEquals(d, solution.get(2).getTo());
    assertTrue(solution.get(2).isCarrying());

    assertEquals(d, solution.get(3).getFrom());
    assertEquals(c, solution.get(3).getTo());
    assertFalse(solution.get(3).isCarrying());

    assertEquals(c, solution.get(4).getFrom());
    assertEquals(b, solution.get(4).getTo());
    assertFalse(solution.get(4).isCarrying());

    assertEquals(b, solution.get(5).getFrom());
    assertEquals(a, solution.get(5).getTo());
    assertFalse(solution.get(5).isCarrying());

    assertEquals(a, solution.get(6).getFrom());
    assertEquals(b, solution.get(6).getTo());
    assertTrue(solution.get(6).isCarrying());

    assertEquals(b, solution.get(7).getFrom());
    assertEquals(c, solution.get(7).getTo());
    assertTrue(solution.get(7).isCarrying());

    assertEquals(c, solution.get(8).getFrom());
    assertEquals(b, solution.get(8).getTo());
    assertFalse(solution.get(8).isCarrying());

    assertEquals(b, solution.get(9).getFrom());
    assertEquals(a, solution.get(9).getTo());
    assertFalse(solution.get(9).isCarrying());

  }

  @Test
  public void shouldComputeFastSolution(){
    RBPMSolution solution = PebbleSolver.computeFastSolution(pathGraph);
    assertNotNull(solution);
    assertEquals(10, solution.size());
    assertEquals(a, solution.get(0).getFrom());
    assertEquals(b, solution.get(0).getTo());
    assertFalse(solution.get(0).isCarrying());

    assertEquals(b, solution.get(1).getFrom());
    assertEquals(c, solution.get(1).getTo());
    assertTrue(solution.get(1).isCarrying());

    assertEquals(c, solution.get(2).getFrom());
    assertEquals(d, solution.get(2).getTo());
    assertTrue(solution.get(2).isCarrying());

    assertEquals(d, solution.get(3).getFrom());
    assertEquals(c, solution.get(3).getTo());
    assertFalse(solution.get(3).isCarrying());

    assertEquals(c, solution.get(4).getFrom());
    assertEquals(b, solution.get(4).getTo());
    assertFalse(solution.get(4).isCarrying());

    assertEquals(b, solution.get(5).getFrom());
    assertEquals(a, solution.get(5).getTo());
    assertFalse(solution.get(5).isCarrying());

    assertEquals(a, solution.get(6).getFrom());
    assertEquals(b, solution.get(6).getTo());
    assertTrue(solution.get(6).isCarrying());

    assertEquals(b, solution.get(7).getFrom());
    assertEquals(c, solution.get(7).getTo());
    assertTrue(solution.get(7).isCarrying());

    assertEquals(c, solution.get(8).getFrom());
    assertEquals(b, solution.get(8).getTo());
    assertFalse(solution.get(8).isCarrying());

    assertEquals(b, solution.get(9).getFrom());
    assertEquals(a, solution.get(9).getTo());
    assertFalse(solution.get(9).isCarrying());
  }

  @Test
  public void shouldComputeFasterSolution(){
    RBPMSolution solution = PebbleSolver.computeFastSolution(pathGraph2);
    assertEquals(14, solution.size());
  }

  @Ignore
  @Test
  public void shouldBeWorseWithOldAlgorithm(){
    //This test may fail because the problem may be reversed due to hash values
    RBPMSolution solution = PebbleSolver.computeSolution(pathGraph2);
    assertTrue(solution.size() > 14);
  }

  @Test
  public void spanningTreeAlgoShouldWorkOnPath(){
    RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(pathGraph);
    reconstructPathGraph();
    RBPMSolution solution1 = PebbleSolver.computeFastSolution(pathGraph);

    assertEquals(RBPMUtil.length(solution1), RBPMUtil.length(solution));
  }

  @Test
  public void spanningTreeAlgoShouldWorkOnCCGraph() throws InterruptedException {
    GraphImpl g = GraphGenerator.generateCompletelyConnectedGraph(26);
    RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(g);
    assertTrue(solution.size() > 26);
    assertTrue(GraphUtil.isSolved(g));
  }

  public void reconstructPathGraph(){
    bluePebble.setCurrentVertex(bluePebble.getOriginalVertex());
    c.removePebble(pebbleA); d.removePebble(pebbleB);
    a.addPebble(pebbleA); b.addPebble(pebbleB);
  }
}
