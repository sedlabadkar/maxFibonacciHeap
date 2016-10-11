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

	public int size;	
	private Node<T> maxNode = null; //This is the root of this fibonacci heap
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
		if (maxNode == null){
			newNode.setRightSibling(newNode);
			newNode.setLeftSibling(newNode);
			maxNode = newNode;
		} else {
			maxNode.getLeftSibling().setRightSibling(newNode);
			newNode.setLeftSibling(maxNode.getLeftSibling());
			maxNode.setLeftSibling(newNode);
			newNode.setRightSibling(maxNode);
		}
		if (maxNode.compareTo(newNode) < 0){
			maxNode = newNode;
		}
		elementLookupHashMap.put(data, newNode);
		this.size++;
		System.out.println ("Added Node : " + data.toString() + " size = " + this.size);
		return true;
	}
	
	public boolean contains(T data){
		return (elementLookupHashMap.get(data) != null);
	}
	
	public boolean isEmpty(){
		return this.size == 0;
	}
	
	public void increaseKey(T data){
		
	}
	
	public T removeMin(){
		return null;
	}
	
	public T remove(){
		return null;
	}
	
	/**
	 * Takes 2 heapOrderedTrees and return a single new tree. 
	 * Return the node of the merged tree.
	 */
	/*
	private Node<T> meld(Node<T> N1, Node<T> N2){
		
		//Degree has to be equal - What do we return in this case?
		//Ideally this should be the only time when this method should return null
		if(N1.getDegree() != N2.getDegree()){
			return null;
		} else {
			//Same degree - Just meld - Actually we should have ran with this assumption
			//Merge the two circular linkedlists here. Pairwise combine later. 
			//Also need to update the parent?
		}
		return null;
	}
	
	/*
	private void cascadeCut(){
		
	}
	
	
	*/
}
