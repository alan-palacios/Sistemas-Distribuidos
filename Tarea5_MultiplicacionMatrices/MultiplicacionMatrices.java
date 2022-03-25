import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;

/**
 * MultiplicacionMatrices
 */
public class MultiplicacionMatrices {
	//N par
	static int N=8;
	static int node=0;

	//urls para conneccion RMI
	static String url0="rmi://20.122.86.136:50000/matrices";
	static String url1="rmi://52.146.33.120:50000/matrices";
	static String url2="rmi://20.25.62.73:50000/matrices";
	static String url3="rmi://52.149.212.206:50000/matrices";
	static String url4="rmi://20.231.47.46:50000/matrices";

	static float[][] A;
	static float[][] B;
	//C=AxB
	static float[][] C;

	public static void main(String[] args) throws Exception {
		node = Integer.parseInt(args[0]);
		N = Integer.parseInt(args[1]);
		A = new float[N][N];
		B = new float[N][N];
		C = new float[N][N];		
		if(node==0){
			clienteRMI();	
		}else{//servers: nodos del 1 al 4 
			serverRMI();
		}
	}

	static class ServerConnection extends Thread {
		float[][] matriz1 = new float[N/4][N];
		float[] [][] divisionesMatriz = new float[4] [N/4][N];
		float[] [][] matricesResultado = new float[4] [N/4][N/4];
		int nodo;
		String url;

		//matriz1 = 1/4 matriz A
		//divisionesMatriz = todas las partes de la matriz B
		ServerConnection(int nodo, String url, float[][]matriz1, float[] [][]divisionesMatriz){
			System.out.println("crea");
			this.nodo = nodo;
			this.url = url;
			this.matriz1 = matriz1;
			this.divisionesMatriz = divisionesMatriz;
		}

		public void run() {
			try {
				//se obtiene una referencia que apunta al objeto remoto asociado a la URL
				InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);
				
				System.out.println("Obteniendo multiplicación de nodo "+nodo);
				for(int i = 0; i < divisionesMatriz.length; i++){
					matricesResultado[i] = r.multiplica_matrices(matriz1, divisionesMatriz[i]);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	static void clienteRMI() {
		try {
			//inicializar matrices A y B
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					A[i][j] = i+2*j;
					B[i][j] = 3*i-j;
					C[i][j] = 0;
				}
			}
			//Transponer matriz B. 
			//Dejar la transpuesta en la misma matriz B
			transponerMatriz(B);
			//dividir matrices
			float [] [][] divisionesA = new float[4] [N/4][N];
			float [] [][] divisionesB = new float[4] [N/4][N];

			dividirMatriz(A, divisionesA);
			dividirMatriz(B, divisionesB);

			//C1, C2, C3, C4
			ServerConnection connection1 = new ServerConnection(1, url1, divisionesA[0], divisionesB);
			connection1.start();

			//C5, C6, C7, C8
			ServerConnection connection2 = new ServerConnection(2, url2, divisionesA[1], divisionesB);
			connection2.start();

			//C9, C10, C11, C12
			ServerConnection connection3 = new ServerConnection(3, url3, divisionesA[2], divisionesB);
			connection3.start();

			//C13, C14, C15, C16
			ServerConnection connection4 = new ServerConnection(4, url4, divisionesA[3], divisionesB);
			connection4.start();

			//esperar a recibir todos los calculos
			connection1.join();
			connection2.join();
			connection3.join();
			connection4.join();
			
			float[][] [][] matricesC = new float [4][4] [N/4][N/4];
			for (int i = 0; i < 4; i++) {
				matricesC[0][i] = connection1.matricesResultado[i];
				matricesC[1][i] = connection2.matricesResultado[i];
				matricesC[2][i] = connection3.matricesResultado[i];
				matricesC[3][i] = connection4.matricesResultado[i];
			}
			
			//comentar
			/* Descomentar
			float[][] [][] matricesC = {
				{ 
					{{1,2}, {13,4}}, 
					{{5, 6}, {7,8}}, 
					{{9, 10}, {11,12}}, 
					{{13, 14}, {15,16}}
				},
				{ 
					{{21,2}, {23,4}}, 
					{{5, 6}, {7,8}}, 
					{{9, 10}, {11,12}}, 
					{{13, 14}, {15,16}}
				},
				{ 
					{{31,2}, {33,4}}, 
					{{5, 6}, {7,8}}, 
					{{9, 10}, {11,12}}, 
					{{13, 14}, {15,16}}
				},
				{ 
					{{41,2}, {43,4}}, 
					{{5, 6}, {7,8}}, 
					{{9, 10}, {11,12}}, 
					{{13, 14}, {15,16}}
				}
			};
			*/

			int divisor = N/4;
			//unir C1, C2, ... C16
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					int celdaHorizontal = (int)Math.floor(j/divisor);
					int celdaVertical = (int)Math.floor(i/divisor);
					C[i][j] = matricesC [celdaVertical][celdaHorizontal][i%2][j%2];
				}
			}
			imprimirMatriz(C, "matriz C");
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

	static void serverRMI() throws Exception{
		System.out.println("Servidor en puerto ");
		try {
			ClaseRMI obj = new ClaseRMI();
			//registra la instancia en el rmiregistry
			Naming.rebind(url0, obj);
		} catch (Exception e) {
			System.out.println(e);
		}
	}


	static void dividirMatriz(float[][] matrizOriginal, float[] [][] divisionesMatriz){
		for (int i = 0; i < N; i++) {
			if(i<N/4){
				divisionesMatriz[0][i]=matrizOriginal[i];
			}else if(i<N*2/4){
				divisionesMatriz[1][i-N/4]=matrizOriginal[i];
			}else if(i<N*3/4){
				divisionesMatriz[2][i-N*2/4]=matrizOriginal[i];
			}else{
				divisionesMatriz[3][i-N*3/4]=matrizOriginal[i];
			}
		}
	}

	static float calcularChecksum(float[][] matriz) {
		float checksum=0;
		for (float[] ds : matriz) {
			for (float d : ds) {
				checksum+=d;	
			}
		}
		return checksum;
	}
	static void transponerMatriz(float[][] matriz) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < i; j++) {
				float x = matriz[i][j];
				matriz[i][j] = matriz[j][i];
				matriz[j][i] = x;
			}
		}
	}


	static void imprimirMatriz(float[][] matriz, String mensaje) {
		System.out.println("\n"+mensaje);
		for (int x=0; x < matriz.length; x++){
			for (int y=0; y < matriz[x].length; y++)
				System.out.print(matriz[x][y]+ "|");   
			System.out.println("\n----------------------------------------");
		}
	}
}