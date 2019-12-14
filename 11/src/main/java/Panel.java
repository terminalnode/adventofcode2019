public class Panel {
    private final int x, y;
    private long color;

    public Panel(int x, int y) {
        this.x = x;
        this.y = y;

        // Hull starts out black
        this.color = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getColor() {
        return color;
    }

    public boolean setColor(long color) {
        if (color == this.color) {
            return false;
        }
        this.color = color;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            Panel other = (Panel) obj;
            return this.x == other.getX() && this.y == other.getY();
        }
        return false;
    }

    @Override
    public int hashCode() {
        // Bijective algorithm supposedly good for hashcodes based on two ints.
        int tmp = y + (x + 1) / 2;
        return x + tmp * tmp;
    }
}
