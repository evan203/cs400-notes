import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StreamDemo {
  public static void main(String[] args) throws Exception {

    // create a int stream
    Stream.of(3, 6, 4, 7, 6, 5)
        // intermediate operation: let through all ints < 6
        .filter((i) -> i < 6)
        // intermediate operation: multiply each int by 2
        .map((i) -> i * 2)
        // terminal operation: print each int
        .forEach((i) -> System.out.println(i));

    // create a stream of each line in a file
    Files.lines(Paths.get("week14.md"))
        // filter out empty lines
        .filter((line) -> !line.trim().equals(""))
        // make each line upper case
        .map((line) -> line.toUpperCase())
        // print ouch each line
        .forEach((line) -> System.out.println(line));
  }
}
