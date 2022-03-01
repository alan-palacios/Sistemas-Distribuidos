import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.*;

public class TokenRing{
	static class RingConnection extends Thread{
		static Object sync= new Object();
    	static boolean canSend = false;
    	static boolean finish = false;
		static int token = 0;
	}

	static class ServerConnection extends RingConnection{
		Socket connection;
		DataOutputStream dataOut;
		DataInputStream dataIn;
		int node;
		int prevNode;
		int port;

		ServerConnection(int node){
			connection = null;
			this.node= node;
			prevNode = (node+5)%6;
			port = 50000+node;
		}

		public void run() {
			// Creando la instancia del Socket Seguro
            //conectarse al servidor "localhost" al puerto 50000+nodo con re-intentos
			ServerSocket servidor = null;
			System.out.println("Server: Servidor en puerto "+port);
			try {
				SSLServerSocketFactory socket_factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
				servidor = socket_factory.createServerSocket(port);
				System.out.println("Server: Esperando cliente");
	  			//esperar la conexión del cliente 
				Socket clientConnection = servidor.accept();
				System.out.println("Server: Cliente conectado");
				DataInputStream entrada = new DataInputStream(clientConnection.getInputStream());
				for(;;){
					//envia la sumatoria al cliente
					int tokenReceived = entrada.readInt();
					System.out.println("Server: Token recibido "+tokenReceived);
					if(tokenReceived>=500){
						finish=true;
						break;
					}
					if(tokenReceived==-2) break;
					synchronized(sync){
						token = tokenReceived+1;
						canSend= true;
					}
				}
				//cerrar conexion
				clientConnection.close();
			} catch (Exception e) {
				System.out.println(e);
			} finally{
				try {
					servidor.close();
				} catch (Exception e2) {
					System.out.println(e2);
				}
			}
		}
	}

	static class NextNodeConnection extends RingConnection{
		Socket connection;
		DataOutputStream dataOut;
		DataInputStream dataIn;
		int node;
		int port;
		int nextNode;

		NextNodeConnection(int node){
			connection = null;
			this.node= node;
			if(node==0) canSend=true;
			nextNode = (node+1)%6;
			port = 50000+nextNode;
		}

		public void run() {
            //conectarse al servidor "localhost" al puerto 50000+siguiente nodo con re-intentos
			System.out.println("Cliente: Intentando conexion con Server"+nextNode+":"+port+"...");
			for(;;){
				try {
					SSLSocketFactory cliente = (SSLSocketFactory) SSLSocketFactory.getDefault();
					connection = cliente.createSocket("localhost", port);	
					dataOut = new DataOutputStream(connection.getOutputStream());
					dataIn = new DataInputStream(connection.getInputStream());
					//una vez que reciba el token anterior envia el token aumentado al siguiente nodo
					for(;;){
						if(finish) break;
						synchronized(sync){
							if(canSend){
								System.out.println("Cliente: enviando token "+token);
								dataOut.writeInt(token);
								canSend=false;
							}
						}
						Thread.sleep(100);
					}
					////cerrar conexion
					connection.close();
					break;
				} catch (Exception e) {
					try {
						//System.out.println("Reintentando conexion con Server"+nextNode+":"+port+"...");
						Thread.sleep(100);
					} catch (Exception errorThread) {
						System.out.println(errorThread);
					}
				}
			}
		}
	}

	public static void main(String[] args){
	    int node = Integer.parseInt(args[0]);
		//inicia un servidor para escuchar al nodo anterior
		ServerConnection serverConnection = new ServerConnection(node);
		//intenta conectarse con el siguiente nodo
		NextNodeConnection nextConnection = new NextNodeConnection(node);
		try {
			serverConnection.start(); 
			nextConnection.start(); 
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
