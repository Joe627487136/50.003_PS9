package Week11;

import java.util.concurrent.*;
import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestThreadPoolSample extends TestCase {
	
    public void testPoolExpansion1() throws InterruptedException {
        int max_pool_size = 10;
        ExecutorService exec = Executors.newFixedThreadPool(max_pool_size);
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread(i);
            exec.execute(worker);
        }
        //todo: insert your code here to complete the test case
        //hint: you can use the following code to get the number of active threads in a thread pool
        int numThreads = 0;
        if (exec instanceof ThreadPoolExecutor) {
        	numThreads = ((ThreadPoolExecutor) exec).getActiveCount();
        }
        exec.shutdownNow();
        assertTrue(numThreads==10);
    }
    public void testPoolExpansion2() throws InterruptedException {
        int max_pool_size = 10;
        ExecutorService exec = Executors.newFixedThreadPool(max_pool_size);
        for (int i = 0; i < 20; i++) {
            Runnable worker = new WorkerThread(i);
            exec.execute(worker);
        }
        //todo: insert your code here to complete the test case
        //hint: you can use the following code to get the number of active threads in a thread pool
        int numThreads = 0;
        if (exec instanceof ThreadPoolExecutor) {
            numThreads = ((ThreadPoolExecutor) exec).getActiveCount();
        }
        exec.shutdownNow();
        assertTrue(numThreads==10);
    }
    public void testPoolExpansion3() throws InterruptedException {
        int max_pool_size = 10;
        ExecutorService exec = Executors.newFixedThreadPool(max_pool_size);
        for (int i = 0; i < 100; i++) {
            Runnable worker = new WorkerThread(i);
            exec.execute(worker);
        }
        //todo: insert your code here to complete the test case
        //hint: you can use the following code to get the number of active threads in a thread pool
        int numThreads = 0;
        if (exec instanceof ThreadPoolExecutor) {
            numThreads = ((ThreadPoolExecutor) exec).getActiveCount();
        }
        exec.shutdownNow();
        assertTrue(numThreads==10);
    }
    public class WorkerThread implements Runnable {
        private int threadnum;
        public WorkerThread(int threadnum){
            this.threadnum = threadnum;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            System.out.print("Thread "+threadnum+" is running\n");
        }
    }
}
