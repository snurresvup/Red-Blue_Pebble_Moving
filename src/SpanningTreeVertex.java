import java.util.Map;

public class SpanningTreeVertex {
  private Vertex modelee;

  public SpanningTreeVertex(Vertex modelee) {
    this.modelee = modelee;

  }

  public Vertex getModelee() {
    return modelee;
  }

  @Override
  public boolean equals(Object o){
    if(o == null) return false;
    if(o == this) return true;
    if(o.getClass() != SpanningTreeVertex.class) return false;
    SpanningTreeVertex other = (SpanningTreeVertex)o;
    return other.modelee.equals(modelee);
  }

  @Override
  public int hashCode(){
    return modelee.hashCode() * 17;
  }
}
