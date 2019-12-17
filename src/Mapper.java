public class Mapper {

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
