import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.stream.Collectors;

class UniversalOrbitMap {
    public static void main(String[] args) throws IOException {
        List<String[]> data = getInput("input");
        Map<String, Planet> planetIndex = createPlanetIndex(data);

        System.out.println("Solution part 1:");
        System.out.println(
            planetIndex
                .values()
                .stream()
                .mapToInt(x -> x.getNumberOfOrbitingPlanets())
                .sum()
            );
    }

    private static Map<String, Planet> createPlanetIndex(List<String[]> orbitalIndex) {
        Map<String, Planet> planetIndex = new HashMap<>();

        orbitalIndex
            .stream()
            .forEach(x -> {
                if (!planetIndex.containsKey(x[0])) {
                    planetIndex.put(x[0], new Planet(x[0]));
                }

                if (!planetIndex.containsKey(x[1])) {
                    planetIndex.put(x[1], new Planet(x[1]));
                }

                Planet orbited = planetIndex.get(x[0]);
                Planet orbiting = planetIndex.get(x[1]);
                orbited.addOrbitingPlanet(orbiting);
            });

        return planetIndex;
    }

    private static List<String[]> getInput(String fn) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fn);

        if (is != null) {
            String[] input = new String(is.readAllBytes())
                .strip()
                .split("\n");

            return Arrays
                .stream(input)
                .map(x -> x.split("[)]"))
                .collect(Collectors.toList());
        }

        // Well, this isn't going to work
        throw new MissingResourceException(
                "Could not find input file",
                UniversalOrbitMap.class.getName(),
                fn);
    }

    private static class Planet {
        private final String name;
        private final Set<Planet> orbitingPlanets;

        public Planet(String name) {
            this.name = name;
            this.orbitingPlanets = new HashSet<>();
        }

        public void addOrbitingPlanet(Planet planet) {
            orbitingPlanets.add(planet);
        }

        public int getNumberOfOrbitingPlanets() {
            int result = orbitingPlanets.size();
            result += orbitingPlanets
                .stream()
                .mapToInt(x -> x.getNumberOfOrbitingPlanets())
                .sum();
            return result;
        }
    }
}
