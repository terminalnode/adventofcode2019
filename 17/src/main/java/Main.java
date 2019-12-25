import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;

public class Main {
    public static void main(String[] args) throws IOException {
        partOne("input");
    }

    private static void partOne(String fileName) throws IOException {
        long[] application = getInput(fileName);
        VacuumRobot robot = new VacuumRobot(application);
        robot.updateMap();
        int sum = robot.getAlignmentParameters()
            .stream()
            .mapToInt(x -> x[0] * x[1])
            .sum();
        System.out.println("Part one: " + sum);
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
