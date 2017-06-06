public class Counter {
  private static Counter instance;
  private long state;

  private Counter(){
    state = 0;
  }

  public static Counter getInstance(){
    if(instance == null) instance = new Counter();
    return instance;
  }

  public long getAValue(){
    state++;
    return state;
  }
}
