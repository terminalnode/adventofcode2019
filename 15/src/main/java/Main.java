import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;

public class Main {
    public static void main(String[] args) throws IOException {
        partOne("input");
        partTwo("input");
    }

    private static void partTwo(String fileName) throws IOException {
        Robot robot = new Robot(getInput(fileName));
        System.out.printf("Part two (%s): %s\n", fileName, robot.gasMap());
    }

    private static void partOne(String fileName) throws IOException {
        Robot robot = new Robot(getInput(fileName));
        System.out.printf("Part one (%s): %s\n", fileName, robot.pathLengthFromOriginToOxygen());
    }

    private static long[] getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            return Arrays.stream(
                    new String(is.readAllBytes())
                        .strip()
                        .split(","))
                    .mapToLong(Long::parseLong)
                    .toArray();
        }

        throw new MissingResourceException(
                "Could not find input file",
                Main.class.getName(),
                fileName);
    }
}
