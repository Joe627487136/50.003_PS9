package Week11;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Trylockexample {
	public static void main (String[] args) throws Exception {
		final ReentrantLock reentrantLock = new ReentrantLock();
		(new RoadHoggingThread(reentrantLock)).start();
		while (true) {
			Thread.sleep(1000);
			boolean flag = reentrantLock.tryLock(1000, TimeUnit.MILLISECONDS);
			if (flag) {
				try {
					System.out.println(Thread.currentThread().getName() +": Lock acquired.");
					System.out.println("Performing task...");
				} finally {
					System.out.println(Thread.currentThread().getName() +": Lock released.");
					reentrantLock.unlock();
				}

				break;
			}
			else {
				System.out.println(Thread.currentThread().getName() +": Lock not available. Retry again");
			}
		}
	}
}

class RoadHoggingThread extends Thread {
	private ReentrantLock reentrantLock;
	public RoadHoggingThread (ReentrantLock reentrantLock) {
		this.reentrantLock = reentrantLock;
	}

	public void run () {
		reentrantLock.lock();
		System.out.println(Thread.currentThread().getName() +": Lock acquired.");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			reentrantLock.unlock();
		}
	}
}