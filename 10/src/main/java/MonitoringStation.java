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
        Set<Coordinate> asteroids = getInput(fileName);
        List<Integer> inLineOfSight = new ArrayList<>();

        for (Coordinate asteroid : asteroids) {
            inLineOfSight.add(
                getVisibleAsteroids(asteroid, asteroids));
        }

        Collections.sort(inLineOfSight);
        int answer = inLineOfSight.get(inLineOfSight.size() - 1);

        System.out.printf("Part 1: %s\n", answer);
    }

    private static int getVisibleAsteroids(Coordinate origin, Set<Coordinate> others) {
        Set<Line> lines = new HashSet<>();

        for (Coordinate other : others) {
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

    private static Set<Coordinate> getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            String[] rows = new String(is.readAllBytes())
                        .strip()
                        .split("\n");

            Set<Coordinate> asteroids = new HashSet<>();

            for (int y = 0; y < rows.length; y++) {
                for (int x = 0; x < rows[y].length(); x++) {
                    if (rows[y].charAt(x) == '#') {
                        asteroids.add(new Coordinate(x, y));
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

    private static class Line {
        private final double slope;
        private final double xSign;

        public Line(Coordinate origin, Coordinate destination) {
            double xDiff = origin.getX() - destination.getX();
            double yDiff = origin.getY() - destination.getY();
            slope = yDiff / xDiff;

            if (origin.getX() > destination.getX()) {
                xSign = 1;
            } else {
                xSign = -1;
            }
        }

        public double getXSign() {
            return xSign;
        }

        public double getSlope() {
            return slope;
        }

        @Override
        public int hashCode() {
            // Apparently bijective algorithm is a good choice
            // for getting a unique hashcode from two numbers
            int slopeHash = Double.valueOf(slope).hashCode();
            int xSignHash = Double.valueOf(xSign).hashCode();

            int tmp = xSignHash + (slopeHash + 1) / 2;
            return slopeHash + tmp * tmp;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass().equals(this.getClass())) {
                Line other = (Line) obj;
                return other.getSlope() == this.slope && other.getXSign() == this.xSign;
            }
            return false;
        }
    }

    private static class Coordinate {
        private final int y, x;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public int hashCode() {
            // Same bijective algorithm used for Line
            int tmp = y + (x + 1) / 2;
            return x + tmp * tmp;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass().equals(this.getClass())) {
                Coordinate other = (Coordinate) obj;
                if (this.x == other.getX() && this.y == other.getY()) {
                    return true;
                }
            }
            return false;
        }
    }
}
