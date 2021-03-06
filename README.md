# QuickLinks
Programming contest question - mass evaluation of chains in massive disjointed semi-cyclic data structure. A detailed problem description can be found on this page: https://progcontest.aut.ac.nz/index.php/9-results/47-contest-report-2019

## Problem Description
You are given the information to build a cyclic graph. This graph may contain up to **100,000 entries**. 
Each node in the graph points to **exactly one** node. This may include itself.

You are then given a number of scenarios (up to **100,000**). 
Each scenario is composed of an origin node and a destination node. 
The task for each scenario is to determine the distance from the origin node to the destination node. 
The answer of -1 indicates that there is no valid path.

### Usage
cat input | java quicklinks.Quicklinks [options]

The options are as follows:

1. -d debug mode. This enables all debug output and timer output.
2. -t: timer mode. Enables timing, and output sent with the tebug command, but not bebug output'
3. -dt <int>: set the significant figures in the timer
4. -bt: break-tree after pre-processing
5. -intr: ignore N-Tree Reduction
6. -se: show exceptions (if not in debug mode)

### Insights
1. The graph structure can be composed of **rings** and **trees**
2. Because every node is required to point to another node, there are no terminal points. 
Structures must terminate in **rings**
3. Every tree must terminate in a **ring**
4. Each ring may have have any number of attached trees
5. A node has one destination, and any number of ancestors
6. If two nodes are in seperate rings, they **CANNOT** be related at all
7. If two nodes are in separate trees, they **CANNOT** be related at all
7. If the origin node is in a ring, and the destination node is not, there also can be no relation
8. We can determine ring/tree membership in O(1) time by keeping arrays of pointers to these structures.
9. We can construct all of the rings for our graph in o(n) time
10. Constructing trees should be doable in O(n) time

### Tree Manipulation
1. If each node in a tree has knowledge of it's height (counting from the loop), many cases can be done in O(1)
2. If we take stock of the width of a tree at each node, we can also eliminate many cases in O(1) time. "Width" in this case refers to an ordered metric marking the number of times the tree has split
3. No element at width N can join an element at width N + K, so every one of these cases can be eliminated in O(1) time
4. The 'width' is to be taken as an identity element for each seperate split in the tree. Further cases can also be eliminated witht the branch offset
5. Branch offset is stricly "number of links from leftmost"
6. Offset allows for eliminations at O(1) in cases where `offset(target) - offset(dest) > height(target) - height(dest)`
7. Additionally, if `offset(target) == offset(dest) && width(target) != width(dest)`, then the two nodes cannot be related

### Metrics
A file is presented, `metric.sh`, which deals with reading metrics for the program. Each test is performed 5 times, then the scores are averaged.
Additionally, times are read specifically for the interval required to pre-process each input. These values are not reliable for small files (jre overhead, bash overhead, time overhead), but they are useful in evaluating the performance of the program on larger systems. For the hardest problems, ~1 second is spent pre-processing, against 15 seconds post-processing. It appears that the biggest offender is tree-searching. Because of this, I hit upon the idea of using 'N-Branch-Reduction' (completely made up name). The results in the file `metrics2.txt` use n-branch-reduction. It gives good time savings on specific problems (large trees), but provides pre-processing overhead which can (marginally) slow down problems that h ave minimal tree height,width or offsets.

I am evaluating a technique to reduce tree-lookup time by certain numeric factors in some cases, based on an evaluation of tree width, height and offset.

The metrics program also serves as a correctness checker - the output of the script with each execution is checked against the given output, and the number of differences (don't trust the number - I'm just running wc on the diff file) is given with each score. The target is 0 differences.

Scores from execution on my machine (ubuntu, AMD Ryzen 3 @ 4x3.1GHz) are presented in the `Metrics.txt` file. In summary, thorns has the worst execution time, but the scores are roughly linear. Execution time for a 100,000 x 100,000 file is about 12x longer than the execution time for a 10,000 x 10,000 file. This leads me to believe that each lookup is roughly O(N log N). Doing the math (10k log2 10k vs 100k log2 100k), this would seem roughly correct. 

### N-Branch-Reduction
This is a technique to shorten path resolution in tree lookups. The principles are as follows:

1. Every node has knowledge of the lowest node in it's branch
2. Given that we know of the lowest node in a branch, and all nodes know their height, we can always tell if a given node has the POTENTIAL to exist between the current node and the lowest branch in the node
3. If the target node doesn't exist between the current node and the lowest node in the branch, then we can test the next branch down instead
4. This technique can be extended from 1-branch at a time to N-branches at a time if a simple dictionary is build before hand. This adds roughly 1% of the time to post processing time.

That's essentially it. Given any two nodes, and knowledge of their characteristics (width, height, offset), it's possible to figure our if there is a potential for the the target node to exist between them. If there is not, then we can figure out if there is a potential for the target node to exist beyond that range. If both these statements are false, there is no path between the trees.

N-Tree reduction is enabled by default, but it can be turned off by passing the -intr argument into the script

### Examples
A sample file has been constructed specifically for testing most of the possible scenarios. This is the `treeTest.txt` file. A pictorial representation of the data structure is given in the `treeTest.png` file, or below:

![Tree Test](https://raw.githubusercontent.com/NBKelly/QuickLinks/master/treeTest.png)

Nodes in green represent a cycle. Nodes in red represent a tree. Nodes in orange represent branching points within the tree.

The 'width' and 'offset' of each node is given in the table below:

Node | Width | Offset
-----|-------|-------
0|0|0
1|-1|-1
2|-1|-1
3|-1|-1
4|-1|-1
5|-1|-1
6|-1|-1
7|-1|-1
8|0|0
9|0|0
10|0|0
11|0|0
12|0|0
13|1|1
14|2|1
15|2|1
16|3|2
17|4|1
18|4|1
19|5|2
20|6|2
21|6|2
22|7|1
23|7|1
24|7|1
25|7|1

10 scenarios are given in the test file, and they (as well as the expected solutions) are as follows:

origin | destination | distance
-------|-------------|---------
12|8|4
13|8|4
16|11|-1
21|18|-1
21|9|3
25|17|-1
25|8|4
21|20|1
19|18|-1
11|22|-1
