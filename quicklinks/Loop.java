package quicklinks;

import java.util.TreeSet;
import java.util.TreeMap;
import java.util.List;

public class Loop {
    //what we want is a set of nodes in the format: node -> index, which can be accessed from just a node
    //given any two nodes, we should be able to figure out the distance between them at O(1)
    TreeMap<Node, Integer> nodes;

    //given any ancestor, we should be able to figure out WHERE on the ring it attaches
    TreeMap<Node, Integer> ancestors;
    
    int size = 0;
    int ancestorCount = 0;
    
    public Loop(List<Node> nodeList, boolean debug) {
	nodes = new TreeMap<>();
	int index = 0;

	StringBuilder sb = new StringBuilder();

	
	if(debug)
	    System.err.println("Loop starting at node " + nodeList.get(0).getID());
	
	for(Node n : nodeList) {
	    //not sure if we need to start at 0 or 1
	    if(debug)
		System.err.printf("%d (%d) ---> ", n.getID(), index + 1);
	    nodes.put(n, ++index);
		
	}

	if(debug)
	    System.err.println();
	
	size = nodes.size();
	
    }

    public boolean contains(Node n) {
	return nodes.containsKey(n);
    }

    public void associateAncestors(List<TreeSet<Node>> ancestorList, boolean DEBUG) {
	ancestors = new TreeMap<Node, Integer>();

	for(Node n : nodes.keySet()) {
	    //this is the index in the ring
	    int index = nodes.get(n);
	    int id = n.getID();
	    
	    //now we can find all the ancestors of our node
	    TreeSet<Node> idAncestors = ancestorList.get(id);
	    for(Node a : idAncestors) {
		//don't add things already in our loop as ancestors
		if(nodes.containsKey(a))
		    continue;

		ancestors.put(a, index);
		if(DEBUG)
		    System.err.printf("NODE %d ANCESTOR OF LOOP AT INDEX %d%n", a.getID(), index);
	    }
	}
    }
    
    public int dist(Node s, Node d) {
	int start = nodes.get(s);
	int end = nodes.get(d);
	int dist = end - start;

	if(dist < 0)
	    dist = size + dist;

	return dist;
    }
}
