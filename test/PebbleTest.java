import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PebbleTest {
  private Pebble bluePebble;

  private Vertex blueStartVertex;


  @Before
  public void setup(){
    bluePebble = new Pebble(PebbleColor.BLUE);
    Pebble redPebble = new Pebble(PebbleColor.RED);
    blueStartVertex = new StartVertex(redPebble, true);
    blueStartVertex.addPebble(bluePebble);
  }


  @Test
  public void shouldHaveAColor(){
    assertEquals(PebbleColor.BLUE, bluePebble.getColor());
  }

  @Test
  public void shouldStartAtStartVertex(){
    assertEquals(blueStartVertex,bluePebble.getCurrentVertex());
  }
}
