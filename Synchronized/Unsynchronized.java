package Synchronized;
/**
 * UnsynchronizedExample
 */
public class Unsynchronized extends Thread{
	static long n;
	public void run() {
		for (int i = 0; i < 100000; i++) {
			n++;
		}
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Unsynchronized t1 = new Unsynchronized();
		Unsynchronized t2 = new Unsynchronized();
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
		System.out.println("Time: "+time);
	}
}