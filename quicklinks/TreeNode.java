package quicklinks;

import quicklinks.Node;

public class TreeNode {
    private int height;
    private int ID;
    private TreeNode child;
    private Node node;
    private NodeTree tree;
    private int width;
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
