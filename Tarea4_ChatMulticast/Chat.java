
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

import javax.swing.MenuSelectionManager;

public class Chat {
	static String ip = "230.0.0.0";
	static int port = 50000;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	static class Worker extends Thread {
		// en un ciclo infinito se recibiran los mensajes
		//enviados al grupo 230.0.0.0:50000 y se desplegarán
		//en la pantalla
		@Override
		public void run() {
			try {
				MulticastSocket socket = new MulticastSocket(port);
				InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName(ip), port);
				NetworkInterface netInter = NetworkInterface.getByName("em1");
				socket.joinGroup(grupo, netInter);
				while (true) {
					byte[] a = recibe_mensaje_multicast(socket, 40);
					String mensaje = new String(a, "UTF-8");
					if(mensaje=="exit") break;
					System.out.println(ANSI_GREEN+mensaje+ANSI_RESET);

				}
				socket.leaveGroup(grupo, netInter);
				socket.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
	}
	
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");	
		new Worker().start();
		String nombre = args[0];
		System.out.println("Ingrese el mensaje a enviar: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			//en un ciclo infinito se leerá cada mensaje del teclado y
			//se enviará al grupo 230.0.0.0:50000
			try {
				Thread.sleep(100);
				String s = br.readLine();
				String mensaje = nombre+" dice "+s;
				envia_mensaje_multicast(mensaje.getBytes(), ip, port);
			}catch(Exception e) {
				System.out.println(e);
			}
		}
	}

	static void envia_mensaje_multicast(byte[] buffer, String ip, int puerto) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		socket.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), puerto));
		socket.close();
	}
	static byte[] recibe_mensaje_multicast(MulticastSocket socket, int longitud_mensaje) throws IOException{
		byte[] buffer = new byte[longitud_mensaje];
		DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
		socket.receive(paquete);
		return paquete.getData();
	}
}
