import java.io.*;

public class Experiments {
  public static final double ITERATIONS = 40.0;
  public static final int[] PROBLEM_SIZES = new int[]{1,2,4,8,16,32,64,128,256};
  public static void main(String[] args) {
    //experimentCCGraphs();
    //System.out.println();
    //compareOnSimplePath();
    //System.out.println();
    //compareRandomPathOnAlgorithms();
    System.out.println("Starting test...");
    //compareSpanningTreeVariationsOnCC("TheGreatTest");
    compareSpanningTreeVariationsOnRandomGraphs("Random graphs");
    //compareAllOnPathGraphs("Path tests");
    //timeAlgorithms("Timing test");
  }

  public static void timeAlgorithms(String filename){
    PrintWriter simpleWrither = null;

    try {
      simpleWrither = new PrintWriter(filename);
      simpleWrither.println("Iterations," + ITERATIONS);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    //Time on random path graphs
    simpleWrither.println("Time on random path graphs");

    for (int k = 0; k < 6; k++) {
      printAlgorithmSignature(simpleWrither, k);

      for(int problemSize : PROBLEM_SIZES){
        int timeSpent = 0;

        for (int i = 0; i < ITERATIONS; i++) {
          PathGraph problem = GraphGenerator.generateRandomPathGraph(problemSize);
          RBPMSolution solution;
          long startTime = 0;
          long endTime = 0;
          switch (k){
            case 0:
              startTime = System.currentTimeMillis();
              solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, false, false);
              endTime = System.currentTimeMillis();
              break;
            case 1:
              startTime = System.currentTimeMillis();
              solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, false, true);
              endTime = System.currentTimeMillis();
              break;
            case 2:
              startTime = System.currentTimeMillis();
              solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, true, false);
              endTime = System.currentTimeMillis();
              break;
            case 3:
              startTime = System.currentTimeMillis();
              solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, true, true);
              endTime = System.currentTimeMillis();
              break;
            case 4:
              startTime = System.currentTimeMillis();
              solution = PebbleSolver.computeSolution(problem);
              endTime = System.currentTimeMillis();
              break;
            case 5:
              startTime = System.currentTimeMillis();
              solution = PebbleSolver.computeFastSolution(problem);
              endTime = System.currentTimeMillis();
              break;
          }
          timeSpent += endTime - startTime;
          if(i < 5) timeSpent = 0;
        }
        double avgTime = timeSpent/(ITERATIONS-5);
        simpleWrither.println(convertToAlgorithmSignature(k) + "," + "Random Path," + problemSize + "," + avgTime);
      }
    }

    //Time on random graphs
    simpleWrither.println("Time on random graphs");

    for (int k = 0; k < 4; k++) {
      printAlgorithmSignature(simpleWrither, k);

      for(int problemSize : PROBLEM_SIZES){
        int timeSpent = 0;

        for (int i = 0; i < ITERATIONS; i++) {
          Graph problem = GraphGenerator.generateRandomGraph(problemSize, 0.5);
          RBPMSolution solution;
          long startTime = 0;
          long endTime = 0;
          switch (k){
            case 0:
              startTime = System.nanoTime();
              solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, false, false);
              endTime = System.nanoTime();
              break;
            case 1:
              startTime = System.nanoTime();
              solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, false, true);
              endTime = System.nanoTime();
              break;
            case 2:
              startTime = System.nanoTime();
              solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, true, false);
              endTime = System.nanoTime();
              break;
            case 3:
              startTime = System.nanoTime();
              solution = PebbleSolver.spanningTreeBasedAlgorithm(problem, true, true);
              endTime = System.nanoTime();
              break;
          }
          timeSpent += endTime - startTime;
        }
        double avgTime = timeSpent/ITERATIONS;
        simpleWrither.println(convertToAlgorithmSignature(k) + "," + "Random Graph," + problemSize + "," + avgTime);
      }
    }


    simpleWrither.flush();
    simpleWrither.close();
  }

  private static String convertToAlgorithmSignature(int k){
    switch (k){
      case 0:
        return "STNP";
      case 1:
        return "STP";
      case 2:
        return "MSTNP";
      case 3:
        return "MSTP";
      case 4:
        return "Simple Path";
      case 5:
        return "Improved Path";
      default:
        throw new IllegalArgumentException();
    }
  }

  public static void compareAllOnPathGraphs(String fileName){
    int pickups, redDistance, blueDistance;

    PrintWriter printWriter = null;
    PrintWriter simpleWriter = null;
    try {
      printWriter = new PrintWriter(fileName);
      simpleWriter = new PrintWriter(fileName + "simple");
      printWriter.println("Iterations," + (int) ITERATIONS);
      simpleWriter.println("Iterations," + (int) ITERATIONS);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    for(int j = 0; j < 2; j++) {
      if(j == 0) {
        printWriter.println("Graph type Simple Path");
        simpleWriter.println("Graph type Simple Path");
      }else{
        printWriter.println("Graph type Random Path");
        simpleWriter.println("Graph type Random Path");
      }
      for (int k = 0; k < 6; k++) {

        printAlgorithmSignature(printWriter, simpleWriter, k);

        for (int problemSize : PROBLEM_SIZES) {
          pickups = 0;
          redDistance = 0;
          blueDistance = 0;

          for (int i = 0; i < ITERATIONS; i++) {
            PathGraph problem = j == 0 ? GraphGenerator.generateSimplePathGraph(problemSize) : GraphGenerator.generateRandomPathGraph(problemSize);
            RBPMSolution solution;
            System.out.println(i + " , " + k + "problem size: " + problemSize);

            if(k<4) {
              solution = computeSolutionUsingAlgorithm(k, problem);
            } else if(k == 4){
              solution = PebbleSolver.computeSolution(problem);
            } else {
              solution = PebbleSolver.computeSolution(problem);
            }

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
    }

    simpleWriter.flush();
    simpleWriter.close();
    printWriter.flush();
    printWriter.close();
  }

  public static void compareSpanningTreeVariationsOnRandomGraphs(String fileName){
    double pickups, redDistance, blueDistance;

    PrintWriter printWriter = null;
    PrintWriter simpleWriter = null;
    try {
      printWriter = new PrintWriter(fileName);
      simpleWriter = new PrintWriter(fileName + "simple");
      printWriter.println("Iterations," + (int) ITERATIONS);
      simpleWriter.println("Iterations," + (int) ITERATIONS);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    for (int k = 0; k < 4; k++) {

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

            solution = computeSolutionUsingAlgorithm(k, problem);

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

  private static void printAlgorithmSignature(PrintWriter simpleWriter, int k) {
    switch (k) {
      case 0:
        simpleWriter.println("STNP");
        System.out.println("STNP");
        break;
      case 1:
        simpleWriter.println("STP");
        System.out.println("STP");
        break;
      case 2:
        simpleWriter.println("MSTNP");
        System.out.println("MSTNP");
        break;
      case 3:
        simpleWriter.println("MSTP");
        System.out.println("MSTP");
        break;
      case 4:
        simpleWriter.println("SimplePath");
        System.out.println("SimplePath");
        break;
      case 5:
        simpleWriter.println("ImpPath");
        System.out.println("ImpPath");
        break;
    }
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
      case 4:
        printWriter.println("SimplePath");
        simpleWriter.println("SimplePath");
        System.out.println("SimplePath");
        break;
      case 5:
        printWriter.println("ImpPath");
        simpleWriter.println("ImpPath");
        System.out.println("ImpPath");
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
