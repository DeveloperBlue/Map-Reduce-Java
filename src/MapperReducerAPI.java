import java.util.Comparator;

public abstract class MapperReducerAPI implements Comparator {
	abstract void Map(Object inputSource);
	
	abstract void Reduce(Object key, int partition_number);

	long Partitioner(Object key, int num_partitions){ 
		String k = (String) key;
		char [] ck = k.toCharArray();
		long hash = 5381;
		int c;
		int i=0;
		while (i<ck.length) {
			c = ck[i++];
			hash = hash * 33 + c;
		}
		long ret =  hash % num_partitions;
		ret = ret>= 0? ret:ret+num_partitions;
		return ret;
	}
	public int compare(Object k1, Object k2) {
		return ((KV)k1).key.toString().compareTo(((KV)k2).key.toString());
	}
	public int compareKeys(Object k1, Object k2) {
		return ((String)k1).compareTo(((String)k2));
	}
	public int compareValues(Object v1, Object v2) {
		return ((String)v1).compareTo(((String)v2));
	}
}
