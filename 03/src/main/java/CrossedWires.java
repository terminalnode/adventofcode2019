import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

public class CrossedWires {
    public static void main(String[] args) throws IOException {
        partOne();
    }

    private static void partOne() throws IOException {
        List<List<Coordinate>> wires = getInputAsStream();
        List<Integer> intersections = wires.get(0)
            .stream()
            .filter(x -> wires.get(1).contains(x))
            .map(x -> x.origoDistance())
            .collect(Collectors.toList());
        System.out.print("Solution for part #1: ");
        System.out.println(Collections.min(intersections));
    }

    private static List<Coordinate> parseWire(String input) {
        List<Coordinate> wire = new ArrayList<>();
        Coordinate start = new Coordinate(0,0);
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

    private static class Coordinate {
        private int x, y;
        
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate clone() {
            return new Coordinate(x, y);
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
    }
}
