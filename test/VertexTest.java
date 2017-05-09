import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VertexTest {
  private Vertex vertex1;
  private Vertex vertex2;

  private Pebble bluePebble;

  @Before
  public void setup(){
    vertex1 = new StartVertex(new Pebble(PebbleColor.RED));
    vertex2 = new TargetVertex();

    bluePebble = new Pebble(PebbleColor.BLUE);

    vertex1.addPebble(bluePebble);
    vertex1.addEdge(vertex2, 1);
  }

  @Test
  public void shouldBeAbleToHaveEdges(){
    assertNotNull(vertex1.getEdges());
  }

  @Test
  public void shouldHaveTwoWayEdges(){
    assertTrue(vertex1.getEdges().keySet().stream().findFirst().orElse(null).hasEdgeTo(vertex1));
  }

  @Test
  public void shouldBeAbleToAddPebble(){
    Pebble pebble = new Pebble(PebbleColor.RED);
    vertex1.replacePebble(pebble);
    Pebble foundPebble = vertex1.getPebble(PebbleColor.RED);
    assertNotNull(foundPebble);
    assertEquals(PebbleColor.RED, foundPebble.getColor());
    assertEquals(pebble, foundPebble);
  }

  @Test
  public void shouldBeAbleToContainBluePebble(){
    Pebble containedPebble = vertex1.getPebble(PebbleColor.BLUE);
    assertNotNull(containedPebble);
    assertEquals(PebbleColor.BLUE, containedPebble.getColor());
    assertEquals(bluePebble, containedPebble);
  }

  @Test(expected = RuntimeException.class)
  public void shouldBeAbleToContainDifferentPebbles(){
    Pebble pebble = new Pebble(PebbleColor.RED);
    vertex1.addPebble(pebble);
    Pebble foundPebble = vertex1.getPebble(PebbleColor.RED);
    Pebble foundPebble2 = vertex1.getPebble(PebbleColor.BLUE);
    assertNotNull(foundPebble);
    assertNotNull(foundPebble2);
    assertEquals(PebbleColor.RED, foundPebble.getColor());
    assertEquals(PebbleColor.BLUE, foundPebble2.getColor());
    assertEquals(pebble, foundPebble);
    assertEquals(bluePebble, foundPebble2);
  }

  @Test(expected = RuntimeException.class)
  public void shouldOnlyContainOnePebbleOfTheSameColor(){
    Pebble pebble = new Pebble(PebbleColor.BLUE);
    Pebble firstPebble = vertex1.getPebble(PebbleColor.BLUE);
    vertex1.addPebble(pebble);
    assertNotEquals(pebble, firstPebble);
    assertEquals(pebble, vertex1.getPebble(PebbleColor.BLUE));
  }

  @Test
  public void shouldConnectProperly(){
    Vertex a = new StartVertex(new Pebble(PebbleColor.RED));
    Vertex b = new StartVertex(new Pebble(PebbleColor.RED));
    Vertex c = new TargetVertex();
    Vertex d = new TargetVertex();

    a.addEdge(b,1);
    b.addEdge(c,1);
    c.addEdge(d,1);

    assertTrue(a.getEdges().containsKey(b));
    assertFalse(a.getEdges().containsKey(c));
    assertFalse(a.getEdges().containsKey(d));

    assertTrue(b.getEdges().containsKey(a));
    assertTrue(b.getEdges().containsKey(c));
    assertFalse(b.getEdges().containsKey(d));

    assertTrue(c.getEdges().containsKey(b));
    assertTrue(c.getEdges().containsKey(d));
    assertFalse(c.getEdges().containsKey(a));

    assertTrue(d.getEdges().containsKey(c));
    assertFalse(d.getEdges().containsKey(b));
    assertFalse(d.getEdges().containsKey(a));
  }
}
