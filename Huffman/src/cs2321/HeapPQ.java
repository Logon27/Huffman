package cs2321;

import java.util.Comparator;

import net.datastructures.*;

/**
 * A Adaptable PriorityQueue based on an heap. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author Logan Zehm
 */

public class HeapPQ<K,V> implements AdaptablePriorityQueue<K,V> {
	
	public ArrayList<Entry<K,V>> heap;
	public Comparator<K> comp;
	
	public HeapPQ() {
	    heap = new ArrayList<Entry<K,V>>();
	    comp = new DefaultComparator<K>();
	}
	
	public HeapPQ(Comparator<K> c) {
	    heap = new ArrayList<Entry<K,V>>();
	    comp = c;
	}
	
	//Inner class so I can initialize entries.
	private static class ObjEntry<K,V> implements Entry<K,V> {
		private K k;
		private V v;
		
		public ObjEntry(K key, V value) {
			k = key;
			v = value;
		}
		public void setKey(K newKey) { k = newKey; }
		public void setValue(V newValue) { v = newValue; }
		public K getKey() { return k; }
		public V getValue() { return v; }
	}
	
	/**
	 * The entry should be bubbled up to its appropriate position 
	 * @param int move the entry at index j higher if necessary, to restore the heap property
	 */
	@TimeComplexity("O(lg n)")
	public void upheap(int j){
		while(j > 0) { 
			//grab the parent of j and store it as a variable.
			int p = parent(j);
			if(comp.compare(heap.get(j).getKey(), heap.get(p).getKey()) >= 0) {
				break; // break the loop when the heap is restored
			}
			swap(j,p); 
			j = p; // continue from parent by setting j equal to parent
		}
	}
	
	/**
	 * The entry should be bubbled down to its appropriate position 
	 * @param int move the entry at index j lower if necessary, to restore the heap property
	 */
	@TimeComplexity("O(lg n)")
	public void downheap(int j){
		while(hasLeft((j))) {
			int leftIndex = left(j);
			//assume the left index is smaller and if the node has a right child check if its smaller
			int smallChildIndex = leftIndex;
			if(hasRight(j)) { 
				int rightIndex = right(j);
				if(comp.compare(heap.get(leftIndex).getKey(), heap.get(rightIndex).getKey()) > 0) {
					smallChildIndex = rightIndex;
				}
			}
			if(comp.compare(heap.get(smallChildIndex).getKey(), heap.get(j).getKey()) >= 0) { 
				break; //the heap order is restored so break from the loop.
			}
			swap(j, smallChildIndex);
			j = smallChildIndex; //continue from the child since we are going down the heap.
		}
	}
	
	@TimeComplexity("O(1)")
	@Override
	public int size() {
		return heap.size();
	}

	@TimeComplexity("O(1)")
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@TimeComplexity("O(lg n)")
	@Override
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		if(key == null) {
			//have to prevent the insertion of null values. it breaks the upheap function.
			throw new IllegalArgumentException("Cannot insert null values");
		} else {
			//insert the value to the end of the heap and call upheap.
			Entry<K,V> newEntry = new ObjEntry<K,V>(key, value);
			heap.add(heap.size(), newEntry);
			upheap( heap.size() - 1); 
			return newEntry;	
		}
	}

	@TimeComplexity("O(1)")
	@Override
	public Entry<K, V> min() {
		if(heap.isEmpty()) { 
			return null;
		}
		//just call the first value in the heap.
		return heap.get(0);
	}

	@TimeComplexity("O(1)")
	@Override
	public Entry<K, V> removeMin() {
		if(heap.isEmpty()) { 
			return null;
		}
		
		Entry<K,V> temp = heap.get(0);
		//swap the lowest value with the last value in the heap. remove the last element and restore heap order.
		swap(0, heap.size()-1); 
		heap.remove(heap.size()-1); 
		downheap(0); // call downheap to restore heap order.
		return temp; // return our temp that was removed
	}

	@TimeComplexity("O(lg n)")
	@Override
	public void remove(Entry<K, V> entry) throws IllegalArgumentException {
		if(heap.isEmpty()) { 
			throw new IllegalStateException("Heap Empty when trying to remove entry");
		}
		
		//find the index of the entry that was given
		int indexEntry = -1;
		for(int i = 0; i < heap.size(); i++) {
			if(entry == heap.get(i)) {
				indexEntry = i;
			}
		}
		//if the entry was not found throw an exception
		if( indexEntry == -1) {
			throw new IllegalArgumentException("Input entry not found");
		}
		
		//swap the index to be removed with the last index in the heap and remove it.
		swap(indexEntry, heap.size()-1); 
		heap.remove(heap.size() - 1); 
		upheap(indexEntry); // call up and down heap to restore heap order
		downheap(indexEntry);
		
	}

	@TimeComplexity("O(1)")
	@Override
	public void replaceKey(Entry<K, V> entry, K key) throws IllegalArgumentException {
		if(entry instanceof ObjEntry) {
			//just call the helper method I created for my inner entry class to set the new key.
			ObjEntry<K,V> ent = (ObjEntry<K,V>) entry;
			ent.setKey(key);
			
			//find the index of the entry that was given
			int indexEntry = -1;
			for(int i = 0; i < heap.size(); i++) {
				if(entry == heap.get(i)) {
					indexEntry = i;
				}
			}
			upheap(indexEntry); // call up and down heap to restore heap order
			downheap(indexEntry);
		} else {
			throw new IllegalArgumentException("Invalid entry to replace key");
		}
		
	}

	@TimeComplexity("O(1)")
	@Override
	public void replaceValue(Entry<K, V> entry, V value) throws IllegalArgumentException {
		if(entry instanceof ObjEntry) {
			//just call the helper method I created for my inner entry class to set the new value.
			ObjEntry<K,V> ent = (ObjEntry<K,V>) entry;
			ent.setValue(value);
		} else {
			throw new IllegalArgumentException("Invalid entry to replace value");
		}
	}
	
	
	//Helper Methods Below This Point
	
	@TimeComplexity("O(1)")
	public void swap(int i, int j) {
		//save the temp var, set the new value then re-set the temp.
		Entry<K,V> temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}
	
	//The three methods below are just formulas for finding indexes of the parent, left, and right nodes of a given index.
	@TimeComplexity("O(1)")
	public int parent(int i) { 
		return (i - 1) / 2; 
	}
	
	@TimeComplexity("O(1)")
	public int left(int i) { 
		return 2*i + 1; 
	} 
	
	@TimeComplexity("O(1)")
	public int right(int i) {
		return 2*i + 2; 
	}

	//check if the left index is within the heap size to see if it exists.
	@TimeComplexity("O(1)")
	public boolean hasLeft(int i) {
		return left(i) < heap.size(); 
	}
	
	//check if the right index is within the heap size to see if it exists.
	@TimeComplexity("O(1)")
	public boolean hasRight(int i) {
		return right(i) < heap.size(); 
	}

}
