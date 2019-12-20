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
		return key.toString().compareTo(kv2.getKey().toString());
	}
}
