import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.*;

public abstract class Vertex {
  private String name;
  private Map<Vertex, Integer> edges;
  private Pebble[] pebbles;
  private boolean blueStart;

  public Vertex(){
    name = Counter.getInstance().getAValue() + "";//System.nanoTime()+"";//UUID.randomUUID().toString();
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
    if(pebbles[pebble.getColor().ordinal()] != null) {
      throw new RuntimeException("Two pebbles cannot reside on the same vertex.");
    }
    pebbles[pebble.getColor().ordinal()] = pebble;
    pebble.setCurrentVertex(this);
  }

  @Override
  public String toString(){
    return name.toString();
    //return name.substring(name.length() - 3, name.length());
  }

  public void removePebble(Pebble pebble) {
    if(pebble == null) {
      throw new IllegalArgumentException("cannot remove null");
    }
    if(pebbles[pebble.getColor().ordinal()] != null && pebbles[pebble.getColor().ordinal()].equals(pebble)) {
      pebbles[pebble.getColor().ordinal()] = null;
    }
  }

  public void replacePebble(Pebble pebble) {
    pebbles[pebble.getColor().ordinal()] = pebble;
    pebble.setCurrentVertex(this);
  }
}
