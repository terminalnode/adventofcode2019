import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.stream.Stream;

/* Day 1: The Tyranny of the Rocket Equation
 * ** Part 1 **
 * For the given input:
 *  - divide each entry by 3 and round down
 *  - subtract 2
 *  - sum the results
 *
 * ** Part 2 **
 * - repeat the above calculation for the added fuel as well
 * - any mass requiring a negative amount of fuel should be 0
 */

public class RocketEquation {
  public static void main(String[] args) throws IOException {
    System.out.println(String.format("Part 1: %s", partOne(getInputAsStream())));
    System.out.println(String.format("Part 2: %s", partTwo(getInputAsStream())));
  }

  private static int partTwo(Stream<String> input) {
    return input
        .mapToInt(Integer::parseInt)
        .map(RocketEquation::partTwoMap)
        .sum();
  }

  private static int partTwoMap(int mass) {
    int requiredFuel = 0;
    while (mass != 0) {
      mass = Math.floorDiv(mass, 3) - 2;
      if (mass < 0) {
        mass = 0;
      }
      requiredFuel += mass;
    }
    return requiredFuel;
  }

  private static int partOne(Stream<String> input) {
    return input
        .mapToInt(Integer::parseInt)
        .map(x -> Math.floorDiv(x, 3) - 2)
        .sum();
  }

  private static Stream<String> getInputAsStream() throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("input");
    if (is != null) {
      String[] allLines = new String(is.readAllBytes()).strip().split("\n");
      return Arrays.stream(allLines);
    }
    // Well, this isn't going to work, but it's something.
    throw new MissingResourceException(
        "Could not find input file",
        RocketEquation.class.getName(),
        "");
  }
}
