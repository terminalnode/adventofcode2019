import org.apache.commons.math3.fraction.Fraction;

public class Line implements Comparable<Line> {
    private final Fraction slope;
    private final int xSign;
    private final double distance;
    private final Asteroid destination;

    public Line(Asteroid origin, Asteroid destination) {
        this.destination = destination;
        int xDiff = origin.getX() - destination.getX();
        int yDiff = origin.getY() - destination.getY();

        if (xDiff != 0) {
            slope = new Fraction(yDiff, xDiff);
        } else {
            // Approximate positive / negative infinity
            slope = yDiff > 0 ?
                new Fraction(Integer.MAX_VALUE, 1) :
                new Fraction(Integer.MIN_VALUE, 1);
        }
        distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        xSign = origin.getX() > destination.getX() ? 1 : -1;
    }

    public double getXSign() {
        return xSign;
    }

    public Fraction getSlope() {
        return slope;
    }

    public double getDistance() {
        return distance;
    }

    public Asteroid getDestination() {
        return destination;
    }

    @Override
    public int hashCode() {
        // Apparently bijective algorithm is a good choice
        // for getting a unique hashcode from two numbers
        int slopeHash = slope.hashCode();

        int tmp = xSign + (slopeHash + 1) / 2;
        return slopeHash + tmp * tmp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(this.getClass())) {
            Line other = (Line) obj;
            return other.getSlope().equals(this.slope) && other.getXSign() == this.xSign;
        }
        return false;
    }

    @Override
    public int compareTo(Line other) {
        return Double.compare(this.getDistance(), other.getDistance());
    }
}
