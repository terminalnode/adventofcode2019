import java.util.ArrayList;
import java.util.List;

public class Coordinate {
    private final int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate getNorth() {
        return new Coordinate(x, y - 1);
    }

    public Coordinate getSouth() {
        return new Coordinate(x, y + 1);
    }

    public Coordinate getWest() {
        return new Coordinate(x - 1, y);
    }

    public Coordinate getEast() {
        return new Coordinate(x + 1, y);
    }

    public List<Coordinate> getNeighbours() {
        List<Coordinate> result = new ArrayList<>();
        result.add(getNorth());
        result.add(getSouth());
        result.add(getWest());
        result.add(getEast());
        return result;
    }

    public boolean isAdjacent(Coordinate coordinate) {
        int otherX = coordinate.getX();
        int otherY = coordinate.getY();
        boolean nearbyX = otherX == x + 1 || otherX == x - 1;
        boolean nearbyY = otherY == y + 1 || otherY == y - 1;

        if (otherY == y && nearbyX) {
            return true;
        }
        return otherX == x && nearbyY;
    }

    @Override
    public int hashCode() {
        // Good old bijective algorithm thing.
        int tmp = y + (x + 1) / 2;
        return x + tmp * tmp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            Coordinate other = (Coordinate) obj;
            return this.x == other.getX() && this.y == other.getY();
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
