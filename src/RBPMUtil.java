public class RBPMUtil {
  public static int length(RBPMSolution solution){
    return solution.stream().mapToInt(t ->
        t.getFrom().getEdges().get(t.getTo())
    ).sum();
  }
}
