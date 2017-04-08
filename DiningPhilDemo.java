package Week11;

import java.util.Random;

public class DiningPhilDemo {
	private static int N = 5;
	
	public static void main (String[] args) throws Exception {	
		PhilosopherDemo[] phils = new PhilosopherDemo[N];
		Fork[] forks = new Fork[N];

		for (int i = 0; i < N; i++) {
			forks[i] = new Fork(i);
		}

		for (int i = 0; i < N; i++) {
			phils[i] = new PhilosopherDemo (i, forks[i], forks[(i+N-1)%N]);
			phils[i].start();
		}
	}
}

class PhilosopherDemo extends Thread {
	private final int index;
	private final Fork left;
	private final Fork right;
	
	public PhilosopherDemo (int index, Fork left, Fork right) {
		this.index = index;
		this.left = left;
		this.right = right;
	}
	
	public void run() {
		Random randomGenerator = new Random();
		try {
			while (true) {
				Thread.sleep(randomGenerator.nextInt(100)); //not sleeping but thinking
				System.out.println("Phil " + index + " finishes thinking.");
				left.pickup();
				Thread.sleep(3000); //not sleeping but thinking
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
		} catch (InterruptedException e) {
			System.out.println("Don't disturb me while I am sleeping, well, thinking.");
		} 
	}
}