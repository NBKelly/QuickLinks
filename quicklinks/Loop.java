package quicklinks;

import java.util.*;

public class Loop {
    //we don't actually need to keep a list of nodes - only a list of ancestors
    private TreeSet<Node> ancestors = new TreeSet<>();

    private int size;
    
    public Loop(int size) {
	this.size = size;
    }

    public int dist(int start, int end) {	
	int dist = end - start;
	if(dist < 0)
	    dist = size + dist;

	return dist;
    }

    public void associateAncestors(TreeSet<Node> idAncestors) {
	ancestors.addAll(idAncestors);
    }

    public Set<Node> getAncestors() {
	return ancestors;
    }

    public int size() {
	return size;
    }
}
