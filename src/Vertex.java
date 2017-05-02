import java.util.*;

public class Vertex {
  private String name;
  private Map<Vertex, Integer> edges;
  private Pebble[] pebbles;

  public Vertex(){
    name = UUID.randomUUID().toString();
    edges = new HashMap<>();
    pebbles = new Pebble[Settings.COLORS];
  }

  public void addEdge(Vertex connection, int weight) {
    if(this == connection) return;
    edges.put(connection, weight);
    if(!connection.hasEdgeTo(this)) connection.addEdge(this, weight);
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
    pebbles[pebble.getColor().ordinal()] = pebble;
    pebble.setCurrentVertex(this);
  }

  @Override
  public String toString(){
    return name;
  }
}
