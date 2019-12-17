import java.io.*;
import java.util.Scanner;

public class WordCount extends MapperReducerAPI{
	public void Map(Object inputSource) {
		String fileName = (String) inputSource;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String token;
		    while ((token = br.readLine()) != null) {
		    	MapReduce.MREmit(token, "1");
		    }
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void Reduce(Object key, int partition_number) {
	    int count = 0;
	    while ((MapReduce.MRGetNext(key, partition_number)) != null)
	        count++;
	    MapReduce.MRPostProcess((String)key, count);
	}

    public static void main(String[] argv) {
    	//for your convenience, use scanner to read input...
    	//if you enter nothing, argv is treated as default input source
    	Scanner s = new Scanner(System.in);
    	String inputFileName;
    	System.out.println("Please enter data source:");
    	inputFileName = s.nextLine();
    	if(inputFileName.isEmpty()) {
    		inputFileName = argv[0];
    	}
    	s.close();
		MapReduce.MRRun(inputFileName, new WordCount(), 20, 20);
	}	
	
}
