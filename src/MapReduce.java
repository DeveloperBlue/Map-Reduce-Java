import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
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

		pt.addToPartition(partition_index, key, value);
		
	}
	
	static Object MRGetNext(Object key, int partition_number) {

		//TODO: implement MRGetNext based on the key and partition_number
		//The state of how many keys have been visited must be saved
		//somewhere because a reducer could be interrupted and switched off

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

	static void MRRun(String inputFileName, MapperReducerAPI mapperReducerObj, int num_mappers, int num_reducers){	
		setup(num_mappers, inputFileName);
		//TODO: launch mappers, main thread must join all mappers before
		// starting sorters and reducers

		pt = new PartitionTable();
		customMR = mapperReducerObj;
		numPartitions = num_reducers;

		// inputFileName_i
		// We should chunk the contents of inputFileName . . .

		for (int i = 0; i < num_mappers; i++){
			Thread mapper_i = new Thread(new Mapper(inputFileName_i));
			mapper_i.start();
			mapper_i.join();
		}

		
    	LOGGER.log(Level.INFO, "All Maps are completed");
		
    	//TODO: launch sorters and reducers. Each partition is assigned a sorter
    	// and a reducer which works like a *pipeline* with mapper. Sorter[i] takes 
    	// over the kv list in the partition[i] and starts sorting, then mapper[i]
    	// can start adding more to partition right away. Reducer[i] waits for 
    	// sorter to sort all kv pairs

    	for (int i = 0; i < numPartitions; i++){
    		Thread sorter_i = new Thread(new Sorter(i));
    		sorter_i.start();
    		sorter_i.join(); // isNeeded?
    	}

    	for (int i = 0; i < num_reducers; i++){
    		Thread reducer_i = new Thread(new Reducer(i));
    		reducer_i.start();
    		reducer_i.join();
    	}


    	//Main thread waits for reducers to complete.
        LOGGER.log(Level.INFO,"Execution of all maps and reduces took in seconds: {0}", (stopWatch.getElapsedTime()));
		teardown();

	}

	/////////////////

	class Sorter {

		long partition_index;

		public Sorter(){

			//each sorter needs to know which partition it works on, so it needs partition index

			//int partitionIdx? //give it a better name! 

			//impl run() for sorters. In run each sorter calls sorting on partition
			//then wake up waiting reducer, perhaps cv_of_partition_ i.signal()?


		}

		public static void run(){

		}

	}

	class Mapper {

		String fileName;

		public Mapper(String fileName){
			this.fileName = fileName;
		}

		public static void run(){
			
		}

		// Each mapper needs to know which file to work on! So it needs file name.
		//String filename 
		//impl run() for mappers. In run each mapper calls user defined map() (which scans the file, and calls MREmit) to inject a kv to //partition table

	}



	/////////////////

	public static void MRPostProcess(String key, int value) {
		pwLock.lock();
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