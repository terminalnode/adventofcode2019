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
        Set<Asteroid> asteroids = getInput("input");
        Set<Asteroid> asteroids2 = getInput("test5");
        partOne(asteroids);
        partTwo(asteroids2);
    }

    private static void partTwo(Set<Asteroid> asteroids) throws IOException {
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
