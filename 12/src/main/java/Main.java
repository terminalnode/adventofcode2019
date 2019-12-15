import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;

public class Main {
    public static void main(String[] args) throws IOException {
        partOne("test1", 10);
        partOne("test2", 100);
        partOne("input", 1000);
    }

    private static void partOne(String fileName, int stepsToTake) throws IOException {
        List<Moon> moons = getInput(fileName);
        for (int i = 0; i < stepsToTake; i++) {
            timeStep(moons);
        }

        int totalEnergy = moons
            .stream()
            .mapToInt(x -> x.getTotalEnergy())
            .sum();

        System.out.printf("Part one (using %s): %s\n", fileName, totalEnergy);
    }

    private static void timeStep(List<Moon> moons) {
        moons.forEach(x -> x.updateVelocity());
        moons.forEach(x -> x.updatePosition());
    }

    private static List<Moon> getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            List<Moon> moons = new ArrayList<>();

            String[] splitInput = new String(is.readAllBytes())
                .strip()
                .split("\n");

            Arrays.stream(splitInput)
                .forEach(x -> moons.add(new Moon(x, moons)));

            return moons;
        }

        throw new MissingResourceException(
                "Could not find input file",
                Main.class.getName(),
                fileName);
    }
}
