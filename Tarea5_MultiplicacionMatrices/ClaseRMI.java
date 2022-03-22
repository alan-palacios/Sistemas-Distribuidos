import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClaseRMI extends UnicastRemoteObject implements InterfaceRMI{
	public ClaseRMI() throws RemoteException{
		super();
	}	
	//matrices de N/4*N
	//retorna matriz N/4*N/4
	public float[][] multiplica_matrices(float[][] matriz1, float[][] matriz2){
		float[][] res = new float[matriz1.length][matriz2.length];
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
}
