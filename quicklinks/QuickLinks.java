package quicklinks;

import java.math.BigInteger;
import java.lang.StringBuilder;
import java.util.*;
import quicklinks.Loop;
    
public class QuickLinks {
    //If you are lost, your code probably begins on line 77
    static Scanner sc = new Scanner(System.in);
    static Scanner ln;
    static int line = 0;
    static int token = 0;
    
    //is the program running in debug mode
    //things like timer, DEBUG(), etc will only work this way
    private static boolean DEBUG = false;
    private static boolean TIMER = false;
    static boolean clean_exit = true;
    //number of significant places to use when running the timer
    //timer runs in nanoseconds, units = xxx.000000000 s, where
    //l(x..x) = magnitude
    private static int DEBUG_TIME_MAGNITUDE = 3;
    private static boolean IGNORE_UNCLEAN = true;
    private static Exception exception = null;
    
    // Input:
    //   * NOTE: checking must be explicit (existence is assumed)
    //   hasNextInt()        -> boolean, input contains another int
    //   hasNextBigInteger() -> boolean, same as above but bigint
    //   hasNextLine()       -> boolean, same as above but String (full line)
    //   nextInt()           -> integer, gets next int from stream
    //   nextBigInteger()    -> bigint,  gets next bigint from stream
    //   nextLine()          -> String,  gets all text from stream up to next line break
    //  
    
    // To print:
    //   Errors: ERR(x)      -> uses stderr, only enabled with -se
    //   Line:   println(x)
    //   Block:  print(x)    -> inserts space after integer argument
    //   Debug:  DEBUG(x)    -> uses stderr, only enabled with -d
    //   Timer:  TEBUG(x)    -> uses stderr, enabled with -d/-t
    //
    // These take input of arbitrary object, or integer
    // V(object) = object.toString();

    // Timer:
    // * start()             -> gets time, returns timer too
    //   split()             -> split(null)
    // * split(Str)          -> prints split time, and also reason
    // * total(Str, bool)    -> prints total time, reason, bool = reset
    //   total()             -> total(null, false)
    //   total(Str)          -> total(Str, false)
    //   total(bool)         -> total(null, bool)
    //   reset()             -> resets timer

