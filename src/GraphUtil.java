public class GraphUtil {
  public static boolean isSolved(Graph problem){
    for(Vertex v : problem.getVertices()){
      if(v instanceof TargetVertex && v.getPebble(PebbleColor.RED) == null) return false;
      if(v instanceof StartVertex && v.getPebble(PebbleColor.RED) != null) return false;
    }
    return true;
  }

  public static int distanceTravelledByBluePebble(RBPMSolution solution, Graph problem){
    if(solution == null) return 0;
    int res = 0;
    for(RBPMSolution.RBPMTuple t : solution){
      res += t.getFrom().getEdges().get(t.getTo());
    }
    return res;
  }

  public static int distanceTraveledByRedPebbles(RBPMSolution solution, Graph problem){
    if(solution == null) return 0;
    int res = 0;
    for(RBPMSolution.RBPMTuple t : solution){
      if(t.isCarrying()){
        res += t.getFrom().getEdges().get(t.getTo());
      }
    }
    return res;
  }

  public static int numberOfPickups(RBPMSolution solution){
    if(solution == null) return 0;
    int pickups = 0;
    boolean carrying = false;
    for(RBPMSolution.RBPMTuple t : solution){
      if(carrying == t.isCarrying()) continue;
      if(!carrying) {
        pickups++;
      }
      carrying = t.isCarrying();
    }
    return pickups;
  }

  public static int numberOfRedPebbles(Graph problem){
    int res = 0;
    for(Vertex v : problem.getVertices()){
      if(v.getPebble(PebbleColor.RED) != null) res++;
    }
    return res;
  }
}
