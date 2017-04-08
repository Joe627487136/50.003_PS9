package Week11;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.*;

public class BoundedBufferWithSpecTest {
	private static final long LOCKUP_DETECT_TIMEOUT = 1000;
	
	@Test
	public void testIsEmptyWhenConstructued () {
		BoundedBufferWithSpec<Integer> bb = new BoundedBufferWithSpec<Integer>(10);
		assertTrue(bb.isEmpty());
		assertFalse(bb.isFull());
	}
	
	@Test
	public void testIsFullAfterPuts () throws InterruptedException {
		final BoundedBufferWithSpec<Integer> bb = new BoundedBufferWithSpec<Integer>(10);
		
		Runnable task = new Runnable () {
			public void run() {
				try {
					bb.put((new Random()).nextInt());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}
		};

		Thread[] threads = new Thread[10];
		
		for (int i = 0; i < 10; i++) {
			threads[i] = new Thread (task);
			threads[i].start();
		}

		for (int i = 0; i < 10; i++) {
			threads[i].join();
		}

		assertTrue(bb.isFull());
		assertFalse(bb.isEmpty());
	}
	
	@Test
	public void testTakeBlocksWhenEmpty () {
		final BoundedBufferWithSpec<Integer> bb = new BoundedBufferWithSpec<Integer>(10);
		Thread taker = new Thread() {
			public void run() {
				try {
					int unused = bb.take();
					assertTrue(false);
				} catch (InterruptedException success) {} //if interrupted, the exception is caught here
			}
		};
		
		try {
			taker.start();
			Thread.sleep(LOCKUP_DETECT_TIMEOUT);
			taker.interrupt();
			taker.join(LOCKUP_DETECT_TIMEOUT);
			assertFalse(taker.isAlive()); //the taker should not be alive for some time
		} catch (Exception unexpected) {
			assertTrue(false);
		}
	}
	@Test
	public void testIsEmptyAfterAlTakePut () throws InterruptedException {
		final BoundedBufferWithSpec<Integer> bb = new BoundedBufferWithSpec<Integer>(10);

		Runnable task = new Runnable () {
			public void run() {
				try {
					bb.put((new Random()).nextInt());
					bb.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		Thread[] Pthreads = new Thread[10];

		for (int i = 0; i < 10; i++) {
			Pthreads[i] = new Thread (task);
			Pthreads[i].start();
		}

		for (int i = 0; i < 10; i++) {
			Pthreads[i].join();
		}

		assertTrue(bb.isEmpty());
		assertFalse(bb.isFull());
	}
	@Test
	public void testIsEmptyAfterFullyTakesPut () throws InterruptedException {
		final BoundedBufferWithSpec<Integer> bb = new BoundedBufferWithSpec<Integer>(10);

		Runnable task = new Runnable () {
			public void run() {
				try {
					bb.put((new Random()).nextInt());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		Runnable task2 = new Runnable () {
			public void run() {
				try {
					bb.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		Thread[] Pthreads = new Thread[10];
		Thread[] Tthreads = new Thread[10];

		for (int i = 0; i < 10; i++) {
			Pthreads[i] = new Thread (task);
			Pthreads[i].start();
		}

		for (int i = 0; i < 10; i++) {
			Pthreads[i].join();
		}
		for (int j = 0; j < 10; j++) {
			Tthreads[j] = new Thread (task2);
			Tthreads[j].start();
		}

		for (int j = 0; j < 10; j++) {
			Tthreads[j].join();
		}

		assertTrue(bb.isEmpty());
		assertFalse(bb.isFull());
	}
}
