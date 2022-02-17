import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Cliente
 */
public class Cliente {
	public static void main(String[] args) {
		connectToServer(args[0]);
	}

	static void connectToServer(String mensaje) {
		Socket conexion = null;
		//for infinito hasta que se conecta al servidor
		for(;;)
			try {
				conexion = new Socket("localhost", 50001);	
				System.out.println('a');
				//Se crean canales de entrada y salida
				DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
				DataInputStream entrada = new DataInputStream(conexion.getInputStream());
				//envío de información
				System.out.println("enviando mensaje: "+mensaje);
				salida.write(mensaje.getBytes());

				//Recibe información del server
				byte[] buffer = new byte[6];
				//Metodo read normal
				read(entrada, buffer, 0, 6);
				System.out.println(new String(buffer, "UTF-8"));

				//cerrar conexion
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

	static void read(DataInputStream f, byte [] b, int posicion, int longitud) throws Exception{
		while (longitud>0) {
			int n = f.read(b, posicion, longitud);
			posicion += n;
			longitud -= n;
		}	
	}
}