import java.util.Comparator;

public class PartitionTable {

	Partition [] partitions;
	int numPartitions;

	public PartitionTable(int numPartitions){
		this.numPartitions = numPartitions;
		partitions = new Partition [numPartitions];
		for (int i = 0; i < numPartitions; i++){
			Partition partition_i = new Partition(i);
			partitions[i] = partition_i;
		}
	}
	
	public void addToPartition(int index, KV kv) {
		partitions[index].addToPartition(kv);
	}

	public void addToPartition(int index, Object key, Object value){
		partitions[index].addToPartition(new KV(key, value));

	}

	public Partition getPartitionAtIndex(long index){
		return partitions[(int)index];
	}
	
	public void printPartitions() {
		for (int i = 0; i < numPartitions; i++) {
			System.out.println("-- Partition " + i + " --");
			partitions[i].printPartition();
		}
	}

	//pass all the add/sort operations to partition (and its linked list) ??

}
