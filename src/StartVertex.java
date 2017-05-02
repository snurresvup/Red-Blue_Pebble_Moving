public class StartVertex extends Vertex {
  private Pebble originalPebble;

  public StartVertex(Pebble originalPebble){
    super();
    this.originalPebble = originalPebble;
  }

  public Pebble getOriginalPebble() {
    return originalPebble;
  }
}
