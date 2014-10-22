package BinomialHeap1;
import java.io.*;
public class BinomialHeap1 {

	//class BinomialHeapNode
	public static class BinomialHeapNode {

		private int key;
		private int degree;
		private BinomialHeapNode parent; 
		private BinomialHeapNode sibling;
		private BinomialHeapNode child;
		public BinomialHeapNode(int k) {

			key = k;
			degree = 0;
			parent = null;
			sibling = null;
			child = null;
		}
		public int getKey() { // returns the key
			return key;
		}

		private void setKey(int value) { // sets key
			key = value;
		}

		public int getDegree() { // returns degree
			return degree;
		}

		private void setDegree(int deg) { // sets degree 
			degree = deg;
		}

		public BinomialHeapNode getParent() { // returns parent of current node
			return parent;
		}

		private void setParent(BinomialHeapNode par) { // sets parent of the current node
			parent = par;
		}

		public BinomialHeapNode getSibling() { // returns the next binomial tree
			return sibling;
		}

		private void setSibling(BinomialHeapNode nextBr) { // sets the next binomial tree 
			sibling = nextBr;
		}

		public BinomialHeapNode getChild() { // returns child of current node
			return child;
		}

		private void setChild(BinomialHeapNode firstCh) { // sets child of current node
			child = firstCh;
		}

		public int getSize() {//no. of nodes
			return (1 + ((child == null) ? 0 : child.getSize()) + ((sibling == null) ? 0
					: sibling.getSize()));
		}

		private BinomialHeapNode reverse(BinomialHeapNode sibl) {
			BinomialHeapNode ret;
			if (sibling != null)
				ret = sibling.reverse(this);
			else
				ret = this;
			sibling = sibl;
			return ret;
		}

		private BinomialHeapNode findMinNode() {
			BinomialHeapNode x = this, y = this;

			int min = x.key;

			while (x != null) {
				if (x.key < min) {
					y = x;
					min = x.key;
				}
				x = x.sibling;
			}

			return y;
		}

		// Find a node with the given key
		private BinomialHeapNode findANodeWithKey(int value) {
			BinomialHeapNode temp = this, node = null;
			while (temp != null) {
				if (temp.key == value) {
					node = temp;
					break;
				}
				if (temp.child == null)
					temp = temp.sibling;
				else {
					node = temp.child.findANodeWithKey(value);
					if (node == null)
						temp = temp.sibling;
					else
						break;
				}
			}

			return node;
		}

	}
	private BinomialHeapNode Nodes;

	private int size;

	public BinomialHeap1() {
		Nodes = null;
		size = 0;
	}

	// 2. Find the minimum key
	public int findMinimum() {
		return Nodes.findMinNode().key;
	}

	// Union of two binomial heaps
	private void merge(BinomialHeapNode binHeap) {
		BinomialHeapNode temp1 = Nodes, temp2 = binHeap;
		while ((temp1 != null) && (temp2 != null)) {
			if (temp1.degree == temp2.degree) {
				BinomialHeapNode tmp = temp2;
				temp2 = temp2.sibling;
				tmp.sibling = temp1.sibling;
				temp1.sibling = tmp;
				temp1 = tmp.sibling;
			} else {
				if (temp1.degree < temp2.degree) {
					if ((temp1.sibling == null)
							|| (temp1.sibling.degree > temp2.degree)) {
						BinomialHeapNode tmp = temp2;
						temp2 = temp2.sibling;
						tmp.sibling = temp1.sibling;
						temp1.sibling = tmp;
						temp1 = tmp.sibling;
					} else {
						temp1 = temp1.sibling;
					}
				} else {
					BinomialHeapNode tmp = temp1;
					temp1 = temp2;
					temp2 = temp2.sibling;
					temp1.sibling = tmp;
					if (tmp == Nodes) {
						Nodes = temp1;
					} else {
					}
				}
			}
		}

		if (temp1 == null) {
			temp1 = Nodes;
			while (temp1.sibling != null) {
				temp1 = temp1.sibling;
			}
			temp1.sibling = temp2;
		} else {
		}
	}
	private void unionNodes(BinomialHeapNode binHeap) {
		merge(binHeap);

		BinomialHeapNode prevTemp = null, temp = Nodes, nextTemp = Nodes.sibling;

		while (nextTemp != null) {
			if ((temp.degree != nextTemp.degree)
					|| ((nextTemp.sibling != null) && (nextTemp.sibling.degree == temp.degree))) {
				prevTemp = temp;
				temp = nextTemp;
			} else {
				if (temp.key <= nextTemp.key) {
					temp.sibling = nextTemp.sibling;
					nextTemp.parent = temp;
					nextTemp.sibling = temp.child;
					temp.child = nextTemp;
					temp.degree++;
				} else {
					if (prevTemp == null) {
						Nodes = nextTemp;
					} else {
						prevTemp.sibling = nextTemp;
					}
					temp.parent = nextTemp;
					temp.sibling = nextTemp.child;
					nextTemp.child = temp;
					nextTemp.degree++;
					temp = nextTemp;
				}
			}

			nextTemp = temp.sibling;
		}
	
	}

