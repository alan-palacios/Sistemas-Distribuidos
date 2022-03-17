package EjemploChat;

import java.net.InetAddress;

public class User {
    String user;
    InetAddress IP;
    int port;
    
    User(String user, InetAddress IP, int port){
       this.user=user;
       this.IP=IP;
       this.port=port;
    }
}
