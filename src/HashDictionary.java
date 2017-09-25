import java.util.Iterator;

/**
 * 
 * @author Taebum Edward Kim #250793133 CS2210b Assignment 2
 * 
 * This HashDictionary class implements hash-table based dictionary. It implements the Dictionary interface 
 * and implements open addressing with double hashing strategy. Hash table uses methods from HashTable class
 * to compute the hash code.
 *
 * @param <K> key in type K
 * @param <V> value in type V
 */
public class HashDictionary<K,V> implements Dictionary<K,V> 
{
    private HashCode<K> hCode;
    private HashTable<K,V> hDictionary;						//Hash table named hDictionary set
    private float loadF;									//Load factor named loadF set
    private int numOps;										//Total number of operations numOps set
   
    
    // Creates a new instance of HashDictionary 
    public HashDictionary(HashCode<K> inputCode, float maxLFactor){
         hCode         = inputCode;         // input hash code
         this.loadF = maxLFactor;
         hDictionary = new HashTable<K,V>(7, hCode, loadF);	//Set hash array of size 7.
         numOps = 0;										//Set number of operations at 0.
    }

    
	//////////////////////////////////////////////////////////////////////////////////////////////////////
    // Returns true if there is  an entry with specified key. Returns null otherwise     
    public Entry<K,V> find(K key){
    	numOps++;											//Increment numOps.
    	if(hDictionary.exists(key) != null){				//If hash-table finds they key, exists returns null
    		return hDictionary.exists(key);    				//so use != null for when key is found and return the entry.
    	}
    	else{
    		return null;
    	}
    }
    
    
    
    public void insert(K key, V value) throws DictionaryException{
    	if(hDictionary.exists(key) == null){				//Make sure key that you want to insert does not yet exist
    		hDictionary.insert(key, value);					//in the hash-table(when exists method returns null).
    	}
    	else{												//Exception for when key already exists.
    		throw new DictionaryException(key + "already inserted.");
    	}
    	numOps++;											//Increment numOps.
    }
    
  
    public void remove(K key) throws DictionaryException{
    	hDictionary.remove(key);
    	numOps++;											//Increment numOps.
    }
    
    public float averNumProbes(){
    	float avgProbes = hDictionary.probes() / numOps;	//Average number of probes performed displayed by this quotient.
    	return avgProbes;
    }


	@Override
	public Iterator<Entry<K, V>> elements() {
		Iterator<Entry<K,V>> iterate = hDictionary;
		return iterate;
	}


	@Override
	public int size() {
		return hDictionary.size();
	}

}