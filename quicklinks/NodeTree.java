package quicklinks;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.TreeMap;
import quicklinks.Node;
import quicklinks.TreeNode;
public class NodeTree {
    private static int _ID = 0;
    private static Object _lock = new Object();
    private int ID;

    TreeNode head;

    //this is an indexed map which can, given an input node, give us as output the placed node within the tree
    //the placed node knows information about it's height, and which tree it belongs to, as well as the child
    //node in the tree
    ArrayList<TreeNode>  nodes = new ArrayList<>();;
    Loop targetLoop;
    public Loop getLoop() {
	return targetLoop;
    }
    public ArrayList<TreeNode> getNodes() {
	return nodes;
    }
    
    public NodeTree(Loop targetLoop) {
	this.targetLoop = targetLoop;
	synchronized(_lock) {
	    ID = _ID++;
	}
    }

    public void assemble(Node node, ArrayList<TreeSet<Node>> ancestors) {
	head = new TreeNode(ID, node, 1, null, this);	
	assemble(head, 1, ancestors);
    }

    private void assemble(TreeNode node, int height, ArrayList<TreeSet<Node>> ancestors) {
	nodes.add(node);
	int identity = node.getNode().getID();
	TreeSet<Node> idAncestors = ancestors.get(identity);

	if(idAncestors != null) {
	    for(Node n : idAncestors) {
		TreeNode next = new TreeNode(ID, n, height + 1, node, this);
		assemble(next, height + 1, ancestors);
	    }
	}
    }

    public Node getHead() {
	return head.getNode();
    }

}
