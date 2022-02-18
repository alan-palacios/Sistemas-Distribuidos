import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLSocketFactory;
import javax.security.sasl.SaslException;

// java -Djavax.net.ssl.trustStore=keystore_cliente.jks -Djavax.net.ssl.trustStorePassword=1234567 ClienteSSL
public class ClienteSSL {
	public static void main(String[] args) {
		String filename = args[0];
		SSLSocketFactory cliente = (SSLSocketFactory)SSLSocketFactory.getDefault();
		Socket conexion = null;
		//for infinito hasta que se conecta al servidor
		for(;;)
			try {
				conexion = cliente.createSocket("localhost", 50001);	
				//Se crean canales de entrada y salida
				DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
				DataInputStream entrada = new DataInputStream(conexion.getInputStream());
				//leer archivo
				byte [] buffer = read_file(filename);
				//envío de información
				salida.writeUTF(filename);
				salida.writeInt(buffer.length);
				salida.write(buffer);
				Thread.sleep(1000);
				conexion.close();
				break;
			} catch (Exception e) {
				try {
					System.out.println("Reintentando conexion...");
					Thread.sleep(100);
				} catch (Exception errorThread) {
					System.out.println(errorThread);
				}
			}
	}
	public static byte[] read_file(String filename) throws Exception {
		FileInputStream f = new FileInputStream(filename);
		byte[] buffer = null;
		try {
			buffer = new byte[f.available()];
			f.read(buffer);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			f.close();
		}
		return buffer;
	}
}