    // GCD, LCM:
    //   Single pairs, or lists of numbers are supported
    public static void solveProblem() throws Exception {
	/**
	 *  Problem Description:
	 *   There are two numbers to start. A (number of nodes) and B (number of scenarios)
	 *   The next A lines consist of a single number (destination node). The target node is taken to be the line number.
	 *   The next B lines consist of two nodes, a target and a destination. For each of these lines, we wish to output the length of the path from the target to the destination node,
	 *    or -1 if there is no path
	 */

	Timer t = new Timer().start();
	
	int num_nodes = nextInt();
	int num_scenarios = nextInt();

	DEBUG(num_nodes + " nodes to read");
	DEBUG(num_scenarios + " scenarios to process");

	t.split("BUILD LIST OF NODES");

	/** Simple: start with a dictionary giving node -> destination
	 *
	 *  An overview of the node class:
	 *  NODE:
	 *    Node(ID, TARGET) -> Node
	 *    getID()          -> int
	 *    getTarget()      -> int
	 */
	Node[] nodes = new Node[num_nodes];
	for(int i = 0; i < num_nodes; i++) {
	    nodes[i] = new Node(i, nextInt());
	    DEBUG(nodes[i].toString());
	}

	DEBUGLINE();
	
	//inverted node list
	ArrayList<TreeSet<Node>> ancestors = new ArrayList<TreeSet<Node>>(num_nodes);
	for(int i = 0; i < num_nodes; i++)
	    ancestors.add(null);
	
	for(int i = 0; i < num_nodes; i++) {
	    Node c = nodes[i];
	    int target = c.getTarget();
	    if(ancestors.get(target) == null)
		ancestors.set(target, new TreeSet<Node>());
	    ancestors.get(target).add(c);
	}

	if(DEBUG) {
	    for(int i = 0; i < num_nodes; i++) {
		DEBUGF("Ancestors of %d: ", i);
		if(ancestors.get(i) != null) {
		    for(Node n : ancestors.get(i))
			DEBUGF("%d ", n.getID());		    
		}
		DEBUG();
	    }
	}

	DEBUGLINE();
	t.split("PROCESS NODES");

	//here is an algorithm:
	//  there are three things that need to be done:
	//  * we need to identify loops
	//  * we need to identify paths leading to loops

	boolean[] visited = new boolean[num_nodes];
	boolean[] looped = new boolean[num_nodes];
	
	for(int index = 0; index < num_nodes; index++) {
	    //given any node, we can follow it through until we build a loop
	    //because every node has a destination, and every destination is valid,
	    //every node will always terminate in a loop
	    ArrayList<Node> chain = new ArrayList<Node>();
	    int position = index;
	    
	    while(!visited[position]) {
		visited[position] = true;
		chain.add(nodes[position]);
		position = nodes[position].getTarget();
	    }

	    if(chain.size() > 0) {
		chain.add(nodes[position]);

		//now we want to generate a loop, and an ancestor chain
		//once all loops are assembled, we can get to work on building ancestor chains for
		//all non-looped components
		int iDex = chain.indexOf(chain.get(chain.size() - 1));
		DEBUG("CHAIN SIZE: " + chain.size() + ", CHAIN START: " + iDex);

		
		List<Node> loop = chain.subList(iDex + 1, chain.size());
		if(loop.size() > 0) {    
		    if(DEBUG) {
			StringBuilder chainStr = new StringBuilder();
			for(Node n : loop)
			    chainStr.append(n.toChain(true));
			
			DEBUG("CHAIN: " + chainStr.toString());
		    }
		    
		    DEBUG("Making loop");
		    Loop myLoop = new Loop(loop, DEBUG);
		    DEBUG("Loop Made");
		}
	    }
	}

	
	t.split("PROCESS SCENARIOS");
	
	t.total("Finished processing of file. ");
	/**
	 *  Timer t = new Timer().start();
	 *  
	 *  while(hasNextInt()) {
	 *      t.split("Started scenario at " + line);
	 *
	 *      //your logic here
	 *  }
	 *  
	 *  t.total("Finished processing of file. 
	 */
    }



    
    /* ^^^^ YOUR WORK
     *
     * LIB IMPLEMENTATIONS BELOW HERE
     * 
     * vvvv NOT YOUR WORk
     */



    
    public static void main(String[] argv) {
	for(int i = 0; i < argv.length; i++) {
	    switch(argv[i]) {
	    case "-se" : IGNORE_UNCLEAN = false; break;
	    case "-d"  : DEBUG = true; IGNORE_UNCLEAN = false;
	    case "-t"  : TIMER = true; break;
	    case "-dt" :
		Scanner tst = null;
		if(i + 1 < argv.length &&
		   (tst = new Scanner(argv[i+1])).hasNextInt()) {
		    DEBUG_TIME_MAGNITUDE = tst.nextInt();
		    i++;
		    break;
		}
		
	    default :
		System.err.
		println("Usage: -se       = (show exceptions),\n" +
			"       -d        = debug mode,\n" +
			"       -t        = timer mode (debug lite),\n" +
			"       -dt <int> = set timer digits");
	    return;
	    }
	}
	try {
	    solveProblem();
	} catch (Exception e) {
	    clean_exit = false;
	    exception = e;
	}
	finally {
	    DEBUG();

	    if(clean_exit) {
		if(!hasNextLine())
		    DEBUG("Program terminated cleanly at line '" + line +
			  "', token '" + token + "' due to no input");
		else {
		    StringBuilder remlines = new StringBuilder();
		    int ct = 0;
		    while(hasNextLine()) {
			if(ct < 5)
			    remlines.append(nextLine() + "\n");
			ct++;
		    }

		    DEBUG("Program terminated cleanly at line '" + line +
			  "', token '" + token + "' with '" + ct + "' lines of input remaining");
		    if(ct <= 5)
			DEBUG(remlines.toString());
		}
	    }
	    else
		DEBUG("Program terminated at line '" + line +"'");
	    
	    DEBUG("Clean exit: " + clean_exit);
	    if(!clean_exit && !IGNORE_UNCLEAN)
		exception.printStackTrace();
	}
    }
    
