import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Servidor
 */
public class Servidor {
	public static void main(String[] args) {
		int port = 50001; 
		try {
			ServerSocket servidor = new ServerSocket(port);
			System.out.println("Servidor en puerto "+port);
			System.out.println("Esperando cliente");
			Socket conexion = servidor.accept();
			//Se crean canales de entrada y salida
			DataOutputStream salida = new DataOutputStream(conexion.getOutputStream());
			DataInputStream entrada = new DataInputStream(conexion.getInputStream());
			//leer int del cliente
			int n = entrada.readInt();
			System.out.println(n);
			//leer double
			double x = entrada.readDouble();
			System.out.println(x);
			//leer cadena
			byte[] buffer = new byte[4];
			read(entrada, buffer, 0, 4);
			System.out.println(new String(buffer, "UTF-8"));
			salida.write("HOLA".getBytes());

			//leer doubles empacados
			byte[] a = new byte[5*8];
			read(entrada, a, 0, 5*8);
			ByteBuffer b = ByteBuffer.wrap(a);
			//extraemos los double
			for (int i = 0; i < 5; i++) {
				System.out.println(b.getDouble());
			}
			//leemos los double
			recibirDoubles(entrada);

			recibirDoublesEmpaquetados(entrada);
			//cerrar conexion
			conexion.close();
			servidor.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {

		}
	}

	static void read(DataInputStream f, byte [] b, int posicion, int longitud) throws Exception{
		while (longitud>0) {
			int n = f.read(b, posicion, longitud);
			posicion += n;
			longitud -= n;
		}	
	}

	static void recibirDoubles(DataInputStream entrada) throws Exception{
		long inicio = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			//leer double
			double x = entrada.readDouble();
			System.out.println(x);
		}
		long tiempo = System.currentTimeMillis() - inicio;
		System.out.println("Envio de doubles: "+tiempo);
	}

	//Resulta mas rápido recibir los doubles empaquetados aunque la impresión de los mismos 
	//provoca requiere mucho más tiempo
	static void recibirDoublesEmpaquetados(DataInputStream entrada) throws Exception{
		long inicio = System.currentTimeMillis();

		//Leer 10000 doubles empaquetados
		byte[] a = new byte[10000*8];
		read(entrada, a, 0, 10000*8);
		ByteBuffer b = ByteBuffer.wrap(a);
		//extraemos los double
		for (int i = 0; i < 10000; i++) {
			System.out.println(b.getDouble());
		}

		long tiempo = System.currentTimeMillis() - inicio;
		System.out.println("Envio de doubles empaquetados: "+tiempo);
	}
}
