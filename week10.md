# cs400 week 10 notes

## Java webserver  

The following is a webserver in a directory with index.html, style.css, and
an image.  

File: `WebServer.java`:  

```java
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler; // interface with method handle()
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.nio.file.Files;
import java.io.File;

public class WebServer {
  public static void main(String[] args) throws IOException {
    /* create the HttpServer object using create() with the port number=8000 to
    listen on and length=0 for request buffer (0 represents no limit) */
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

    // set the server to serve from relative directory "/" 
    HttpContext context = server.createContext("/");

    /* set the HttpHandler which calls hanlde() on every request using anon
    class to implement the interface */
    context.setHandler( new HttpHandler() {
      @Override
      public void handle(HttpExchange exchange) throws IOException {
        // get the request file path
        String requestedFilePath = exchange.getRequestURI().getPath();

        System.out.println("Request incoming: " + requestedFilePath);

        // we remove the leading slash using substring
        File inFile = new File(requestedFilePath.substring(1));

        if (requestedFilePath.equals("/graphSearch")) {
          // get query string (everything after the ? in the URI)
          String queryString = exchange.getRequestURI().getQuery();
          String startLoc = null;
          String endLoc = null;
          for (String element : queryString.split("&")) {
            if (element.startsWith("start=")) startLoc =
            element.substring(6); // the string "start=" is 6 chars
            else if (element.startsWith("end=")) endLoc = 
            element.substring(4); // the string "end=" is 4 chars
          }

          // this shows us that our string manipulation works
          System.out.println("got start=" + startLoc + " and end=" + endLoc);

          // since we don't have dijkstra in the scope of the demo, we use a
          // placeholder, but you'd call some backend class
          String placeholderResults = 
              "<h3>Shortest Path (placeholder in server)</h3>" +
              "<ul><li>" + startLoc + "</li>" +
              "<li>Placeholder (java server)</li>" +
              "<li>" + endLoc + "</li></ul>";
          
          // response headers
          exchange.getResponseHeaders().add("Content-type", "text/html");
          exchange.sendResponseHeaders(200, placeholderResults.length());
          // response body output stream
          exchange.getResponseBody().write(placeholderResults.getBytes());
          exchange.getResponseBody().close();
              
        }

        // we make sure the file exists
        else if (inFile.exists()) {

          // handle different types of file extensions, set type appropriately
          String contentType = "text/html";
          if (requestedFilePath.endsWith(".css")) contentType = "text/css";
          if (requestedFilePath.endsWith(".png")) contentType = "image/png";

          exchange.getResponseHeaders().add("Content-type", contentType);

          exchange.sendResponseHeaders(200, inFile.length());

          // read the file and send to output stream
          Files.copy(inFile.toPath(), exchange.getResponseBody());

          // close the output stream
          exchange.getResponseBody().close();

        }
        else {
          // send a response back to the browser (client)
          String response = "<p>Hello Web Server</p>";
          exchange.getResponseHeaders().add("Content-type", "text/html");
          // status code 200 = OK, byte length is the length of chars in response
          exchnge.sendResponseHeaders(200, resonse.length());
          OutputStream respStream = exchange.getResponseBody()
          // this method expects an array of bytes
          respStream.write(response.getBytes());
          respStream.close();
        }

      }
    });

    // make the server start listening (loops and waits for incoming requests)
    server.start();

    // after the start method returns and the webserver has started
    System.out.println("Server started and listening");
  }
}
```

Compile the webserver code using `$ javac WebServer.java`.  

We specified the server will listen on port 8000 in our source code but browsers
use http on port 80.  

Linux doesn't allow non-root users to listen on 80, so we set forwarding to send
incoming requests on 80 to 8000.  

We use `$ sudo iptables -A PREROUTING -t nat -p tcp --dport 80 -j REDIRECT
--to-ports 8000`  

## Linear Sorting  

Comparison sorts: compare items in the sequence input to each other. For
example: bubble=O(n^2), heap&merge=O(nlogn)  

Stable sort: maintains original order of equal items in sequence. This is
helpful for when we want to sort multiple times with respect to different
attributes of the data we are sorting (ex: by price then alphabetically).  

Unstable sort: doesn't maintain relative order.  

### Counting sort  

Takes into account the range R of symbols into consideration.  

For an input sequence of single decimal digits, R={0, 1, ... , 9}.  

Ex: input sequence (8, 8, 9, 0, 1, 3, 9, 0, 3, 5, 3).  

Consider an array of indexes 0 to 9. We step through the input sequence and
count the frequency of each index in the sequence.  

Ex: index:counts=(0:2, 1:1, 2:0, 3:3, 4:0, 5:1, 6:0, 7:0, 8:2, 9:2).  

Then we consider an array of indexes 0 to 9 and compute the end position for
each symbol in the input array.  

Generically, this computation is as follows:  

endpos at index 0 = count of 0 - 1,  

endpos at index i for i > 0 = endpos at index i-1 + count of i.  

Computing this for the example, there are 2 0s so the endpos of 0 element is
2-1 = 1, there is 1 1 element so endpos is 1 + 1 = 2. For element 2, there are 0
so it equals 2, and so on.

Ex: index:endpos=(0:1, 1:2, 2:2, 3:5, 4:5, 5:6, 6:6, 7:6, 8:8, 9:10).  

Then we compute the output by going back to front over the input sequence. This
keeps our sort stable.  

