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

    public void updateVelocity() {
        for (Moon moon : moons) {
            if (moon.getX() > x) {
                vx++;
            } else if (moon.getX() < x) {
                vx--;
            }

            if (moon.getY() > y) {
                vy++;
            } else if (moon.getY() < y) {
                vy--;
            }

            if (moon.getZ() > z) {
                vz++;
            } else if (moon.getZ() < z) {
                vz--;
            }
        }
    }

    public void updatePosition() {
        x += vx;
        y += vy;
        z += vz;
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

    @Override
    public String toString() {
        return String.format("pos=<x=%s, y=%s, z=%s>, vel=<x=%s, y=%s, z=%s>", x, y, z, vx, vy, vz);
    }
}
