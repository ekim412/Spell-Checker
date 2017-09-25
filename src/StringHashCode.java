
public class StringHashCode implements HashCode<String>{
	
	public int giveCode(String key){
		char keyArray[] = key.toCharArray();
		int hash = keyArray[keyArray.length - 1];
		int i = keyArray.length - 2;
		
		while(i >=0){
			hash = (hash * 33) + keyArray[i];
			i--;
		}
		return hash;
	}

}
