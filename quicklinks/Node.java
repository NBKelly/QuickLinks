package quicklinks;

public class Node {
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
}
