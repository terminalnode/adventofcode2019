import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.OptionalInt;
import java.util.Set;

class MonitoringStation {
    public static void main(String[] args) throws IOException {
        partOne("input");
    }

    private static void partOne(String fileName) throws IOException {
        Set<Asteroid> asteroids = getInput(fileName);
        asteroids.forEach(x -> x.calculateDistances(asteroids));
        OptionalInt mostVisible = asteroids.stream()
            .mapToInt(x -> x.getNumberOfVisibleAsteroids())
            .max();
        System.out.printf("Part one: %s\n", mostVisible.getAsInt());
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
