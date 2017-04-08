package Week11;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhil {
	private static int N = 5;
	
	public static void main (String[] args) throws Exception {	
		Philosopher[] phils = new Philosopher[N];
		Fork[] forks = new Fork[N];
		ReentrantLock[] locks = new ReentrantLock[5];

		for (int i = 0; i < N; i++) {
			forks[i] = new Fork(i);
		}
		for (int i = 0; i < N; i++) {
			locks[i] = new ReentrantLock();
			phils[i] = new Philosopher(i, forks[i], forks[(i + N - 1) % N],locks[i]);
			phils[i].start();
		}
	}
}

class Philosopher extends Thread {
	private final int index;
	private final Fork left;
	private final Fork right;
	private final ReentrantLock lock;
	
	public Philosopher (int index, Fork left, Fork right, ReentrantLock lock) {
		this.index = index;
		this.left = left;
		this.right = right;
		this.lock = lock;
	}
	
	public void run() {
		Random randomGenerator = new Random();
		try {
			while (true) {
				Thread.sleep(randomGenerator.nextInt(100)); //not sleeping but thinking
				System.out.println("Phil " + index + " finishes thinking.");
				left.pickup();
				boolean flag = lock.tryLock(1000, TimeUnit.MILLISECONDS);
				if(flag){
					System.out.println("Phil " + index + " picks up left fork.");
					right.pickup();
					System.out.println("Phil " + index + " picks up right fork.");
					Thread.sleep(randomGenerator.nextInt(100)); //eating
					System.out.println("Phil " + index + " finishes eating.");
					left.putdown();
					System.out.println("Phil " + index + " puts down left fork.");
					right.putdown();
					System.out.println("Phil " + index + " puts down right fork.");
				}
				else {
					left.putdown();
				}

			}
		} catch (InterruptedException e) {
			System.out.println("Don't disturb me while I am sleeping, well, thinking.");
		}
	}
}

class Fork {
	private final int index;
	private boolean isAvailable = true;
	public Object lock = new Object();
	
	public Fork (int index) {
		this.index = index;
	}
	
	public synchronized void pickup () throws InterruptedException {
			while (!isAvailable) {
				wait();
			}

			isAvailable = false;
			notifyAll();
	}
	
	public synchronized void putdown() throws InterruptedException {
			while (isAvailable) {
				wait();
			}

			isAvailable = true;
			notifyAll();
	}
	
	public String toString () {
		if (isAvailable) {
			return "Fork " + index + " is available.";			
		}
		else {
			return "Fork " + index + " is NOT available.";						
		}
	}
}