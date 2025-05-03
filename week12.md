# cs400 Lecture Notes - Week 12: Tries; More JavaFX GUI  

## Tries  

Imagine we have a large set of strings which we want to insert into a data
structure that allows us to do those insertions, lookups, and removals
efficiently.  

Tries are data structures that represent our set as a tree.  

Start: root node (rectangle). We then insert a children arr with one potential
child per character we expect our string to contain. We assume the language of
the strings is the lowercase alphabet a to z. The references to those children
are stored in an arr are initialized with null values.  

In java, we have a restriction for the index of arr: Integers. So, we need some
kind of mapping from our characters in the alphabet to integers. We will use the
ASCII value and int subtraction to map a to 0 and z to 25.  

Nodes of a try also include a boolean called "final".  

We adding another node, a child of the root node, at index c. With those two
nodes, we can spell out the string c; we go char by char in the string.  

If we start at the root node and not immediately transition down into any child,
we have the empty string.  

If we want a string to be a part of our set, we flip the final boolean to true.
This allows us to spell out strings that are actually a part of our set. So, for
the string c, we set the child node of the root final=true.  

When representing the tree visually, we may use + and - chars to represent the
final bit. We also put a char next to each edge between nodes to indicate which
char we are adding.  

### Insertion  

Insert: life, lime, leg; into empty try.  

- Create root node  
- Create l subtree from root  
- Create i subtree from root->l  
- Create f subtree from root->l->i  
- Create e subtree from root->l->i->e, set final=true.  

### Deletion  

We start with a lookup for the string we plan to delete. We step down nodes
character by character. We then set final to false. Then, recursively, if the
current node has no children references in the array, we can remove the node
entirely, then traverse into the parent. The base case of the recursive case is
if there are other children references in the current node or if the node is
marked final (excluding the initial node we removed).  

### Complexities  

Let n be the number of strings in the trie.  

Let l be the length of the string.  

Lookup: O(l): l+1 steps  

Insertion: O(l): l+1 steps  

Deletion: O(l): 2(l+1) steps  

Note that n does not determine time complexity.  

## JavaFX Layout managers  

We've seen in our initial javafx program that one way of placing our gui
elements (button, label, triangle, poly, etc) is by assigning them fixed
coordinates. That works, but it makes our app contents inflexible to window
resizing and different resolution screens. To keep the positioning more
flexible, we can use layout managers.  

Layout managers allow us to place gui components inside of them and handle the
positioning of components themselves with different strategies. We will look at
four layout managers.  

### BorderPane  

[BorderPane Javadoc](https://openjfx.io/javadoc/11/javafx.graphics/javafx/scene/layout/BorderPane.html)

Takes up a rectangular area and divides it into 5 subareas:  

- Top: the top strip of the window
- Bottom: the bottom strip of the window
- Left: left strip of the window
- Right: right strip of the window
- Center: center area surrounded by top, right, bottom, and left.

### HBox and VBox

[HBox Javadoc](https://openjfx.io/javadoc/23/javafx.graphics/javafx/scene/layout/HBox.html)

HBox lays out its children in a single horizontal row. If the hbox has a border and/or padding set, then the contents will be laid out within those insets.

HBox example:

```java
HBox hbox = new HBox(8); // spacing = 8
hbox.getChildren().addAll(new Label("Name:), new TextBox());
```

VBox is the same but vertical  

### GridPlane  

[GridPlane Javadoc](https://openjfx.io/javadoc/23/javafx.graphics/javafx/scene/layout/GridPane.html)

GridPane lays out its children within a flexible grid of rows and columns. If a border and/or padding is set, then its content will be laid out within those insets.

### Scene Graph  

- Start with a root node, of type Scene.  
- The child node of this Scene node is a BorderPane node, which takes up the
entire space of the scene  
- Then, the borderpane subdivides the entire application window into the 5
spaces  
- We then set the children of the BorderPane node. This could be the menu bar,
text area, label, VBox, etc.  

## Event

- A java object representing a user interaction with the GUI
- Contains data about the interaction
- Subtype of javafx.event.Event - ex:
  - MouseEvent
  - KeyEvent
  - ActionEvent
- Created by the specific element the user interacts with (called the target)
- Javafx notifies the target, which then is sent to the parent node recursively
unil we reach the Scene at root.
- We may choose to stop the event from moving up to the root of the scene graph
by consuming it

## Event Handlers  

- Objects with a method containing code to react to an event
- Implement interface javafx.event.EventHandler. That is,

```java
public interface EventHandler<T extends Event> {
  public void hande(T event);
}
```

- Can be registered with a node in the scene graph
- We could use regular class, anonymous class, or lambda expression to
implement.  

### Registration  

- Scene graph nodes have method:  

```java
.addEventHandler(EventType<T> eventType,
    EventHandler<? super T> eventHandler)
```

- For example:

```java
.addEventHandler(KeyEvent.KEY_TYPED, EventHandler<KeyEvent> handler)
.addEventHandler(MouseEvent.MOUSE_CLICKED, EventHandler<MouseEvent> handler)
.addEventHandler(ActionEvent.ACTION, EventHandler<ActionEvent> handler)
```
