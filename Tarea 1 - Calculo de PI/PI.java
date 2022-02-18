import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PI{
	static class ServerConnection extends Thread{
		static Object sync= new Object();
    	static double pi = 0;
		Socket connection;
		DataOutputStream dataOut;
		DataInputStream dataIn;
		int nodo;
		int port;

		ServerConnection(int nodo){
			connection = null;
			this.nodo= nodo;
			port = 40000+nodo;
		}

		public void tryConnection() throws Exception{
			connection = new Socket("localhost", port);	
			//if the connection is successful we create the channels
			dataOut = new DataOutputStream(connection.getOutputStream());
			dataIn = new DataInputStream(connection.getInputStream());
		}

		public void run() {
            //conectarse al servidor "localhost" al puerto 50000+nodo con re-intentos
			for(;;){
				try {
					tryConnection();	
            		//Esperar la sumatoria
            		
            		
            		
					double sumatoria = dataIn.readDouble();
            		//pi += sumatoria; //sincronizarlo
					synchronized(sync){
						pi =sumatoria + pi;
					}
				        System.out.println("El valor de la variable pi es: "+pi);
	
					////cerrar conexion
					connection.close();
					break;
				} catch (Exception e) {
					try {
						System.out.println("Reintentando conexion con Server"+nodo+":"+port+"...");
						Thread.sleep(100);
					} catch (Exception errorThread) {
						System.out.println(errorThread);
					}
				}
			}
		}

	}


	public static void main(String[] args){
	    int nodo = Integer.parseInt(args[0]);
	    if(nodo == 0){ //CLIENTE
	        //crear 4 threads
			ServerConnection connection1 = new ServerConnection(1);
			ServerConnection connection2 = new ServerConnection(2);
			ServerConnection connection3 = new ServerConnection(3);
			ServerConnection connection4 = new ServerConnection(4);
			try {
	        	//iniciar 4 threads
				connection1.start(); 
				connection2.start(); 
				connection3.start(); 
				connection4.start(); 
	        	//esperar a que terminen los 4 threads (esto es una barrera)
				connection1.join();
				connection2.join();
				connection3.join();
				connection4.join();
	        	//desplegar el valor de la variable "pi"
				System.out.println(ServerConnection.pi);

			} catch (Exception e) {
				System.out.println(e);
			}
	    }
	    else{ //SERVERS
			int port = 40000+nodo; 
			ServerSocket servidor = null;
			System.out.println("Servidor en puerto "+port);
			try {
				servidor = new ServerSocket(port);
				System.out.println("Esperando cliente");
	    		//esperar la conexión del cliente 
				Socket clientConnection = servidor.accept();
				System.out.println("Cliente conectado");
				System.out.println("Calculando Sumatoria ...");
	    		//calcular la sumatoria
	    		double sumatoria = 0;
	    		int i = 0;
	    		while(i < 10000000){
	    			sumatoria= 4.0 / (8*i+2*(nodo-2)+3)+sumatoria;
	    			i++;
	    		}
	    		if(nodo % 2 == 0 ){
	    			sumatoria = (-1)*sumatoria;
	    		}
		//		double sumatoria = nodo*2;
	    		//envia la sumatoria al cliente
				DataOutputStream salida = new DataOutputStream(clientConnection.getOutputStream());
				System.out.println("Enviando sumatoria "+sumatoria);
				salida.writeDouble(sumatoria);
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
}
