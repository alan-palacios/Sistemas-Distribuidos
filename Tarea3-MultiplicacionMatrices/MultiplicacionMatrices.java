import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * MultiplicacionMatrices
 */
public class MultiplicacionMatrices {
	static class ServerConnection extends Thread{
		double[][] matriz1 = new double[N][N];
		double[][] matriz2 = new double[N][N];
		double[][] matrizResultado = new double[N/2][N/2];
		Socket connection;
		DataOutputStream dataOut;
		DataInputStream dataIn;
		int nodo;
		int port;

		ServerConnection(int nodo, double[][]matriz1, double[][]matriz2){
			System.out.println("crea");
			connection = null;
			this.nodo= nodo;
			this.matriz1= matriz1;
			this.matriz2= matriz2;
			port = 50000+nodo;
		}

		public void run() {
			for(;;){
				try {
					connection = new Socket("localhost", port);	
					//if the connection is successful we create the channels
					dataOut = new DataOutputStream(connection.getOutputStream());
					dataIn = new DataInputStream(connection.getInputStream());
					
					System.out.println("se envia "+nodo);
					//Enviar matriz1 al nodo 1
					try{
						for(int i = 0; i < N/2; i++){
							for(int j = 0; j < N; j++){
								dataOut.writeDouble(matriz1[i][j]);
							}
						}
					}catch(Exception e){
						System.out.println(e);
						System.out.println("Error al escribir la Matriz 1");
					}

					//dataOut.writeDouble(nodo);
					//Enviar matriz2 al nodo 1
					try{
						for(int i = 0; i < N/2; i++){
							for(int j = 0; j < N; j++){
								dataOut.writeDouble(matriz2[i][j]);
							}
						}
					}catch(Exception e){
						System.out.println("Error al enviar la Matriz 2");
					}

					//dataOut.writeDouble(nodo);
					//Recibir matrizResultado del nodo 1
					
					try{
						for(int i = 0; i < N/2; i++){
							for(int j = 0; j < N/2; j++){
								matrizResultado[i][j] = dataIn.readDouble();
							}
						}
					}catch(Exception e){
						System.out.println("Error al recibir la matriz resultado");
					}

					imprimirMatriz(matrizResultado, "Matriz Multiplicada");
					
					//double res = dataIn.readDouble();
					//System.out.println("el producto es: "+res);
					//cerrar conexion
					connection.close();
					break;
				} catch (Exception e) {
					try {
						//System.out.println("Reintentando conexion con Server"+nodo+":"+port+"...");
						Thread.sleep(100);
					} catch (Exception errorThread) {
						System.out.println(errorThread);
					}
				}
			}
		}

	}

	//N par
	static int N=8;
	static int node=0;
	static double[][] A = new double[N][N];
	static double[][] B = new double[N][N];
	//C=AxB
	static double[][] C = new double[N][N];
	
	static int port1 = 50001;
	static int port2 = 50002;
	static int port3 = 50003;

