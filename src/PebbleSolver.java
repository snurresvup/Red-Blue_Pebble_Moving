import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class PebbleSolver {
  public RBPMSolution spanningTreeBasedAlgorithm(Graph problem){
    SpanningTree spanningTree = computeSpanningTree(problem);
  }

  private SpanningTree computeSpanningTree(Graph problem) {
    return null;
  }

  public Map<StartVertex, TargetVertex> computeAssignmentOnPath(Graph g){
    //TODO check if graph is a path?
    Map<StartVertex, TargetVertex> res = new HashMap<>();
    Queue<Vertex> q = new LinkedList<>();

    Vertex v = g.getFirstVertex();

    LinkedList<Vertex> graph = convertGraphToList(g);

    for(Vertex vert : graph){
      if(q.peek().getClass() == vert.getClass()){
        q.add(vert);
      } else {
        if(v instanceof StartVertex){
          res.put((StartVertex)v, (TargetVertex)vert);
        } else {
          res.put((StartVertex)vert, (TargetVertex)vert);
        }
      }
    }

    return res;
  }

  private LinkedList<Vertex> convertGraphToList(Graph g) {
    LinkedList<Vertex> res = new LinkedList<>();
    Vertex v = g.getFirstVertex();

    //Move v to the end of the path
    Vertex prev = v;
    while(v.getEdges().size() > 1){
      for (Vertex vertex : v.getEdges().keySet()) {
        if(!vertex.equals(prev)) {
          prev = v;
          v = vertex;
        }
      }
    }

    prev = v;
    res.add(v);

    while(hasNextOnPath(v, prev)){
      //Move v to next vertex along the path
      for (Vertex vertex : v.getEdges().keySet()) {
        if (!vertex.equals(prev)) {
          prev = v;
          v = vertex;
        }
      }
      res.add(v);
    }

    return res;
  }

  private boolean hasNextOnPath(Vertex v, Vertex prev) {
    return v.getEdges().size() > 1 || v == prev;
  }
}