public class Edge {
  private Vertex a, b;

  public Edge(Vertex a, Vertex b){
    this.a = a;
    this.b = b;
  }

  @Override
  public boolean equals(Object o){
    if(o == null) return false;
    if(this == o) return true;
    if(o.getClass() != Edge.class) return false;
    Edge e = (Edge) o;
    return (a.equals(e.a) && b.equals(e.b))
            || (a.equals(e.b) && b.equals(e.a));
  }

  @Override
  public int hashCode(){
    return a.hashCode() + b.hashCode();
  }

  public Vertex getA() {
    return a;
  }

  public Vertex getB() {
    return b;
  }
}
