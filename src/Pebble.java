public class Pebble {
  private PebbleColor color;
  private Vertex currentVertex;

  public Pebble(PebbleColor color){
    this.color = color;
  }

  public PebbleColor getColor() {
    return color;
  }

  public Vertex getCurrentVertex() {
    return currentVertex;
  }

  public void setCurrentVertex(Vertex currentVertex) {
    this.currentVertex = currentVertex;
  }

  @Override
  public String toString(){
    return color.toString() + " pebble";
  }
}