	// 4. Insert a node 
	public void insert(int value) {
		if (value > 0) {
			BinomialHeapNode temp = new BinomialHeapNode(value);
			if (Nodes == null) {
				Nodes = temp;
				size = 1;
			} else {
				unionNodes(temp);
				size++;
			}
		}
	}

	// 5. Extract the node with the minimum key
	public int extractMin() {
		if (Nodes == null)
			return -1;

		BinomialHeapNode temp = Nodes, prevTemp = null;
		BinomialHeapNode minNode = Nodes.findMinNode();
		while (temp.key != minNode.key) {
			prevTemp = temp;
			temp = temp.sibling;
		}

		if (prevTemp == null) {
			Nodes = temp.sibling;
		} else {
			prevTemp.sibling = temp.sibling;
		}
		temp = temp.child;
		BinomialHeapNode fakeNode = temp;
		while (temp != null) {
			temp.parent = null;
			temp = temp.sibling;
		}

		if ((Nodes == null) && (fakeNode == null)) {
			size = 0;
		} else {
			if ((Nodes == null) && (fakeNode != null)) {
				Nodes = fakeNode.reverse(null);
				size = Nodes.getSize();
			} else {
				if ((Nodes != null) && (fakeNode == null)) {
					size = Nodes.getSize();
				} else {
					unionNodes(fakeNode.reverse(null));
					size = Nodes.getSize();
				}
			}
		}

		return minNode.key;
	}

	// 6. Decrease a key value
	public void decreaseKeyValue(int old_value, int new_value) {
		BinomialHeapNode temp = Nodes.findANodeWithKey(old_value);
		if (temp == null)
			return;
		temp.key = new_value;
		BinomialHeapNode tempParent = temp.parent;

		while ((tempParent != null) && (temp.key < tempParent.key)) {
			int z = temp.key;
			temp.key = tempParent.key;
			tempParent.key = z;

			temp = tempParent;
			tempParent = tempParent.parent;
		}
	}
	public void displayHeap()
	{
		System.out.println("\nheap:");
		displayHeap(Nodes);
		System.out.println("\n");
	}
       	private void displayHeap(BinomialHeapNode r)
	{
	       	if(r!=null)
		{
			
			
		       //System.out.print("");
			displayHeap(r.child);
			System.out.println(""+r.key);
			//System.out.println("\n");			
			displayHeap(r.sibling);
			System.out.print(" ");
			
		}
	      	
	}
	// Delete a node 
	public void delete(int value) {
		if ((Nodes != null) && (Nodes.findANodeWithKey(value) != null)) {
			decreaseKeyValue(value, findMinimum() - 1);
			extractMin();
		}
	}

	public static void main(String[] args)throws IOException {
		try
		{
		BinomialHeap1 b = new BinomialHeap1();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("no.of elements");
		int n=Integer.parseInt(br.readLine());
		System.out.println("enter the elements:");
		for(int i=0;i<n;i++)
		{
			int k=Integer.parseInt(br.readLine());
			b.insert(k);
		}
		b.displayHeap();
		System.out.println("min: " + b.findMinimum());
		System.out.println("size: " + b.size);
		b.extractMin();
		b.displayHeap();
		System.out.println("enter node to be deleted:");
		//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                int k=Integer.parseInt(br.readLine());
                b.delete(k);
                b.displayHeap();
		System.out.println("min: " + b.findMinimum());
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}

