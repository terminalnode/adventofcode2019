import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.fraction.Fraction;

class MonitoringStation {
    public static void main(String[] args) throws IOException {
        Set<Asteroid> asteroids = getInput("input");
        Set<Asteroid> asteroids2 = getInput("input");
        partOne(asteroids);
        partTwo(asteroids2);
    }

    private static void partTwo(Set<Asteroid> asteroids) throws IOException {
        Asteroid bestAsteroid = getBestAsteroid(asteroids);
        Map<Fraction, Queue<Line>> rightSide = filterLinesByXSign(bestAsteroid.getDistances(), 1);
        Map<Fraction, Queue<Line>> leftSide = filterLinesByXSign(bestAsteroid.getDistances(), -1);

        List<Fraction> rightSideSlopes = rightSide
            .keySet()
            .stream()
            .collect(Collectors.toList());
        List<Fraction> leftSideSlopes = leftSide
            .keySet()
            .stream()
            .collect(Collectors.toList());
        Collections.sort(rightSideSlopes);
        Collections.sort(leftSideSlopes);

        int vaporised = 0;
        Asteroid lastVaporised = null;
        while (vaporised < 200) {
            for (Fraction f : rightSideSlopes) {
                Line nextLine = rightSide.get(f).poll();
                if (nextLine != null) {
                    vaporised++;
                    lastVaporised = nextLine.getDestination();
                }
                if (vaporised == 200) {
                    break;
                }
            }

            if (vaporised == 200) {
                break;
            }

            for (Fraction f : leftSideSlopes) {
                Line nextLine = leftSide.get(f).poll();
                if (nextLine != null) {
                    vaporised++;
                    lastVaporised = nextLine.getDestination();
                }
                if (vaporised == 200) {
                    break;
                }
            }
        }

        System.out.println("Part two: " + (lastVaporised.getX() * 100 + lastVaporised.getY()));
    }

    private static Map<Fraction, Queue<Line>> filterLinesByXSign(List<Line> lines, int xSign) {
        Map<Fraction, Queue<Line>> result = new HashMap<>();

        List<Line> filteredLines = lines.stream()
            .filter(x -> x.getXSign() == xSign)
            .collect(Collectors.toList());

        filteredLines
            .stream()
            .forEach(x -> {
                Fraction slope = x.getSlope();
                if (!result.containsKey(slope)) {
                    result.put(slope, new PriorityQueue<>());
                }
                Queue<Line> slopeLines = result.get(slope);
                slopeLines.add(x);
            });

        return result;
    }

    private static void partOne(Set<Asteroid> asteroids) throws IOException {
        Asteroid bestAsteroid = getBestAsteroid(asteroids);
        System.out.printf("Part one: %s\n", bestAsteroid.getNumberOfVisibleAsteroids());
    }

    private static Asteroid getBestAsteroid(Set<Asteroid> asteroids) {
        asteroids.forEach(x -> x.calculateDistances(asteroids));
        List<Asteroid> asteroidList = new ArrayList<>(asteroids);
        Collections.sort(asteroidList);
        return asteroidList.get(asteroidList.size() - 1);
    }

    private static Set<Asteroid> getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            String[] rows = new String(is.readAllBytes())
                        .strip()
                        .split("\n");

            Set<Asteroid> asteroids = new HashSet<>();

            for (int y = 0; y < rows.length; y++) {
                for (int x = 0; x < rows[y].length(); x++) {
                    if (rows[y].charAt(x) == '#') {
                        asteroids.add(new Asteroid(x, y));
                    }
                }
            }
            return asteroids;
        }

        throw new MissingResourceException(
                "Could not find input file",
                MonitoringStation.class.getName(),
                fileName);
    }
}
