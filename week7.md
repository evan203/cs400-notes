# cs400 lecture notes week 7

Begin: WED 5 Mar 2025.

## More graph terminology

### Weighted graphs

  each edge is assigned a 'weight'

#### Implementation

Using a adjacencyMatrix implementation, we use doubles to store weight instead of just a boolean

```java
double[][] adjacencyMatrix;
```

using a list, we make a list of edge weights

```java
List<Graphnode<T>> adjacencyList;
List<Double> edgeWeights;
```

### Cost of a path

#### Weighted graphs

For weighted graphs: sum of edge weights on a path.
Ex: if weight from A->B is 3 and B->C is 4,
cost(ABC) = 3+4=7

#### Unweighted graphs

For unweighted graphs: length of path (# of edges)

### Connected Graphs

#### connected graph

a path exists between every pair of nodes.

#### Directed graph

Strongly connected: a path exists between every pair of nodes with edge directions respected
Weakly connected: a path exists between every pair of nodes with edge directions ignored.
If, after turning all directed edges into undirected edges, it is connected,
then the original directed graph weakly connected.

### Subgraphs

G' is a subgraph of G if:
The set of nodes of G' is a subset of the odes of G, and
The set of edges of G' is a subset of the edges of G edges.

## Lambda Expression Syntax in Java

`TraversableTree.java`:

```java
public class TraversableTree<T extends Comparable<T>> extends RedBlackTree<T> {
  /**
  * Performs an in-order traversal on the tree by using a recursive
  * helper method.
  */

  // - public void inOrderTraversal() {
  // + 
  public void inOrderTraversal(BSTNodeVisitor<T> visitor)
    // - this.inOrderTraversal(this.root);
    // + 
    this.inOrderTraversal(this.root, visitor);
  }
  /**
  * Recursive helper to perform in-order traversals.
  * @param node the current node we're traversing
  */
  // - protected void inOrderTraversal(BinaryTreeNode<T> node) {
  // + 
  protected void inOrderTraversal(BinaryTreeNode<T> node, 
      BSTNodeVisitor<T> visitor) {
    if (node != null) {
      this.inOrderTraversal(node.childLeft(), visitor);
      // - System.out.println(node.getData());
      // + 
      visitor.visit(node);
      this.inOrderTraversal(node.childRight(), visitor);
    }
  }
  public static void main(String[] args) {
    TraversableTree<Integer> tree = new TraversableTree<>();
    tree.insert(1);
    tree.insert(13);
    tree.insert(22);
    tree.insert(8);
    tree.insert(5);
    // - tree.inOrderTraversal();
    // +
    // tree.inOrderTraversal(new PrintValueVisitor<>());
    // tree.inOrderTraversal(new CountChildrenVisitor<>());

    // we instantiate the interface we want to create an instance of using a
    // lambda exp {}. in between the {} we provide an implementation of the
    // interface.
    tree.inOrderTraversal(new BSTNodeVisitor<>() {
      @Override
      public void visit(BinaryTreeNode<Integer> currentNode) {
        System.out.println(currentNode);
      }
    })
  }
}
```

In the case that the user of TraversableTree<> wants to pass a method into
inOrderTraversal to change the behavior, we have added an argmument
to the method.
We create the type of this argument in a new java file:
We make it a interface and with a generic type

`BSTNodeVisitor.java`:

```java
public interface BSTNodeVisitor<T extends Comparable<T>> {
  /**
   * A method called for every node we visit. 
   * @param currentNode node to visit
  */
  public void visit(BinaryTreeNode<T> currentNode) {
  }
}
```

Now, we've made our traversal more flexible, but we need a new class that
implements the interface.

`PrintValueVisitor.java`:

```java
public class PrintValueVisitor<T extends Comparable<T>> 
implements BSTNodeVisitor<T> {
  @Override
  public void visit(BinaryTreeNode<T> currentNode) {
    System.out.println(currentNode.getData());
  }
}
```

With this class defined, we can use this in main()
Now, we allow users to pass in any implementation of BSTNodeVisitor<>() to
allow for some alternative way to visit the nodes.

For example, we could make a new implementation that prints the data and the
number of children in the node.

`CountChildrenVisitor.java`:

```java
public class CountChildrenVisitor<T extends Comparable<T>> 
implements BSTNodeVisitor<T> {
  @Override
  public void visit(BinaryTreeNode<T> currentNode) {
    int numberChildren = 0;
    if (currentNode.childLeft() != null) numberChildren++;
    if (currentNode.childRight() != null) numberChildren++;
    System.out.println(currentNode.getData() + " has " + numberChildren); 
  }
}
```

Review:
Before, our TraversableTree class had a hard coded visit procedure where it just
printed. Then, we came up with was a interface called BSTNodeVisitor which
has a method visit that takes a BinaryTreeNode argument. Then, we created a
class implementing this interface that uses println to visit it.
In summary, we made our code more flexible. However, the work one has to do to
implement TraversableTree is extensive: they have to implement the
BSTNodeVisitor interface in a new class to use TraversableTree; having to create
so many java files is a pita and creates code that is less readable: in the
place we are calling the inOrderTraversal all we see is the instantiation of the
visitor method.

In the cases where all we want from the class is a single object we can use to
pass into our inOrderTraversal method, java has a shorthand to use instead of
a regular class.

Java calls this an Anonymous class:
we instantiate the interface we want to create an instance of using a lambda
exp {}. in between the {} we provide an implementation of the interface.

```java
    tree.inOrderTraversal(new BSTNodeVisitor<>() {
      @Override
      public void visit(BinaryTreeNode<Integer> currentNode) {
        System.out.println(currentNode);
      }
    });

```

Java knows that BSTNodeVisitor has one method called visit and the type of
arg the method expects. Because we duplicate a lot of information, there is an
additional shorthand we can use for anonymous classes for interfaces that have a
single abstract method.

We provide a list of parameters of the single abstract methods in ().
The single method of the Visitor interface has one argument.
We give the argument the name, but not the type, since java infers that from the
interface definition. Then, we follow the argument list up with an arrow
character sequence ->. Last we have {} which is the body of the visitor method
Java calls these types of expressions lambda expressions
These are only good for interfaces that have a single abstract method. If the
interface has more, java will reject the labmda expression.

```java
    tree.inOrderTraversal( (currentNode) -> {
      System.out.println(currentNode.getData());
    } );

```

The following code produces an error:

```java
int count = 0;
double sum = 0.0;
// this gives an error! count can't be used inside the lambda exp
tree.inOrderTraversal( (currentNode) -> {
  count++; 
  System.out.println(currentNode.getData());
});
```

This is the effective way to do this without an error, using an anonymous class

```java
// this gives an error! count can't be used inside the lambda exp
tree.inOrderTraversal( new BSTNodeVisitor<>() {
  int count = 0;
  double sum = 0.0;
  @Override 
  public void visit(BinaryTreeNode<T> currentNode) {
    count++; 
    System.out.println(currentNode.getData());
  }
});
```

## Here are the types of interfaces and labmda expressions we can use

### Void in, Void out

```java
public interface Runnable {
  public void run();
}

// note: using a single statement does not need {} and ;
// the ; terminates the r1 = (); statement
Runnable r1 = () -> System.out.println("Hello, world!");

// body of run method with two lines of code
Runnable r2 = () -> {
  System.out.print("Hello, ");
  System.out.println("world!");
};
```

### 1 In, Void out

```java
public interface Consumer {
  public void consume(double num);
}

Consumer c1 = (num) -> System.out.println(num);
Consumer c2 = (num) -> {
  double d = num*num;
  System.out.println(d);
};
```

### 1 In, 1 Out

In a single line lambda expression, we do not need to include return in
the code body.

If we must write a body with multiple lines of code, we must include return in
the code body block - inside {}.

```java
public interface Function {
  public double map(double num);
}

Function f1 = (x) -> 5.0*x*x - 2.3*x + 7.0;

Function f2 = (x) -> {
  double z = x*x;
  System.out.println(z);
  return 5.0*z - 2.3x + 7;
};
```

### 2 In, 1 Out

```java
public interface BiFunction {
  public double apply(double x, double y);
}

BiFunction b1 = (x, y) -> x*y;
BiFunction b2 = (x, y) -> {
  double z = x*y;
  return -43.21*z + x*x - y*y + 3*x;
}
```

## 10 March 2025

## Spanning Trees

Let G be an undirected graph with V nodes. If T:
is a subgraph of G,
contains all nodes in G,
is connected, and
has exactly (V-1) edges,
then T is a spanning tree of G.

The spanning tree gives us the minimum number of edges to connect all nodes in a
graph. Every time during a traversal, we only visit every node exactly once:
storing these edges we visit we can create a spanning tree of our graph.

### Forming Spanning trees using DFT and BFT

```pseudocode
DFT(v):
  mark v as visited
  for each unvisited successor u of v:
    // add v-u to spanning tree
    DFT(u)
```

```pseudocode
BFT(v):
  q = new queue()
  mark v as visited 
  q.enqueue(v)
  while (!q.isEmpty()):
    c = q.dequeue()
    for each unvisited successor u of c:
      // add edge c-u to spanning tree 
      mark u as visited
      q.enqueue(u)
```

### Minimum spanning tree

In a weighted graph, spanning trees with the lowest sum of edge widths.
To find, there are two algorithms.

### Prim's Algorithm

For weighted, connected, and undirected graphs.
We start at a starting node, v.
We use a PriorityQueue to push all edges of the graph to a data struct.
The priority is the weight of the edge. This lets us keep track of the min edge

```pseudocode
prim(v):
  pq = new PriorityQueue()
  mark v as visited
  pq.insert(v.outgoingEdges)
  while (!pq.isEmpty()):
    c = pq.removeMin()
    if (c.endNode is unvisited):
      mark c.endNode as visited
      add c to tree
      pq.insert(c.endNode.outgoingEdges to unvisited nodes)
```

Example walk through of Prim's algo.
florian fr just adds shit to the slide tracing through adding/removing from
pq and visiting shit. idk why we need a walk through for some pseudo code

### Kruskal's Algorithm

For weighted, connected, and undirected graphs.

```pseudocode
kruskal(edgeList):
  sort edgeList
  forest = new set of trees
  while (!edgeList.isEmpty()):
    c = edgeList.removeFirst()
    if (c.startNode and c.endNode in different trees):
      forest.joinAddEdge(c.startNode, c.endNode)
```

Then florian goes through a walkthrough.
