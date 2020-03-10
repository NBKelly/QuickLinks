package quicklinks;

public class Node implements Comparable<Node> {
    int id;
    int target;

    public Node(int id, int target) {
	this.id = id;
	this.target = target;
    }

    public int getID() {
	return id;
    }

    public int getTarget() {
	return target;
    }

    public String toString() {
	return String.format("Node %d -> %d", id, target);
    }

    public String toChain(boolean c) {
	String res = "" + id;

	if(c)
	    res += " -> ";

	return res;
    }

    public int compareTo(Node t) {
	if(t == null)
	    return -99;
	
	return t.id - id;
    }
}
