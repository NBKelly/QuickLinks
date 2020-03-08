import java.util.TreeMap;
import java.util.ArrayList;

public class Loop {
    //what we want is a set of nodes in the format: node -> index, which can be accessed from just a node
    //given any two nodes, we should be able to figure out the distance between them at O(1)
    TreeMap<Node, Integer> nodes;
    int size = 0;
    public Loop(ArrayList<Node> nodeList) {
	nodes = new TreeMap<>();
	int index = 1;
	
	for(Node n : nodeList) {
	    //not sure if we need to start at 0 or 1
	    nodes.put(n, index++);
	}

	size = nodes.size();
	//assuming:
	//a 1
	//b 2
	//c 3
	//d 4
	//e 5
	//
	// dist(a, b) = b - a >= 0 ? b - a : size - (b - a)
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