	public static void main(String[] args) {
		node = Integer.parseInt(args[0]);
		N = Integer.parseInt(args[1]);
		if(node==0){
			runNode0();	
		}else{//nodos 1 al 3 servers
			int port = 50000+node; 
			ServerSocket servidor = null;
			System.out.println("Servidor en puerto "+port);
			try {
				servidor = new ServerSocket(port);
				System.out.println("Esperando cliente");
	    		//esperar la conexión del cliente 
				Socket clientConnection = servidor.accept();
				System.out.println("Cliente conectado");
				DataOutputStream salida = new DataOutputStream(clientConnection.getOutputStream());
				DataInputStream entrada = new DataInputStream(clientConnection.getInputStream());
				//leer num1
				//double num1=entrada.readDouble();
				double[][] matrizTemporal1 = new double[N/2][N];
				double[][] matrizTemporal2 = new double[N][N/2];
				try{
					for(int i = 0; i < N/2; i++){
						for(int j = 0; j < N; j++){
							matrizTemporal1[i][j] = entrada.readDouble();
						}
					}
				}catch(Exception e){
					System.out.println("Error al recibir la matriz 1");
				}
				//double num2=entrada.readDouble();
				try{
					for(int i = 0; i < N; i++){
						for(int j = 0; j < N/2; j++){
							matrizTemporal2[i][j] = entrada.readDouble();
						}
					}
				}catch(Exception e){
					System.out.println("Error al recibir la matriz 2");
				}

				//double producto = num1*num2;
				double[][] matrizMultiplicada = new double[N/2][N/2];
				imprimirMatriz(matrizTemporal1, "Matriz Temporal1");
				imprimirMatriz(matrizTemporal2, "Matriz Temporal2");
				matrizMultiplicada = multiplicarMatrices(matrizTemporal1, matrizTemporal2);
				try{
					for(int i = 0; i < N/2; i++){
						for(int j = 0; j < N/2; j++){
							salida.writeDouble(matrizMultiplicada[i][j]);
						}
					}
				}catch(Exception e){
					System.out.println("Error al enviar la Matriz Multiplicada");
				}

				imprimirMatriz(matrizMultiplicada, "Matriz Multiplicada antes");
				System.out.println("Matriz Multiplicada enviada" + node);
				//salida.writeDouble(producto);
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
	
	static void runNode0() {
		try {
			//inicializar matrices A y B
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					A[i][j] = i+5*j;
					B[i][j] = 5*i-j;
					C[i][j] = 0;
				}
			}
			//Transponer matriz B. 
			//Dejar la transpuesta en la misma matriz B
			transponerMatriz(B);
			//dividir matrices
			double[][] A1 = new double[N/2][N];
			double[][] A2 = new double[N/2][N];
			double[][] B1 = new double[N/2][N];
			double[][] B2 = new double[N/2][N];
			dividirMatriz(A, A1, A2);
			dividirMatriz(B, B1, B2);

			//Enviar matriz A1 y B1 al nodo 1 y recibir matriz C1=A1xB1
			ServerConnection connection1 = new ServerConnection(1, A1, B1);
			connection1.start();
			//Enviar matriz A1 y B2 al nodo 2 y recibir matriz C2=A1xB2 
			ServerConnection connection2 = new ServerConnection(2, A1, B2);
			connection2.start();
			//Enviar matriz A2 y B1 al nodo 3 y recibir matriz C3=A2xB1
			ServerConnection connection3 = new ServerConnection(3, A2, B1);
			connection3.start();
			//Realizar el producto C4=A2xB2 (renglon por renglon)
			double[][] C4 = multiplicarMatricesRenglon(A2, B2);
			//esperar a recibir todos los calculos
			connection1.join();
			connection2.join();
			connection3.join();
			
			double[][] C1 = connection1.matrizResultado;
			double[][] C2 = connection2.matrizResultado;
			double[][] C3 = connection3.matrizResultado;
			//unir C1, C2, C3, C4

			imprimirMatriz(C1, "Matriz C1");
			imprimirMatriz(C2, "Matriz C2");
			imprimirMatriz(C3, "Matriz C3");
			imprimirMatriz(C4, "Matriz C4");

			int conti = 0;
			int contj = 0;

			for(int i = 0; i < N/2; i++){
				contj = 0;
				for(int j = 0; j < N/2; j++){
					C[i][j] = C1[conti][contj];
					System.out.println(C1[conti][contj]);
					contj++;
				}
				conti++;
				System.out.println("Fila agregada");
			}

			System.out.println("Fragmento C1 agregado");

			conti = 0;
			contj = 0;

			for(int i = N/2; i < N; i++){
				contj = 0;
				for(int j = 0; j < N/2; j++){
					C[i][j] = C2[conti][contj];
					contj++;
				}
				conti++;
			}

			System.out.println("Fragmento C2 agregado");

			conti = 0;
			contj = 0;

			for(int i = 0; i < N/2; i++){
				contj = 0;
				for(int j = N/2; j < N; j++){
					C[i][j] = C3[conti][contj];
					contj++;
				}
				conti++;
			}

			System.out.println("Fragmento C3 agregado");

			conti = 0;
			contj = 0;

			for(int i = N/2; i < N; i++){
				contj = 0;
				for(int j = N/2; j < N; j++){
					C[i][j] = C4[conti][contj];
					contj++;
				}
				conti++;
			}

			System.out.println("Fragmento C4 agregado");


			/*
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if(i<N/2){ //arriba
						if(j<N/2) //izquierda
							C[i][j] = C1[i][j];
						else //derecha
							C[i][j] = C2[i][j];
					}else{ //abajo
						if(j<N/2)
							C[i][j] = C3[i][j];
						else
							C[i][j] = C4[i][j];
					}
				}
			}
			*/
			//calcular el checksum de matriz C
			if(N==8){
				//si N=8 entonces desplegar las matrices A, B y C	
				imprimirMatriz(A, "matriz A");
				imprimirMatriz(B, "matriz B");
				imprimirMatriz(C, "matriz C");
				//mostrar checksum
				System.out.println("Checksum: "+calcularChecksum(C));

			}else if(N==1000){
				//mostrar checksum
				System.out.println("Checksum: "+calcularChecksum(C));
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	static void dividirMatriz(double[][] matrizOriginal, double[][] matriz1, double[][] matriz2){
		for (int i = 0; i < N; i++) {
			if(i<N/2){
				matriz1[i]=matrizOriginal[i];
			}else{
				matriz2[i-N/2]=matrizOriginal[i];
			}
		}
	}

	static double calcularChecksum(double[][] matriz) {
		double checksum=0;
		for (double[] ds : matriz) {
			for (double d : ds) {
				checksum+=d;	
			}
		}
		return checksum;
	}
	static void transponerMatriz(double[][] matriz) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < i; j++) {
				double x = matriz[i][j];
				matriz[i][j] = matriz[j][i];
				matriz[j][i] = x;
			}
		}
	}
	static double[][] multiplicarMatricesRenglon(double[][] matriz1, double[][] matriz2){
		double[][] res = new double[matriz1.length][matriz2.length];
		if(matriz1[0].length == matriz2[0].length){
			for (int i = 0; i < matriz1.length; i++) {
				for (int j = 0; j < matriz2.length; j++) {
					for (int k = 0; k < matriz1[0].length; k++) {
						res[i][j] += matriz1[i][k] * matriz2[j][k];
					}
				}
			}

		}
		return res;
	}

	static double[][] multiplicarMatrices(double[][] matriz1, double[][] matriz2){
		double[][] res = new double[matriz1.length][matriz2[0].length];
		// se comprueba si las matrices se pueden multiplicar
		if (matriz1[0].length == matriz2.length) {
			for (int i = 0; i < matriz1.length; i++) {
				for (int j = 0; j < matriz2[0].length; j++) {
					for (int k = 0; k < matriz1[0].length; k++) {
						res[i][j] += matriz1[i][k] * matriz2[k][j];
					}
				}
			}
		}
		return res;
	}

	static void imprimirMatriz(double[][] matriz, String mensaje) {
		System.out.println("\n"+mensaje);
		for (int x=0; x < matriz.length; x++){
			for (int y=0; y < matriz[x].length; y++)
				System.out.print(matriz[x][y]+ "|");   
			System.out.println("\n----------------------------------------");
		}
	}
}