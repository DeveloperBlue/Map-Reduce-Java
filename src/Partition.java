import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Partition {

	
	int partition_index;
	
	public LinkedList<KV> partitionList;
	public Lock partition_lock = new ReentrantLock();
	public Condition triggerReducer = partition_lock.newCondition();
	
	public int currentMRCursor;

	public boolean isSorted;
	public boolean wasReducerSignaled;

	public Partition(int partition_index){
		this.partition_index = partition_index;
		this.isSorted = false;
		this.wasReducerSignaled = false;
		this.currentMRCursor = 0;
		partitionList = new LinkedList<>();
	}


	void addToPartition(KV key) {
		/*
		 * Sadly we can't just insert sort, as that defeats the purpose of the project
		 * 
		*/
		partition_lock.lock();
		try {
			partitionList.insertSort(key, Comparator.comparing(kv -> kv.key.toString()));
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			partition_lock.unlock();
		}
		
		
		// partition_lock.lock();
		// partitionList.addLast(key);
		// partition_lock.unlock();
		
	}

	void printPartition() {

		StringBuffer result = new StringBuffer();
	      for(KV o : partitionList)
	      	result.append(o.key + " : " + o.val + " | ");

	      System.out.println(result.toString());
	}

	void sortPartition(){
		partition_lock.lock();
		// We can't use mergesort as this defeats the purpose of the project
		// But we already sorted when inserting :question_mark: (This is wrong)
		
		// TODO
		// Now we implement a sort . . .
		
		System.out.println("Finished sorting " + partition_index);
		isSorted = true;
		partition_lock.unlock();
		
	}

	boolean isSorted(){
		return isSorted;
	}

	
	//how many linked lists in it?
	//At least coarse granularity concurrency control (fine granularity comes with extra credits) should be implemented at this level --
	//you can maintain a CV/lock in this partition class, or create a cv/lock array in PartitionTable as
	//long access to each partition is synchronized.  You can also extend LinkedList and create your own SyncLinkedList inside which you can implement fine granularity locking.

}
