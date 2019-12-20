import java.util.Comparator;

public class PartitionTable {

	public Partition [] partitions;
	public int numPartitions;

	public PartitionTable(int numPartitions){
		
		this.numPartitions = numPartitions;
		
		partitions = new Partition [numPartitions];
		
		for (int i = 0; i < numPartitions; i++){
			Partition partition_i = new Partition(i);
			partitions[i] = partition_i;
		}
		
	}
	
	//
	
	public void addToPartition(int partition_index, KV kv) {
		partitions[partition_index].addToPartition(kv);
	}

	public void addToPartition(int partition_index, Object key, Object value){
		partitions[partition_index].addToPartition(new KV(key, value));

	}

	public Partition getPartitionAtIndex(long index){
		return partitions[(int)index];
	}
	
	//
	
	public void printPartitions() {
		for (int i = 0; i < numPartitions; i++) {
			System.out.println("-- Partition " + i + " --");
			partitions[i].printPartition();
		}
	}
	
	//

	//pass all the add/sort operations to partition (and its linked list) ??

}
