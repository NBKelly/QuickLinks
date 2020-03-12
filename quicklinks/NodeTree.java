package quicklinks;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.TreeMap;
import quicklinks.Node;
import quicklinks.TreeNode;
import java.util.LinkedList;

public class NodeTree {
    private static int _ID = 0;
    private static Object _lock = new Object();
    private int ID;

    TreeNode head;

    //this is an indexed map which can, given an input node, give us as output the placed node within the tree
    //the placed node knows information about it's height, and which tree it belongs to, as well as the child
    //node in the tree
    ArrayList<TreeNode>  nodes = new ArrayList<>();
    //these are all the branches for our tree. We can do a little bit of arithmetic to fix these things together
    ArrayList<TreeNode> branch = new ArrayList<TreeNode>();
    Loop targetLoop;
    int width = 0;
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
	head = new TreeNode(ID, node, 1, null, this, 0, 0);	
	assemble_inline(head, 1, ancestors, width);
    }

    //this is fast even for big lines!
    private void assemble_inline(TreeNode node, int height, ArrayList<TreeSet<Node>> ancestors, int _width) {
	LinkedList<TreeNode> deck = new LinkedList<TreeNode>();
	boolean first = true;
	int pass = 0;
	while(true) {
	    nodes.add(node);
	    int offset = node.getOffset();
	    int identity = node.getNode().getID();
	    TreeSet<Node> idAncestors = ancestors.get(identity);
	    //System.err.println("Assemble at width " + width + ", pass " + pass++);

	    if(idAncestors != null) {
		int width_ext = 0;
		for(Node n : idAncestors) {
		    if(width_ext == 0) {
			TreeNode next = new TreeNode(ID, n, height + 1, node, this, _width, offset);
			if(first) {
			    branch.add(next);
			    first = false;
			}
			deck.addFirst(next);
		    }
		    else {
			width = width + 1;
			TreeNode next = new TreeNode(ID, n, height + 1, node, this, width, offset + 1);
			branch.add(next);
			//branch.put(width, next);
			deck.add(next);
		    }
		    width_ext++;
		}		
	    }

	    if(deck.size() == 0)
		break;

	    node = deck.poll();
	    height = node.getHeight();
	    _width = node.getWidth();
	}
    }

    //deprecated: recursive method of solving doesn't work for big lines
    /*private void assemble(TreeNode node, int height, ArrayList<TreeSet<Node>> ancestors, int _width) {
	nodes.add(node);
	int identity = node.getNode().getID();
	TreeSet<Node> idAncestors = ancestors.get(identity);
	System.err.println("assemble at width " + width + " " + (ctr++));
	if(idAncestors != null) {
	    int width_ext = 0;
	    for(Node n : idAncestors) {
		if(width_ext == 0) {
		    TreeNode next = new TreeNode(ID, n, height + 1, node, this, _width);
		    assemble(next, height + 1, ancestors, _width);
		} else {
		    width = width + 1;
		    TreeNode next = new TreeNode(ID, n, height + 1, node, this, width);
		    branch.put(width, next);
		    assemble(next, height + 1, ancestors, width);
		}
		width_ext++;
	    }
	}
	System.err.println("Done at ctr " + ctr);
	}*/

    //a note on tree resolution: given a width, we can instantly lookup where the next branch is
    //given that branch, we can follow
    //given a source and dest, we follow branches until:
    //additionally, we can instantly discount a tree if the dest has a higher width than the origin
    //width(origin) == width(dest)
    //or
    //height(origin) < height(dest)

    public Node getHead() {
	return head.getNode();
    }

    public int dist(TreeNode origin, TreeNode destination) {
	//we assume that both of these are already in the tree
	if(origin == null || destination == null || origin.getID() != destination.getID())
	    return -2; //incompatible

	if(destination.getWidth() > origin.getWidth())
	    return -3; //width-elimination

	if(origin.getHeight() < destination.getHeight())
	    return -4; //height-elimination
	    
	if(origin.getWidth() == destination.getWidth()) {
	    return origin.getHeight() - destination.getHeight();
	}
	
	int original_height = origin.getHeight();

	while(origin.getWidth() > destination.getWidth() && origin.getHeight() > destination.getHeight()) {
	    TreeNode nn = branch.get(origin.getWidth());
	    if(nn == origin)
		nn = nn.getChild();
	    origin = nn;

	    //another way to cut this short: if the difference in width is greater than the difference in height
	    //this can save us significant time in some scenarios
	    /*if(origin.getHeight() - destination.getHeight() < origin.getWidth() - destination.getWidth())
	      return -6;*/

	    if(origin.getOffset() < destination.getOffset())
		return -1;

	    if(origin.getOffset() <= destination.getOffset() && origin.getWidth() != destination.getWidth())
		return -1;
	}

	if(origin.getWidth() == destination.getWidth() && origin.getHeight() >= destination.getHeight())
	    return original_height - destination.getHeight();

	return -5;
    }

}
