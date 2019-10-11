package cs2321;

import net.datastructures.BinaryTree;
import net.datastructures.Position;

import java.util.Iterator;

public class LinkedBinaryTree<E> implements BinaryTree<E> {
	
	//Array for recursive traversal
    ArrayList<Position<E>> traverse = new ArrayList<>();

    //initialize the root and size
    private Node<E> root;
    private int size;

    //default to a null root and size 0
    public LinkedBinaryTree() {
        root = null;
        size = 0;
    }

    public LinkedBinaryTree(E e, Node<E> left, Node<E> right, Node<E> parent) {
        root = new Node<E>(e, left, right, parent);
        size++;
    }

    @Override
    public Position<E> root() {
        return root;
    }

    @Override
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
    	return validate(p).getParent();
    }

    @Override
    public Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException {
    	
        ArrayList<Position<E>> temp = new ArrayList<>();
        
        //check if the parent has left and right children and if so add to our arraylist
        if (validate(p).hasLeft()){
            temp.addLast(validate(p).getLeft());
        }
        
        if (validate(p).hasRight()){
            temp.addLast(validate(p).getRight());
        }

        //return arraylist because it is iterable
        return temp;
    }

    @Override
    /* count only direct child of the node, not further descendant. */
    public int numChildren(Position<E> p) throws IllegalArgumentException {
        int count = 0;

        //if the parent has left or right children then increment the count.
        if (validate(p).hasLeft()){
            count++;
        }
        if (validate(p).hasRight()){
            count++;
        }

        return count;
    }

    
    @Override
    public boolean isInternal(Position<E> p) throws IllegalArgumentException {
    	//if the parent has a left or right child then return true.
        if (validate(p).hasRight() || validate(p).hasLeft()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isExternal(Position<E> p) throws IllegalArgumentException {
    	//if the position is not internal its external
    	return !isInternal(p);
    }

    @Override
    public boolean isRoot(Position<E> p) throws IllegalArgumentException {
        return validate(p).getParent() == null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
    	return this.size == 0;
    }

    @Override
    public Iterator<E> iterator() {

        ArrayList<E> temp = new ArrayList<>();
        //loop through all the positions in the binary tree and add their elements to the arraylist
        for (Position<E> node : positions()){
            temp.addLast(node.getElement());
        }
        
        //return the iterator of the arraylist of elements
        return temp.iterator();
    }

    @Override
    public Iterable<Position<E>> positions() {
    	//return our recursive traversal array given the current root
        return RecursiveTraversal(root);
    }

    //preorder recursive traversal method
    public ArrayList<Position<E>> RecursiveTraversal(Position<E> node){

    	//if the node is null just return the arraylist
        if (node == null) return traverse;
        
        //visit the parent node first and add to the arraylist. then recursively call the same method on the children
        traverse.addLast(node);
        if(validate(node).hasLeft()) {
        	RecursiveTraversal(validate(node).getLeft());
        }
        if(validate(node).hasRight()) {
        	RecursiveTraversal(validate(node).getRight());
        }

        return traverse;
    }

    //method to return the left child
    @Override
    public Position<E> left(Position<E> p) throws IllegalArgumentException {
		if (validate(p).getLeft() == null) {
			return null;
		}
		return validate(p).getLeft();
    }

    //method to return the right child
    @Override
    public Position<E> right(Position<E> p) throws IllegalArgumentException {
		if (validate(p).getRight() == null) {
			return null;
		}
		return validate(p).getRight();
    }

    @Override
    public Position<E> sibling(Position<E> p) throws IllegalArgumentException {

        Node<E> parent = validate(p).getParent();
        
        //throw an exception if the given position is the root
		if (isRoot(p)) {
			throw new IllegalArgumentException("Cannot fetch sibling of root");
		}

		//check if the given nodes parent has another child and return it
        if (parent.hasLeft() && parent.hasRight()) {
            if (validate(p) == parent.getLeft()) {
                return parent.getRight();
            } else {
                return parent.getLeft();
            }
        }else {
            return null;
        }

    }

	/* creates a root for an empty tree, storing e as element, and returns the 
	 * position of that root. An error occurs if tree is not empty. 
	 */
    public Position<E> addRoot(E e) throws IllegalStateException {
        if (size != 0){
            throw new IllegalStateException("Linked Binary Tree already has a root.");
        }else {
        	//if the linked binary tree doesnt have a root create a new node and set it to the root.
            this.root = new Node<E>(e , null, null, null);
            size++;
        }

        return root;
    }

	/* creates a new left child of Position p storing element e, return the left child's position.
	 * If p has a left child already, throw exception IllegalArgumentExeption. 
	 */
    public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {

        if (validate(p).hasLeft()){
            throw new IllegalArgumentException("Left Child already exists");
        }else {
        	validate(p).setLeft(new Node<E>(e , null, null, validate(p)));
            size++;
        }

        return validate(p).getLeft();
    }

	/* creates a new right child of Position p storing element e, return the right child's position.
	 * If p has a right child already, throw exception IllegalArgumentExeption. 
	 */
    public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {

        if (validate(p).hasRight()){
            throw new IllegalArgumentException("Right Child already exists");
        }else {
        	validate(p).setRight(new Node<E>(e , null, null, validate(p)));
            size++;
        }

        return validate(p).getRight();
    }

	/* Attach trees t1 and t2 as left and right subtrees of external Position. 
	 * if p is not external, throw IllegalArgumentExeption.
	 */
    public void attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) throws IllegalArgumentException{

        if (isExternal(p)){
        	//if the position is external set its left and right children to the given binary trees.
        	validate(p).setLeft(t1.root);
        	validate(p).setRight(t2.root);
        	//set both of the sub trees parents
            t1.root.setParent(validate(p));
            t2.root.setParent(validate(p));
            //combine the size of the sub trees
            size += t1.size + t2.size;
        }else {
            throw new IllegalArgumentException("Position is not external");
        }

    }

    //inner node class that implements position
    public static class Node<E> implements Position<E> {
        private E e;
        private Node<E> left;
        private Node<E> right;
        private Node<E> parent;

        public Node(E data, Node<E> left, Node<E> right, Node<E> parent) {
            this.e = data;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public Node<E> getLeft() {
            return left;
        }

        public void setLeft(Node<E> left) {
            this.left = left;
        }

        public Node<E> getRight() {
            return right;
        }

        public void setRight(Node<E> right) {
            this.right = right;
        }

        public Node<E> getParent() {
            return parent;
        }

        public void setParent(Node<E> parent) {
            this.parent = parent;
        }
        
        @Override
        public E getElement() {
            return e;
        }

        public void setElement(E element) {
            e = element;
        }
        
        public boolean hasLeft(){
            if (this.left == null){
                return false;
            }else {
                return true;
            }
        }

        public boolean hasRight(){
            if (this.right == null){
                return false;
            }else {
                return true;
            }
        }
    }
    
    //helper method to safely cast positions to nodes
	public Node<E> validate(Position<E> p) throws IllegalArgumentException {
		if (!(p instanceof Node)) throw new IllegalArgumentException("Invalid position");
			Node<E> node = (Node<E>) p;
			return node;
	}
    
}