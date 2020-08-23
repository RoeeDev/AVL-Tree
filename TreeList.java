import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 *
 * Tree list
 *
 * An implementation of a Tree list with  key and info
 *
 */
 public class TreeList{
	 
	 private treeNode root;
	 private int n;
	 /**
   * public Item retrieve(int i)
   *
   * returns the item in the ith position if it exists in the list.
   * otherwise, returns null
   */
	 	
	 public Item retrieve(int i)
	 {
		 if (i < 0 || i >= n) {
			 return null;
		 }
		 else {
			 treeNode resultNode = select(i+1);
			 return resultNode.getItem();
		 }
	 }

  /**
   * public int insert(int i, int k, String s) 
   *
   * inserts an item to the ith position in list  with key k and  info s.
   * returns -1 if i<0 or i>n otherwise return 0.
   */
   public int insert(int i, int k, String s) {
	   if(i<0 || i>n) {
		 return -1;
	   }
	   treeNode currNode;
	   treeNode addedNode = new treeNode (k , s);
	   if(root == null) { //list is empty
		   root = addedNode;
		   n++;
		   return 0;
	   }
	   else if(i == n){ //insert-last
		   currNode = root;
		   while(currNode.getRight() != null) {
			   currNode = currNode.getRight();
		   }
		   currNode.setRight(addedNode);
	   }
	   else { // i>=0 , i<n and tree is not empty.
		   currNode = select(i+1);	//current element in index i
		   if(currNode.getLeft() == null) { //currNode doesnt have a left son
			   currNode.setLeft(addedNode);
		   }
		   else { //currNode has a left son - got left and all the way right
			   currNode = currNode.getLeft();
			   while (currNode.getRight() != null) {
				   currNode = currNode.getRight();
			   }
			   currNode.setRight(addedNode);
		   }
		   
	   }
	   addedNode.setParent(currNode);
	   n++;

	   //backward steps to maintain legal AVLTree and maintain each node's size, height    
	   while(currNode != null) {
		   int newHeight = nodeHeight(currNode); //calculate and set node's height
		   currNode.setHeight(newHeight);
	   
		   int currBF = nodeBF(currNode); //Balance factor calculations
		   int sonBF;
	   
		   if(currBF==2 || currBF==-2) { // must rotate the tree
		   
			   if(currBF == -2) { // rotate side depends on right son BF															// currBF=-2 then check right son BF
				   sonBF=nodeBF(currNode.getRight());
			   								
				   if(sonBF==-1) { // maintain with 1 left rotation
					   this.leftRotate(currNode);
				  
				   }
				   else { //maintain with right and then left rotations (sonBF=1)
					   treeNode x = currNode.getRight();
					   this.rightRotate(x);
				   
					 //update x's size and height
					   x.setHeight(nodeHeight(x));
					   int xSize = nodeSize(x);
					   x.setSize(xSize);

					 //update y's (x's parent) size and height
					   treeNode y = x.getParent();
					   y.setHeight(nodeHeight(y));
					   int ySize = nodeSize(y);
					   y.setSize(ySize);
				   
					   // 2nd rotation -> left rotation
					   this.leftRotate(currNode);
				   }
			   
				   // after the last rotation - (1st for sonBF=-1 , 2nd for sonBF=1) - update x and y
				   currNode.setHeight(nodeHeight(currNode));
				   int currSize = nodeSize(currNode);
				   currNode.setSize(currSize);
				   
				   treeNode y = currNode.getParent();
				   y.setHeight(nodeHeight(y));
				   int ySize = nodeSize(y);
				   y.setSize(ySize);
				   currNode = y;
			   
			   }
			   else { //currBF=2	
				   sonBF=nodeBF(currNode.getLeft()); // rotate side depends on left son BF	
				   if(sonBF==1) { // maintain with 1 right rotation
					   this.rightRotate(currNode);			   		
				   }
				   else { // maintain with left then right rotations
					   treeNode x = currNode.getLeft(); //x is the left son of currNode
					   this.leftRotate(x);
				   
					   //update x's size and height
					   x.setHeight(nodeHeight(x));
					   int xSize = nodeSize(x);
					   x.setSize(xSize);
					   //update y's size and height
					   treeNode y = x.getParent();
					   y.setHeight(nodeHeight(y));
					   int ySize = nodeSize(y);
					   y.setSize(ySize);
					   
					   this.rightRotate(currNode);				   
				   }
			   
				   // after the last rotation - (1st for sonBF=1 , 2nd for sonBF=-1) - update x and y
				   currNode.setHeight(nodeHeight(currNode));
				   int currSize = nodeSize(currNode);
				   currNode.setSize(currSize);
				
				   treeNode y = currNode.getParent();
				   y.setHeight(nodeHeight(y));
				   int ySize = nodeSize(y);
				   y.setSize(ySize);
						
				   currNode = y;
			   }
		   }
		   else { //no BF criminal size update
			   int newSize = nodeSize(currNode);
			   currNode.setSize(newSize);
			   
			   currNode=currNode.getParent();
		   }
	   }	   
	   return 0;
   }

   
   
   
  /**
   * public int delete(int i)
   *
   * deletes an item in the ith posittion from the list.
	* returns -1 if i<0 or i>n-1 otherwise returns 0.
   */
   public int delete(int i)
   {
	   if(i<0 || i>=n) {
		   return -1;
	   }
	   else if(root == null) { //tree is empty tree
		   return -1;
	   }
	   treeNode currNode = select(i+1);
	   int currRank;

	   //'remove' currNode from the tree:
	   currNode = this.deleteNode(currNode); //this method will return the closest node that changed its size and height
	   if (currNode != null) { //must update before checking BF criminals
		   int currSize = nodeSize(currNode);
		   currNode.setSize(currSize);
		   currNode.setHeight(nodeHeight(currNode));
	   }

	   while(currNode != null) { //backward steps to maintain legal AVLTree and node's size and height
		   int currBF = nodeBF(currNode);
		   int sonBF;

		   if(currBF==2 || currBF==-2) {

			   if(currBF == -2) { // rotate side depends on right son BF
				   sonBF=nodeBF(currNode.getRight());
				   if((sonBF==-1) || (sonBF==0)) {  // maintain with 1 left rotation
					   this.leftRotate(currNode);
					}
					else { //maintain with right and then left rotations (sonBF=1)
						treeNode x = currNode.getRight(); //x is the right son of currNode
						this.rightRotate(x);
	  
						//update x's size and height
						x.setHeight(nodeHeight(x));
						int xSize = nodeSize(x);
						x.setSize(xSize);
						
						//update y's size and height
						treeNode y = x.getParent();
						y.setHeight(nodeHeight(y));
						int ySize = nodeSize(y);
						y.setSize(ySize);

						// 2nd rotation -> left rotation
						this.leftRotate(currNode);
					}

				   // after the last rotation - (1st for sonBF=-1,0 , 2nd for sonBF=1) - update x and y
				   //update currNode = x
				   currNode.setHeight(nodeHeight(currNode));
				   int currSize = nodeSize(currNode);
				   currNode.setSize(currSize);
					
				   treeNode y = currNode.getParent();
				   y.setHeight(nodeHeight(y));
				   int ySize = nodeSize(y);
				   y.setSize(ySize);
				   currNode = y;
			   }
			   else { //currBF=2
				   sonBF=nodeBF(currNode.getLeft()); // rotate side depends on left son BF
				   if((sonBF==1) || (sonBF==0)) { // maintain with 1 right rotation
						this.rightRotate(currNode);
					}
				   else { // maintain with left then right rotations
					   treeNode x = currNode.getLeft(); //x is the left son of currNode
					   this.leftRotate(x);
					   
					   //update x's size and height
					   x.setHeight(nodeHeight(x));
					   int xSize = nodeSize(x);
					   x.setSize(xSize);
					   //update y's size and height
					   treeNode y = x.getParent();
					   y.setHeight(nodeHeight(y));
					   int ySize = nodeSize(y);
					   y.setSize(ySize);			
					 
					   this.rightRotate(currNode);				  
				   }
				   //after the last rotation - (1st for sonBF=1,0 , 2nd for sonBF=-1) - update x and y
				   currNode.setHeight(nodeHeight(currNode));
				   int currSize = nodeSize(currNode);
				   currNode.setSize(currSize);
					
				   treeNode y = currNode.getParent();
				   y.setHeight(nodeHeight(y));
				   int ySize = nodeSize(y);
				   y.setSize(ySize);					
									
				   currNode = y;
			   }
		   }
		   else { //no BF criminal size update
			   int newSize = nodeSize(currNode);
			   currNode.setSize(newSize);
			   currNode.setHeight(nodeHeight(currNode));
			   currNode=currNode.getParent();
		   }
	   }
	   return 0;	   
   }
   
   //this method return the element in position i'th in the list
   private treeNode select (int i) {
	   if(i<=0 || i>n) {
		   return null;
	   }
	   return selectRec(root, i);
   }
     
   public treeNode selectRec (treeNode curr , int i){
	   int rankCnt = 0;
	   if(curr.getLeft() == null) {
		   rankCnt = 1;
	   }
	   else {
		   rankCnt = curr.getLeft().getSize() +1;
	   }
	   if(rankCnt == i) {
		   return curr;
	   }
	   else if(rankCnt > i) {
		   return selectRec(curr.getLeft(), i);
	   }
	   else {
		   return selectRec(curr.getRight(), i-rankCnt);  
	   }
   }

   // Left rotation method   
   private void leftRotate(treeNode x) {
	   treeNode y = x.getRight();
	   x.setRight(y.getLeft());
	   if (y.getLeft() != null) {
		   y.getLeft().setParent(x);
	   }
	   y.setParent(x.getParent());
	   if (x.getParent() == null) {
		   this.root = y;
	   }
	   else if (x == x.getParent().getLeft()) {
		   x.getParent().setLeft(y);
	   }
	   else {
		   x.getParent().setRight(y);
	   }
	   y.setLeft(x);
	   x.setParent(y);
   }

	//right rotation method
	private void rightRotate(treeNode x) {
		treeNode y = x.getLeft();
		x.setLeft(y.getRight());
		if (y.getRight() != null) {
			y.getRight().setParent(x);
		}
		y.setParent(x.getParent());
		if (x.getParent() == null) {
			this.root = y;
		}
		else if (x == x.getParent().getRight()) {
			x.getParent().setRight(y);
		}
		else {
			x.getParent().setLeft(y);
		}
		y.setRight(x);
		x.setParent(y);
	}
	
	//this method returns a given node's BF
	private int nodeBF(treeNode currNode) {
		//default heights of empty subtree:
		int leftHeight=-1;
		int rightHeight =-1;
		//calculate node's subtrees heights 
		if(currNode.getLeft() != null) {
			leftHeight = currNode.getLeft().getHeight();
		}
		if(currNode.getRight() != null) {
			rightHeight = currNode.getRight().getHeight();
		}
		return leftHeight-rightHeight;
	}
	
	//this method returns a given node's height
	private int nodeHeight(treeNode currNode) {
		//default heights of empty subtree:
		int leftHeight = -1;
		int rightHeight = -1;
		//calculate node's subtrees heights   
		if (currNode.getLeft() != null) {
			leftHeight = currNode.getLeft().getHeight();
		}
		if (currNode.getRight() != null) {
		   rightHeight = currNode.getRight().getHeight();
		}
		return (1 + Math.max(leftHeight, rightHeight));
		}

	//this method returns a given node's size
	private int nodeSize(treeNode currNode) {
		//default size of empty subtree:
		int leftSize = 0;
		int rightSize = 0;
		//calculate node's subtrees sizes
		if (currNode.getLeft() != null) {
			leftSize = currNode.getLeft().getSize();
		}
		if (currNode.getRight() != null) {
		   rightSize = currNode.getRight().getSize();
		}
		return (1 + leftSize + rightSize);
	   	}
	   
	//this method removed a given node from the tree and returns the closest node that has to be maintained
	private treeNode deleteNode(treeNode currNode) {
	   treeNode leftNode = currNode.getLeft();
	   treeNode rightNode = currNode.getRight();
	   treeNode parentNode = currNode.getParent();
		   
	   // check if currNode is a root or a left/right to decide which node to return at the end
	   boolean left = false;
	   if(parentNode != null) { //currNode is not the root
		   if(currNode == parentNode.getLeft()) { //currNode is a left subtree of its parent
			   left = true;
		   }
	   }
		   
		   					
	   if ((leftNode == null) && (rightNode == null)) { //currNode is a leaf
		   if(root == currNode) { //currNode is also a root
			   root = null;
		   }
		   else if(currNode == parentNode.getLeft()) { //currNode is left subtree of its parent
			   parentNode.setLeft(null);
			   currNode.setParent(null);
		   }
		   else { //currNode is right subtree of its parent
			   parentNode.setRight(null);
			   currNode.setParent(null);
		   }
	   }
	   //currNode has only one subtree
	   else if (((leftNode == null) && (rightNode != null)) || ((leftNode != null) && (rightNode == null)) ) {	//currNode has one non-empty subtree
		   if(root == currNode) {
			   if(leftNode != null) {
				   root = leftNode;
			   }
			   else {
				   root = rightNode;
			   }
			   root.setParent(null);
		   }
		   else if(currNode == parentNode.getLeft()) { //currNode is left subtree of its parent
			   if(leftNode != null) { //currNode has left subtree
				   parentNode.setLeft(leftNode);
				   leftNode.setParent(parentNode);
			   }
			   else { //currNode has right subtree
				   parentNode.setLeft(rightNode);
				   rightNode.setParent(parentNode);
			   }
		   }
		   else { //currNode is right subtree of its parent
			   if(leftNode != null) { //currNode has left subtree
				   parentNode.setRight(leftNode);
				   leftNode.setParent(parentNode);
			   }
			   else { //currNode has right subtree
				   parentNode.setRight(rightNode);
				   rightNode.setParent(parentNode);
			   }
		   }
	   }
	   else { //currNode has two non-empty subtree.
		   treeNode consecNode = rightNode;	//find the consecutive node of currNode	
		   while (consecNode.getLeft()!=null) {
			   consecNode =  consecNode.getLeft();
		   }
		   treeNode consecParent = consecNode.getParent(); //consecNode parent
		   treeNode consecRight = consecNode.getRight(); //consecNode right (does'nt have left node)
		   
		   if(consecNode != rightNode) { // is consecNode not rightNode of deleted node
			   consecParent.setLeft(consecRight);
			   if(consecRight != null) {
				   consecRight.setParent(consecParent);
			   }
			   consecNode.setRight(rightNode);
			   rightNode.setParent(consecNode);
		   }   
		   consecNode.setParent(parentNode);
		   if(root == currNode) { //currNode is the root
			   root = consecNode;
		   }
		   else if(currNode == parentNode.getLeft()) {	//currNode is left subtree of its parent
				   parentNode.setLeft(consecNode); // place consecNode instead of currNode
		   }
		   else { //currNode is right subtree of its parent
			   parentNode.setRight(consecNode); // place consecNode instead of currNode
		   }
		   // connect left subtree of currNode to consecNode (left subtree)
		   consecNode.setLeft(leftNode);
		   leftNode.setParent(consecNode);
		   
		   //update size and height of consecNode:
		   consecNode.setHeight(nodeHeight(consecNode));
		   int consecSize = nodeSize(consecNode);
		   consecNode.setSize(consecSize);
		   //totally remove of currNode 
		   currNode.setParent(null);
		   currNode.setLeft(null);
		   currNode.setRight(null);
		   n--;
		   if(consecNode == rightNode) {
			   return consecNode;
		   }
		   else {
			   return consecParent;
		   }
	   }
	   //last part of first 2 situations - currNode was a leaf or has only one subtree.
	   //delete currNode entirely
	   currNode.setParent(null);
	   currNode.setLeft(null);
	   currNode.setRight(null);
	   n--;
	   if(parentNode == null) {	//currNode was the origin root
		   return root;
	   }
	   else if(left == true) {
		   if(parentNode.getLeft()!= null) {
			   return parentNode.getLeft();
		   }
		   return parentNode;
	   }
	   else {
		   if(parentNode.getRight()!= null) {
			   return parentNode.getRight();
		   }
		   return parentNode; 
	   }   
	}
	   
   private class treeNode {
	   private Item info;
	   private int size;
	   private int height;
	   private treeNode right;
	   private treeNode left;
	   private treeNode parent;
		  
	   public treeNode(int key, String val) {
		   this.info = new Item (key , val);
		   this.size = 1; // a new node will always have size=1 before rotations (insert as a leaf)
	   }
	   //this method returns the node's Item
	   public Item getItem() {
		   return this.info;
	   }
	   //this method sets the node's left son
	   public void setLeft(treeNode node)
	   {
		   left = node;
	   }
	   //this method returns the node's left son
	   public treeNode getLeft()
	   {
		   return left;
	   }
	   //this method sets the node's right son
	   public void setRight(treeNode node)
	   {
		   right = node;
	   }
	   //this method returns the node's right son	
	   public treeNode getRight()
	   {
		   return right;
	   }
	   //this method sets the node's parent
	   public void setParent(treeNode node)
	   {
			parent = node;
	   }
	   //this method returns the node's parent
	   public treeNode getParent()
	   {
		   return parent;
	   }
	   //this method sets the node's height
	   public void setHeight(int height)
	   {
		   this.height = height;
	   }
	   //this method returns the node's height
	   public int getHeight()
	   {
		   return height;
	   }
	   //this method returns the node's size
	   public int getSize() 
	   {
		   return size;
	   }
	   //this method sets the node's size son
	   public void setSize(int s) 
	   {
		   this.size = s;
	   }
   }
 }
 
   
