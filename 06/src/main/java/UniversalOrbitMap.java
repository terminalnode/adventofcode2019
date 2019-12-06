import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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
                .sum());

        System.out.println("Solution part 2:");
        System.out.println(shortestPath(
            planetIndex.get("YOU"),
            planetIndex.get("SAN"),
            planetIndex.values()) - 2);
    }

    private static int shortestPath(Planet start, Planet stop, Collection<Planet> planets) {
        int stepsTaken = 0;
        Set<Planet> visitedSet = new HashSet<>();
        Set<Planet> currentPlanets = new HashSet<>();
        visitedSet.add(start);
        currentPlanets.add(start);
        
        while (!visitedSet.contains(stop)) {
            Set<Planet> newCurrentPlanets = new HashSet<>();

            currentPlanets
                .stream()
                .forEach(mainPlanet -> mainPlanet.getNearbyPlanets()
                    .stream()
                    .forEach(orbitingPlanet -> {
                        if (!visitedSet.contains(orbitingPlanet)) {
                            visitedSet.add(orbitingPlanet);
                            newCurrentPlanets.add(orbitingPlanet);
                        }
                    })
                );
            stepsTaken++;
            currentPlanets = newCurrentPlanets;
        }

        return stepsTaken;
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
                orbiting.addParentPlanet(orbited);
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
        private final Set<Planet> nearbyPlanets;

        public Planet(String name) {
            this.name = name;
            this.orbitingPlanets = new HashSet<>();
            this.nearbyPlanets = new HashSet<>();
        }

        public void addOrbitingPlanet(Planet planet) {
            orbitingPlanets.add(planet);
            nearbyPlanets.add(planet);
        }

        public void addParentPlanet(Planet planet) {
            nearbyPlanets.add(planet);
        }

        public Set<Planet> getNearbyPlanets() {
            return nearbyPlanets;
        }

        public int getNumberOfOrbitingPlanets() {
            int result = orbitingPlanets.size();
            result += orbitingPlanets
                .stream()
                .mapToInt(x -> x.getNumberOfOrbitingPlanets())
                .sum();
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != this.getClass()) {
                return false;
            }

            Planet otherPlanet = (Planet) obj;
            if (otherPlanet.name.equals(this.name)) {
                return true;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
