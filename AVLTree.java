import java.util.Arrays;

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {

	private IAVLNode root;
	private IAVLNode min;
	private IAVLNode max;
	private int n;					
	

   /**
	* public boolean empty()
	* 
	* returns true if and only if the tree is empty
	* 
	*/

	public boolean empty() {
		return (root==null);
	}

   /**
	* public String search(int k)
	*
	* returns the info of an item with key k if it exists in the tree
	* otherwise, returns null
	*/
	
	public String search(int k)
	{
		IAVLNode currNode = root;
		int currKey;
		while (currNode != null) {
			currKey = currNode.getKey();
			if(currKey == k) {
				return currNode.getValue();
			}
			else if (currKey > k) {
				currNode = currNode.getLeft();
			}
			else {
				currNode = currNode.getRight();
			}
		}
		return null;
	}

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the AVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
	public int insert(int k, String i) {
		int rotationsCnt = 0;
		IAVLNode currNode;
	  
		if(root == null) { //inserting for an empty tree
			AVLNode newNode = new AVLNode(k, i);
			newNode.setSize(1);
			root = newNode;
			n++;
			min = root;
			max = root;
			return rotationsCnt;
		   
		   // node default height initialized to 0
		}	   			
	   
		else { //inserting in a non empty tree 
			IAVLNode prev = root;
			currNode = root;
			int currKey;
			while (currNode != null) { //finds the right spot for the new node
				prev = currNode;
				currKey = currNode.getKey();
				if(currKey == k) { //k already exist
					return -1;
				}
				else if (currKey > k) {
					currNode = currNode.getLeft();
				}
				else {
					currNode = currNode.getRight();
				}
			}
			AVLNode preCastedNode = new AVLNode(k, i); // preCasted for initializing size
			preCastedNode.setSize(1);
			IAVLNode newNode = preCastedNode;//newNode (IAVLNode type) is the actual inserted node
			// node's default height initialized to 0
		   
			if (min.getKey() > k){								//min update
				min = newNode;
			}
			if (max.getKey() < k) { 								//max update
				max = newNode;
			}
			if (prev.getKey() > k) { 							//insert as a left subtree
				prev.setLeft(newNode);
			}
			else {												//insert as a right subtree
				prev.setRight(newNode);
			}
			n++; 												
			newNode.setParent(prev);
			currNode = newNode.getParent();
		}
	   
	   
		//backward steps to maintain legal AVLTree and node's size and height. 
		while(currNode != null) {
			int newHeight = nodeHeight(currNode); //calculate and set node's height
			currNode.setHeight(newHeight); 
		   
			int currBF = nodeBF(currNode); //Balance factor calculations
			int sonBF;
		   
			if(currBF==2 || currBF==-2) { // must rotate the tree
			   
				if(currBF == -2) {	// rotate side depends on right son BF														
					sonBF=nodeBF(currNode.getRight());
				   								
					if(sonBF==-1) { // maintain with 1 left rotation
						this.leftRotate(currNode);
						rotationsCnt++;
					  
				   }
				   else { //maintain with right and then left rotations (sonBF=1)
					   IAVLNode x = currNode.getRight(); //x is the right son of currNode
					   this.rightRotate(x);
					   rotationsCnt++;
					   
					 //update x's size and height
					   x.setHeight(nodeHeight(x));
					   int xSize = nodeSize(x);
					   AVLNode preUpXNode = (AVLNode) x;
					   preUpXNode.setSize(xSize);
					   x = preUpXNode;
					   
					   IAVLNode y = x.getParent(); //y is x's parent 
					 
					   y.setHeight(nodeHeight(y));
					   int ySize = nodeSize(y);
					   AVLNode preUpYNode = (AVLNode) y;
					   preUpYNode.setSize(ySize);
					   y = preUpYNode;
					   
					   
					   // 2nd rotation -> left rotation
					   this.leftRotate(currNode);
					   rotationsCnt++;
				   }
				   
				   // after the last rotation - (1st for sonBF=-1 , 2nd for sonBF=1) - update x and y
				   currNode.setHeight(nodeHeight(currNode));
				   int currSize = nodeSize(currNode);
				   AVLNode preUpCurrNode = (AVLNode) currNode;
				   preUpCurrNode.setSize(currSize);
				   currNode = preUpCurrNode;

				   IAVLNode y = currNode.getParent();
				   y.setHeight(nodeHeight(y));
				   int ySize = nodeSize(y);
				   AVLNode preUpYNode = (AVLNode) y;
				   preUpYNode.setSize(ySize);
				   y = preUpYNode;
				   currNode = preUpYNode;
				   
				   
				   
			   }
			   else {//currBF=2
				   sonBF=nodeBF(currNode.getLeft()); // rotate side depends on left son BF
				   if(sonBF==1) { // maintain with 1 right rotation
				   		this.rightRotate(currNode); 
				   		rotationsCnt++;
				   		
				   		
				   	}
				   else {// maintain with left then right rotations
					   IAVLNode x = currNode.getLeft(); //x is the left son of currNode
					   this.leftRotate(x);
					   rotationsCnt++;
					   //update x's size and height
					   x.setHeight(nodeHeight(x));
					   int xSize = nodeSize(x);
					   AVLNode preUpXNode = (AVLNode) x;
					   preUpXNode.setSize(xSize);
					   x = preUpXNode;
					   //update y's size and height
					   IAVLNode y = x.getParent();
					   y.setHeight(nodeHeight(y));
					   int ySize = nodeSize(y);
					   AVLNode preUpYNode = (AVLNode) y;
					   preUpYNode.setSize(ySize);
					   y = preUpYNode;
						
					   this.rightRotate(currNode);
					   rotationsCnt++;
					   
				   	}
				   // after the last rotation - (1st for sonBF=1 , 2nd for sonBF=-1) - update x and y
			   		currNode.setHeight(nodeHeight(currNode));
					int currSize = nodeSize(currNode);
					AVLNode preUpCurrNode = (AVLNode) currNode;
					preUpCurrNode.setSize(currSize);
					currNode = preUpCurrNode;
					
					IAVLNode y = currNode.getParent();
					y.setHeight(nodeHeight(y));
					int ySize = nodeSize(y);
					AVLNode preUpYNode = (AVLNode) y;
					preUpYNode.setSize(ySize);
					y = preUpYNode;
							
					currNode = y;
			   	}
			}
			else { //no BF criminal size update
				int newSize = nodeSize(currNode);
				   	AVLNode preUpCastNode = (AVLNode) currNode;
				   	preUpCastNode.setSize(newSize);
				   	currNode = preUpCastNode;
				   	currNode=currNode.getParent();
			}   
		}	   
		return rotationsCnt;
	}



  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   						// finding the node with key k
	   if(root == null) { //tree is empty tree
		   return -1;
	   }
	   int rotationsCnt = 0;
	   IAVLNode currNode = this.root;
	   int currKey;
	   while (currNode != null) { //finds if a node with key k exist 
		   currKey = currNode.getKey();
		   if(currKey == k) {
			   break;
		   }
		   else if (currKey > k) {
			   currNode = currNode.getLeft();
		   }
		   else {
			   currNode = currNode.getRight();
		   }
		   if(currNode == null) { //key k does not exist
			   return -1;
		   }
	   }
	   
	   if (min == currNode) { //min left subtree must be null.
		   if(currNode.getRight() != null) { //min has a right subtree
			   min = currNode.getRight();
		   }
		   else {
			   min = currNode.getParent();
		   } 
	   }
	   if (max == currNode) { //max's right subtree must be null 
		   if(currNode.getLeft() != null) {
			   max = currNode.getLeft();
		   }
		   else {
			   max = currNode.getParent();
		   } 
	   }
	   //'remove' currNode from the tree: 
	   currNode = this.deleteNode(currNode); //this method will return the closest node that changed its size and height
	   if(currNode != null) { //must update before checking BF criminals
		   int currSize = nodeSize(currNode);
		   AVLNode curr = (AVLNode) currNode;
		   curr.setSize(currSize);
		   currNode = curr;
		   currNode.setHeight(nodeHeight(currNode));
	   }
	   
	   while(currNode != null) { //backward steps to maintain legal AVLTree and node's size and height
		   int currBF = nodeBF(currNode);
		   int sonBF;

		   if(currBF==2 || currBF==-2) {
			   if(currBF == -2) { // rotate side depends on right son BF															// currBF=-2 then check right son BF
				   sonBF=nodeBF(currNode.getRight());
				   if((sonBF==-1) || (sonBF==0)) { // maintain with 1 left rotation
					   this.leftRotate(currNode);
					   rotationsCnt++;
				   }
				   else {//maintain with right and then left rotations (sonBF=1)
					   IAVLNode x = currNode.getRight(); //x is the right son of currNode
					   this.rightRotate(x);
					   rotationsCnt++;
					   
					  //update x's size and height
					   x.setHeight(nodeHeight(x));
					   int xSize = nodeSize(x);
					   AVLNode preUpXNode = (AVLNode) x;
					   preUpXNode.setSize(xSize);
					   x = preUpXNode;
					   
					   IAVLNode y = x.getParent();
					  //update y's size and height
					   y.setHeight(nodeHeight(y));
					   int ySize = nodeSize(y);
					   AVLNode preUpYNode = (AVLNode) y;
					   preUpYNode.setSize(ySize);
					   y = preUpYNode;
					   
					  // 2nd rotation -> left rotation
					   this.leftRotate(currNode);
					   rotationsCnt++;
				   }
				   
				  // after the last rotation - (1st for sonBF=-1,0 , 2nd for sonBF=1) - update x and y
				  //update currNode = x
				   currNode.setHeight(nodeHeight(currNode));
				   int currSize = nodeSize(currNode);
				   AVLNode preUpCurrNode = (AVLNode) currNode;
				   preUpCurrNode.setSize(currSize);
				   currNode = preUpCurrNode;

				   IAVLNode y = currNode.getParent();
				   y.setHeight(nodeHeight(y));
				   int ySize = nodeSize(y);
				   AVLNode preUpYNode = (AVLNode) y;
				   preUpYNode.setSize(ySize);
				   y = preUpYNode;
				   currNode = preUpYNode;
			   }
			   
			   else { //currBF=2
				   sonBF=nodeBF(currNode.getLeft()); // rotate side depends on left son BF	
				   if((sonBF==1) || (sonBF==0)) { // maintain with 1 right rotation
					   this.rightRotate(currNode);
					   rotationsCnt++;
				   		
				   }
				   else {// maintain with left then right rotations
					   IAVLNode x = currNode.getLeft(); //x is the left son of currNode
					   this.leftRotate(x);
					   rotationsCnt++;
					  //update x's size and height
					   x.setHeight(nodeHeight(x));
					   int xSize = nodeSize(x);
					   AVLNode preUpXNode = (AVLNode) x;
					   preUpXNode.setSize(xSize);
					   x = preUpXNode;
					  //update y's size and height
					   IAVLNode y = x.getParent();
					   y.setHeight(nodeHeight(y));
					   int ySize = nodeSize(y);
					   AVLNode preUpYNode = (AVLNode) y;
					   preUpYNode.setSize(ySize);
					   y = preUpYNode;
						
					   this.rightRotate(currNode);
					   rotationsCnt++;
				   }

				  //after the last rotation - (1st for sonBF=1,0 , 2nd for sonBF=-1) - update x and y
				   currNode.setHeight(nodeHeight(currNode));
				   int currSize = nodeSize(currNode);
				   AVLNode preUpCurrNode = (AVLNode) currNode;
				   preUpCurrNode.setSize(currSize);
				   currNode = preUpCurrNode;
					
				   IAVLNode y = currNode.getParent();
				   y.setHeight(nodeHeight(y));
				   int ySize = nodeSize(y);
				   AVLNode preUpYNode = (AVLNode) y;
				   preUpYNode.setSize(ySize);
				   y = preUpYNode;
				   
				   currNode = y;
			   }
		   }
		   else { //no BF criminal size update
			   int newSize = nodeSize(currNode);
			   AVLNode preUpCastNode = (AVLNode) currNode;
			   preUpCastNode.setSize(newSize);
			   currNode = preUpCastNode;
			   currNode.setHeight(nodeHeight(currNode));
			   currNode=currNode.getParent();
		   }
	   }
	   return rotationsCnt;
   }

  /**
   * public String min()
   *
   * Returns the info of the item with the smallest key in the tree,
   * or null if the tree is empty
   */
   public String min()
   {
	   if( min != null) {
		   return min.getValue();
	   }
	   return null;
   }

  /**
   * public String max()
   *
   * Returns the info of the item with the largest key in the tree,
   * or null if the tree is empty
   */
   public String max()
   {
	   if( max != null) {
		   return max.getValue();
	   }
	   return null;
	   
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   public int[] keysToArray()
   {
	   int [] arr = new int [n];
	   int [] i = new int [1]; //index-array
       keysToArrayRec(root , arr , i);
       return arr;
   }
   // this method is the recursive method that help builds the keys array.
   private void keysToArrayRec(IAVLNode node, int [] array, int [] index) {
	   if (node != null) {
		   keysToArrayRec(node.getLeft() , array , index);
		   array[index[0]] = node.getKey();
		   index[0]++;
		   keysToArrayRec(node.getRight(), array , index);
	   }
   }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
   public String[] infoToArray()
   {
	   String [] arr = new String[n];
	   int [] i = new int [1]; //index-array
	   InfoToArrayRec(root , arr , i);
	   return arr;
    }
   // this method is the recursive method that help builds the info array.
   private void InfoToArrayRec(IAVLNode node, String [] array, int [] index) {
	   if (node != null) {
		   InfoToArrayRec(node.getLeft() , array , index);
 		   array[index[0]] = node.getValue();
 		   index[0]++;
 		   InfoToArrayRec(node.getRight(), array , index);
 	   }
    }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
    public int size()
  	{
  		return n;
  	}
   
     /**
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IAVLNode getRoot()
   {
	   return root;
   }
   
   //Left rotation method
   private void leftRotate(IAVLNode x) {
	   IAVLNode y = x.getRight();
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
   private void rightRotate(IAVLNode x) {
	   IAVLNode y = x.getLeft();
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
   private int nodeBF(IAVLNode currNode) {
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
   private int nodeHeight(IAVLNode currNode) {
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
   private int nodeSize(IAVLNode currNode) {
	 //default size of empty subtree:
	   int leftSize = 0;
	   int rightSize = 0;
	 //calculate node's subtrees sizes
	   if (currNode.getLeft() != null) {
		   leftSize = ((AVLNode)currNode.getLeft()).getSize();
	   }
	   if (currNode.getRight() != null) {
		   rightSize = ((AVLNode)currNode.getRight()).getSize();
	   }
	   return (1 + leftSize + rightSize);
   }
   
 //this method removed a given node from the tree and returns the closest node that has to be maintained
   private IAVLNode deleteNode(IAVLNode currNode) {
	   IAVLNode leftNode = currNode.getLeft();
	   IAVLNode rightNode = currNode.getRight();
	   IAVLNode parentNode = currNode.getParent();
	   
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
		   else if(left == true) { //currNode is left subtree of its parent
			   parentNode.setLeft(null);
			   currNode.setParent(null);
		   }
		   else { //currNode is right subtree of its parent
			   parentNode.setRight(null);
			   currNode.setParent(null);
		   }
	   }
	   
	   	
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
		   else if(left == true) {	//currNode is left subtree of its parent
			   if(leftNode != null) {	//currNode has left subtree
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
		   IAVLNode consecNode = rightNode;	//find the consecutive node of currNode
		   while (consecNode.getLeft()!=null) {
			   consecNode =  consecNode.getLeft();
		   }
		   IAVLNode consecParent = consecNode.getParent();	//consecNode parent
		   IAVLNode consecRight = consecNode.getRight();	//consecNode right (does'nt have left node)
		   
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
		   else if(left == true) { //currNode is left subtree of its parent
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
			AVLNode consec = (AVLNode) consecNode;
			consec.setSize(consecSize);
			consecNode = consec;
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

	/**
	   * public interface IAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IAVLNode{	
		public int getKey(); //returns node's key 
		public String getValue(); //returns node's value [info]
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node 
	}

   /**
   * public class AVLNode
   *
   * If you wish to implement classes other than AVLTree
   * (for example AVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IAVLNode)
   */
  public class AVLNode implements IAVLNode{
	  
	  private int key;
	  private int size;
	  private String val;
	  private int height;
	  private AVLNode right;
	  private AVLNode left;
	  private AVLNode parent;
	  
	  public AVLNode(int key , String val) {
		  this.key = key;
		  this.val = val;
	  }
	  	//this method returns the node's key
		public int getKey()
		{
			return key;
		}
		//this method returns the node's value
		public String getValue()
		{
			return val;
		}
		//this method sets the node's left son
		public void setLeft(IAVLNode node)
		{
			left = (AVLNode) node;
		}
		//this method returns the node's left son
		public IAVLNode getLeft()
		{
			return left;
		}
		//this method sets the node's right son
		public void setRight(IAVLNode node)
		{
			right = (AVLNode) node;
		}
		//this method returns the node's right son
		public IAVLNode getRight()
		{
			return right;
		}
		//this method sets the node's parent
		public void setParent(IAVLNode node)
		{
			parent = (AVLNode) node;
		}
		//this method returns the node's parent
		public IAVLNode getParent()
		{
			return parent;
		}
		//this method sets the node's geight
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
		public int getSize() {
			return size;
		}
		//this method sets the node's size
		public void setSize(int s) {
			this.size = s;
		}
  }
}




