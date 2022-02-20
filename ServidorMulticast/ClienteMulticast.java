package ServidorMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;

/**
 * ServidorMulticast
 * go to root folder and run 
 * java ServidorMulticast/ClienteMulticast 
 */
public class ClienteMulticast {

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");	
		try {
			MulticastSocket socket = new MulticastSocket(50000);
			InetSocketAddress grupo = new InetSocketAddress(InetAddress.getByName("230.0.0.0"), 50000);
			NetworkInterface netInter = NetworkInterface.getByName("em1");
			socket.joinGroup(grupo, netInter);
			byte[] a = receive_message(socket, 4);
			System.out.println(new String(a, "UTF-8"));

			byte[] buffer = receive_message(socket, 5*8);
			ByteBuffer b = ByteBuffer.wrap(buffer);
			for (int i = 0; i < 5; i++) {
				System.out.println(b.getDouble());
			}
			socket.leaveGroup(grupo, netInter);
			socket.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}	

	public static byte[] receive_message(MulticastSocket socket, int messageLength) throws IOException {
		byte [] buffer = new byte[messageLength];
		DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
		socket.receive(paquete);
		return paquete.getData();
	}
}