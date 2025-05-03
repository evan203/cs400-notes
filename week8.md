# CS400 Week 8 Lecture Notes  

## 12 March 2025  

### Graph Minimum Spanning Tree Generating Algorithms Time Complexity  

E = number of edges
V = number of nodes

#### Prim's Algorithm  

`pq.insert(v.outgoingEdges)` takes E\*log(E).  
`while(!pq.isEmpty())` has E iterations.  
`pq.removeMin()` takes log(E)  
`pq.insert(c.endNode ... )` takes log(E)  
So, Prim's algo is E \* log(E) + E \* 2 \* log(E)  
which is **O(E \* log(E))**, or **O(E \* log(V))**.  

#### Kruskal's Algorithm  

`sort edgeList` takes E\*log(E).  
`init nodeSets iwth singleton sets` takes V.  
`while(!edgeList.isEmpty())` has E iterations.  
`if (c.startNode and c.endNode in diff nodeSets)` takes log(V).  
`nodeSets.join(c.startNode, c.endNode)` takes log(V).  
So, Kruskal's Algo takes E \* log(E) + V + E \* 2 \* log(V)  
which is **O(E \* log(V))**

