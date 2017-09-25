import java.util.Iterator;

/**
 * 
 * @author Taebum Edward Kim #250793133 CS2210b Assignment 2
 * 
 * This is the HashTable class where methods used in HashDictionary have been created.
 *
 * @param <K> key in type K
 * @param <V> value in type V
 */

public class HashTable<K,V> implements Iterator<Entry<K,V>>{

	private Entry<K,V>[] hashTable;											//the hash-table dictionary.
	private HashCode<K> hCode;
	private float loadF;
	private int entries;													//number of items in the dictionary.
	private int probes;														//number of probes
	private int index;														//
	private int count;
	
	/**
	 * HashTable constructor with self-explanatory variable names.
	 * @param capacity
	 * @param inputCode
	 * @param loadF
	 */
	public HashTable(int capacity, HashCode<K> inputCode, float loadF){		//Constructor.
		int newCapacity = nextPrimeOf(capacity);
		hashTable = new Entry[newCapacity];									//Create new hash-table with capacity.
		this.loadF = loadF;
		hCode = inputCode;
		entries = 0;														//0 entries since it begins as empty.
	}
	
	/**
	 * The insert function checks if value is null, if the key already 
	 * exists and if the load factor isn't greater than the load factor
	 * before insertion.
	 * @param key 
	 * @param value
	 * @throws DictionaryException
	 */
	public void insert(K key, V value) throws DictionaryException{
		if(value == null){													//Make sure value exists.
			throw new NullPointerException();
		}
		
		int hVal = hCode.giveCode(key);
		int compression = compress(Math.abs(hVal));
		int compression2 = compression;
		boolean collision = true;
		while(collision == true){											//Make sure the key doesn't already exist in the hash-table.
			if(hashTable[compression] != null){
				probes++;
				compression = compress2(hVal, compression2, count);
			}
			else{
				collision = false;											//When there is no collision, it will end the while loop.
			}
		}
		float currentL = entries / size();
		if(currentL >= loadF){												//Rehash table if the load factor is larger.
			rehash();
		}
		hashTable[compression] = new Entry<K,V>(key, value);				//Create new entry and increment entries.
		entries++;
		
	}
	
	/**
	 * Compression method using Multiply Add and Divide compression function.
	 * @param y represents the hash code.
	 * @return the hash code mapped in a more appropriate range.
	 */
	public int compress(int y){												//MAD compression method to compress hash code once.
		probes++;
		int a = 181;														//Used somewhat large prime numbers for a and b.
		int b = 67;
		int madCompression = Math.abs(a * y + b) % hashTable.length;
		return madCompression;
	}
	
	/**
	 * Compresses hash code again.
	 * @param x is the uncompressed value.
	 * @param compressedHash is the first compression.
	 * @param i represents number of times compress.
	 * @return 
	 */
	public int compress2(int x, int compressedHash, int i){	
		probes++;
		int prime = nextPrimeOf(size() / 2);
		int rehash = prime - (x % prime);
		int compression = (compressedHash + (i * rehash)) % hashTable.length;
		probes++;															//Increment probes.
		return Math.abs(compression);
	}
	
	/**
	 * Remove method checks if the key exists in the hash-table
	 * and deletes the entry if it does.
	 * @param key
	 * @throws DictionaryException
	 */
	public void remove(K key) throws DictionaryException{
		int hVal = hCode.giveCode(key);
		int compression = compress(Math.abs(hVal));
		if(hashTable[compression] == null){
			throw new DictionaryException();
		}
		else{
			Entry<K,V> oldEntry = hashTable[compression];
			hashTable[compression] = null;
			entries--;
		} 
	}
	
	/**
	 * Exists function checks if key can be found and returns 
	 * the key if found.
	 * @param key
	 * @return
	 */
	public Entry<K,V> exists(K key){
		int hVal = hCode.giveCode(key);
		Entry<K,V>[] table2 = hashTable;							//Create this object so that original hashTable
		for(int p = 0; p <= size(); p++){							//doesn't get affected just in case.
			int i = compress(hVal + p);								//Use linear probing.
			if(table2[i] == null){
				return null;
			}
			if(table2.equals(key) && table2.hashCode() == hVal){	//Make sure the entry is available.
				return hashTable[i];
			}
		}
		return null;
	}
	
	/**
	 * This method double hashes the key.
	 * @param key
	 * @return -1 if nothing found or the index number (compression of hVal)
	 * if it is found.
	 */
	private int doubleHash(K key){
		int hVal = hCode.giveCode(key);
		boolean found = false;
		int hash = compress(hVal);
		int compression = hash;
		int i = 1;
		while(i < size() && !found){								//While loop until it is found or if all 
			if(hashTable[hash] == null){							//possible keys are searched.
				break;
			}
			if(!hashTable[hash].equals(key)){
				hash = compress2(hVal, compression, i);
				i++;
			}
			else{
				found = true;
			}
		}
		if(found == true){
			return hash;
		}
		else{
			return -1;
		}
	}
	
	/**
	 * This is the rehash method to basically increase size when needed.
	 * @throws DictionaryException
	 */
	private void rehash() throws DictionaryException{
		int largerPrime = nextPrimeOf(size() * 2);					//At least 2x greater.
		Entry<K,V>[] oldTable = hashTable;
		hashTable = new Entry[largerPrime];
		
		count++;
		
		
		for(int i = 0; i < oldTable.length; i++){					//Modify to new corresponding keys.
			if(oldTable[i] != null){
				hashTable[i].modifyValue(oldTable[i].Value());
			}
		}
	}
	
	/**
	 * Method to find the next prime number.
	 * @param p is the current prime number.
	 * @return
	 */
	private int nextPrimeOf(int p){
		int x = 2;
		boolean prime = false;
		while(!prime){
			for(int i = 2; (i*i) <= p; i++){
				if(p % x == 0){
					prime = false;
				}
			}
			if(!prime){
				p++;
			}
			prime = true;
		}
		return p;
	}
	
	/**
	 * Simple size function for the hash-table.
	 * @return
	 */
	public int size(){
		return hashTable.length;
	}
	
	/**
	 * Returns the number of probes.
	 * @return
	 */
	public int probes(){
		return probes;
	}
	
	/**
	 * Needed hasNext method to implement iterator.
	 */
	public boolean hasNext() {
		int temp = index;
		while(hashTable[temp] == null){ //Run while loop until an existing value is found.
			if(temp + 1 < size()){
				temp++;
			}
			else{
				break;
			}
		}
		if((temp + 1) < size()){		//Unless it's the last entry, hasNext will be true.
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	/**
	 * Needed this method to implement iterator too.
	 */
	public Entry<K, V> next() {			
		while(hashTable[index] == null){	//While loop until existing value is found or all indexes checked.
			index++;		
		}
		Entry<K,V> nextEntry = hashTable[index];
		index++;
		return nextEntry;
	}
}
