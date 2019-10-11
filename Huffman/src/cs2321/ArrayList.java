package cs2321;

import java.util.Iterator;

import net.datastructures.List;

/*
 * Author: Logan Zehm
 * Assignment 1
 * This class is designed to be a array list class.
 * It is essentially a regular array class of a generic type, but
 * the arraylist is able to expand when full and contains extra index modification methods.
 */

public class ArrayList<E> implements List<E> {
	
	public static final int CAPACITY = 16;
	private E[] data;
	private int size = 0;

	//sets the default capacity to 16.
	public ArrayList() {
		this(CAPACITY);
	}
	
	//create an arraylist with a custom capacity.
	@SuppressWarnings("unchecked")
	public ArrayList(int capacity) {
		data = (E[]) new Object[capacity];
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@TimeComplexity("O(1)")
	@Override
	public E get(int i) throws IndexOutOfBoundsException {
		//check if the element is within range and if not throw an exception.
        if (i < 0 || i >= size()) throw new ArrayIndexOutOfBoundsException();
        return data[i];
	}

	@TimeComplexity("O(1)")
	@Override
	public E set(int i, E e) throws IndexOutOfBoundsException {
		//check the index range, save a temp var to return, and set the new value.
		checkIndex(i, size);
		E temp = data[i];
		data[i] = e;
		return temp;
	}

	@TimeComplexity("O(n)")
	@Override
	public void add(int i, E e) throws IndexOutOfBoundsException {
		/* TCJ
		 * The worst case is O(n) because if you use this method to add an element
		 * to the first slot it will have to shift all the elements in the list
		 */
		
		//check the index and expand the arraylist if necessary
		checkIndex(i, size + 1);
		if(size == data.length) expandArrayList();
		//shift the list and set the new element then increment the size.
		for(int k = size - 1; k >= i; k--) data[k + 1] = data[k];
		data[i] = e;
		size++;
	}
	
	@TimeComplexity("O(n)")
	@Override
	public E remove(int i) throws IndexOutOfBoundsException {
		/* TCJ
		 * The worst case is O(n) because if you use this method to remove an element
		 * in the first slot it will have to shift all the elements in the list
		 */
		
		//check the index and save a temp variable to return
		checkIndex(i, size);
		E temp = data[i];
		//shift the array and decrease the size of the array by one.
		for(int k = i; k < size - 1; k++) data[k] = data[k + 1];
		data[size - 1] = null;
		size--;
		return temp;
	}

	
	//create a new instance of ArrayListIterator
	@Override
	public Iterator<E> iterator() {
		return new ArrayListIterator();
	}

	 /**
	   * add a new element to the front of the list
	   * @param e  	the given element to add to the list
	   * @throws IndexOutOfBoundsException if i is not a valid position for this list
	   */
	@TimeComplexity("O(n)")
	public void addFirst(E e) throws IndexOutOfBoundsException {
		/* TCJ
		 * The worst case is O(n) because when you add something to the beginning of the list
		 * all the elements in the list need to be shifted over by one.
		 */
		
		//if the array is full then expand it.
		if(size == data.length) expandArrayList();
		//shift the list and replace the element.
		for(int k = size - 1; k >= 0; k--) data[k + 1] = data[k];
		data[0] = e;
		size++;
	}
	
	 /**
	   * add a new element to the back of the list
	   * @param e  	the given element to add to the list
	   * @throws IndexOutOfBoundsException if i is not a valid position for this list
	   */
	@TimeComplexity("O(1)")
	public void addLast(E e) throws IndexOutOfBoundsException {
		//if the array is full then expand it.
		if(size == data.length) expandArrayList();
		//set the new element at the current size and increment the size.
		data[size] = e;
		size++;
		
	}
	
	//remove the first element in the list.
	@TimeComplexity("O(1)")
	public E removeFirst() throws IndexOutOfBoundsException {
		//use the first array position and shift the list
		E temp = data[0];
		for(int k = 0; k < size - 1; k++) data[k] = data[k + 1];
		data[size - 1] = null;
		//decrement the size.
		size--;
		return temp;
	}
	
	//remove the last element in the list.
	@TimeComplexity("O(1)")
	public E removeLast() throws IndexOutOfBoundsException {
		//use the last array position and decrement the size.
		E temp = data[size - 1];
		data[size - 1] = null;
		size--;
		return temp;
	}
	
	//copy data to new array and double capacity
	@TimeComplexity("O(n)")
	public void expandArrayList() {
		/* TCJ
		 * The worst case is O(n) because you have to loop through the whole
		 * list and copy over all the elements to a new array.
		 */
		
		/*
		 * not sure if this is allowed but I figured it would be easier to just use the array method
		 * designed to copy arrays for simplicity instead of manually looping to copy elements to a new array.
		 */
		data = java.util.Arrays.copyOf(data, data.length * 2);
	}
	
	 /**
	   * checks whether the index i is in the range [0, n-1]
	   * @param i  	the given index
	   * @param n  	the ending range index
	   * @throws IndexOutOfBoundsException if i is not a valid position for this list
	   */
	@TimeComplexity("O(1)")
	public void checkIndex(int i, int n) throws IndexOutOfBoundsException {
		if(i < 0 || i >= n) throw new IndexOutOfBoundsException("Given index " + i + " out of bounds.");
	}
	
	@TimeComplexity("O(n)")
    private class ArrayListIterator implements java.util.Iterator<E> {
		/* TCJ
		 * The worst case is O(n) because an iterator will most likely loop
		 * over all elements within the list.
		 */

        private int current = 0;

        //need to go to size not size - 1 because current is incremented at the start of the iterator.
        public boolean hasNext() {
            return current < size();
        }

        //throwing a new no such element exception if there is no next element.
        public E next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return data[current++];
        }

        //call the arraylist and remove the decrement of the current position.
        public void remove() {
            ArrayList.this.remove(--current);
        }
    }
	
}
