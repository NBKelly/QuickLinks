package quicklinks;

import java.util.TreeMap;
import java.util.List;

public class Loop {
    //what we want is a set of nodes in the format: node -> index, which can be accessed from just a node
    //given any two nodes, we should be able to figure out the distance between them at O(1)
    TreeMap<Node, Integer> nodes;
    int size = 0;

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

    public int dist(Node s, Node d) {
	int start = nodes.get(s);
	int end = nodes.get(d);
	int dist = end - start;

	if(dist < 0)
	    dist = size - dist;

	return dist;
    }
}
