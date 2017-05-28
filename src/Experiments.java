import java.io.*;

public class Experiments {
  public static final double ITERATIONS = 40.0;
  public static final int[] PROBLEM_SIZES = new int[]{2};//{1,2,4,8,16};//,32,64,128,256};
  public static void main(String[] args) {
    //experimentCCGraphs();
    //System.out.println();
    //compareOnSimplePath();
    //System.out.println();
    //compareRandomPathOnAlgorithms();
    System.out.println("Starting test...");
    //compareSpanningTreeVariationsOnCC("TheGreatTest");
    compareSpanningTreeVariationsOnRandomGraphs("Random graphs");
  }

  public static void compareSpanningTreeVariationsOnRandomGraphs(String fileName){
    double pickups = 0.0;
    double redDistance = 0.0;
    double blueDistance = 0.0;


    PrintWriter printWriter = null;
    PrintWriter simpleWriter = null;
    try {
      printWriter = new PrintWriter(fileName);
      simpleWriter = new PrintWriter(fileName + "simple");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    printWriter.println("Iterations," + (int) ITERATIONS);
    simpleWriter.println("Iterations," + (int) ITERATIONS);

    for (int k = 3; k < 4; k++) {

      printAlgorithmSignature(printWriter, simpleWriter, k);

      double[] connectionChances = new double[]{0.3, 0.5, 0.7};

      for(double connectionChance : connectionChances) {
        printWriter.println("Graph type " + connectionChance);
        simpleWriter.println("Graph type " + connectionChance);
        for (int problemSize : PROBLEM_SIZES) {
          pickups = 0.0;
          redDistance = 0.0;
          blueDistance = 0.0;

          for (int i = 0; i < ITERATIONS; i++) {
            Graph problem = GraphGenerator.generateRandomGraph(problemSize, connectionChance);
            RBPMSolution solution;
            System.out.println(i + " , " + k + "problem size: " + problemSize);
            //try {
              solution = computeSolutionUsingAlgorithm(k, problem);
            /*}catch (Exception e){
              problem.show();
              try {
                Thread.sleep(2000);
              } catch (InterruptedException e1) {
                e1.printStackTrace();
              }
              throw e;
            }*/

            pickups += GraphUtil.numberOfPickups(solution);
            redDistance += GraphUtil.distanceTraveledByRedPebbles(solution, problem);
            blueDistance += GraphUtil.distanceTravelledByBluePebble(solution, problem);
          }

          printWriter.println();

          printWriter.println("Problem Size," + problemSize);
          printWriter.println("Pickups," + pickups);
          printWriter.println("Red Distance," + redDistance);
          printWriter.println("Blue Distance," + blueDistance);

          simpleWriter.println(problemSize + "," + pickups + "," + redDistance + "," + blueDistance);

        }
      }
      simpleWriter.println();
      printWriter.println();
    }

    simpleWriter.flush();
    simpleWriter.close();
    printWriter.flush();
    printWriter.close();
  }

  private static RBPMSolution computeSolutionUsingAlgorithm(int k, Graph problem) {
    RBPMSolution solution = null;
    try {
      switch (k) {
        case 0:
          solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, false, false);
          break;
        case 1:
          solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, false, true);
          break;
        case 2:
          solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, true, false);
          break;
        case 3:
          solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, true, true);
      }
    } catch (UnsolvableProblemException e){
      System.err.println(problem + " was unsolvable");
    }
    return solution;
  }

  public static void compareSpanningTreeVariationsOnCC(String fileName){
    double pickups = 0.0;
    double redDistance = 0.0;
    double blueDistance = 0.0;


    PrintWriter printWriter = null;
    PrintWriter simpleWriter = null;
    try {
      printWriter = new PrintWriter(fileName);
      simpleWriter = new PrintWriter(fileName + "simple");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    printWriter.println("Iterations," + (int) ITERATIONS);
    simpleWriter.println("Iterations," + (int) ITERATIONS);

    for (int k = 0; k < 4; k++) {

      printAlgorithmSignature(printWriter, simpleWriter, k);

      for (int problemSize : PROBLEM_SIZES) {
        pickups = 0.0;
        redDistance = 0.0;
        blueDistance = 0.0;

        for (int i = 0; i < ITERATIONS; i++) {
          Graph problem = GraphGenerator.generateCompletelyConnectedGraph(problemSize);
          RBPMSolution solution = computeSolutionUsingAlgorithm(k,problem);
          pickups += GraphUtil.numberOfPickups(solution);
          redDistance += GraphUtil.distanceTraveledByRedPebbles(solution, problem);
          blueDistance += GraphUtil.distanceTravelledByBluePebble(solution, problem);
        }

        printWriter.println();

        printWriter.println("Problem Size," + problemSize);
        printWriter.println("Pickups," + pickups);
        printWriter.println("Red Distance," + redDistance);
        printWriter.println("Blue Distance," + blueDistance);

        simpleWriter.println(problemSize + "," + pickups + "," + redDistance + "," + blueDistance);

      }
      simpleWriter.println();
      printWriter.println();
    }

    simpleWriter.flush();
    simpleWriter.close();
    printWriter.flush();
    printWriter.close();
  }

  private static void printAlgorithmSignature(PrintWriter printWriter, PrintWriter simpleWriter, int k) {
    switch (k) {
      case 0:
        printWriter.println("STNP");
        simpleWriter.println("STNP");
        System.out.println("STNP");
        break;
      case 1:
        printWriter.println("STP");
        simpleWriter.println("STP");
        System.out.println("STP");
        break;
      case 2:
        printWriter.println("MSTNP");
        simpleWriter.println("MSTNP");
        System.out.println("MSTNP");
        break;
      case 3:
        printWriter.println("MSTP");
        simpleWriter.println("MSTP");
        System.out.println("MSTP");
        break;
    }
  }

  public static void compareOnSimplePath(){
    double iterations = 40;
    double pickups = 0.0;
    double redDistance = 0.0;
    double blueDistance = 0.0;

    for (int i = 0; i < iterations; i++) {
      PathGraph problem = GraphGenerator.generateSimplePathGraph(100);
      RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(problem,true, true);
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
    double iterations = 40;
    double pickups = 0.0;
    double redDistance = 0.0;
    double blueDistance = 0.0;

    for (int i = 0; i < iterations; i++) {
      PathGraph problem = GraphGenerator.generateRandomPathGraph(100);
      RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, true, true);
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
    double iterations = 40.0;
    double pickups = 0.0;
    double redDistance = 0.0;
    double blueDistance = 0.0;
    for (int i = 0; i < iterations; i++) {
      Graph problem = GraphGenerator.generateCompletelyConnectedGraph(100);
      RBPMSolution solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, true, true);
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
