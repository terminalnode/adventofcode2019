public class Line {
    private final double slope;
    private final double xSign;

    public Line(Asteroid origin, Asteroid destination) {
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
