import org.graphstream.algorithm.APSP;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

  public static void outputArrayRepresentationToFile(Graph problem, int iteration, int maxIterations, String filename){
    problem.computeAPSPForGraph();

    ArrayList<Vertex> allVertices = new ArrayList<>(problem.getVertices());

    Vertex source = new StartVertex(new Pebble(PebbleColor.RED));
    Vertex sink = new TargetVertex();
    Vertex[] startVertices = problem.getVertices().stream().filter(vertex -> vertex instanceof StartVertex).toArray(Vertex[]::new);
    Vertex[] targetVertices = problem.getVertices().stream().filter(vertex -> vertex instanceof TargetVertex).toArray(Vertex[]::new);
    int problemSizeSquared = (int) Math.pow(problem.getVertices().size()/2,2);

    Vertex[] startNodes = constructStartNodesArray(source, startVertices, targetVertices, problemSizeSquared);

    Vertex[] endNodes = constructEndNodesArray(sink, startVertices, targetVertices, problemSizeSquared);

    int[] capacities = new int[startVertices.length + problemSizeSquared + targetVertices.length];
    Arrays.fill(capacities, 1);

    int[] costs = constructCostsArray(problem, startVertices, targetVertices, problemSizeSquared);

    int[] supplies = new int[2 + startVertices.length + targetVertices.length];
    Arrays.fill(supplies, 0);
    supplies[0] = startVertices.length;
    supplies[supplies.length-1] = -startVertices.length;

    JSONObject jsonObject = new JSONObject();
    JSONArray jsonStartNodes = new JSONArray();
    for (Vertex i: startNodes) {
      if(i.equals(source)) jsonStartNodes.add(0);
      else if (i.equals(sink)) jsonStartNodes.add(allVertices.size()+1);
      else jsonStartNodes.add(allVertices.indexOf(i)+1);//Long.parseLong(i.toString(),16));
    }

    JSONArray jsonEndNodes = new JSONArray();
    for(Vertex i: endNodes) {
      if(i.equals(source)) jsonEndNodes.add(0);
      else if(i.equals(sink)) jsonEndNodes.add(allVertices.size()+1);
      else jsonEndNodes.add(allVertices.indexOf(i)+1);//Long.parseLong(i.toString(),16));
    }

    JSONArray jsonCapacities = new JSONArray();
    for (int i : capacities) {
      jsonCapacities.add(i);
    }

    JSONArray jsonCosts = new JSONArray();
    for (int i: costs) {
      jsonCosts.add(i);
    }

    JSONArray jsonSupplies = new JSONArray();
    for (int i: supplies){
      jsonSupplies.add(i);
    }

    jsonObject.put("start_nodes", jsonStartNodes);
    jsonObject.put("end_nodes", jsonEndNodes);
    jsonObject.put("capacities", jsonCapacities);
    jsonObject.put("costs", jsonCosts);
    jsonObject.put("supplies", jsonSupplies);

    //System.out.println(jsonObject.toJSONString());

    try{
      if(iteration > 0 && iteration < maxIterations) {
        Files.write(Paths.get(filename + ".json"), (jsonObject.toJSONString() + ",").getBytes(), StandardOpenOption.APPEND);
      }else if(iteration == maxIterations) {
        Files.write(Paths.get(filename + ".json"), (jsonObject.toJSONString() + "]").getBytes(), StandardOpenOption.APPEND);
      } else {
        FileWriter fileWriter = new FileWriter(filename + ".json");
        fileWriter.write("[" + jsonObject.toJSONString() + ",");
        fileWriter.flush();
        fileWriter.close();
      }
      //FileWriter file = new FileWriter(filename + ".json");
      //file.write(jsonObject.toJSONString());
      //file.flush();
      //file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static int[] constructCostsArray(Graph problem, Vertex[] startVertices, Vertex[] targetVertices, int problemSizeSquared) {
    int[] costs = new int[startVertices.length + problemSizeSquared + targetVertices.length];

    for (int i = 0; i < startVertices.length; i++) {
      costs[i] = 0;
    }

    int offset = startVertices.length;

    for (int i = 0; i < startVertices.length; i++) {
      Vertex sv = startVertices[i];
      APSP.APSPInfo info = problem.getGSGraph().getNode(sv.toString()).getAttribute(APSP.APSPInfo.ATTRIBUTE_NAME);
      for (int k = 0; k < targetVertices.length; k++) {
        Vertex tv = targetVertices[k];
        costs[offset + i*startVertices.length + k] = (int) info.getLengthTo(tv.toString());
      }
    }

    offset = startVertices.length + problemSizeSquared;
    for (int i = 0; i < targetVertices.length; i++) {
      costs[offset + i] = 0;
    }

    return costs;
  }

  private static Vertex[] constructEndNodesArray(Vertex sink, Vertex[] startVertices, Vertex[] targetVertices, int problemSizeSquared) {
    Vertex[] e1 = startVertices.clone();
    Vertex[] e2 = new Vertex[problemSizeSquared];
    for (int i = 0; i < startVertices.length; i++) {
      for (int k = 0; k < targetVertices.length; k++) {
        e2[i*(startVertices.length)+k] = targetVertices[k];
      }
    }
    Vertex[] e3 = new Vertex[targetVertices.length];
    Arrays.fill(e3, sink);

    return concatAll(e1, e2, e3);
  }

  private static Vertex[] constructStartNodesArray(Vertex source, Vertex[] startVertices, Vertex[] targetVertices, int problemSizeSquared) {
    Vertex[] s1 = new Vertex[startVertices.length];
    Arrays.fill(s1, source);
    Vertex[] s2 = new Vertex[problemSizeSquared];
    for (int i = 0; i < startVertices.length; i++) {
      for (int k = 0; k < targetVertices.length; k++) {
        s2[i*(targetVertices.length)+k] = startVertices[i];
      }
    }
    Vertex[] s3 = targetVertices.clone();

    return concatAll(s1,s2,s3);
  }

  public static <T> T[] concatAll(T[] first, T[]... rest) {
    int totalLength = first.length;
    for (T[] array : rest) {
      totalLength += array.length;
    }
    T[] result = Arrays.copyOf(first, totalLength);
    int offset = first.length;
    for (T[] array : rest) {
      System.arraycopy(array, 0, result, offset, array.length);
      offset += array.length;
    }
    return result;
  }

  public static void resetGraphToOriginalState(Graph problem){
    ArrayList<Pebble> redPebbles = new ArrayList<>();
    for(Vertex v:problem.getVertices()){
      Pebble redPebble = v.getPebble(PebbleColor.RED);
      if(redPebble != null){
        redPebble.getCurrentVertex().removePebble(redPebble);
        redPebbles.add(redPebble);
      }
    }
    for(Pebble p:redPebbles){
      p.getOriginalVertex().addPebble(p);
    }
    problem.getBluePebble().getCurrentVertex().removePebble(problem.getBluePebble());
    problem.getBluePebble().getOriginalVertex().addPebble(problem.getBluePebble());
  }
}
