public class Experiments {
  public static void main(String[] args) {
    experimentCCGraphs();
    System.out.println();
    compareOnSimplePath();
    System.out.println();
    compareRandomPathOnAlgorithms();
  }

  public static void compareOnSimplePath(){
    double iterations = 10;
    double pickups = 0.0;
    double redDistance = 0.0;
    double blueDistance = 0.0;

    for (int i = 0; i < iterations; i++) {
      PathGraph problem = GraphGenerator.generateSimplePathGraph(100);
      RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(problem);
      pickups += GraphUtil.numberOfPickups(solution);
      redDistance += GraphUtil.distanceTraveledByRedPebbles(solution, problem);
      blueDistance += GraphUtil.distanceTravelledByBluePebble(solution, problem);
    }

    double pickupsP = 0.0;
    double redDistanceP = 0.0;
    double blueDistanceP = 0.0;

    for (int i = 0; i < iterations; i++) {
      PathGraph problem = GraphGenerator.generateSimplePathGraph(100);
      RBPMSolution solution = PebbleSolver.computeFastSolution(problem);
      pickupsP += GraphUtil.numberOfPickups(solution);
      redDistanceP += GraphUtil.distanceTraveledByRedPebbles(solution, problem);
      blueDistanceP += GraphUtil.distanceTravelledByBluePebble(solution, problem);
    }

    System.out.println("Comparison:");
    System.out.println("\t\t\t\t" + "Path Algo" + "\t\t\t" + "Spanning Tree" + "\t\t\t" + "path/spanning");
    System.out.println("Pickups\t\t\t" + pickupsP/iterations + "\t\t\t\t" + pickups/iterations + "\t\t\t\t\t" + pickupsP/pickups);
    System.out.println("Red Distance\t" + redDistanceP/iterations + "\t\t\t\t" + redDistance/iterations + "\t\t\t\t\t" + redDistanceP/redDistance);
    System.out.println("Blue Distance\t" + blueDistanceP/iterations + "\t\t\t\t" + blueDistance/iterations + "\t\t\t\t\t" + blueDistanceP/blueDistance);
  }

  public static void compareRandomPathOnAlgorithms(){
    double iterations = 10;
    double pickups = 0.0;
    double redDistance = 0.0;
    double blueDistance = 0.0;

    for (int i = 0; i < iterations; i++) {
      PathGraph problem = GraphGenerator.generateRandomPathGraph(100);
      RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(problem);
      pickups += GraphUtil.numberOfPickups(solution);
      redDistance += GraphUtil.distanceTraveledByRedPebbles(solution, problem);
      blueDistance += GraphUtil.distanceTravelledByBluePebble(solution, problem);
    }

    double pickupsP = 0.0;
    double redDistanceP = 0.0;
    double blueDistanceP = 0.0;

    for (int i = 0; i < iterations; i++) {
      PathGraph problem = GraphGenerator.generateRandomPathGraph(100);
      RBPMSolution solution = PebbleSolver.computeFastSolution(problem);
      pickupsP += GraphUtil.numberOfPickups(solution);
      redDistanceP += GraphUtil.distanceTraveledByRedPebbles(solution, problem);
      blueDistanceP += GraphUtil.distanceTravelledByBluePebble(solution, problem);
    }

    System.out.println("Comparison:");
    System.out.println("\t\t\t\t" + "Path Algo" + "\t\t\t" + "Spanning Tree" + "\t\t\t" + "path/spanning");
    System.out.println("Pickups\t\t\t" + pickupsP/iterations + "\t\t\t\t" + pickups/iterations + "\t\t\t\t\t" + pickupsP/pickups);
    System.out.println("Red Distance\t" + redDistanceP/iterations + "\t\t\t\t" + redDistance/iterations + "\t\t\t\t\t" + redDistanceP/redDistance);
    System.out.println("Blue Distance\t" + blueDistanceP/iterations + "\t\t\t\t" + blueDistance/iterations + "\t\t\t\t\t" + blueDistanceP/blueDistance);
  }

  public static void experimentCCGraphs(){
    double iterations = 1.0;
    double pickups = 0.0;
    double redDistance = 0.0;
    double blueDistance = 0.0;
    for (int i = 0; i < iterations; i++) {
      Graph problem = GraphGenerator.generateCompletelyConnectedGraph(100);
      RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(problem);
      pickups += GraphUtil.numberOfPickups(solution);
      redDistance += GraphUtil.distanceTraveledByRedPebbles(solution, problem);
      blueDistance += GraphUtil.distanceTravelledByBluePebble(solution, problem);
    }

    System.out.println("Results:\n");
    System.out.println("\tAverage Pickups: " + pickups/iterations);
    System.out.println("\tAverage red distance: " + redDistance/iterations);
    System.out.println("\tAverage blue distance: " + blueDistance/iterations);
  }
}
