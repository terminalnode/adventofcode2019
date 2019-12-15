import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.ArithmeticUtils;

public class Main {
    public static void main(String[] args) throws IOException {
        partOne("test1", 10);
        partOne("test2", 100);
        partOne("input", 1000);
        System.out.println();
        partTwo("test1");
        partTwo("test2");
        partTwo("input");
    }

    public static void partTwo(String fileName) throws IOException {
        // This solution assumes that the moons will return to their starting state.
        // Not sure if all repeating patterns will do this, but the examples and my input does.
        // So it works for me, and keeping a record of all previous states is *very* expensive.

        List<Moon> moons = getInput(fileName);

        List<Integer> xStartingState = moons.stream()
            .map(moon -> moon.hashX())
            .collect(Collectors.toList());

        List<Integer> yStartingState = moons.stream()
            .map(moon -> moon.hashY())
            .collect(Collectors.toList());

        List<Integer> zStartingState = moons.stream()
            .map(moon -> moon.hashZ())
            .collect(Collectors.toList());

        boolean xSolved = false;
        boolean ySolved = false;
        boolean zSolved = false;

        long xSteps = 0;
        long ySteps = 0;
        long zSteps = 0;

        while (!xSolved || !ySolved || !zSolved) {
            moons.forEach(x -> x.updateVelocity());
            moons.forEach(x -> x.updatePosition());

            if (!xSolved) {
                xSteps++;
                List<Integer> xState = moons.stream()
                    .map(moon -> moon.hashX())
                    .collect(Collectors.toList());
                xSolved = xState.equals(xStartingState);
            }

            if (!ySolved) {
                ySteps++;
                List<Integer> yState = moons.stream()
                    .map(moon -> moon.hashY())
                    .collect(Collectors.toList());
                ySolved = yState.equals(yStartingState);
            }

            if (!zSolved) {
                zSteps++;
                List<Integer> zState = moons.stream()
                    .map(moon -> moon.hashZ())
                    .collect(Collectors.toList());
                zSolved = zStartingState.equals(zState);
            }
        }

        /* Now we need to find the least common multiple of xSteps, ySteps and zSteps.
         * This will work, but it's very slow. Takes about 2 minutes and 45 seconds to solve input.

        long smallestStep = Math.min(xSteps, Math.min(ySteps, zSteps));
        long lcm = smallestStep;
        while (lcm % xSteps != 0 || lcm % ySteps != 0 || lcm % zSteps != 0) {
            lcm += smallestStep;
        }
        */

        // This Apache Commons-method however is very fast.
        long lcm = ArithmeticUtils.lcm(xSteps, ArithmeticUtils.lcm(ySteps, zSteps));
        System.out.printf("Part two (using %s): %s\n", fileName, lcm);
    }

    private static void partOne(String fileName, long stepsToTake) throws IOException {
        List<Moon> moons = getInput(fileName);
        for (long i = 0; i < stepsToTake; i++) {
            moons.forEach(x -> x.updateVelocity());
            moons.forEach(x -> x.updatePosition());
        }

        int totalEnergy = moons
            .stream()
            .mapToInt(x -> x.getTotalEnergy())
            .sum();

        System.out.printf("Part one (using %s): %s\n", fileName, totalEnergy);
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
