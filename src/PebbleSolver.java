import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;


public class PebbleSolver {
  public static RBPMSolution spanningTreeBasedAlgorithm(GraphImpl problem){
    RBPMSolution solution = new RBPMSolution();
    SpanningTree spanningTree = computeSpanningTree(problem);
    SpanningTreeVertex leaf = spanningTree.getLeaf();

    while(!spanningTree.isEmpty()) {
      if (leaf.getModelee() instanceof StartVertex) {
        if (leaf.getModelee().getPebble(PebbleColor.RED) != null) {
          //Start vertex with red pebble
          SpanningTreeVertex closestEmptyVertex = spanningTree.findClosestEmptyVertex(leaf);
          solution.addAll(shiftAllOnPath(leaf, closestEmptyVertex, spanningTree));
        } else {
          //Empty start vertex
        }
      } else if (leaf.getModelee() instanceof TargetVertex) {
        if (leaf.getModelee().getPebble(PebbleColor.RED) != null) {
          //Target vertex with red pebble
        } else {
          //Empty target vertex
          SpanningTreeVertex closestPebble = spanningTree.findClosestPebble(leaf);
          solution.addAll(moveRedPebbleToVertex(closestPebble.getModelee().getPebble(PebbleColor.RED), leaf.getModelee(), problem));
        }
      }
      spanningTree.removeLeaf(leaf);
      leaf = spanningTree.getLeaf();
    }

    return solution;
  }

  private static RBPMSolution shiftAllOnPath(SpanningTreeVertex from, SpanningTreeVertex to, SpanningTree spanningTree) {
    Pebble bluePebble = from.getModelee().getPebble(PebbleColor.BLUE);
    if(bluePebble == null) throw new IllegalArgumentException("Blue pebble must be at from vertex");

    LinkedList<SpanningTreeVertex> path = spanningTree.getPath(from, to, null);

    RBPMSolution solution = moveBluePebbleToVertex(bluePebble, path.get(path.size()-2).getModelee(), spanningTree.getOrigin());

    ListIterator<SpanningTreeVertex> listIterator = path.listIterator(path.size()-1);

    SpanningTreeVertex currentEmptyVertex = listIterator.next();
    listIterator.previous();
    SpanningTreeVertex current = listIterator.previous();
    solution.add(new RBPMSolution.RBPMTuple(current.getModelee(), currentEmptyVertex.getModelee(), true));
    solution.add(new RBPMSolution.RBPMTuple(currentEmptyVertex.getModelee(), current.getModelee(), false));

    while (listIterator.hasPrevious()){
      currentEmptyVertex = current;
      current = listIterator.previous();
      solution.add(new RBPMSolution.RBPMTuple(currentEmptyVertex.getModelee(), current.getModelee(), false));
      solution.add(new RBPMSolution.RBPMTuple(current.getModelee(), currentEmptyVertex.getModelee(), true));
      if(listIterator.hasPrevious())solution.add(new RBPMSolution.RBPMTuple(currentEmptyVertex.getModelee(), current.getModelee(), false));
    }

    return solution;
  }

  private static RBPMSolution moveBluePebbleToVertex(Pebble bluePebble, Vertex destination, Graph graph) {
    return shortestPath(bluePebble.getCurrentVertex(), destination, graph, false);
  }

  private static RBPMSolution moveRedPebbleToVertex(Pebble pebble, Vertex destination, Graph graph) {
    RBPMSolution solution = new RBPMSolution();
    Vertex pebbleVertex = pebble.getCurrentVertex();
    Vertex blueVertex = graph.getBluePebble().getCurrentVertex();

    solution.addAll(shortestPath(blueVertex, pebbleVertex, graph, false));
    solution.addAll(shortestPath(pebbleVertex, destination, graph, true));

    return solution;
  }

  private static RBPMSolution shortestPath(Vertex fromVertex, Vertex toVertex, Graph graph, boolean carrying) {
    HashMap<Vertex, Pair<Integer,Vertex>> distances = new HashMap<>();
    Vertex current = fromVertex;
    distances.put(fromVertex, new Pair<>(0, null));
    Set<Vertex> unvisited = new HashSet<>(graph.getVertices());
    unvisited = unvisited.stream().filter(v -> v.equals(fromVertex)).collect(Collectors.toSet());
    for(Vertex v : unvisited){
      distances.put(v, null);
    }

    while(!unvisited.isEmpty()) {
      for (Vertex v : current.getEdges().keySet()) {
        if (distances.get(v) == null) {
          distances.put(v, new Pair<>(distances.get(current).getKey() + current.getEdges().get(v), current));
          continue;
        }

        int distanceCandidate = current.getEdges().get(v) + distances.get(current).getKey();
        if (distanceCandidate < distances.get(v).getKey()) {
          distances.put(v, new Pair<>(distanceCandidate, current));
        }
      }

      unvisited.remove(current);

      current = unvisited.stream().min(Comparator.comparingInt(s -> distances.get(s).getKey())).get();
    }

    return backtrack(fromVertex, toVertex, distances, carrying);
  }

