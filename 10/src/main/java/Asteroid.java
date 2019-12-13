public class Asteroid {
    private final int y, x;

    public Asteroid(int x, int y) {
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
            Asteroid other = (Asteroid) obj;
            if (this.x == other.getX() && this.y == other.getY()) {
                return true;
            }
        }
        return false;
    }
}
