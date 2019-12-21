class KV {
	public Object key;
	public Object val;
	public KV(Object key, Object value) {
		this.key = key;
		this.val = value;
	} 
	
	public Object getKey() {
		return key;
	}
	
	public Object getValue() {
		return val;
	}
	
	public int compareTo(KV kv2) {
		
		/*
		
		try {
			
			int int_comparison_check_a = Integer.parseInt(key.toString());
			int int_comparison_check_b = Integer.parseInt(kv2.getKey().toString());
			
			System.out.println("Comparing integers . . .");
			
			if (int_comparison_check_a == int_comparison_check_b) {
				return 0;
			} else if (int_comparison_check_a > int_comparison_check_b) {
				return 1;
			} else {
				return -1;
			}
			
		} catch(NumberFormatException e) {
			
		}
		
		*/
		
		return key.toString().compareTo(kv2.getKey().toString());
	}
}
