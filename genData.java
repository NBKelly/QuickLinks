import java.util.*;
    
public class genData {

    public static void main(String[] args) {
	int num_nodes = 12;
	int num_scenarios = 4;
	Random rand = new Random();
	System.out.printf("%d %d%n", num_nodes, num_scenarios);
	for(int i = 0; i < num_nodes; i++) {
	    System.out.println(rand.nextInt(num_nodes));
	}

	for(int i = 0; i < num_scenarios; i++) {
	    System.out.printf("%d %d%n", rand.nextInt(num_nodes) , rand.nextInt(num_nodes));
	}
    }
}
