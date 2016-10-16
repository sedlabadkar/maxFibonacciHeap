package hashTagCounter;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author   Sachin Edlabadkar
 *  TODO: Need to check parent, left/right sibling, child, childCut and degree. Basically everything inside the node should be set properly
 *  TODO: Make sure that it is
 */

public class MaxFibonacciHeap<T extends Comparable<T>> {

	private static final boolean HEAPLOGS  = false;
	
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
	/**
	* What could go wrong? When will this node return false? 
	* FINAL: Shouldn't need anymore changes other than the above comment
	*/	
	public boolean insert(T data){
		Node<T> newNode = new Node<T>(data);
		//Add to top level circular linkedlist
		if (maxNode == null){
			newNode.setRightSibling(newNode);
			newNode.setLeftSibling(newNode);
			maxNode = newNode;
		} else {
			maxNode.getLeftSibling().setRightSibling(newNode);
			newNode.setLeftSibling(maxNode.getLeftSibling());
			maxNode.setLeftSibling(newNode);
			newNode.setRightSibling(maxNode);
			//Update maxNode
			if (maxNode.compareTo(newNode) < 0){
				maxNode = newNode;
			}
		}
		//Add to lookup hashmap
		elementLookupHashMap.put(data, newNode);
		this.size++;
		if (HEAPLOGS){
			System.out.println ("Added Node : " + maxNode.getLeftSibling().toString() + " size = " + this.size + "\n\n");
			System.out.println ("MaxNode = " + maxNode.toString() + "\n\n");
		}
		return true;
	}
	
	/**
	* Steps:
	* 1. Delete Max
	* 2. Meld children into top level circular linked list(at the root level)
	* 3. update Max, Pairwise Combine, Update parent & ChildCut members of each node(hopefully only melded nodes)
	*
	* FINAL Just check one more time the procedure for updatemax and pairwise combine
	*/	
	
	/**
	 * Steps:
	 * 1. Detach the node
	 * 2. Meld child Nodes to the top
	 * 3. Pairwise combine
	 * 4. Update Max
	 * 5. return the max node
	 * @param nodeToRemove
	 * @return maxNode
	 */
	public T removeMax(){
		Node<T> secondList = null, nextNode, currNode, childNode, tempNode, sameDegreeNode, returnNode = maxNode;
		HashMap<Integer, Node<T>> sameDegreeMap = new HashMap<Integer, Node<T>>();
		//Detach the subtree from the top level circular list
		//TODO: tempNode could be maxNode here
		tempNode = returnNode.getRightSibling();
		
		returnNode.getLeftSibling().setRightSibling(tempNode);
		tempNode.setLeftSibling(returnNode.getLeftSibling());
		returnNode.setLeftSibling(returnNode);
		returnNode.setRightSibling(returnNode);
	
		//Time to meld the child subtree to the top
		childNode = maxNode.getChild();
		returnNode.setChild(null);
		returnNode.setDegree(0);
		if (childNode != null){
			childNode.setParent(null);
			childNode.setChildCut(false);
		}
			
		currNode = meld(tempNode, childNode);
		
		//Node has been completely detached from the heap - Time to update heap variables 
		this.size--;
		this.elementLookupHashMap.remove(returnNode.getData());
		
		//At the point the heap is given by currNode. tempNode may point to the same node or it may not. We will use tempNode to
		//traverse through the top level list in the heap and to do pairwise combine and update max process
		
		
		/**
		 * pairwise combine steps
		 * 1. Extract the node - detach the subtree from the top level tree
		 * 2. Check in the hashtable if another node has the same degree
		 * 3. if no node is found, add to hashTable
		 * 4. Else do combine for the two add this newly combined node to the hashTable
		 * 5. Form a new circular linkedlist on the side - add this node here
		 * 6. Repeat till all nodes have been traversed
		 * 7. The newly formed circular linkedlist should be the complete heap. I think. Fingers crossed
		 */
		
		//It is possible that there is only one node in the top level list, and that temp node now points to itself/currNode
		//This also means that there aren't any nodes to do pairwise combine - yay
		nextNode = currNode.getRightSibling();
		
		while (nextNode != currNode){
			sameDegreeNode = sameDegreeMap.get(tempNode.getDegree());
			//1. Extract the node - detach the subtree from the top level tree 
			tempNode = nextNode;
			nextNode = tempNode.getRightSibling();
			tempNode.getLeftSibling().setRightSibling(nextNode);
			nextNode.setLeftSibling(tempNode);
			tempNode.setLeftSibling(tempNode);
			tempNode.setRightSibling(tempNode);
			if (tempNode.getParent() != null){
				if (HEAPLOGS)System.out.println("looks like we are not in the top level anymore");
			}
			
			//2. Check in the hashtable if another node has the same degree
			if (sameDegreeNode == null){
				//3. if no node is found, add to hashTable
				sameDegreeMap.put(tempNode.getDegree(), tempNode);
			} else {
				//Detach samedegreeNode subtree
				Node<T> sameDegreeDetachTempNode = sameDegreeNode.getRightSibling();
				//4. Else do combine for the two add this newly combined node to the hashTable
				tempNode = combine(tempNode, sameDegreeNode);
				sameDegreeMap.put(tempNode.getDegree(), tempNode);
			}
			//5. Form a new circular linkedlist on the side - add this node here
			secondList = meld(secondList, tempNode);
		}
		
		return returnNode.getData();
	}
	
