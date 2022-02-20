package Multithreading;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor
 */
public class Servidor {
	public static void main(String[] args) {
		int port = 50001; 
		ServerSocket servidor = null;
		System.out.println("Servidor en puerto "+port);
		try {
			servidor = new ServerSocket(port);
			for(;;){
				System.out.println("Esperando cliente");
				Socket conexion = servidor.accept();
				//crear hilo
				Worker worker = new Worker(conexion);
				worker.start();
			}
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
