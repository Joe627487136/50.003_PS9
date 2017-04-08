package Week11;

import java.util.concurrent.Semaphore;

import net.jcip.annotations.GuardedBy;

//this class is thread safe
public class BoundedBufferWithSpec<E> {	
	//class invariant: availableItems + availableSpaces = items.length
	//class invariant: availableItems >= 0
	//class invariant: avaliableSpaces > = 0
	//class invariant: putPosition >= 0 && putPosition < items.length
	//class invariant: takePosition >= 0 && takePosition < items.length
	private final Semaphore availableItems, availableSpaces;	
	@GuardedBy("this") private final E[] items;	
	@GuardedBy("this") private int putPosition = 0,	takePosition = 0;	
	
	public BoundedBufferWithSpec(int capacity) {	
		availableItems = new Semaphore(0);	
		availableSpaces = new Semaphore(capacity);	
		items = (E[]) new Object[capacity];	
	}	

	public boolean isEmpty() {	
		return availableItems.availablePermits() == 0;	
	}	
	//postcondition: return true if and only if availableSpaces = items.length
	
	public boolean isFull() {
		return availableSpaces.availablePermits() == 0;	
	}

	//pre-condition: items is not null
	public void put(E x) throws InterruptedException {	
		availableSpaces.acquire();	
		doInsert(x);	
		availableItems.release();	
	}	
	//postcondition: availableSpaces'.availablePermits() = availableSpaces.availablePermits() - 1
	//postcondition: availableItems'.availablePermits() = availableItems.availablePermits() + 1
	//postcondition: putPosition' = putPosition + 1 if putPosition < items.length-1; 0 otherwise
 
	public E take() throws InterruptedException {	
		availableItems.acquire();	
		E item = doExtract();	
		availableSpaces.release();	
		return item;	
	}	
 
	private synchronized void doInsert(E x) {	
		int i = putPosition;	
		items[i] = x;	
		putPosition = (++i == items.length)? 0 : i;	
	}
	
	private synchronized E doExtract() {	
		int i = takePosition;	
		E x = items[i];	
		items[i] = null;	
		takePosition = (++i == items.length)? 0 : i;	
		return x;	
	}	
}
