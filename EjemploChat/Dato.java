package EjemploChat;

import java.io.Serializable;

public class Dato implements Serializable{
    int n;//Number of package
    byte[] data;//Information
    int t;//Total packages sent
  
  public Dato(int n, byte[] b, int t){
      this.n=n;
      data=b;
      this.t=t;   
  }

}
