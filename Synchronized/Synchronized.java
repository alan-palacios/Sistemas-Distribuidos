package Synchronized;
/**
 * SynchronizedExample
 */
public class Synchronized extends Thread{
	static long n;
	//Common static object to make possible the synchronization between the threads
	static Object obj = new Object();
	public void run() {
		for (int i = 0; i < 100000; i++) {
			//Synchronized instructions
			synchronized (obj){
				n++;
			}
		}
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Synchronized t1 = new Synchronized();
		Synchronized t2 = new Synchronized();
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
			System.out.println(n);
		} catch (Exception e) {
			System.out.println(e);
		}
		long time = System.currentTimeMillis() - start;
		//it takes more time in its execution 
		//because the threads have to wait to each other to
		//finish the synchronized instructions
		System.out.println("Time: "+time);
	}
}