import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Partition {

	public Partition(){

	}
	
	//how many linked lists in it?
	//At least coarse granularity concurrency control (fine granularity comes with extra credits) should be implemented at this level --
	//you can maintain a CV/lock in this partition class, or create a cv/lock array in PartitionTable as
	//long access to each partition is synchronized.  You can also extend LinkedList and create your own SyncLinkedList inside which you can implement fine granularity locking.

}
