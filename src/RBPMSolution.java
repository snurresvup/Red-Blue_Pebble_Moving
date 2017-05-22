import java.util.ArrayList;

public class RBPMSolution extends ArrayList<RBPMSolution.RBPMTuple>{
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Solution :\n");
    for(RBPMTuple t : this){
      sb.append(t);
      sb.append("\n");
    }
    return sb.toString();
  }

  public static class RBPMTuple {
    private Vertex from;
    private Vertex to;
    private boolean carrying;

    public RBPMTuple(Vertex from, Vertex to, boolean carrying) {
      this.from = from;
      this.to = to;
      this.carrying = carrying;
    }

    public Vertex getFrom() {
      return from;
    }

    public Vertex getTo() {
      return to;
    }

    public boolean isCarrying() {
      return carrying;
    }

    @Override
    public String toString() {
      return from + " -> " + to + " : " + (carrying ? "carrying" : "not carrying");
    }
  }
}
