import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class VertexTest {
  private Vertex vertex1;
  private Vertex vertex2;

  private Pebble bluePebble;

  @BeforeEach
  public void setup(){
    vertex1 = new Vertex();
    vertex2 = new Vertex();

    bluePebble = new Pebble(PebbleColor.BLUE);

    vertex1.addPebble(bluePebble);
    vertex1.addEdge(vertex2);
  }

  @Test
  public void shouldBeAbleToHaveEdges(){
    assertNotNull(vertex1.getEdges());
  }

  @Test
  public void shouldHaveTwoWayEdges(){
    assertTrue(vertex1.getEdges().stream().findFirst().orElse(null).hasEdgeTo(vertex1));
  }

  @Test
  public void shouldBeAbleToAddPebble(){
    Pebble pebble = new Pebble(PebbleColor.RED);
    vertex2.addPebble(pebble);
    Pebble foundPebble = vertex2.getPebble(PebbleColor.RED);
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

  @Test
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

  @Test
  public void shouldOnlyContainOnePebbleOfTheSameColor(){
    Pebble pebble = new Pebble(PebbleColor.BLUE);
    Pebble firstPebble = vertex1.getPebble(PebbleColor.BLUE);
    vertex1.addPebble(pebble);
    assertNotEquals(pebble, firstPebble);
    assertEquals(pebble, vertex1.getPebble(PebbleColor.BLUE));
  }
}
