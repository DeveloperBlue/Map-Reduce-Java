import java.util.Comparator;

public class PartitionTable {

	Partition [] partitions;
	int numPartitions;

	boolean didSort = false;

	public PartitionTable(int numPartitions){
		this.numPartitions = numPartitions;
		partitions = new Partition [numPartitions];
	}

	public static void addToPartition(long index, Object key, Object value){
		didSort = false;
		Partition partition = new Partition();

	}

	public static void sortPartition(long index){

		didSort = true;
	}

	public static boolean isSorted(long index){
		return didSort;
	}

	//pass all the add/sort operations to partition (and its linked list) ??

}
