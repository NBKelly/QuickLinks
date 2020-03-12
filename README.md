# QuickLinks
Programming contest question - mass evaluation of chains in massive disjointed semi-cyclic data structure.

## Problem Description
You are given the information to build a cyclic graph. This graph may contain up to **100,000 entries**. 
Each node in the graph points to **exactly one** node. This may include itself.

You are then given a number of scenarios (up to **100,000**). 
Each scenario is composed of an origin node and a destination node. 
The task for each scenario is to determine the distance from the origin node to the destination node. 
The answer of -1 indicates that there is no valid path.

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

Scores from execution on my machine (ubuntu, AMD Ryzen 3 @ 4x3.1GHz) are presebted in the `Metrics.txt` file.

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
