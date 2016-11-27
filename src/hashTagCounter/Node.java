/**
 * 
 */
package hashTagCounter;

/**
 * @author   Sachin Edlabadkar
 */
class Node<T extends Comparable <T>> implements Comparable<Node<T>>{

	public int degree;
	public Node<T> child, left, right, parent;
	public boolean childCut;
	public T data; 
	
	public Node(T data) {
		this.data = data;
		this.child = null;
		this.parent = null;
		this.left = this;
		this.right = this;
		this.childCut = false;
		this.degree = 0;
	}
	
	public int getDegree() {
		return degree;
	}
	
	public void increaseDegree(int amount){
		this.degree += amount;
	}
	
	public void decreaseDegree(int amount){
		this.degree -= amount;
	}
	
	public void setDegree(int degree){
		this.degree = degree;
	}
	
	public Node<T> getChild() {
		return child;
	}

	public void setChild(Node<T> child) {
		this.child = child;
	}

	public Node<T> getLeftSibling() {
		return left;
	}

	public void setLeftSibling(Node<T> leftSibling) {
		this.left = leftSibling;
	} 

	public Node<T> getRightSibling() {
		return right;
	}

	public void setRightSibling(Node<T> rightSibling) {
		this.right = rightSibling;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public boolean getChildCut() {
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
		if (this.left != null) toStr.append("leftSibling=" + left.getData().toString() + ",\n");
		if (this.right!= null) toStr.append("rightSibling=" + right.getData().toString() + ",\n");
		if (this.parent != null) toStr.append("parent=" + parent.getData().toString() + ",\n");
		toStr.append("childcut=" + (childCut == true?"True":"False") + ",\n");
		toStr.append("degree =" + degree + "]");
		
		return toStr.toString();
	}
}