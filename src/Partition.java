import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Partition {

	public LinkedList<KV> partitionList;
	public Lock partition_lock = new ReentrantLock();
	public Condition triggerReducer = partition_lock.newCondition();

	public boolean isSorted;

	public Partition(){
		partitionList = new LinkedList<>();
		isSorted = true;
	}

	void addToPartition(KV key) {
		partition_lock.lock();
		try {
			partitionList.insertSort(key, Comparator.comparing(kv -> kv.key.toString()));
			isSorted = false;
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			partition_lock.unlock();
		}
		
	}

	void printPartition() {
		partitionList.toString();
	}

	void sortPartition(){
		// Sort using mergesort?
		// But we already sorted when inserting :question_mark:
		isSorted = true;
	}

	void signalReducer(){

	}

	boolean getIsSorted(){
		return isSorted;
	}

	LinkedList.Node<KV> getPartitionListHead() {
		return partitionList.head;
	}
	
	//how many linked lists in it?
	//At least coarse granularity concurrency control (fine granularity comes with extra credits) should be implemented at this level --
	//you can maintain a CV/lock in this partition class, or create a cv/lock array in PartitionTable as
	//long access to each partition is synchronized.  You can also extend LinkedList and create your own SyncLinkedList inside which you can implement fine granularity locking.

}
