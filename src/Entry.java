
public class Entry<K,V> {
	
	private  K k;
	private V v;
	
	public Entry(K k, V v){
		this.k = k;
		this.v = v;
	}
	
	public K Key(){
		return k;
	}
	
	public V Value(){
		return v;
	}
	
	public void modifyValue(V v){
		this.v = v;
	}

}