	/**
	* Simple method to see if an element already exists in the heap
	*/
	public T contains(T data){
		return (elementLookupHashMap.get(data)!= null? elementLookupHashMap.get(data).getData():null);
	}
	
		/**
	* Its really difficult to describe this method because of its complicated nature
	* Might come in handy. 
	*/
	public boolean isEmpty(){
		return this.size == 0;
	}
	
	/**
	 * Steps:
	 * 1. Replace data
	 * 2. Take subtree out
	 * 3. Merge the subtree to the top level circular list
	 * 4. Cascadecut the parent
	 * 
	 * One important thing to note here is that this data value is the final value. 
	 * Application should get the current value, change it and then send it here to be updated
	 * I think this goes more with the fact that we are trying to make this a generic DataStructure 
	 * 
	 * TODO: Probably, only need to do the detach, meld and, cascadeCut part when the node changed is larger than its parent
	 */
	public boolean increaseKey(T data){
		//Get the node for this data, the incoming value here is the final value and not the amount
		Node<T> nodeToChange = elementLookupHashMap.get(data), tempNode;
		if (nodeToChange == null) {
			return false;
		}else {
			nodeToChange.setData(data);
		}
		
		if (nodeToChange.getParent() != null){
			//TODO: Revisit the condition, should this be leq? or just lessthan?
			if (nodeToChange.compareTo(nodeToChange.getParent()) <= 0){
				return true;
			}
			
			//Detach subtree
			tempNode = nodeToChange.getRightSibling();
			nodeToChange.getLeftSibling().setRightSibling(tempNode);
			tempNode.setLeftSibling(nodeToChange.getLeftSibling());
			nodeToChange.getParent().setChild(tempNode.equals(nodeToChange)?null:tempNode); //What if tempNode is the same node?i.e parent has only 1 child
			nodeToChange.getParent().setDegree(nodeToChange.getParent().getDegree() - 1);
			nodeToChange.setParent(null);
			nodeToChange.setLeftSibling(nodeToChange);
			nodeToChange.setRightSibling(nodeToChange);
			nodeToChange.setChildCut(false);
			
			maxNode = meld(maxNode, nodeToChange); 
		}
		//Here only a single node has been moved to the top, so we can update max quickly
		if (nodeToChange.compareTo(maxNode) > 0){
			maxNode = nodeToChange;
		}
		
		cascadeCut(nodeToChange.getParent());
		nodeToChange.setParent(null);
		
		if(HEAPLOGS){
			System.out.println ("\n\nChanged Node : " + nodeToChange.toString() + " size = " + this.size + "\n\n");
			System.out.println ("\n\nMaxNode = " + maxNode.toString() + "\n\n");
		}
		return true;
	}
	

	public T remove(Node<T> nodeToRemove){
		return null;
	}


	/**
	 * Takes 2 heapOrderedTrees and return a single new tree. 
	 * Return the node of the merged tree.
	 * NOTE: Degree of the parent may change(most likely will change) after this
	 */
	private Node<T> meld(Node<T> N1, Node<T> N2){
		Node<T> T1, T2;
		if(HEAPLOGS) System.out.println("Meld");
		
		if(N1 == null && N2 == null) {
			return null;
		} else if (N1 != null && N2 != null && N1.equals(N2)){
			return N1;
		} else if(N1 != null && N2 == null){
			if(HEAPLOGS) System.out.println("\n\nN2 is null = " + N1.toString());
			return N1;
		} else if(N1 == null && N2 != null) {
			if(HEAPLOGS) System.out.println("\n\nN1 is null = " + N2.toString());
			return N2;
		} else {
			if(HEAPLOGS) System.out.println("\n\nMelding = " + N1.toString() + " \n\t " + N2.toString() + "\n");
			T1 = N1.getLeftSibling();
			T2 = N2.getLeftSibling();
			
			T1.setRightSibling(N2);
			N2.setLeftSibling(T1);
			T2.setRightSibling(N1);
			N1.setLeftSibling(T2);

			if (HEAPLOGS) System.out.println("\n\nPost Meld: " + N1.toString() + " \n\t " + N2.toString() + " \n");
			return N1;	
		}
	}

