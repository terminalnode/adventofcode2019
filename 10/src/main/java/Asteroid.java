import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Asteroid implements Comparable<Asteroid> {
    private final int y, x;
    private List<Line> distances;

    public Asteroid(int x, int y) {
        this.x = x;
        this.y = y;
        this.distances = null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Line> getDistances() {
        return distances;
    }

    public int getNumberOfVisibleAsteroids() {
        return distances
            .stream()
            .collect(Collectors.toSet())
            .size();
    }

    public Asteroid calculateDistances(Set<Asteroid> allAsteroids) {
        distances = new ArrayList<>();

        for (Asteroid other : allAsteroids) {
            if (other == this) {
                continue;
            }

            Line newLine = new Line(this, other);
            distances.add(newLine);
        }

        return this;
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
            Asteroid other = (Asteroid) obj;
            if (this.x == other.getX() && this.y == other.getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Asteroid other) {
        return this.getNumberOfVisibleAsteroids() - other.getNumberOfVisibleAsteroids();
    }

    @Override
    public String toString() {
        return String.format("Asteroid(%s,%s)", x, y);
    }
}
