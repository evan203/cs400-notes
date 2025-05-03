# CS400 Lecture Notes - Week 13 - 23 to 30 April 2025 - Skip Lists + Regex  

## Skip Lists  

Sorted arrays:  

- can use binary search
- insertion/deletion is O(n)

Linked lists:  

- must use linear search (inefficient)
- insertion/deletion is O(1)

Skip lists aim to get the best features of both of these.  

We introduce "express lanes" into linked list. Define lanes as:

- L0: the base linked list
- L1: skip every other node in L0
- L2: skip every other node in L1
- ...
- L(log_2(n)): first node is linked to last node

### Lookup  

We start with the last lane L3. Compare the value in the first node of L3 to the
lookup value. We skip ahead in L3 if value > L3_0. Then, we see that this last
node is greater than value, so we step down into lane L2 and say in the first
node.  

We now peek ahead in L2 to the value of the next node. Comparing this to value,
this next node is smaller than value, so we step ahead on the current lane and
take our new position in this next node. Then we peek ahead to the next node in
L2, which is larger than what we're looking for, so we go down to L1.  

We now peek ahead in L1, which is smaller than what we're looking for, so we
advance to the next node in L1. Peeking ahead, this is greater than what we're
looking for, so we traverse down to L0.

We peek ahead, and we've found our value in the next node in L0.  

### Insertion  

TODO take notes

### Deletion  

TODO take notes

## Regular Expressions

Florian takes us to [regex101.com](https://regex101.com)

We copy/paste the canvas assignment page into the text box.

Ex: A\d\d\.\w+
Searches all assignment names such that we start with 'A' followed by 2 digits
('\d') followed by a '.' then any word character between 1 and unlimited times
('w+')  

Ex: \[AQ\]
Matches a single character present in the list AQ

### Java  

File `RegexDemo.java`:  

```java
import java.util.regex.Pattern;
import java.util.regex.Matcher;

String text1 = "firstname.lastname@cs.wisc.edu";

// we want a regex to match valid emails to verify the string as valid/invalid

// we need to escape \ chars when using String in java
// i.e.: regex "\w" becomes java string "\\w"
String regex = "[\\w\\.]+@[\\w\\.]+\\.\\w{2,3}";

Pattern emailPattern = Pattern.compile(regex);
Matcher emailMatcher = emailPattern.matcher(text1);
// will print true/false if the string matches. 
System.out.println("is valid email: " + emailMatcher.matches());

// using a shorthand way to use .matches()
System.out.println("is valid email: " + Pattern.matches(regex, text1));


// multi-line string pattern matching
String text2 = """
    "
    [PAQ]\d{2,3}\.[\w-]+
    "
    gm
    Match a single character present in the list below [PAQ]
    PAQ
    matches a single character in the list PAQ (case sensitive)
    \d
    matches a digit (equivalent to [0-9])
    {2,3} matches the previous token between 2 and 3 times, as many times as 
    possible, giving back as needed (greedy)
    \. matches the character . 
    Match a single character present in the list below [\w-]
    + matches the previous token between one and unlimited times, as many times 
    as possible, giving back as needed (greedy)
    \w matches any word character 
    - matches the character -
    """;
Matcher text2Matcher = emailPattern.matcher(text2);
while (text2Matcher.find()) {
  String email = text2Matcher.group();
  int start = text2Matcher.start();
  int end = text2Matcher.end();
  System.out.println(email + " at position " + start + " to " + end);
}
```

### Bash  

Say we have some multi-line text file `string.txt` which we want to search for
email addresses within.

```bash
# set regex type to perl: -P
grep -P '[\w\.]+@[\w\.]+\.\w{2,3}' string.txt
# only display matches: -o
grep -Po '[\w\.]+@[\w\.]+\.\w{2,3}' string.txt
```
