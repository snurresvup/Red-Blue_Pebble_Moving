
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class PebbleSolverTest {
  private PathGraph pathGraph;
  private Vertex a,b,c,d;

  @Before
  public void setup(){
    Pebble bluePebble = new Pebble(PebbleColor.BLUE);
    pathGraph = new PathGraph(bluePebble);
    Pebble pA = new Pebble(PebbleColor.RED), pB = new Pebble(PebbleColor.RED);
    a = new StartVertex(pA);
    b = new StartVertex(pB);
    c = new TargetVertex();
    d = new TargetVertex();

    bluePebble.setCurrentVertex(a);

    a.addEdge(b,1);
    b.addEdge(c,1);
    c.addEdge(d,1);

    pathGraph.addVertex(a);
    pathGraph.addVertex(b);
    pathGraph.addVertex(c);
    pathGraph.addVertex(d);
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
}