    public static int GCD(List<Integer> li) {
	if(li.size() < 1)
	    return 1;

	int st = li.get(0);

	for(int i = 1; i < li.size(); i++) {
	    if(st == 1)
		return 1;
	    st = GCD(st, li.get(i));	    
	}
	
	return st;
    }

    public static int GCD(int[] li) {
	if(li.length < 1)
	    return 1;

	int st = li[0];

	for(int i = 1; i < li.length; i++) {
	    if(st == 1)
		return 1;
	    st = GCD(st, li[i]);	    
	}
	
	return st;
    }
    
    public static int GCD(int a, int b) {
	if (a <= 0)
	    return b;

	while (b > 0) {
	    if (a > b)
		a = a - b;
	    else
		b = b - a;
	}

	return a;
    }

    public static int LCM(List<Integer> li) {
	if(li.size() < 1)
	    return 1;

	int st = li.get(0);

	for(int i = 1; i < li.size(); i++) {
	    int val = li.get(i);
	    st = (st * val) / GCD(st, val);
	}
	
	return st;
    }

    public static int LCM(int[] li) {
	if(li.length < 1)
	    return 1;

	int st = li[0];

	for(int i = 1; i < li.length; i++) {
	    int val = li[i];
	    st = (st * val) / GCD(st, val);
	}
	
	return st;
    }

    
    //LCM = (a * b) / GCD(a b)
    public static int LCM(int a, int b) {
	long m = a * (long)b;
	return (int) (m / GCD(a, b));
    }
    //Now here is some GCD stuff
    
    // NOW HERE IS SOME TIMER STUFF
    private static class Timer {
	//we cheat with this timer, and when we split, we account
	//for the time taken in the split
	//the timer only does anything when debug is enabled
	private long startTime;
	private long splitTime;
	
	public Timer start() {
	    if(DEBUG || TIMER) {
		this.startTime = this.splitTime = System.nanoTime();
		TEBUG("Event - Timer Started");
	    }
	    return this;
	}

	public void split() {
	    split(null);
	}
	
	public void split(String val) {
	    if(DEBUG || TIMER) {
		val = (val == null ? "" : (" - event: " + val));
		
		long split = System.nanoTime() - splitTime;

		if(split < 0)
		    split *= -1;
		StringBuilder splitStr = new StringBuilder("" + split);

		while(splitStr.length() < 9 + DEBUG_TIME_MAGNITUDE)
		    splitStr.insert(0, '0');
		    //splitStr = "0" + splitStr;
		
		//get the location of the decimal point
		splitStr.insert(splitStr.length() - 9, '.');
		
		//splitStr = splitStr.
		TEBUG("Split time: " + splitStr + val);

		//account for the time spent in output
		splitTime = System.nanoTime() - split;
	    }
	}

	public void total(String val, boolean reset) {
	    if(DEBUG || TIMER) {
		val = (val == null ? "" : (" - event: " + val));
		long split = System.nanoTime() - startTime;

		if(split < 0)
		    split *= -1;
		
		StringBuilder splitStr = new StringBuilder("" + split);

		while(splitStr.length() < 9 + DEBUG_TIME_MAGNITUDE)
		    splitStr.insert(0, '0');
		    //splitStr = "0" + splitStr;
		
		//get the location of the decimal point
		splitStr.insert(splitStr.length() - 9, '.');

		//splitStr = splitStr.
		TEBUG("Total time: " + splitStr.toString() + val);
		if(reset) reset();
	    }
	}

