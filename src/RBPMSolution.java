import java.util.ArrayList;

public class RBPMSolution extends ArrayList<RBPMSolution.RBPMTuple>{

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
  }
}
