import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Moon {
    private int x, y, z;
    private int vx, vy, vz;
    private List<Moon> moons;

    public Moon(String input, List<Moon> moons) {
        this.moons = moons;

        Pattern getNumbers = Pattern.compile("(-?\\d+)");
        Matcher matcher = getNumbers.matcher(input);
        matcher.find();
        x = Integer.parseInt(matcher.group());
        matcher.find();
        y = Integer.parseInt(matcher.group());
        matcher.find();
        z = Integer.parseInt(matcher.group());
        vx = 0;
        vy = 0;
        vz = 0;
    }

    public void updateXVelocity() {
        for (Moon moon : moons) {
            if (moon.getX() > x) {
                vx++;
            } else if (moon.getX() < x) {
                vx--;
            }
        }
    }

    public void updateYVelocity() {
        for (Moon moon : moons) {
            if (moon.getY() > y) {
                vy++;
            } else if (moon.getY() < y) {
                vy--;
            }
        }
    }

    public void updateZVelocity() {
        for (Moon moon : moons) {
            if (moon.getZ() > z) {
                vz++;
            } else if (moon.getZ() < z) {
                vz--;
            }
        }
    }

    public void updateVelocity() {
        updateXVelocity();
        updateYVelocity();
        updateZVelocity();
    }

    public void updateXPosition() {
        x += vx;
    }

    public void updateYPosition() {
        y += vy;
    }

    public void updateZPosition() {
        z += vz;
    }

    public void updatePosition() {
        updateXPosition();
        updateYPosition();
        updateZPosition();
    }

    public int getTotalEnergy() {
        return getPotentialEnergy() * getKineticEnergy();
    }

    public int getPotentialEnergy() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public int getKineticEnergy() {
        return Math.abs(vx) + Math.abs(vy) + Math.abs(vz);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int hashX() {
        return bijectiveAlgorithm(x, vx);
    }

    public int hashY() {
        return bijectiveAlgorithm(y, vy);
    }

    public int hashZ() {
        return bijectiveAlgorithm(z, vz);
    }

    private int bijectiveAlgorithm(int a, int b) {
        int tmp = a + (b + 1) / 2;
        return b + tmp * tmp;
    }

    @Override
    public String toString() {
        return String.format("pos=<x=%s, y=%s, z=%s>, vel=<x=%s, y=%s, z=%s>", x, y, z, vx, vy, vz);
    }
}
