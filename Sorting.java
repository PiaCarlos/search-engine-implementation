package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may (or may not) need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {
    	
    	ArrayList<K> allKeys = new ArrayList<K>();
    	allKeys.addAll(results.keySet());
    	
    	// do the mergeSort
    	mergeSort(allKeys, results);
    	
    	return allKeys;
    } 
    
    
    public static <K, V extends Comparable<V>> void mergeSort(ArrayList<K> allKeys, HashMap<K, V> results) {
    
       int keySize = allKeys.size();	
       
       // Base case
       if (keySize <= 1) {
    	   return; 
    	  }
    	
       int midPoint = keySize/2;
       
       // get elements
       ArrayList<K> rightSide = new ArrayList<>(allKeys.subList(midPoint, keySize));
       ArrayList<K> leftSide = new ArrayList<>(allKeys.subList(0, midPoint));
    	
       // do mergeSort for both sides 
       mergeSort(rightSide, results);
       mergeSort(leftSide, results);
    
       // Merge them together
       
       int rightCount = 0;
       int leftCount = 0;
       int n = 0;
       
       while (rightCount < rightSide.size() && leftCount < leftSide.size() ) {
    	   
    	   if (results.get(rightSide.get(rightCount)).compareTo(results.get(leftSide.get(leftCount))) >= 0) {
    		   allKeys.set(n,rightSide.get(rightCount));
		       rightCount++;
    	   }
    	   else {
    		   allKeys.set(n,leftSide.get(leftCount));
    		   leftCount++;
    	   }
    	    n++; 
           }
       
       while (rightCount < rightSide.size()) {
    	   allKeys.set(n,rightSide.get(rightCount));
	   	   n++;
	       rightCount++; 
       }
       
       while (leftCount < leftSide.size()) {
    	   allKeys.set(n,leftSide.get(leftCount));
		   n++;
		   leftCount++; 
       }
      
    } 
}


    
  