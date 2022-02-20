package Multithreading;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Worker extends Thread {
	Socket conexion;
	Worker(Socket conexion){
		this.conexion=conexion;
	}

	public void run() {
		System.out.println("Cliente conectado");
		try {
			//Se crean canales de entrada y salida
			DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
			DataInputStream entrada = new DataInputStream(conexion.getInputStream());
			//leer cadena
			byte[] buffer = new byte[8];
			read(entrada, buffer, 0, 8);
			System.out.println(new String(buffer, "UTF-8"));
			salida.write("SERVER".getBytes());

			//cerrar conexion
			conexion.close();
			System.out.println("Cliente desconectado");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	static void read(DataInputStream f, byte [] b, int posicion, int longitud) throws Exception{
		while (longitud>0) {
			int n = f.read(b, posicion, longitud);
			posicion += n;
			longitud -= n;
		}	
	}
}
