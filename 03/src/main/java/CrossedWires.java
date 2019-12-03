import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

public class CrossedWires {
    public static void main(String[] args) throws IOException {
        List<List<Coordinate>> wires = getInputAsStream();
        List<Coordinate> wireOne = wires.get(0);
        List<Coordinate> wireTwo = wires.get(1);
        List<Coordinate> wireOneIntersectors = wireOne
            .stream()
            .parallel()
            .filter(x -> wireTwo.contains(x))
            .collect(Collectors.toList());
        List<Coordinate> wireTwoIntersectors = wireTwo
            .stream()
            .filter(x -> wireOneIntersectors.contains(x))
            .collect(Collectors.toList());

        // Part one
        partOne(wireOneIntersectors);

        // Part two
        partTwo(wireOneIntersectors, wireTwoIntersectors);
    }

    private static void partTwo(List<Coordinate> wireOne, List<Coordinate> wireTwo) {
        List<Integer> combinedPlacements = new ArrayList<>();
        wireOne.forEach(point -> {
            wireTwo.forEach(x -> {
                if (x.equals(point)) {
                combinedPlacements.add(x.getPlacement() + point.getPlacement());
                }
            });
        });
        Collections.sort(combinedPlacements);
        System.out.print("Solution for part #2: ");
        System.out.println(combinedPlacements.get(0));
    }

    private static void partOne(List<Coordinate> intersections) {
        Collections.sort(intersections);
        System.out.print("Solution for part #1: ");
        System.out.println(intersections.get(0).origoDistance());
    }

    private static List<Coordinate> parseWire(String input) {
        List<Coordinate> wire = new ArrayList<>();
        Coordinate start = new Coordinate(0,0,0);
        wire.add(start);

        Arrays.stream(input.split(","))
            .map(x -> CrossedWires.parseDirections(wire.get(wire.size() - 1), x))
            .forEach(x -> wire.addAll(x));

        wire.remove(0);
        return wire;
    }

    private static List<Coordinate> parseDirections(Coordinate start, String input) {
        List<Coordinate> wirePart = new ArrayList<>();
        char direction = input.charAt(0);
        int steps = Integer.parseInt(input.substring(1));
        Coordinate newCoordinate = start;
        while (steps > 0) {
            steps--;
            newCoordinate = newCoordinate.clone();
            newCoordinate.incrementPlacement();
            switch (direction) {
                case 'U':
                    newCoordinate.incrementY();
                    break;
                case 'D':
                    newCoordinate.decrementY();
                    break;
                case 'R':
                    newCoordinate.incrementX();
                    break;
                case 'L':
                    newCoordinate.decrementX();
            }
            wirePart.add(newCoordinate);
        }
        return wirePart;
    }

    private static List<List<Coordinate>> getInputAsStream() throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("input");
        if (is != null) {
            String[] allWires = new String(is.readAllBytes())
                    .strip()
                    .split("\n");

            return Arrays.stream(allWires)
                .map(CrossedWires::parseWire)
                .collect(Collectors.toList());
        }
        // Well, this isn't going to work
        throw new MissingResourceException(
                "Could not find input file",
                CrossedWires.class.getName(),
                "");
    }

    private static class Coordinate implements Comparable<Coordinate> {
        private int x, y, placement;

        public Coordinate(int x, int y, int placement) {
            this.x = x;
            this.y = y;
            this.placement = placement;
        }

        public Coordinate clone() {
            return new Coordinate(x, y, placement);
        }

        public int getPlacement() {
            return this.placement;
        }

        public void incrementPlacement() {
            this.placement++;
        }

        public void incrementX() {
            this.x++;
        }

        public void incrementY() {
            this.y++;
        }

        public void decrementX() {
            this.x--;
        }

        public void decrementY() {
            this.y--;
        }

        public int origoDistance() {
            return Math.abs(x) + Math.abs(y);
        }

        @Override
        public int hashCode() {
            return (x + y) * placement;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != this.getClass()) {
                return false;
            }

            Coordinate other = (Coordinate) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public String toString() {
            return String.format("Coordinate(%s,%s)", x, y);
        }

        @Override
        public int compareTo(Coordinate other) {
            return origoDistance() - other.origoDistance();
        }
    }
}
