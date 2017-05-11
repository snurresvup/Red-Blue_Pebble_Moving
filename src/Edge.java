public class Edge<T> {
  private T a, b;

  public Edge(T a, T b){
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

  public T getA() {
    return a;
  }

  public T getB() {
    return b;
  }
}
