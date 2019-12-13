import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;

class MonitoringStation {
    public static void main(String[] args) throws IOException {
        partOne("input");
    }

    private static void partOne(String fileName) throws IOException {
        Set<Asteroid> asteroids = getInput(fileName);
        List<Integer> inLineOfSight = new ArrayList<>();

        for (Asteroid asteroid : asteroids) {
            inLineOfSight.add(
                getVisibleAsteroids(asteroid, asteroids));
        }

        Collections.sort(inLineOfSight);
        int answer = inLineOfSight.get(inLineOfSight.size() - 1);

        System.out.printf("Part 1: %s\n", answer);
    }

    private static int getVisibleAsteroids(Asteroid origin, Set<Asteroid> others) {
        Set<Line> lines = new HashSet<>();

        for (Asteroid other : others) {
            if (other == origin) {
                continue;
            }

            Line newLine = new Line(origin, other);
            if (!Double.isNaN(newLine.getSlope())) {
                lines.add(newLine);
            }
        }

        return lines.size();
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