	/**
	 * This is the core of the pairwise combine procedure - Should only be called for top level nodes; doesnt provide security to deal any violation of this rule
	 * Combines 2 trees after comparing their root nodes
	 * Larger one becomes the root of the new tree, smaller one becomes its child
	 * 
	 * TODO: Make Better
	 */
	private Node<T> combine(Node<T> N1, Node<T> N2){
		if(HEAPLOGS) System.out.println("Combine");
		if(N1 == null && N2 == null) {
			return null;
		} else if(N1 != null && N2 == null){
			return N1;
		} else if(N1 == null && N2 != null) {
			return N2;
		}
		//Cannot be combined. Possible loss of binomial property of member trees
		if (N1.getDegree() != N2.getDegree()) {
			return N1; //Returning null here will cause us to loose the pointer to tempNode, which is in N1. 
		}
		
		if (N1.equals(N2)){
			return N1;
		}
		
		if(HEAPLOGS){
			System.out.println("\n\n----------------------------------------------PreCombine----------------------------------------------------------------");
			System.out.println("N1 = " + N1.toString());
			System.out.println("-------------------------------------------------------------------------------------------------------------------------");
			System.out.println("N2 = " + N2.toString());
			System.out.println("-------------------------------------------------------------------------------------------------------------------------\n\n");
		}
		if (N2.compareTo(N1) > 0) {
			if(N1.getParent()!=null){
				N1.getParent().setChild(N1.equals(N1.getRightSibling())?null:N1.getRightSibling());
				N1.getParent().setDegree(N1.getParent().getDegree() - 1);
			}
			N1.setParent(N2);
			N1.setLevel(N2.getLevel() + 1);
			//Detach the node and meld into the child node of parent
			//if (N2.getChild() == null){
				Node<T> tempNode = N1.getRightSibling();
				N1.getLeftSibling().setRightSibling(tempNode);
				tempNode.setLeftSibling(N1.getLeftSibling());
				N1.setRightSibling(N1);
				N1.setLeftSibling(N1);
			//}
			N2.setChild(meld(N2.getChild(), N1));
			N2.setDegree(N2.getDegree() + 1);
			N1.setChildCut(false);
			if (HEAPLOGS) {
				System.out.println("\n\n----------------------------------------------PostCombine----------------------------------------------------------------");
				System.out.println("N1 = " + N1.toString());
				System.out.println("-------------------------------------------------------------------------------------------------------------------------");
				System.out.println("N2 = " + N2.toString());
				System.out.println("-------------------------------------------------------------------------------------------------------------------------\n\n");
			}
			return N2;
		} else {
			if(N2.getParent()!=null) {
				N2.getParent().setChild(N2.equals(N2.getRightSibling())?null:N2.getRightSibling());
				N2.getParent().setDegree(N2.getParent().getDegree() - 1);
			}
			N2.setParent(N1);
			N2.setLevel(N1.getLevel() + 1);
			//if (N1.getChild() == null){
				Node<T> tempNode = N2.getRightSibling();
				N2.getLeftSibling().setRightSibling(tempNode);
				tempNode.setLeftSibling(N2.getLeftSibling());
				N2.setRightSibling(N2);
				N2.setLeftSibling(N2);
			//}
			N1.setChild(meld(N1.getChild(), N2));
			N1.setDegree(N1.getDegree() + 1);
			N2.setChildCut(false);
			if (HEAPLOGS){
				System.out.println("\n\n----------------------------------------------PostCombine----------------------------------------------------------------");
				System.out.println("N1 = " + N1.toString());
				System.out.println("-------------------------------------------------------------------------------------------------------------------------");
				System.out.println("N2 = " + N2.toString());
				System.out.println("-------------------------------------------------------------------------------------------------------------------------\n\n");
			}
			return N1;
		}
	}
	
