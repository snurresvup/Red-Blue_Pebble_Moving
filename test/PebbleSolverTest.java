import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PebbleSolverTest {
  private PathGraph pathGraph;
  private Vertex a,b,c,d;

  @BeforeEach
  public void setup(){
    pathGraph = new PathGraph();
    Pebble pA = new Pebble(PebbleColor.RED), pB = new Pebble(PebbleColor.RED);
    a = new StartVertex(pA);
    b = new StartVertex(pB);
    c = new TargetVertex();
    d = new TargetVertex();

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
}