	public void total(boolean val) {
	    total(null, val);
	}
	
	public void total(String val) {
	    total(val, false);
	}

	public void total() {
	    total(null, false);
	}

	public void reset() {
	    if(DEBUG || TIMER) {
		DEBUG("Event - Timer Reset");
		startTime = splitTime = System.nanoTime();
	    }
	}
    }
    
    private static String nextLine() {
	if(!hasNextLine())
	    DEBUG("No such element [string] at line " + line
		  + " token " + token);
	token++;
	return ln.nextLine();
    }
    
    private static boolean hasNextLine() {
	if(ln == null || !ln.hasNextLine()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	if(ln.hasNextLine())
	    return true;
	
	boolean res = false;

	while(!ln.hasNextLine()) {
	    //if the current line has no integer
	    //then we scan through until we find
	    //another valid line, or there are
	    //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));
	    
	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }

    
    // HERE IS SOME CONVENIENCE JUNK TO GET THE NEXT BIGINT ALWAYS
    private static BigInteger nextBigInt() {
	if(!hasNextBigInteger())
	    DEBUG("No such element [bigint] at line " + line
		  + " token " + token);
	token++;
	return ln.nextBigInteger();
    }
    
    private static boolean hasNextBigInteger() {
	if(ln == null || !ln.hasNextBigInteger()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	if(ln.hasNextBigInteger())
	    return true;
	
	boolean res = false;

	while(!ln.hasNextBigInteger()) {
	    //if the current line has no integer
	    //then we scan through until we find
	    //another valid line, or there are
	    //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));
	    
	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }
    
    // HERE IS SOME CONVENIENCE JUNK TO GET THE NEXT INTEGER ALWAYS
    private static int nextInt() {
	if(!hasNextInt())
	    DEBUG("No such element [int] at line " + line
		  + " token " + token);
	token++;
	return ln.nextInt();
    }
    
    private static boolean hasNextInt() {
	if(ln == null || !ln.hasNextInt()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	if(ln.hasNextInt())
	    return true;
	
	boolean res = false;

	while(!ln.hasNextInt()) { //if the current line has no integer
	                          //then we scan through until we find
	                          //another valid line, or there are
	                          //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));

	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }

    //if the line is empty, or is a comment, return false
    private static boolean checkNextLine() {
	String l = sc.nextLine();
	line++;
	token = 0;
	boolean res = !(l.length() == 0 || l.charAt(0) == '#');
	if(res)
	    ln = new Scanner(l);
	return res;
    }

    // NOW HERE IS HOW WE PRINT
    public static String padr(int output, int len) {
	String s = "" + output;

	while(s.length() < len)
	    s += " ";

	return s;
    }

    public static void print(int output) {
	System.out.print(output + " ");
    }
    
    public static void print(Object output) {
	System.out.print(output);
    }
    
    public static void println(int output) {
	System.out.println(output);
    }
    
    public static void println(Object output) {
	System.out.println(output);
    }

    private static void ERR() {
	if(DEBUG || !IGNORE_UNCLEAN)
	    System.err.println();
    }

    public static void ERR(Object output) {
	if(DEBUG || !IGNORE_UNCLEAN)
	    System.err.println(output.toString());
    }

    public static void ERR(int output) {
	if(DEBUG || !IGNORE_UNCLEAN)
	    System.err.println(output);
    }
    
    private static void DEBUG() {
	if(DEBUG) System.err.println();
    }

    private static void DEBUGF(String arg, Object... args) {
	if(DEBUG) System.err.printf(arg, args);
    }

    private static void DEBUGLINE() {
	DEBUG();
	DEBUG("--------------------------------------");
	DEBUG();
    }
    
    public static void TEBUG(Object output) {
	if(TIMER || DEBUG) System.err.println(output.toString());
    }
    
    public static void DEBUG(Object output) {
	if(DEBUG) System.err.println(output.toString());
    }

    public static void DEBUG(int output) {
	if(DEBUG) System.err.println(output);
    }
}