	//TODO: Handle root node
	//TODO: Parents will end up loosing child nodes
	public void cascadeCut(Node<T> node){
		Node<T> tempNode, parent;
		if(HEAPLOGS) System.out.println("Cascade cut");
		// isNotNull        isNotRootNode               Has childCut set
		if(node != null && node.getParent() != null && node.isChildCut()){
			if(HEAPLOGS) System.out.println("Cascade cut for = " + node.toString());
			//detach this node
			tempNode = node.getRightSibling();
			node.getLeftSibling().setRightSibling(tempNode);
			tempNode.setLeftSibling(node.getLeftSibling());
			parent = node.getParent();
			parent.setChild(parent.getDegree() == 1? null: tempNode); //This can be the only child, then set to null. Else to next child. 
																  //All we care about is a pointer to the childlevel Circular list.
			parent.setDegree(parent.getDegree() - 1);
			node.setParent(null);//Node has been moved to the top, has no parent now(poor guy)
			maxNode = meld(maxNode, node); 
			
			//Here only a single node has been moved to the top, so we can updated max quickly
			if (node.compareTo(maxNode) > 0){
				maxNode = node;
			}
			
			cascadeCut(parent);
		} else {
			return;
		}
	}
	
	/**
	 * This is a function to print the Heap, it doesn't really serve any purpose in the working of the DS
	 * But it makes debugging a lot easier as it allows to see the heap after each operation.
	 * We are following a simple DFS strategy, preorder, to print the values. Following is the strategy:
	 * push circular linkedlist on stack --> pop out --> print --> push child list on stack --> do till stack is empty
	 */
	public void printHeap(){
		LinkedList<Node<T>> printStack = new LinkedList<Node<T>>();
		Node<T> tempNode, currNode, anchorNode;
		currNode = maxNode;
		anchorNode  = currNode;
		tempNode = anchorNode.getRightSibling();
		while (tempNode != anchorNode){
			printStack.push(tempNode);
			tempNode = tempNode.getRightSibling();
		}
		printStack.push(maxNode);
		if(HEAPLOGS)System.out.println("\n\n-->");
		while (printStack.size() != 0){
			currNode = printStack.pop();
			//Print this
			if(HEAPLOGS)System.out.println("\n" + currNode.toString());
			//Add its children to the stack
			anchorNode = currNode.getChild();
			if (anchorNode != null){
				tempNode = anchorNode.getRightSibling();
				printStack.push(anchorNode);
				while (tempNode != anchorNode){
					printStack.push(tempNode);
					tempNode = tempNode.getRightSibling();
				}
			}
		}
	}
	
	/**
	 * Method to detach a subtree from the given circular linked list
	 * NOTE: May change the child cut property. This method will not change it
	 * @param listNode : next Node in the list
	 * @param nodeToDetach
	 * @return
	 */
	private boolean detachSubtree(Node<T> nodeToDetach, Node<T> listNode){
		Node<T> parentNode;
		parentNode = nodeToDetach.getParent();
		if (listNode.equals(nodeToDetach)){
			//There is only one node in the list, removing it is not possible
			return false;
		}
		
		nodeToDetach.getLeftSibling().setRightSibling(listNode);
		listNode.setLeftSibling(nodeToDetach.getLeftSibling());
		nodeToDetach.setLeftSibling(nodeToDetach);
		nodeToDetach.setRightSibling(nodeToDetach);
		
		if(parentNode != null){
			parentNode.setDegree(parentNode.getDegree() - 1);
			parentNode.setChild(listNode);
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param nodeToDetach
	 * @param listNode : nextNode in the list
	 * @return
	 */
	private boolean detachNode(Node<T> nodeToDetach, Node<T> listNode){
		Node<T> childNode, parentNode, tempNode;
		if (detachSubtree(nodeToDetach, listNode)){
			childNode = nodeToDetach.getChild();
			parentNode = listNode.getParent();
			if (childNode != null) {
				nodeToDetach.setChild(null);
				//Parent Nodes of all child nodes need to be changed
				childNode.setParent(parentNode);
				tempNode = childNode.getRightSibling();
				while(tempNode != childNode){
					tempNode.setParent(parentNode);
					tempNode = tempNode.getRightSibling();
				}
				listNode = meld(listNode, childNode);
				if (parentNode != null){
					parentNode.setDegree(parentNode.getDegree() + nodeToDetach.getDegree());
				}
			} //else there is no subtree to detach from
			return true;
		} else {
			return false;
		}
	}
	

}
