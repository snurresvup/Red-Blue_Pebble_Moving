import java.util.Map;
import java.util.Set;

public class PathGraph extends Graph {

  public PathGraph(Pebble bluePebble) {
    super(bluePebble);
  }

  @Override
  public void addVertex(Vertex addedVertex, Map<Vertex, Integer> connections) {
    if(connections.size() > 2) {
      throw new IllegalArgumentException("A vertex on a path can have no more than two connections");
    }
    for(Vertex v : connections.keySet()){
      if(v.getEdges().size() > 1 && !v.getEdges().containsKey(addedVertex)) {
        throw new IllegalArgumentException("A vertex on a path can have no more than two connections");
      }
    }

    super.addVertex(addedVertex, connections);
  }

  @Override
  public boolean addConnection(Vertex vertexA, Vertex vertexB, int weight) {
    if(vertexA.getEdges().size() > 1 && !vertexA.getEdges().containsKey(vertexB) ||
        vertexB.getEdges().size() > 1 && !vertexB.getEdges().containsKey(vertexA)) {
      throw new IllegalArgumentException("A vertex on a path can have no more than two connections");
    }

    return super.addConnection(vertexA, vertexB, weight);
  }

  @Override
  public Vertex getFirstVertex(){
    Vertex v = super.getFirstVertex();

    //Move v to the end of the path
    Vertex prev = v;
    while(v.getEdges().size() > 1){
      for (Vertex vertex : v.getEdges().keySet()) {
        if(!vertex.equals(prev)) {
          prev = v;
          v = vertex;
          break;
        }
      }
    }

    return v;
  }
}
