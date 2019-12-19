import java.util.Comparator;

public class PartitionTable {

	Partition [] partitions;
	int numPartitions;

	public PartitionTable(int numPartitions){
		this.numPartitions = numPartitions;
		partitions = new Partition [numPartitions];
		for (int i = 0; i < numPartitions; i++){
			Partition partition_i = new Partition();
			partitions[i] = partition_i;
		}
	}

	public static void addToPartition(long index, Object key, Object value){
		didSort = false;
		partitions[(int)index].addToPartition(new KV(key, value));

	}

	public static Partition getPartitionAtIndex(long index){
		return partitions[(int)index];
	}

	//pass all the add/sort operations to partition (and its linked list) ??

}
