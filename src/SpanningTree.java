import java.util.List;
import java.util.PriorityQueue;

public class SpanningTree extends Graph{
  private List<Vertex> leafs;
  private Graph origin;

  public SpanningTree(Graph origin, SpanningTreeType type){
    this.origin = origin;

    switch (type){
      case MINIMUM: constructMinimumSpanningTreePrim(origin);
    }
  }

  private void constructMinimumSpanningTreePrim(Graph origin) {
    Vertex root = origin.getFirstVertex();

  }

  public enum SpanningTreeType {
    MINIMUM, RANDOM
  }
}
