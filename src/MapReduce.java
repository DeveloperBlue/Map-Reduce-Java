import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapReduce {
	// for final output		
	static PrintWriter pw;
	static Lock pwLock;
	static StopWatch stopWatch;
	static Logger LOGGER;
	
	// External functions: these are what you must define
	static PartitionTable pt ;
	static MapperReducerAPI customMR; 
	static int numPartitions;
	
	static void MREmit(Object key, Object value) {
		//TODO: (key, value) must be emit into PartitionTable.
		// use Partitioner defined in MapperReducerAPI to
		// compute the index of partitions where this key will be
		// added.

		long partition_index = customMR.Partitioner(key, numPartitions);

		pt.addToPartition((int) partition_index, new KV(key, value));
		
	}
	
	static Object MRGetNext(Object key, int partition_number) {

		//TODO: implement MRGetNext based on the key and partition_number
		//The state of how many keys have been visited must be saved
		//somewhere because a reducer could be interrupted and switched off
		
		Partition current_partition = pt.getPartitionAtIndex(partition_number);
		
		try {
			KV node = current_partition.partitionList.get(current_partition.currentMRCursor);
			
			if (Objects.equals(node.key, key)) {
				current_partition.currentMRCursor = current_partition.currentMRCursor + 1;
				return node;
			} else {
				return null;
			}
		} catch (IndexOutOfBoundsException e) {
			current_partition.currentMRCursor = current_partition.currentMRCursor + 1;			
		}
		//dragon here,  Usually user supplied reduce() invokes this function to get and process all the //values for a key,
		//for instance, keep on calling MRGetNext(“fox”) three times to figure out there are 3 “fox” in text.
		//  while ((MapReduce.MRGetNext(key, partition_number)) != null)
		// 	count++;
		//this has to work like an “iterator”...
		//each reducer could have a different progress of reduction. MRGetNext is a static method but it
		//has to figure out the cursor on each partition.
		//perhaps maintain an array of visiting states in PartitionTable? 
		
		return null; //just to pass compile.
		
	}
	
	static String getSplitFileName(String filename, int current, int maximum) {
		if (current < 10) {
			return filename + ".0" + current;
		} else {
			return filename + "." + current;
		}
		/*
		int ln_cur = String.valueOf(current).length();
		int ln_max = String.valueOf(maximum).length();
		if (ln_cur < ln_max) {
			return filename + "." + "0".repeat(ln_max - ln_cur) + current;
		} else {
			return filename + "." + current;
		}
		*/
	}

	static void MRRun(String inputFileName, MapperReducerAPI mapperReducerObj, int num_mappers, int num_reducers){
		
		System.out.println("Working Directory: \"" + System.getProperty("user.dir") + "\"");
		System.out.println("InputFileName: " + inputFileName);
		
		File file_check = new File(inputFileName);
		
		try {
			if (!file_check.exists()) {
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Failed to find file in cwd " + System.getProperty("user.dir"));
			System.exit(0);
		}
		
		setup(num_mappers, inputFileName);

		//TODO: launch mappers, main thread must join all mappers before
		// starting sorters and reducers

		pt = new PartitionTable(num_mappers);
		customMR = mapperReducerObj;
		numPartitions = num_reducers;

		Thread[] map_array = new Thread [num_mappers];

		for (int i = 0; i < num_mappers; i++){
			Thread mapper_i = new Thread(new Mapper(getSplitFileName(inputFileName, i, num_mappers)));
			map_array[i] = mapper_i;
			mapper_i.start();
		}

		for (int i = 0; i < map_array.length; i++){
			try {
				map_array[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// pt.printPartitions();
		
    	LOGGER.log(Level.INFO, "All Maps are completed");
    	
    	
		
    	//TODO: launch sorters and reducers. Each partition is assigned a sorter
    	// and a reducer which works like a *pipeline* with mapper. Sorter[i] takes 
    	// over the kv list in the partition[i] and starts sorting, then mapper[i]
    	// can start adding more to partition right away. Reducer[i] waits for 
    	// sorter to sort all kv pairs

    	Thread[] sort_array = new Thread [numPartitions];

    	for (int i = 0; i < numPartitions; i++){
    		Thread sorter_i = new Thread(new Sorter(i));
    		sort_array[i] = sorter_i;
    		sorter_i.start();
    	}

		//

		Thread[] reducer_array = new Thread [num_reducers];
		
    	for (int i = 0; i < num_reducers; i++){
    		Thread reducer_i = new Thread(new Reducer(i));
    		reducer_array[i] = reducer_i;
    		System.out.println("Waiting for Reducer " + i);
    		reducer_i.start();
    	}
    	
    	for (int i = 0; i < reducer_array.length; i++){
			try {
				reducer_array[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


    	//Main thread waits for reducers to complete.
        LOGGER.log(Level.INFO,"Execution of all maps and reduces took in seconds: {0}", (stopWatch.getElapsedTime()));
		teardown();

	}

	/////////////////

	static class Mapper extends Thread {

		String fileName;

		public Mapper(String fileName){
			this.fileName = fileName;
		}

		public void run(){
			customMR.Map(fileName);
		}

		// Each mapper needs to know which file to work on! So it needs file name.
		//String filename 
		//impl run() for mappers. In run each mapper calls user defined map() (which scans the file, and calls MREmit) to inject a kv to //partition table

	}

	static class Sorter extends Thread {

		int partition_index;
		Partition partition;

		public Sorter(int partition_index){

			//each sorter needs to know which partition it works on, so it needs partition index

			//int partitionIdx? //give it a better name! 

			this.partition_index = partition_index;
			this.partition = pt.getPartitionAtIndex(partition_index);

			//impl run() for sorters. In run each sorter calls sorting on partition
			//then wake up waiting reducer, perhaps cv_of_partition_ i.signal()?
		}

		public void run(){
			
			System.out.println("Sorting Partition " + this.partition_index);
			partition.sortPartition();
			
			if (!partition.wasReducerSignaled) {
				synchronized(partition) {
					System.out.println("Signaling Reducer for Partition " + this.partition_index);
					partition.wasReducerSignaled = true;
					partition.notify();
				}
			}
			
		}

	}

	static class Reducer extends Thread {

		int partition_index;
		Partition partition;

		public Reducer (int partition_index){
			this.partition_index = partition_index;
			this.partition = pt.getPartitionAtIndex(partition_index);
		}

		public void run(){
			
			System.out.println("Recuder for Partition " + this.partition_index + " waiting for signal . . .");
			
			if (!partition.wasReducerSignaled) {
				synchronized(partition) {
					try {
						partition.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			/*
			while (!partition.isSorted()){
				// System.out.println("Busy wait on partition " + this.partition_index);
				// busy wait . . .
			}
			*/
			
			Iterator<KV> partition_iterator = partition.partitionList.iterator();
			KV last_kv = null;
			
			while (partition_iterator.hasNext()) {
				
				KV current_kv = partition_iterator.next();
				
				if ((last_kv == null) || !(Objects.equals(last_kv.key, current_kv.key))) {
					customMR.Reduce(current_kv.key, partition_index);
					last_kv = current_kv;
				}
				
			}
		
			System.out.println("Completed reduction on Partition " + this.partition_index);

		}


	}


	/////////////////

	public static void MRPostProcess(String key, int value) {
		pwLock.lock();
		System.out.printf("%s:%d\n", (String)key, value);
		pw.printf("%s:%d\n", (String)key, value); 
		pwLock.unlock();
	}

	private static void setup (int nSplits, String inputFile) {
		try {
			pwLock = new ReentrantLock();
			stopWatch = new StopWatch();
			LOGGER = Logger.getLogger(MapReduce.class.getName());
			//split input into as many as nSplits files.
			Process p = Runtime.getRuntime().exec(new String[] { "/bin/sh" , "-c", "./split.sh "+inputFile +" " +nSplits});
			p.waitFor();
			int exitVal = p.exitValue();
			assert(exitVal == 0);
			pw = new PrintWriter(new FileWriter("res/out.txt"));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}

	static private void teardown( ) {
		pw.close();
        try {
			Process p = Runtime.getRuntime().exec(new String[] { "/bin/sh" , "-c", "./test.sh" });

			// Strange bug on my machine were p.waitFor() never returns
			// Fixed using: https://stackoverflow.com/questions/5483830/process-waitfor-never-returns
			
			BufferedReader reader =
			new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((reader.readLine()) != null) {}
			//
			p.waitFor();
			int exitVal = p.exitValue();
	        if(exitVal == 0) {
	        	LOGGER.log(Level.INFO, "PASSED");
	        } else {
		        LOGGER.log(Level.INFO, "FAILED, process exit value = {0}", exitVal);
	        }
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.toString());
		}
	}
	
	static private class StopWatch{
		 private long startTime;
		    public StopWatch() {
		        startTime = System.currentTimeMillis();
		    }
		    public double getElapsedTime() {
		        long endTime = System.currentTimeMillis();
		        return (double) (endTime - startTime) / (1000);
		    }
	}
}