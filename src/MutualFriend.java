import java.io.*;
import java.util.Scanner;

public class MutualFriend extends MapperReducerAPI{
	public void Map(Object inputSource) {
		String fileName = (String) inputSource;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String token;
		    while ((token = br.readLine()) != null) {
		    	
		    	String[] kp_split = token.split("\\s+", 2); // "A	B,C,D" ==> ["A" , "B C D"]
		    	String person = kp_split[0].trim();
		    	String[] friends = kp_split[1].trim().split(","); // B.C,D ==> ["B", "C", "D"]
		    	
		    	// System.out.println("p:'" + person + "'");
		    
		    	// (person, friends[i]) -> (friends)
		    	for (int i = 0; i < friends.length; i++) {
		    		// MapReduce.MREmit(person + ", " + friends[i], kp_split[1]);
		    		System.out.println("f:'" + friends[i] + "'");
		    		try {
		    			int p1 = Integer.parseInt(person);
		    			int p2 = Integer.parseInt(friends[i]);
		    			// System.out.println(((p1 < p2) ? (person + "," + friends[i].trim()) : (friends[i].trim() + "," + person)) + " : " + kp_split[1].trim());
		    			MapReduce.MREmit((p1 < p2) ? (person + "," + friends[i].trim()) : (friends[i].trim() + "," + person), kp_split[1].trim());
		    		} catch(NumberFormatException e) {
		    			
		    		}
		    	}

		    }
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void Reduce(Object key, int partition_number) {
		

	    String friends_master = "";
	    
	    Object friends = MapReduce.MRGetNext(key, partition_number);
	    while (friends != null) {
	    	// System.out.println("Friends of " + key + " : " + friends);
	    	friends_master = (friends_master + "," + friends).trim();
	    	friends = MapReduce.MRGetNext(key, partition_number);
	    }
	    
	    String[] friends_arr = friends_master.split(",");
	    String mutual_friends = "";
	    
	    for (int i = 0; i < friends_arr.length; i++) {
	        for (int j = i + 1 ; j < friends_arr.length; j++) {
	             if (friends_arr[i].equals(friends_arr[j])) {
	            	 mutual_friends = (mutual_friends + " " + friends_arr[i]).trim();
	             }
	        }
	    }
	    
	    // System.out.println("Mutual friends of '" + key + "' -> '" + mutual_friends + "'");
	    MapReduce.MRPostProcess((String)key, mutual_friends, "\t");
	    
	}

    public static void main(String[] argv) {
    	//for your convenience, use scanner to read input...
    	//if you enter nothing, argv is treated as default input source
    	Scanner s = new Scanner(System.in);
    	String inputFileName;
    	System.out.println("Please enter data source for mutualfriends:");
    	inputFileName = s.nextLine();
    	if(inputFileName.isEmpty()) {
    		inputFileName = argv[0];
    	}
    	s.close();
    	MapReduce.setTestScript("./test_mutual_friends.sh");
		MapReduce.MRRun(inputFileName, new MutualFriend(), 60, 60);
	}	
	
}
