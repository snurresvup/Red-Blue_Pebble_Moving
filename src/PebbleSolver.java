import com.sun.org.apache.regexp.internal.RE;

import java.util.*;


public class PebbleSolver {
  public RBPMSolution spanningTreeBasedAlgorithm(Graph problem){
    SpanningTree spanningTree = computeSpanningTree(problem);
    return null; //TODO not done
  }

  private SpanningTree computeSpanningTree(Graph problem) {
    return null;
  }


  public static RBPMSolution computeSolution(PathGraph pathGraph){
    RBPMSolution solution = new RBPMSolution();
    LinkedList<Vertex> graph = convertPathGraphToList(pathGraph);
    Map<StartVertex, TargetVertex> assignment = computeAssignmentOnPath(pathGraph);
    Pebble bluePebble = pathGraph.getBluePebble();

    for (int i = 0; i < assignment.size(); i++) {
      Pebble r = findLeftMostFreePebble(graph, assignment);

      int blueIndex = graph.indexOf(bluePebble.getCurrentVertex());
      int rIndex = graph.indexOf(r.getCurrentVertex());
      int direction = Integer.signum(rIndex - blueIndex);

      if(blueIndex != rIndex) transitionToRedPebble(solution, graph, assignment, bluePebble, r, direction);

      moveRedPebbleToTarget(r, graph, assignment, solution);
    }

    movePebbleToOrigin(bluePebble, graph, solution);

    return solution;
  }

  private static void movePebbleToOrigin(Pebble bluePebble, LinkedList<Vertex> graph, RBPMSolution solution) {
    Vertex startVertex = bluePebble.getCurrentVertex();
    int currentIndex = graph.indexOf(startVertex);

    Vertex originVertex = bluePebble.getOriginalVertex();
    int originIndex = graph.indexOf(originVertex);

    int direction = Integer.signum(originIndex - currentIndex);


    Iterator<Vertex> iterator;
    if(direction < 0){
      iterator = graph.descendingIterator();
    } else {
      iterator = graph.listIterator();
    }

    //Position iterator
    Vertex previousVertex = iterator.next();
    while(!previousVertex.equals(startVertex)) previousVertex = iterator.next();

    startVertex.removePebble(bluePebble);

    Vertex currentVertex;
    do {
      currentVertex = iterator.next();
      solution.add(
          new RBPMSolution.RBPMTuple(
              previousVertex
              , currentVertex
              , false
          )
      );
      previousVertex = currentVertex;
    } while(!currentVertex.equals(originVertex));

    currentVertex.addPebble(bluePebble);
  }

  private static void moveRedPebbleToTarget(Pebble r, LinkedList<Vertex> graph, Map<StartVertex, TargetVertex> assignment, RBPMSolution solution) {
    Vertex currentVertex = r.getCurrentVertex();
    Pebble bluePebble = currentVertex.getPebble(PebbleColor.BLUE);
    if(bluePebble == null) throw new IllegalStateException("Cannot move red pebble without blue pebble");
    Vertex tg = assignment.get(r.getOriginalVertex());

    int currentIndex = graph.indexOf(currentVertex);
    int targetIndex = graph.indexOf(tg);
    int direction = Integer.signum(targetIndex - currentIndex);

    currentVertex.removePebble(bluePebble);

    while(currentIndex != targetIndex){
      Vertex nextVertex = graph.get(currentIndex + direction);
      solution.add(
          new RBPMSolution.RBPMTuple(
              currentVertex
              , nextVertex
              , true
          )
      );
      //May be removed to optimize
      currentVertex.removePebble(r);
      nextVertex.addPebble(r);

      currentIndex += direction;
      currentVertex = graph.get(currentIndex);
    }

    currentVertex.addPebble(bluePebble);
  }

  private static void transitionToRedPebble(RBPMSolution solution
                                          , LinkedList<Vertex> graph
                                          , Map<StartVertex
                                          , TargetVertex> assignment
                                          , Pebble bluePebble
                                          , Pebble r
                                          , int direction) {
    int bluePosition = graph.indexOf(bluePebble.getCurrentVertex());
    int rIndex = graph.indexOf(r.getCurrentVertex());
    while(rIndex != bluePosition){
      boolean moveRedPebble = hitchARide(bluePosition, direction > 0, graph, assignment);
      solution.add(
          new RBPMSolution.RBPMTuple(
              graph.get(bluePosition)
              , graph.get(bluePosition + direction)
              , moveRedPebble
          )
      );
      Vertex v = graph.get(bluePosition);
      Vertex vNext = graph.get(bluePosition + direction);

      if(moveRedPebble) {
        Pebble redPebble = v.getPebble(PebbleColor.RED);
        v.removePebble(redPebble);
        vNext.addPebble(redPebble);
      }

      v.removePebble(bluePebble);
      vNext.addPebble(bluePebble);

      bluePosition += direction;
    }
  }

  private static boolean hitchARide(int bluePosition, boolean goingRight, LinkedList<Vertex> graph, Map<StartVertex, TargetVertex> assignment) {
    Pebble redPebble = graph.get(bluePosition).getPebble(PebbleColor.RED);
    if(redPebble == null) return false;

    if(graph.indexOf(redPebble.getCurrentVertex())
        - graph.indexOf(assignment.get(redPebble.getOriginalVertex())) == 0) return false; //Red pebble in correct spot

    boolean redPebbleGoingRight = graph.indexOf(redPebble.getCurrentVertex())
                                  - graph.indexOf(assignment.get(redPebble.getOriginalVertex())) < 0;

    if(redPebbleGoingRight && goingRight && graph.get(bluePosition + 1).getPebble(PebbleColor.RED) == null) return true;

    return !redPebbleGoingRight && !goingRight && graph.get(bluePosition - 1).getPebble(PebbleColor.RED) == null;
  }

  private static Pebble findLeftMostFreePebble(LinkedList<Vertex> graph, Map<StartVertex, TargetVertex> assignment) {
    for(Vertex candidateVertex : graph){
      Pebble candidatePebble = candidateVertex.getPebble(PebbleColor.RED);
      if(candidatePebble != null && isFreePebble(candidatePebble, graph, assignment)){
        return candidatePebble;
      }
    }
    return null;
  }

  private static boolean isFreePebble(Pebble candidatePebble, LinkedList<Vertex> graph, Map<StartVertex, TargetVertex> assignment) {
    Vertex currentCandidatePosition = candidatePebble.getCurrentVertex();
    Vertex candidateTarget = assignment.get(candidatePebble.getOriginalVertex());

    if(currentCandidatePosition.equals(assignment.get(candidatePebble.getOriginalVertex()))) return false;

    //For debugging try to check if target index < candidate index. (Should never happen)
    for (int i = graph.indexOf(currentCandidatePosition)+1; i <= graph.indexOf(candidateTarget); i++) {
      if(graph.get(i).getPebble(PebbleColor.RED) != null) return false;
    }

    return true;
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