package quicklinks;

import quicklinks.Node;

/*
 * TreeNode - nodes that go without our NodeTree structure. Each node contains direct onformation about it's
 *            location: height, width, offset, parent tree, next node, encapsulated node
 */
public class TreeNode {
    /*the following properties are specified for treenodes:
       height - the distance from the terminal ring structure to this node. The root of the tree has H=1 */
    private int height;
    /* ID     - the identity of the host tree. We can seperate nodes from different trees in O(1) */
    private int ID;
    /* child  - the single unique node which is a child to this node */
    private TreeNode child;
    /* node   - the component node associated with this treeNode - used for equalities */
    private Node node;
    /* tree   - the parent tree of this node*/
    private NodeTree tree;
    /* width  - the 'identity' of this branch in the tree. width is counted from the top, and is incremented
                by 1 for each split in the tree. See 'treeTest.txt, treeTest.png' for pictorial demonstration.
    */
    private int width;
    /* offset - the distance from the current branch to the leftmost branch. Considering the leftmost branch to be
                vertical, the offset is the number of splits between the current branch and the leftmost branch
    */      
    private int offset = 0;
    
    public TreeNode(int ID, Node node, int height, TreeNode child, NodeTree tree, int width, int offset) {
	this.ID = ID;
	this.node = node;
	this.height = height;
	this.child = child;
	this.tree = tree;
	this.width = width;
	this.offset = offset;
    }

    public int getWidth() {
	return width;
    }

    public Node getNode() {
	return node;
    }

    public int getID() {
	return ID;
    }

    public int getHeight() {
	return height;
    }

    public TreeNode getChild() {
	return child;
    }

    public NodeTree getTree() {
	return tree;
    }

    public int getOffset() {
	return offset;
    }
}
