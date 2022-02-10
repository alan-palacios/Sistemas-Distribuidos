import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Cliente
 */
public class Cliente {
	public static void main(String[] args) {
		connectToServer();
	}

	static void connectToServer() {
		Socket conexion = null;
		//for infinito hasta que se conecta al servidor
		for(;;)
			try {
				conexion = new Socket("localhost", 50001);	
				//Se crean canales de entrada y salida
				DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
				DataInputStream entrada = new DataInputStream(conexion.getInputStream());
				//envío de información
				salida.writeInt(123);
				salida.writeDouble(1234567890.1234567890);
				salida.write("hola".getBytes());

				//Recibe información del server
				byte[] buffer = new byte[4];
				//Metodo read normal
				//entrada.read(buffer, 0, 4);
				//Metodo read modificado para asegurarse de recibir el mensaje completo
				read(entrada, buffer, 0, 4);
				System.out.println(new String(buffer, "UTF-8"));

				//Enviar un conjunto de datos empaquetandolos
				// 5 datos * 8 bytes - longitud de double
				ByteBuffer byteBuffer = ByteBuffer.allocate(5*8);
				byteBuffer.putDouble(1.1);
				byteBuffer.putDouble(1.2);
				byteBuffer.putDouble(1.3);
				byteBuffer.putDouble(1.4);
				byteBuffer.putDouble(1.5);
				byte[] byteArrSend = byteBuffer.array();
				salida.write(byteArrSend);
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

	static void enviarDoubles(){
		for (int i = 0; i < 1000; i++) {
			
		}
	}
}