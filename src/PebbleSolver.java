import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class PebbleSolver {
  public RBPMSolution spanningTreeBasedAlgorithm(Graph problem){
    SpanningTree spanningTree = computeSpanningTree(problem);
    return null; //TODO not done
  }

  private SpanningTree computeSpanningTree(Graph problem) {
    return null;
  }

  public static Map<StartVertex, TargetVertex> computeAssignmentOnPath(PathGraph g){
    Map<StartVertex, TargetVertex> res = new HashMap<>();
    Queue<Vertex> q = new LinkedList<>();

    LinkedList<Vertex> graph = convertPathGraphToList(g);

    for(Vertex vert : graph){
      if(q.isEmpty() || q.element().getClass() == vert.getClass()){
        q.add(vert);
      } else {
        if(vert instanceof StartVertex){
          res.put((StartVertex)vert, (TargetVertex)q.remove());
        } else {
          res.put((StartVertex)q.remove(), (TargetVertex)vert);
        }
      }
    }

    return res;
  }

  private static LinkedList<Vertex> convertPathGraphToList(PathGraph g) {
    LinkedList<Vertex> res = new LinkedList<>();
    Vertex v = g.getFirstVertex();

    Vertex prev = v;
    res.add(v);

    do {
      //Move v to next vertex along the path
      for (Vertex vertex : v.getEdges().keySet()) {
        if (!vertex.equals(prev)) {
          prev = v;
          v = vertex;
          res.add(v);
          break;
        }
      }
    } while(hasNextOnPath(v));

    return res;
  }

  private static boolean hasNextOnPath(Vertex v) {
    return v.getEdges().size() > 1;
  }
}