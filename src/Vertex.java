import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Vertex {
  private Set<Vertex> edges;
  private Pebble[] pebbles;

  public Vertex(){
    edges = new HashSet<>();
    pebbles = new Pebble[Settings.COLORS];
  }

  public void addEdge(Vertex connection) {
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
}
