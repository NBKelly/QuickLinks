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
    ArrayList<TreeNode> N_branch = new ArrayList<TreeNode>();
    boolean use_n_branch = false;
    int N = 8;
    int maximum_offset = 0;
    int max_height = 0;
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
    
    public void assemble(Node node, ArrayList<TreeSet<Node>> ancestors, boolean ntr) {
	head = new TreeNode(ID, node, 1, null, this, 0, 0);	
	assemble_inline(head, 1, ancestors, width);
	int factor = 1 + (int)Math.sqrt(Math.min(2*max_height, Math.min(maximum_offset, width)) - 2);
	//System.out.printf("H: %d, MO: %d, MW: %d, F: %d%n", max_height, maximum_offset, width, factor);
	if(ntr)
	    n_cr(factor);
    }

    private void n_cr(int factor) {
	if(factor <= 5) {
	    return;
	}

	use_n_branch = true;
	TreeNode rootBranch = branch.get(0);
	for(TreeNode targetNode : branch) {	    
	    if(targetNode == rootBranch) {
		N_branch.add(null);
		continue;
	    }

	    boolean target_found = false;
	    TreeNode branch_red = targetNode;
	    //what we want to do is get the branch that's FACTOR branches away
	    for(int reduction = 0; reduction < factor; reduction++) {
		branch_red = branch.get(branch_red.getWidth());
		int branch_red_width = branch_red.getWidth();
		TreeNode nc = branch_red.getChild();
		branch_red = nc;
		
		//if it's on width 0, then we can just abort
		if(branch_red.getWidth() == rootBranch.getWidth()) {
		    target_found = true;
		    N_branch.add(branch_red);
		    break;
		}		
	    }
	    if(!target_found)
		N_branch.add(branch_red);
	}
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
	    max_height = Math.max(max_height, height + 1);
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
			if(offset + 1 > maximum_offset)
			    maximum_offset = offset + 1;
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

	if(use_n_branch) {
	    //first we try the n_branch
	    while(origin.getWidth() > destination.getWidth() && origin.getHeight() > destination.getHeight()) {
		TreeNode nn = N_branch.get(origin.getWidth());
		if(nn == null)
		    break;
		if(nn == origin)
		    nn = nn.getChild();
		if(nn == null)
		    break;

		if(nn.getOffset() < destination.getOffset())
		    break;
		else if (nn.getOffset() <= destination.getOffset() && nn.getWidth() != destination.getWidth())
		    break;
		else {
		    origin = nn;
		    continue;
		}
	    }
	}
	
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
