import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PebbleTest {
  private Pebble bluePebble;

  private Vertex blueStartVertex;


  @BeforeEach
  public void setup(){
    bluePebble = new Pebble(PebbleColor.BLUE);
    blueStartVertex = new StartVertex(bluePebble);
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
