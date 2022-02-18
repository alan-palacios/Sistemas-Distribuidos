import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

// java -Djavax.net.ssl.keyStore=keystore_servidor.jks -Djavax.net.ssl.keyStorePassword=1234567 ServidorSSL
public class ServidorSSL {
	public static void main(String[] args) {
		connectToServer();
	}

	static void connectToServer() {
		SSLServerSocketFactory socket_factory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		ServerSocket server_socket = null;
		//for infinito hasta que se conecta al servidor
		try {
			server_socket = socket_factory.createServerSocket(50001);	
			Socket conexion = server_socket.accept();
			//Se crean canales de entrada y salida
			DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
			DataInputStream entrada = new DataInputStream(conexion.getInputStream());

			String filename = entrada.readUTF();
			int longitud = entrada.readInt();
			byte[] buffer = new byte[longitud];
			read(entrada, buffer, 0, longitud);
			write_file("server_"+filename, buffer);
			Thread.sleep(1000);
			conexion.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void write_file(String filename, byte[] buffer) throws Exception {
		FileOutputStream f = new FileOutputStream(filename);
		try {
			f.write(buffer);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			f.close();
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