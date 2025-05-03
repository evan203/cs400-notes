# CS 400 Week 11 Lecture notes - Hash Tables, GUI (JavaFX)  

## Maps  

An efficient way to insert, lookup, and delete values.  

Compared to an array or linked list, we have instant insert(no resize in array),
lookup (no linear search), and delete (no data shift in array)

Structure of data: maps a key to a value. Ex: ID -> Person.  

If we have keys of a comparable type, we could use a search tree to store one
key, value pair based on keys, giving a treemap lookup of O(logn).  

## Hashing  

We use a function, instead of a tree to map. Then, f(key) = index of value in
memory. Calling this function is constant time complexity, instead of logn

## Hash Functions  

We call elements in the domain keys, and elements in the codomain are indexes in
memory, where the value of the index is the mapped value of the key.  

We want the memory location to be a valid hash index. In Java, this means the
index has to be an integer i such that 0 <= i < array length.  

### Ideal Hashing  

Assume we want to store 100 student records, using an array of size 100 and
student IDs as keys.  

Let student IDs be 11 000, 11 001, ..., 11 099.  

Then, the hash function would be:  

```java
int hash(int key) {
  return key - 11000;
}
```

Then, our hash map table operations would be:  

```java
class HashDemo<K, V> {
  private V[] arr;

  void insert(K key, V value) {
    arr[hash(key)] = value;
  }

  V lookup(K key) {
    return arr[hash(key)];
  }

  void remove (K key) {
    arr[hash(key)] = null;
  }
}
```

The caveat here is that student IDs are not perfect, like from 11 000 to 11 099.  

### Real World Hashing Example  

UW Student IDs are 10 digits: 9021453190, ..., 9033879101, ...

Our max java array length is Integer.MAX ~= 2e9. Hence, we can't create an array
to map every possible 10 digit number ID to a student value.  

If we used just the last 3 digits of IDs, then we would end up with collisions.
Then, we would end up with multiple keys being mapped to the same index.  

One important part of making hashing efficient is having a good hash function
that doesn't have collisions.  

### Hash functions should  

1. be deterministic: for a given input value, it must always generate the same
hash value

2. achieve uniform distribution across output range

3. minimize collisions

4. be fast and easy to compute

### Java API for Hash Functions  

We aren't going to reinvent the wheel; Java has these functions already.  

`int hashCode()` method has implementations for built-in data
types such as String, Double, etc. It can also be overridden for custom
classes.  

To use, we call `hashCode()` on a key object and convert result in the range of
integer to a valid hash index using abs and modulo.  

```java
// compute a valid hash index of someKey within the range of arr
int index = Math.abs(someKey.hashCode()) % arr.length;
```

## Hash Tables  

- hash table: arr that contains the pairs (key, value)  
- table size: capacity of hash table (arr length)  
- load factor (LF) = `num_key_value_pairs / table_size`

### Table Size Collision Experiments  

Florian ran an experiment 10 times per each table size in 10, 100, 1000, 10000.  

In each experiment, he generated 100 random integer keys and counted how many
collusions occur on average per each table size.  

| table size | load factor | num collisions |
| --------------- | --------------- | --------------- |
| 10 000 | 0.01 |  0 or 1 |
| 1 000 | 0.1 | 3 to 7 |
| 100 | 1 | 37 to 47 |
| 10 | 10 | 90 |

So, even oversized hash tables still have collisions. Plus, the sweet spot for
the load factor is between 0.1 and 1 for this hash method. Literature shows
optimal load factor is between .7 and .8.  

To stay within this optimal load factor, we need to resize as needed.  

When the table is "full" (above some load factor threshold that the user can
designate), we resize. A good default is within .7 to .8  

### Hash Table Resize Operation  

We start with a hashtable with a hash function defined as f: key -> index, with
f(key) = key % table_size.  

This table starts with size=5 and 3 inserted elements. Since our LF threshold is
lower than our current LF, we want to resize.  

We take the old arr and double it in length. Now, we need to make sure that our
pre-existing k,v pairs are re-inserted. We copy them over into the same index
they were in the old table. We run into the problem that our table is part of
the hash function, so now the keys don't map to their values anymore.  

So, we rehash: step through the old table and run each key through our hash
function with the updated table_size, copying each key and associated value into
the index of the new, larger array.  

Now, we can lookup the old keys in the new arr.  

Resizing: O(1)  

Rehashing: O(n)  

In amortized terms, over many inserts, on average resizing is O(1).  

It's a little disenhartening that we have to do this linear time resizing and
rehashing operation, but what saves us it that after we zoom out and resize our
hash table, we know we can do the next insertions very quickly, until the new
larger hashtable has reached the new LF threshold again. This makes the time
spent rehashing over the cheap inserts making our insert on average O(1).  

## Collision Handling  

There is two different families of handling we can use, allowing us to store a
key value where the hash wants us to store to the same hash that another key is
at.

### Open Addressing  

