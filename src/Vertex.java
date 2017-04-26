import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Vertex {
  private String name;
  private Set<Vertex> edges;
  private Pebble[] pebbles;

  public Vertex(){
    name = UUID.randomUUID().toString();
    edges = new HashSet<>();
    pebbles = new Pebble[Settings.COLORS];
  }

  public void addEdge(Vertex connection) {
    if(this == connection) return;
    edges.add(connection);
    if(!connection.hasEdgeTo(this)) connection.addEdge(this);
  }

  public boolean hasEdgeTo(Vertex connection){
    return edges.contains(connection);
  }

  public Set<Vertex> getEdges() {
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