  private static RBPMSolution backtrack(Vertex fromVertex, Vertex toVertex, HashMap<Vertex, Pair<Integer, Vertex>> distances, boolean carrying) {
    if(fromVertex.equals(toVertex)) throw new IllegalArgumentException("cannot have self loop (from = to)");
    Pebble bluePebble = fromVertex.getPebble(PebbleColor.BLUE);
    if(bluePebble == null) throw new IllegalArgumentException("The blue pebble must be at the from vertex");
    LinkedList<Vertex> path = new LinkedList<>();
    Vertex current = toVertex;
    while(!current.equals(fromVertex)) {
      path.addFirst(current);
      current = distances.get(current).getValue();
    }

    RBPMSolution solution = new RBPMSolution();
    Iterator<Vertex> iterator = path.iterator();
    Vertex prev = iterator.next();
    while(iterator.hasNext()){
      Vertex next = iterator.next();
      solution.add(new RBPMSolution.RBPMTuple(prev, next, carrying));
      if(carrying){
        Pebble redPebble = prev.getPebble(PebbleColor.RED);
        prev.removePebble(redPebble);
        next.addPebble(redPebble);
      }
      prev.removePebble(bluePebble);
      next.addPebble(bluePebble);
      prev = next;
    }

    return solution;
  }

  private static SpanningTree computeSpanningTree(GraphImpl problem) {
    return new SpanningTree(problem, SpanningTree.SpanningTreeType.BFS);
  }


  public static RBPMSolution computeFastSolution(PathGraph pathGraph){
    LinkedList<Vertex> graph = convertPathGraphToList(pathGraph);
    Map<StartVertex, TargetVertex> assignment = computeAssignmentOnPath(pathGraph);

    Pebble bluePebble = pathGraph.getBluePebble();
    Vertex blueStartVertex = bluePebble.getOriginalVertex();
    int blueStartIndex = graph.indexOf(blueStartVertex);

    LinkedList<Vertex> leftSubProblem = new LinkedList<>(graph.subList(0, blueStartIndex+1));
    LinkedList<Vertex> rightSubProblem = new LinkedList<>(graph.subList(blueStartIndex, graph.size()));

    Collections.reverse(rightSubProblem);
    LinkedList<Vertex> reversedOriginalGraph = new LinkedList<>(graph);
    Collections.reverse(reversedOriginalGraph);

    RBPMSolution solution;

    double leftPebbleOverflow = getNumberOfPebblesInGraph(leftSubProblem) - getNumberOfTargetPositionsInGraph(leftSubProblem);

    if(leftPebbleOverflow > 0){
      solution = computeSolution(leftSubProblem, graph, assignment, bluePebble);
      solution.addAll(computeSolution(rightSubProblem, reversedOriginalGraph, assignment, bluePebble));
    } else {
      solution = computeSolution(rightSubProblem, reversedOriginalGraph, assignment, bluePebble);
      solution.addAll(computeSolution(leftSubProblem, graph, assignment, bluePebble));
    }

    movePebbleToOrigin(bluePebble, graph, solution);

    return solution;
  }

  private static double getNumberOfTargetPositionsInGraph(LinkedList<Vertex> leftSubProblem) {
    return leftSubProblem.stream().filter(v -> v instanceof TargetVertex).count();
  }

  private static double getNumberOfPebblesInGraph(LinkedList<Vertex> leftSubProblem) {
    return leftSubProblem.stream().filter(v -> v.getPebble(PebbleColor.RED) != null).count();
  }

  public static RBPMSolution computeSolution(PathGraph pathGraph){
    LinkedList<Vertex> graph = convertPathGraphToList(pathGraph);
    Map<StartVertex, TargetVertex> assignment = computeAssignmentOnPath(pathGraph);
    Pebble bluePebble = pathGraph.getBluePebble();

    return computeSolution(graph, assignment, bluePebble);
  }

  private static RBPMSolution computeSolution(LinkedList<Vertex> graph, Map<StartVertex, TargetVertex> assignment, Pebble bluePebble){
    RBPMSolution solution = computeSolution(graph, graph, assignment, bluePebble);
    movePebbleToOrigin(bluePebble, graph, solution);
    return solution;
  }

  private static RBPMSolution computeSolution(LinkedList<Vertex> subGraph, LinkedList<Vertex> originalGraph, Map<StartVertex, TargetVertex> assignment, Pebble bluePebble) {
    RBPMSolution solution = new RBPMSolution();
    for (int i = 0; i < getNumberOfPebblesInGraph(subGraph); i++) {
      Pebble r = findLeftMostFreePebble(subGraph, originalGraph, assignment);

      if(r == null) continue;

      int blueIndex = originalGraph.indexOf(bluePebble.getCurrentVertex());
      int rIndex = subGraph.indexOf(r.getCurrentVertex());
      int direction = Integer.signum(rIndex - blueIndex);

      if(blueIndex != rIndex) transitionToRedPebble(solution, originalGraph, assignment, bluePebble, r, direction);

      moveRedPebbleToTarget(r, originalGraph, assignment, solution);
    }
    //Moved outside for reuse
    //movePebbleToOrigin(bluePebble, originalGraph, solution);

    return solution;
  }

  private static void movePebbleToOrigin(Pebble bluePebble, LinkedList<Vertex> graph, RBPMSolution solution) {
    Vertex startVertex = bluePebble.getCurrentVertex();
    int currentIndex = graph.indexOf(startVertex);

    Vertex originVertex = bluePebble.getOriginalVertex();
    int originIndex = graph.indexOf(originVertex);

    int direction = Integer.signum(originIndex - currentIndex);
    if(direction == 0) return;

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
      if(!iterator.hasNext()) throw new RuntimeException("PANIC!!!");
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

  private static Pebble findLeftMostFreePebble(LinkedList<Vertex> searchedGraph, LinkedList<Vertex> originalGraph, Map<StartVertex, TargetVertex> assignment) {
    for(Vertex candidateVertex : searchedGraph){
      Pebble candidatePebble = candidateVertex.getPebble(PebbleColor.RED);
      if(candidatePebble != null && isFreePebble(candidatePebble, originalGraph, assignment)){
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