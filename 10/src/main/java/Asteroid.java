import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Asteroid {
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
        return new HashSet<>(distances).size();
    }

    public Asteroid calculateDistances(Set<Asteroid> allAsteroids) {
        distances = new ArrayList<>();

        for (Asteroid other : allAsteroids) {
            if (other == this) {
                continue;
            }

            Line newLine = new Line(this, other);
            if (!Double.isNaN(newLine.getSlope())) {
                distances.add(newLine);
            }
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
}
