/**
 * 
 */
package hashTagCounter;

import java.util.HashMap;

/**
 * @author   Sachin Edlabadkar
 * UFID:     70647958 
 */

/*
 * As per the current design, Node and MaxFibonnaciHeap Classes are Type-Agnostic and are operating on Generic types.
 * This poses a problem for the implementation of contains method. Actually its not a problem, but its still is. 
 * To run a successful contains method, HashTagCounter.java would need to send in Data object with the value for 
 * String HashTag and Integer Count. The count is variable, sending the exact object may be difficult, which means that 
 * hashmap lookup may fail. 
 * 
 * Maybe we can do something using hashCode and Equals in some way
 * 
 */
public class MaxFibonacciHeap<T extends Comparable<T>> {

	private int size;	
	private Node<T> minNode = null; //This is the root of this fibonacci heap
	HashMap<T, Node<T>> elementLookupHashMap;
	
//-----------------------------------------------------------------//
// 																   //
//						 Contructors                               //                                     
//  															   //
//-----------------------------------------------------------------//	
	public MaxFibonacciHeap(){
		this.setSize(0);
		elementLookupHashMap = new HashMap<T, Node<T>>();
	}
//-----------------------------------------------------------------//
//																   //
//						 Getters & Setters                         //                                     
//  															   //
//-----------------------------------------------------------------//
	public int getSize() {
		return size;
	}

	private void setSize(int size) {
		this.size = size;
	}
	
//-----------------------------------------------------------------//
// 															       //
//						Other Methods                              //                                     
//  															   //
//-----------------------------------------------------------------//
	//Methods to implement
	//1. Insert
	//2. RemoveMin
	//3. FindMin
	//5. Size
	//6. IsEmpty()
	//7. Meld
	//8. Arbitrary Remove
	//9. IncreaseKey
	
	public boolean insert(T data){
		Node<T> newNode = new Node<T>(data);
		//System.out.println ("Adding Node : " + data.toString());
		elementLookupHashMap.put(data, newNode);
		return true;
	}
	
	public boolean contains(T data){
		return (elementLookupHashMap.get(data) != null);
	}
	
	public void increaseKey(T data){
		
	}
	
	public T removeMin(){
		return null;
	}
	
	public T remove(){
		return null;
	}
	
	public boolean isEmpty(){
		return this.getSize() == 0;
	}
	
	private void cascadeCut(){
		
	}
	
	private void meld(){
		
	}
}
