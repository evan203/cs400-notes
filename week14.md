# CS400 Week 14 Lecture Notes: Streams and Pipes  

## Java Streams  

Process data events.  

### Requirements  

1. Data source operation: specify what data will be put onto the stream; an
   object representing the new stream of type `Stream<T>`.  

2. Chain of intermediate operations: in_stream -> operations -> out_stream.
   Intermediate operations are lazy: they won't process data without a terminal
   element to actively start pushing data through the stream.  

3. One terminal operation: these are eager - they actively pull data through the
   stream.  

### Data Sources Examples  

Most are in the package `java.util.stream`  

Interface `Stream<T>`, has notable static methods:  

- `Stream.generate(Supplier<T> supplier);`

- `Stream.of(T... items)`

- `Stream.concat(Stream stream1, Stream stream2)`

- `Stream.empty()`

Class `java.nio.file.Files` has many static methods that return streams.

### Intermediate Operation Examples  

Interface `Predicate<T>` defines a single method with return type boolean.  

Instance method of `Stream`: `.filter(Predicate<T> )`

If the predicate method returns true, it allows the data item to pass through
the filter, and if false, it does not pass it through the output stream.  

Example:  

Given a stream of integers 1, 2, 3, 4, it goes into intermediate operation
filter. We can use a lambda expression to implement the predicate. In this
example, we want to filter odd numbers (let through evens). That would
be:  `1, 2, 3, 4` -> `.filter( (i) -> i % 2 == 0 )`.  

`Function<T, R>` is a method that takes items of type T and returns a result of
type R to go onto the output stream.  

We can use method of Stream `.map(Function<T, R> f)`  

Example:  

Given a stream of strings: "cs400", "java", we want to output upper case. That would
be: `.map( (str) -> str.toUpperCase() )`. Then, we would get "CS400", "JAVA".  

Other intermediate operation methods are:  

`.limit(int i)`: after i items are let through, the stream will stop.  

`.skip(int i)`: stream removes first i data items, lets remaining through.  

### Terminal Operation Examples  

Similarly, these are instance methods of the Stream type. While intermediate
operations always return a new Stream, terminal operations return data types
representing the final result of the stream. The most useful operations are:  

`.findFirst()`: returns the first T of the incoming `Stream<T>`  

`Consumer<T>` is an interface with a single void method with argument of type
`T` where we can perform an operation to each item in the stream  

`.forEach(Consumer<T> c)`: performs c on each T,  

`.count()`: returns the numeber of elements in the stream  

`.min(Comparator<T> c)` and `max(c)` returns the min/max T in the stream  

## Examples of Stream Usage  

See `StreamDemo.java`. Here's some of it:  

```java
Stream.of(3, 6, 4, 7, 6, 5)
    .filter((i) -> i < 6)
    .map((i) -> i * 2)
    .forEach((i) -> System.out.println(i));
    // output: 6, 8, 10

Files.lines(Paths.get("week14.md"))
    .filter((line) -> !line.trim().equals(""))
    .map((line) -> line.toUpperCase())
    .forEach((line) -> System.out.println(line));
    // output: non-empty lines, all uppercase
```

## Redirection + Pipes in Bash  

Redirecting Input: `program < file` - program gets stdin from file contents

Redirecting Output: `program > file` - stdout from program sent to new file

Appending Redirected Output: `program >> file` - stdout appended to file

Redirecting Standard Error: `program 2> file`

Redirecting Standard Output and Standard Error: `program &> file`

Appending Standard Output and Standard Error: `program &>> file`

Redirect output but still output: `program | tee file`

Append out redirect but still output: `program | tee -a file`

We may also use `/dev/null` as file to discard output.

Pipe stdout of program1 to stdin of program 2: `program1 | program2`

## 2nd Semester Review  

What numbers will the following code print to the terminal?  

```java
Stream.of(10, 7, 4, 2, 5, 11)
    .filter((i) -> i  % 2 == 1)
    .map((i) -> i / 2)
    .forEach((i) -> System.out.print(i + " "))
```

Solution: 3, 2, 5 (7/2=3, 5/2=2, 11/2=5)

We are given a skip list where the lowest lane is 3, 8, 14, 38, 54, 62. Lookup 40.

