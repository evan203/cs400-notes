# CS400 Lecture Notes - Week 9  

## HTML  

Florian creates an html document with `<!DOCTYPE html>`,
`<html>`, `<head>`, `<title>`, `<meta>`, `<body>`.  

Inside `<meta>` we set an attribute `name="author"` and
`content="Florian H"`. These tags are for search engines to determine
the contents of the html document.  

Inside the `<body>` we create a `<h1>` element with a header for the page.  

Then, we create a `<p>` element and write some paragraph text.  

Next, we create an `<img src="logos.png" width="300"/>` element.  

Next, an unordered list `<ul>` element, with list elements `<li>`. We can also
use `<ol>` to an create ordered list.  

To create links, we make child elements of `<li>` that are
`<a href="https://foo.bar">link to foobar</a>`.  

We can create input elements `<input type="">`, with type `button`, `text`,
`file`, and `checkbox`.  

We can create comments with `<!-- This is a comment -->`.  

To change style of an element, we use `style="color: red"` for example in the
attributes of a given text element, to change the text color to red.  

Some more style attributes are font-family, font-weight, background-color,
border-style, border-color, border-width.  

## CSS  

Adding these styles to individual elements is not reusable and readable. So, we
use CSS.  

We can create a `<style>` element in `<head>` with style attributes inside curly
brackets.  

Then, we can use selectors  before the curly braces with styles
to apply these styles to elements that match the selectors.  

One type of selector is by element type (h1, p, h2, etc).  

Another is by element id, which has to be defined in the element. So, if we
have some list item we want to give an id: `<li id="xyz"> ...`, and then in the
`<style>`, we can use `#xyz {color: blue}`.  

Another way to do this is create a `class="abc"` attribute, which is selected in
CSS with a dot `.` before the class we want to select, instead of a `#`.  

To put css into a file and then use it in an html document, we add a
`<link rel="myStyle.css">` element inside `<head>`.  

## CSL Web Server  

We copy the files on a local machine with
`scp scamehorn@best-linux.cs.wisc.edu:~/public/html`.  

Then, index.html is served at <https://pages.cs.wisc.edu/~scamehorn/>.  

Webserver in java will be covered on Friday.

## Shortest paths - Dijkstra's Algorithm  

- Finds the shortest (lowest-cost) path in a graph from start node to all other
  nodes.  
- Is the fastest, single start, shortest path algorithm for directed and
undirected graphs with unbounded, non-negative edge weights.  

### Pseudocode  

```python
# v: starting node
dijkstra(v):
  pq = new PriorityQueue()

  # paths are represented as an array of:
  # destination node of path, predecessor node of path, and cost of path

  # init with the same start and destination node
  pq.insert([dest:v, pred:null, cost:0])

  # main loop, iterating through paths in pq
  while (!pq.isEmpty()):

    # pull off path with lowest cost
    [dest, pred, cost] = pq.removeMin()

    # dest is visited ==> there exisits a lower cost path to get to dest
    # so, only consider paths with unvisited destinations
    if dest is unvisited:
      mark dest as visited
      store pred and cost of dest

      # look for possible extensions of this shortest path
      for each edge with weight w to unvisited nieghbor u of dest:
        pq.insert([u, dest, cost+w])
```

### Example algorithm walkthrough

Start at node A: dijkstra(A).  
We init the priority queue.  
Push the first path onto pq. So, `pq={[A, null, 0]}`.  

Start the main loop.  
Pull off the first path: `[A, null, 0]`. pq becomes empty.  
Dest A is unvisited, so we've found the shortest path from A to A.  
So, visit A. Then, we store the pred=null and cost=0.  
Next, we look at the outgoing edges: B, C, and E; all three are currently
unvisited.  
So, we insert the 3 new paths and get
`pq={[B, A, 4], [C, A, 2], [E, A, 15]}`.  

We start a new iteration of our loop.  
We pull off the lowest cost path: `[C, A, 2]`.  
Now, `pq={[B, A, 4], [E, A, 15]}`.  
C is unvisited, so this path to C is the shortest path from A to C.  
We mark C as visited and store pred=A and cost=2.  
Now we look at all unvisited neighbors of C: only D.  
So, we insert the new path and we get
`pq={[B, A, 4], [E, A, 15], [D, C, 7]}`.  

We start a new iteration of the while loop.  
We pull off the lowest cost path: `[B, A, 4]`.  
Now, `pq={[E, A, 15], [D, C, 7]}`.  
We check the status of the destination node B: it is currently unvisited.  
So, we mark B as visited and store pred=A and dest=4.  
We look at B's outgoing unvisited edges: D and E.  
We insert these two paths, and we get
`pq={[E, A, 15], [D, C, 7], [D, B, 5], [E, B, 14]}`.  

