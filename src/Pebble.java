public class Pebble {
  private PebbleColor color;
  private Vertex currentVertex;

  private StartVertex originalVertex;

  public Pebble(PebbleColor color){
    this.color = color;
  }


  public PebbleColor getColor() {
    return color;
  }

  public Vertex getCurrentVertex() {
    return currentVertex;
  }

  public StartVertex getOriginalVertex() {
    return originalVertex;
  }

  public void setCurrentVertex(Vertex currentVertex) {
    if(this.currentVertex == null){
      if(!(currentVertex instanceof StartVertex)) throw new IllegalStateException("The first vertex of a pebble must be a starting position");
      originalVertex = (StartVertex)currentVertex;
    }
    this.currentVertex = currentVertex;
  }

  @Override
  public String toString(){
    return color.toString() + " pebble";
  }
}
