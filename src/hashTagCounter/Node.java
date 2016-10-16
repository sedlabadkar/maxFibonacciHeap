/**
 * 
 */
package hashTagCounter;

/**
 * @author   Sachin Edlabadkar
 *  TODO: Remove level
 */
class Node<T extends Comparable <T>> implements Comparable<Node<T>>{

	private int degree, level;
	private Node<T> child, leftSibling, rightSibling, parent;
	private boolean childCut;
	private T data; 
	
//-----------------------------------------------------------------//
//                                                                 //
//                       Contructors                               //                                     
//                                                                 //
//-----------------------------------------------------------------//
	public Node(T data) {
		this.data = data;
		this.child = null;
		this.parent = null;
		this.leftSibling = this;
		this.rightSibling = this;
		this.childCut = false;
		this.degree = 0;
		this.level = 0;
	}
//-----------------------------------------------------------------//
//                                                                 //
//                       Getters & Setters                         //                                     
//                                                                 //
//-----------------------------------------------------------------//
	
	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Node<T> getChild() {
		return child;
	}

	public void setChild(Node<T> child) {
		this.child = child;
	}

	public Node<T> getLeftSibling() {
		return leftSibling;
	}

	public void setLeftSibling(Node<T> leftSibling) {
		this.leftSibling = leftSibling;
	}

	public Node<T> getRightSibling() {
		return rightSibling;
	}

	public void setRightSibling(Node<T> rightSibling) {
		this.rightSibling = rightSibling;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public boolean isChildCut() {
		return childCut;
	}

	public void setChildCut(boolean childCut) {
		this.childCut = childCut;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
//-----------------------------------------------------------------//
//                                                                 //
//                      Other Methods                              //                                     
//                                                                 //
//-----------------------------------------------------------------//

	@Override
	//The heap should be able to compare the nodes so that it can be moved around. 
	//This relies on the data to have implemented the compareTo method
	public int compareTo(Node<T> o) {
		if (o == null) return 1;
		return data.compareTo(o.data);
	}

	@Override
	public String toString() {
		StringBuilder toStr = new StringBuilder();
		toStr.append("[data= " + data + ",\n");
		if (this.child != null) toStr.append("child=" + child.getData().toString() + ",\n");
		if (this.leftSibling != null) toStr.append("leftSibling=" + leftSibling.getData().toString() + ",\n");
		if (this.rightSibling != null) toStr.append("rightSibling=" + rightSibling.getData().toString() + ",\n");
		if (this.parent != null) toStr.append("parent=" + parent.getData().toString() + ",\n");
		toStr.append("childcut=" + (childCut == true?"True":"False") + ",\n");
		toStr.append("degree =" + degree + ",\n");
		toStr.append("level =" + level + "]");
		
		return toStr.toString();
	}

}
