import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        // Should return 31
        partOne("test0");

        // Should return 165
        // partOne("test1");

        // Should return 13312
        // partOne("test2");

        // Should return 180697
        // partOne("test3");

        // Should return 2210736
        // partOne("test4");

        // TBA
        // partOne("input");
    }

    public static void partOne(String fileName) throws IOException {
        List<Reaction> reactions = getInput(fileName);
    }

    private static List<Reaction> getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            return Arrays.stream(
                new String(is.readAllBytes())
                    .strip()
                    .split("\n"))
                .map(x -> new Reaction(x))
                .collect(Collectors.toList());
        }

        throw new MissingResourceException(
                "Could not find input file",
                Main.class.getName(),
                fileName);
    }
}
