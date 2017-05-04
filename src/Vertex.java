import java.util.*;

public abstract class Vertex {
  private String name;
  private Map<Vertex, Integer> edges;
  private Pebble[] pebbles;
  private boolean blueStart;

  public Vertex(){
    name = UUID.randomUUID().toString();
    edges = new HashMap<>();
    pebbles = new Pebble[Settings.COLORS];
    blueStart = false;
  }

  public Vertex(boolean blueStart){
    this();
    this.blueStart = blueStart;
  }

  public void addEdge(Vertex connection, int weight) {
    if(this == connection) return;
    edges.put(connection, weight);
    if(!connection.hasEdgeTo(this)) connection.addEdge(this, weight);
  }

  public void addEdge(Map.Entry<Vertex,Integer> edge){
    addEdge(edge.getKey(), edge.getValue());
  }

  public boolean hasEdgeTo(Vertex connection){
    return edges.containsKey(connection);
  }

  public Map<Vertex, Integer> getEdges() {
    return edges;
  }

  public Pebble getPebble(PebbleColor color){
    return pebbles[color.ordinal()];
  }

  public void addPebble(Pebble pebble) {
    if(pebbles[pebble.getColor().ordinal()] != null) throw new RuntimeException("Two pebbles cannot reside on the same vertex.");
    pebbles[pebble.getColor().ordinal()] = pebble;
    pebble.setCurrentVertex(this);
  }

  @Override
  public String toString(){
    return name;
  }

  public void removePebble(Pebble pebble) {
    if(pebbles[pebble.getColor().ordinal()] != null && pebbles[pebble.getColor().ordinal()].equals(pebble)) {
      pebbles[pebble.getColor().ordinal()] = null;
    }
  }
}
