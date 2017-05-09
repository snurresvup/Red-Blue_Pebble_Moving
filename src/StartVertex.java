public class StartVertex extends Vertex {
  private Pebble originalPebble;

  public StartVertex(Pebble originalPebble){
    super();
    assignOriginalRedPebble(originalPebble);
    addPebble(originalPebble);
  }

  public StartVertex(Pebble originalPebble, boolean blueStart){
    super(blueStart);
    assignOriginalRedPebble(originalPebble);
    addPebble(originalPebble);
  }

  private void assignOriginalRedPebble(Pebble originalPebble) {
    if(originalPebble.getColor() == PebbleColor.BLUE) {
      throw new IllegalArgumentException("Original pebble cannot be blue");
    }
    this.originalPebble = originalPebble;
  }

  public Pebble getOriginalPebble() {
    return originalPebble;
  }
}
