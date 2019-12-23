import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        // Part one
        partOne("test0", 31L);
        partOne("test1", 165L);
        partOne("test2", 13312L);
        partOne("test3", 180697L);
        partOne("test4", 2210736L);
        partOne("input", 387001L);

        // Part two
        System.out.println();
        partTwo("test2", 13312L);
        partTwo("test3", 180697L);
        partTwo("test4", 2210736L);
        partTwo("input", 387001L);
    }

    public static void partTwo(String fileName, long expected) throws IOException {
        Map<String, Reaction> reactions = getInput(fileName);
        ReactionManager rm = new ReactionManager(reactions);

        long result = 0L;
        String correctResponse = result == expected ? "Correct!" : "Wrong!";
        System.out.printf("Part two (using %s): %s (%s)\n", fileName, result, correctResponse);
    }

    public static void partOne(String fileName, long expected) throws IOException {
        Map<String, Reaction> reactions = getInput(fileName);
        ReactionManager rm = new ReactionManager(reactions);
        rm.produce("FUEL", 1);

        long result = rm.getOreConsumed();
        String correctResponse = result == expected ? "Correct!" : "Wrong!";
        System.out.printf("Part one (using %s): %s (%s)\n", fileName, result, correctResponse);
    }

    private static Map<String, Reaction> getInput(String fileName) throws IOException {
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
                .collect(
                    Collectors.toMap(x -> x.getEndProduct().getName(), x -> x));
        }

        throw new MissingResourceException(
                "Could not find input file",
                Main.class.getName(),
                fileName);
    }
}