This is a family of strategies that assumes that at every element in the hash
table, we are storing at most one key-value pair. If there is a collision, where
the key maps to an index that alr contains a different key and its associated
value, we just look for the next "open address", or the next index in our table
that is open, where we can insert the given key-value pair into. What the next
available address depends on the type of open addressing.

#### Linear Probing  

This is a type of open addressing where we go to our hash function and get a
hash index for our key, H_k. If that index is open, then no issue, but if it is
not, we literally add +1 to our current hash index and check that. We add +1
until we find an index that is open.  

Ex:  

We start with a hash table of length 11 with 6 key value pairs inserted. We
only have the keys shown as elements into the table. In the example, we do an
additional insertion using open addressing. The first one is key 166. We run
that key through our hash function, (key % (table_length)), which gives us H_166
= 1, where our table is open, there is no key-value pair stored there, so we
just put that item into index 1 into our table.  

Next, we start with key 359, H_359 = 7. Index 7 in the table already has a
different key with its associated value at that index, so we now go to the next
index, 8. Checking index 8, it is open, so we insert there.  

The last example is key 263. H_263 = 10, which is the last index in the table
and is not open. Taking a step to the right is index 10+1 = 11, which is out of
bounds of our table. What open addressing does is warp around the beginning
using another modulo operation. This keeps adding until we get to index 5 which
is open.

#### Lookup in Open Addressing  

We want to lookup key 263 in the table. We take the key and compute using the
hash function. We get index 10, which is opccupied. Then we check for the key we
are looking for in that spot, which is a different one than what we're looking
for. That tells us that the key we are looking for is further down this probing
sequence. Then, we check index 0, 1, 2, 3, and finally 4, where our key is
finally located.  

When we lookup a key that we check all index sequences and end up at a null
element in the array, then we can simply throw an exception or return null,
since it follows that the key-value pair is not present in the hashtable.  

#### Deletion in Open Addressing  

When we lookup the key-value using open addressing, we don't set it to null, we
set it to a deletion marker, such as some special java object stored in a field
of our class. That way, linear probing will still continue when it arrives at a
deletion marker so that collisions that are not directly at H_k but instead at
some (H_k + i) % length where i is some linear sequential collision handled
index.

Generally, in practice linear probing works very well in hashtables. It's
weakness is hash functions that don't distribute our hash values evenly across
the array: i.e. the hash function is more likely to hash keys to a certain range
(such as the front, beginning, etc). That makes hashing worse, as we create
clusters in our hash table in regions that our hash function favors.  

To not run into this problem, there exists another open addressing strategy to
get around this problem.  

#### Quadratic Probing  

Instead of always adding +1 and looking next door, we instead add polynomials of
order 2 of our step number along the probing sequence to the original H_k. That
is, H_k + Ai + B(i)^2. Typically, the weights are set to A=0, B=1. Then, i is
the step number. So, i iterates starting at 0 and increasing by 1 until we find
our associated key-value pair or null. This gradually increases the size of the
step that we take away from the original index H_k.  

For example, looking at the same hash table we looked at before.  

We need to set an upper threshold to take a maximum number of steps we can take
before we give up to not be able to insert our key value pair. This would be the
array length -1. Because of this, quadratic probing is problematic.  

#### Double Hashing  

This uses a second hash function for the step size, s. That is, s = hash2(key),
where hash(key) is the original hash function to map a key to a value. Then, our
probing sequence is: H_k, H_k + 1s, H_k + 2s, ...  

This works well, but it has the issue that it relies on a second, independent
hash function. So, we have another option.  

### Chaining Collision Handing  

This family stores a chain to every key-hash value that can hold multiple
key,value pairs. These chains are linked lists that store all key-value pairs
for a given hash of keys where the keys collide to the same hash.  

### Complexities for Open Addressing  

Let N = number of key-value pairs to be stored in the hash table  

| | worst case | avg case | best case |
| ----------- | ---------- | -------- | --------- |
| insert (put) | O(N) | O(1) | O(1) |
| lookup (get) | O(N) | O(1)| O(1) |
| remvoe | O(N) | O(1) | O(1) |

Even though we will never get away from linear worst case time complexity,
like the avg case time complexity of linear data structures, we can git constant
average time complexity using hash tables.  

### Tophat Question  

Insert the following keys: 18, 4, 50, 3 in this order into an initially empty
hash table of size 5 with indices 0...4 and a load factor threshold of 1.
Use the following hash function: hash(key) = key % table_size.  

Manage any collision by using linear probing. What is the index that the final
key 3 will be inserted into?  

Calculating,
hash(18) = 3
hash(4) = 4
hash(50) = 0
hash (3) = 3.  

We see a collision at index 3. So, we add and get to index 4, which is also
taken, so we add and get to index 0, which is also taken, so finally, we add and
insert into index 1.  

## JavaFX  

Florian shows how to VPN and then remote desktop; we are just going to install
java-openjfx on arch.

Compile command:  

```bash
javac \
  --module-path /usr/lib/jvm/java-24-openjfx/lib/ \
  --add-modules javafx.graphics \
  JavaFXApp.java
```

See example file `JavaFXApp.java`