We start a new while loop iteration.  
Popping next minimum element is `[D, B, 5]` and we get
`pq={[E, A, 15], [D, C, 7], [E, B, 14]}`.  
Since D is unvisited, we mark D as visited and store pred=B and cost=5.  
Now we look at possible extensions of this path.  
D has 2 outgoing unvisited edges, so we add these to the pq and get
`pq={[E, A, 15], [D, C, 7], [E, B, 14], [E, D, 8], [F, D, 5]}`.  

We start a new while loop iteration.  
Our next minimum element `[F, D, 5]` gets popped, and
`pq={[E, A, 15], [D, C, 7], [E, B, 14], [E, D, 8]}`.  
Because F is unvisited, we set F to visited, store pred=D and cost=5.  
Looking for possible extensions, there's 1 outgoing unvisited edge.  
Adding this to pq, we get
`pq={[E, A, 15], [D, C, 7], [E, B, 14], [E, D, 8], [H, F, 9]}`.  

We start a new while loop iteration.  
We pop min element `[D, C, 7]` and
`pq={[E, A, 15] [E, B, 14], [E, D, 8], [H, F, 9]}`.  
D is visited, so this is NOT the shortest path to node D.
Thus, we move on to the next iteration of the while loop.  

We pop min element `[E, D, 8]` and we get
`pq={[E, A, 15] [E, B, 14], [H, F, 9]}`.  
Since E is unvisited, we visit E, store pred=D, and cost=8.  
Looking at outgoing visited edges, there are none, so we do nothing.  

Onto the next iteration of the while loop.  
We pop next min element: `[H, F, 9]` and get
`pq={[E, A, 15] [E, B, 14]}`.  
H is unvisited, so we visit H, store pred=F, and cost=9.  
Looking at outgoing visited edges, there are none, so we do nothing.  

We start a new while loop iteration.  
We pop the next min element: `[E, B, 14]`.  
We've alr visited node E and discovered a better lower cost path, so we do
nothing.  

We start another while loop iteration.  
We pop `[E, A, 15]`, and now the pq is empty.  
E is alr visisted, so we move on.  

Now, the pq is empty, so we terminate.  
We are left with stored data of all the shortest edges.  

#### Reconstructing paths  

Path A to E: Looking at E, cost=8, path: start with E and work backwards. Our
stored pred of E is D, so on the shortest path from A to E we must go through D.
Now we do this same lookup recursively. D's stored pred is B. Then, B's stored
pred is A. So, the path is ABDE, with a total cost of 8.  

## Graphs in files  

we are going to make a directed graph
newfile: `campus.dot`

```plaintext
digraph campus {
  "Comp Sci" -> "Union South";
  "Comp Sci" -> "Discovery";
}
```

Here, comp sci is a node, then the edge is the arrow, then the outgoing node is
union south.
So, we save these two edges to the `campus.dot` file.

To create a svg visualization of the graph:
`$ dot -Tsvg campus.dot -O`
Now, we have `campus.dot.svg`, which we can open and view.  

To add additional edge attributes/values, we can use [] after edges

```plaintext
digraph campus {
  "Comp Sci" -> "Union South" [seconds=15, label="15 sec"];
  "Comp Sci" -> "Discovery" [seconds=10, label="15 sec"];
}
```

To visualize this in our svg, we have to add label="myLabelHere"

So, this is the graph format we're going to use for P2.
For reading the code in the graph file, we only are expected to read this format
with the edges on a separate line.
We could read in each line line by line, check for the arrow symbol to determine
if the line is an edge, and then we could parse the start and end nodes to
insert into our graph data structure in java.

## Dijkstra's algorithm continued  

We ended last lecture with an execution of the algorithm, with node A as the
starting node for the graph.

| Vertex | Visited | pred | cost |
| --------- | ---------- | ---------- | ---------- |
| A | T | null | 0 |
| B | T | A | 4 |
| C | T | A | 2 |
| D | T | B | 5 |
| E | T | D | 8 |
| F | T | D | 5 |
| G | F |  |  |
| H | T | F | 9 |

### Reconstructing paths continued  

- Path A to E:  
  - Path cost: 8 (comes from cost(E))
  - Path D -> E: Before we get to node D, we go to B
  - Path B -> D: pred = a
  - Path A -> B: now, we've assm our total path
  - Full path: A B D E.
- Path A to F:
  - Path cost: cost(F) = 5
  - Full path: A B D F
- Path A to G:
  - Path cost: cost(G) = undefined, since G isn't visited
  - No path exists from A to G

### Runtime complexity  

V: num nodes  
E: num edges

Max number of paths in pq: E  
Adding/removing path from pq: log(E)  

So, algo runtime complexity is `O(E * 2log(E))`  
Substituting logE for `log(V^2)`,
= `O(E* log(E))`.  

A C D B
