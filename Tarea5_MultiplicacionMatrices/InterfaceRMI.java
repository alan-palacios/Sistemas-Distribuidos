import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote{
	public float[][] multiplica_matrices(float[][] matriz1, float[][] matriz2) throws RemoteException;
}	