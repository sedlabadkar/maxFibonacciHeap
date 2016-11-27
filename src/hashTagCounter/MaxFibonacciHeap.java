/**
 * 
 */
package hashTagCounter;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author   Sachin Edlabadkar
 */
public class MaxFibonacciHeap<T extends Comparable<T>> {

    private Node<T> maxNode;

    /**
     * Function to add new node into the heap
     * @param generic data value to be added
     * @return a pointer to the new node added, this will make it easy for the application
     * to do increaseKey later on for this node.
     */
    public Node<T> insert(T data) {
        Node<T> newNode = new Node<T>(data);
        maxNode = meld(maxNode, newNode);
        return newNode;
    }
    
    /**
     * Function to add new node into the heap
     * @param node pointer
     * This function is used by the application when it wants to add a node back in
     * For example, after doing remove max, if the application wants to add the node back, it can do through this method
     * @return a pointer to the new node added, this will make it easy for the application
     * to do increaseKey later on for this node.
     */
    public void insert(Node<T> node){
    	maxNode = meld(maxNode, node);
    }

    /**
     * Basic removemax function, returns the maxnode
     * RemoveMax(H)
     * 1. z = H.max
     * 2. if z != nil
     * 3. 	for each child x of z
     * 4.		add x to the root list of H
     * 5. 		x.p = nil
     * 6.   remove z from the root list of H
     * 7.   if z = z.right
     * 8.   	H.max = nil
     * 9.   else
     * 10.		H.max = z.right
     * 11.      consolidate()
     * 12. return z			
     * 
     */
    public Node<T> removeMax() {
    	//Empty Heap
    	if (maxNode == null)
    		return null;
    	//Heap has some elements 
    	//1
    	Node<T> returnNode = maxNode;
    	//3, 4, 5
        if(returnNode.getChild() != null) {
            returnNode.getChild().setParent(null);
            Node<T> curChild = returnNode.getChild().getRightSibling();
            while(curChild != returnNode.getChild()) {
                curChild.setParent(null);
                curChild = curChild.getRightSibling();
            }
        }

        Node<T> right = returnNode.getRightSibling();
        //6
        detachNode(returnNode);
        //7 & 8
        if (returnNode == right){
        	maxNode = null;
        	return returnNode;
        } else {
        	maxNode = meld(right, returnNode.getChild());
            returnNode.setChild(null);
            if(right != null) {
                maxNode = right;
                combine();
            }

        }
        return returnNode;
    }

    /**
     * Method to traverse through the root list and combine trees 
     */
    private void combine() {
        HashMap<Integer, Node<T>> degreeMap = new HashMap<Integer, Node<T>>();
        degreeMap.put(maxNode.getDegree(), maxNode);
        //Q to the resQ
        LinkedList<Node<T>> queue = new LinkedList<Node<T>>();
        if(maxNode == null) {
            return;
        }
        //add the entire top level list to the queue
        queue.add(maxNode);
        Node<T> iter = maxNode.getRightSibling();
        while(iter != maxNode) {
            queue.add(iter);
            iter = iter.getRightSibling();
        }
        
        //queue.poll(); //Why?
        //Iterate till queue is not empty
        while(queue.peek() != null) {
        	Node<T> handle = queue.remove();
            if(handle.compareTo(maxNode) > 0){
                maxNode = handle;
            }
            //TODO: Check duplicates 
            while(degreeMap.containsKey(handle.getDegree()) && handle != degreeMap.get(handle.getDegree())) {
            	Node<T> sameDegreeNode = degreeMap.get(handle.getDegree()), bigger, smaller, right;
                degreeMap.remove(handle.getDegree());
                if (handle.compareTo(sameDegreeNode) > 0){
                	bigger = handle;
                	smaller = sameDegreeNode;
                } else {
                	bigger = sameDegreeNode;
                	smaller = handle;
                }
                
                //Can these two nodes be null?
                
                detachNode(smaller);
                smaller.setParent(bigger);
                right = smaller.getRightSibling();
                while(right != smaller) {
                    right .setParent(bigger);
                }
                bigger.setChild(meld(bigger.getChild(), smaller));
                bigger.increaseDegree(1);
                degreeMap.put(bigger.getDegree(), bigger);
            }
        }
    }

    /**
     * increasekey of the given node by the given data
     * maintain heap property
     */
    public void increaseKey(Node<T> nodeToChange, T newData){
        if(newData.compareTo(nodeToChange.getData()) < 0){
        	System.out.println("Decrease key is not allowed. Returning");
        	return; 
        }
        nodeToChange.setData(newData);

        //cascading cut only makes sense if the node is now greater than its parent
        if(nodeToChange.getParent() != null && nodeToChange.compareTo(nodeToChange.getParent()) > 0) {
            cut(nodeToChange, nodeToChange.getParent());
            cascadingCut(nodeToChange.getParent());
        }

        if(nodeToChange.compareTo(maxNode) > 0){
            maxNode = nodeToChange;
        }
    }

	/**
	 * Takes two lists and merge them together 
	 * NOTE: parent's degree may change
	 */
    private Node<T> meld(Node<T> N1, Node<T> N2){
		Node<T> T1, T2;
		
		if(N1 == null && N2 == null) {
			return null;
		} else if (N1 != null && N2 != null && N1.equals(N2)){
			return N1;
		} else if(N1 != null && N2 == null){
			return N1;
		} else if(N1 == null && N2 != null) {
			return N2;
		} else {
			T1 = N1.getLeftSibling();
			T2 = N2.getLeftSibling();
			
			T1.setRightSibling(N2);
			N2.setLeftSibling(T1);
			T2.setRightSibling(N1);
			N1.setLeftSibling(T2);

			return N1;	
		}
    }

    /**
     * Cut(h,x,y)
     * 1. remove x from the child list of y and decrease y's degree
     * 2. add x to the root list
     * 3. x.p = nil
     * 4. x.mark = false
     */
    private void cut(Node<T> node, Node<T> parent) {
    	//1 & 3
        detachNode(node);
        //2
        meld(maxNode, node);
        //4
        node.setChildCut(false);
    }

    /**
     * This function detaches the node and its subtree from its list and parent
     * used in Cut, combine etc
     * TODO: make sure to not lose the child pointer of the node
     */
    private void detachNode(Node<T> nodeToDetach) {
    	if(nodeToDetach.getRightSibling() != nodeToDetach) {
            nodeToDetach.getLeftSibling().setRightSibling(nodeToDetach.getRightSibling());
            nodeToDetach.getRightSibling().setLeftSibling(nodeToDetach.getLeftSibling());
            if (nodeToDetach.getParent() != null) {
                nodeToDetach.getParent().setChild(nodeToDetach.getRightSibling());
                nodeToDetach.getParent().decreaseDegree(1);
            }
            nodeToDetach.setLeftSibling(nodeToDetach);
            nodeToDetach.setRightSibling(nodeToDetach);
        } else {
        	if (nodeToDetach.getParent() != null) {
                nodeToDetach.getParent().setChild(null);
                nodeToDetach.getParent().setDegree(0);;
            }
        }
        nodeToDetach.setParent(null);
    }

    private void cascadingCut(Node<T> node) {
        if(node != null && node.getParent() != null) {
        	if(node.getChildCut()) {
                cut(node, node.getParent());
                cascadingCut(node.getParent());
            } else {
                node.setChildCut(true);
            }
        }
    }
}