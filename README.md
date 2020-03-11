# QuickLinks
Programming contest question - mass evaluation of chains in massive disjointed semi-cyclic data structure.

## Problem Description
You are given the information to build a cyclic graph. This graph may contain up to **1,000,000 entries**. 
Each node in the graph points to **exactly one** node. This may include itself.

You are then given a number of scenarios (up to **1,000,000**). 
Each scenario is composed of an origin node and a destination node. 
The task for each scenario is to determine the distance from the origin node to the destination node. 
The answer of -1 indicates that there is no valid path.

### Insights
1. The graph structure can be composed of **rings** and **chains**
2. Because every node is required to point to another node, there are no terminal points. 
Structures must terminate in **rings**
3. Every chain must have a child **ring**
4. Every ring may have have any number of ancestor chains
5. A node has one destination, and any number of ancestors
6. If two nodes are in seperate rings, they **CANNOT** be related at all
7. If the origin node is in a ring, and the destination node is not, there also can be no relation
8. We can determine ring/chain membership in O(1) time by keeping arrays of pointers to these structures.
9. We can construct all of the rings for our graph in o(n) time

### Pitfalls
There are several specific pitfalls in how problems can be generated:
1. Massive ring structures requirefast indexing. A ring of 1,000,000 entries should be indexable in log(n) time
2. Large chains should be indexable in short times.
