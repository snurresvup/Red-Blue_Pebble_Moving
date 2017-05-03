public class StartVertex extends Vertex {
  private Pebble originalPebble;

  public StartVertex(Pebble originalPebble){
    super();
    assignOriginalRedPebble(originalPebble);
  }

  public StartVertex(Pebble originalPebble, boolean blueStart){
    super(blueStart);
    assignOriginalRedPebble(originalPebble);
  }

  private void assignOriginalRedPebble(Pebble originalPebble) {
    if(originalPebble.getColor() == PebbleColor.BLUE) {
      throw new IllegalArgumentException("Originam pebble may not be blue");
    }
    this.originalPebble = originalPebble;
  }

  public Pebble getOriginalPebble() {
    return originalPebble;
  }
}