Computing this for the example, we start with 3. Lookup the endpos of 3, which
is 5, so we set output at index 5 to 3. Then we decrement the endpos of 3 to 4
in the endpos array. Then we move on to the next data item in the input: 5. Go
into endpos array, lookup position for 5: 6. We put 5 at index 6 of output, then
decrement endpos at 5. Next item is 3. Lookup endpos at 3: 4. Put 3 into output
at index 4. Decrement endpos at 3. Lookup 0 at endpos: it is 1. So, put 0 into
position 1 of output, and decrement endpos at 0. Next item: 9. Lookup 9 at
endpos: it is 10, so put a 9 in output at index 10, and decrement endpos of 9.
Then, next element of input sequence is 3. Endpos at 3 is 3, so put a 3 at
output index 3 and decrement endpos at 3. Next element is 1: endpos at 1 is 2,
so put at 1 at index 2 of output and decrement endpos at 1. Next element is 0:
endpos at 0 is 0, so put a 0 at output index 0 and decrement endpos 0. Next
element is 9: endpos at 9 is 9, so put a 9 at output index 9 and decrement
endpos at 9. Next element is 8: endpos at 8 is 8, so put a 8 at output index 8
and decrement endpos at 8. Last element is 8: endpos is 8 so put an 8 at output
index 8 then decrement endpos at 8. Now, our output sequence is the sorted input
sequence. Output=(0, 0, 1, 3, 3, 3, 5, 8, 8, 9, 9). Here, our sort is stable.  

If the range is big, like 3 digit integers, but only a sequence of 9 integers,
this would require arrays too large. So, we take our data items and break them
down into single positions. So we apply one counting sort for the first digit,
then the second digit, then the third digit. That is, we apply our counting sort
algorithm for each symbol position in our input data elements. That algorithm is
the Radix Sort.

### Radix sort  

Input=(432, 534, 311, 119, 650, 903, 121, 777).  

Start by only looking at the rightmost digit. Then we sort the rightmost digit
using counting sort. So we will create a counts array from 0 to 9. Then we count
the frequency of each digit (in just the rightmost position) and store this into
our counts array. Counts_3=(1, 2, 1, 1, 1, 0, 0, 1, 0, 1).  

Then we compute the endpos array for the third digit.
Endpos_3=(0, 2, 3, 4, 5, 5, 5, 6, 6, 7).  

Then we put together our sorted output sequence for the third digit, inserting
the full element into the output array.
Output=(650, 311, 121, 432, 903, 534, 777, 119).  

Now, we've completed iteration 1 of counting sort as a part of our radix sort,
effectively sorting elements of the input sequence by their least significant
digit (3rd digit).  

For iteration 2, we move on to the next position to the left of the input
elements, or the second least significant digit. We perform a counting sort on
these, and get:
counts=(1, 2, 1, 2, 0, 1, 0, 1, 0, 0)
endpos=(-1, 0, 2, 3, 5, 5, 6, 6, 7, 7)
output=(903, 311, 119, 121, ).  

We then use this as the input sequence for our third iteration of counting sort,
sorting on the first digit, which now is the most significant digit. We get:
output=(119, 121, 311, 432, 534, 650, 777, 903).  

#### Complexity  

Let n = size of input data = number of elements in input.  

Let R = range of symbols.  

Let Q = length of data items.  

Computing the counts array is n computations. Computing the endpos array takes R
computations. Computing the output sequecne takes n computations. Thus, the
complexity of counting sort is 2n + R.  

Counting sort: O(n)  

In a radix sort, we compute counting sort for Q iterations

Radix sort: O(Qn))

## HTML Document to provide a start/end location for graph shortest search  

File `graph.html`:  

```html
<!DOCTYPE html>
<html>
  <head>
    <title>Shortest Path Search</title>
    <meta name="author" content="Florian H">

    <script src="script.js"> </script>
  </head>

  <body>
    <h1>Shortest Path Search</h1>

    <label>Start location:</label>
    <input id="start" type="text" placeholder="Start location" />

    <br />

    <label>End location:</label>
    <input id="end" type="text" placeholder="End location" />

    <br />

    <input type="button" value="Search" onclick="buttonClicked()">
    <div id="result"></div>
  </body>
</html>
```

file: `script.js`:  

```js
function buttonClicked() {
  console.log("button was clicked");

  // js doesn't have types
  // querySelectorAll takes a css selector string argument and returns an
  // array containing elements matching the selector
  // we access the 0th index of the arr since there is only one unique
  // input element with id=start, then we access the input value.
  let start = documnet.querySelectorAll("#start")[0].value;
  let end = documnet.querySelectorAll("#end")[0].value;
  
  // fetch takes a string with an address, returns Promise object
  // we have to worry about strings/special chars in start, end, so we
  // encode them with encodeURIComponent()
  fetch("http://host/graphStart?start=" + 
    encodeURIComponent(start + "&end=" + end)
  )
    // the Promise object represents the future results
    // we tell it what to do once it gets the results with then()
    // which takes a function as a parameter; the function has a param
    // response
    .then( (response) =>  response.text() )
    // response.text() is another promise object
    .then( (htmlFragment) => {
      // puts the contents of result variable into the inner html of the 
      // div with id=result
      document.querySelectorAll("#result")[0].innerHTML = htmlFragment;
    });
}
```
