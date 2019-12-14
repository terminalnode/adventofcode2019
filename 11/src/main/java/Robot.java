import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Robot {
    private int x, y;
    private final Map<Panel, Panel> hull;
    private final IntcodeComputer brain;
    private final Queue<Long> brainInput;
    private Direction currentDirection;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private static class Rotate {
        public static long LEFT = 0;
        public static long RIGHT = 1;
    }

    public Robot(long[] application) {
        this.x = 0;
        this.y = 0;

        this.hull = new HashMap<>();
        this.hull.put(new Panel(x, y), new Panel(x, y));

        this.brain = new IntcodeComputer(application);
        this.brain.reinitialize();
        this.brainInput = new LinkedList<>();
        this.brain.setInput(brainInput);

        this.currentDirection = Direction.UP;
    }

    public Map<Panel, Panel> getHull() {
        return hull;
    }

    public boolean isHalted() {
        return brain.isHalted();
    }

    public void run() {
        addInput(getCurrentPanel().getColor());
        brain.run();
        autoPaint();
        autoMove();
    }

    private Panel getCurrentPanel() {
        Panel dummyPanel = new Panel(x,y);

        if (hull.containsKey(dummyPanel)) {
            return hull.get(dummyPanel);
        }

        hull.put(dummyPanel, dummyPanel);
        return dummyPanel;
    }

    public void addInput(long newInput) {
        brainInput.add(newInput);
    }

    public void forcePaint(long newColor) {
        getCurrentPanel().setColor(newColor);
    }

    private void autoPaint() {
        Panel currentPanel = getCurrentPanel();
        long paintInstruction = brain.getOutput().poll();
        currentPanel.setColor(paintInstruction);
    }

    private void autoMove() {
        turn();
        move();
    }

    private void move() {
        switch (currentDirection) {
            case UP: y++; break;
            case DOWN: y--; break;
            case RIGHT: x++; break;
            case LEFT: x--; break;

            default:
                throw new IllegalStateException(
                    String.format("Robot has invalid direction: %s", currentDirection));
        }
    }

    private void turn() {
        long rotation = brain.getOutput().poll();

        if (rotation == Rotate.LEFT) {
            switch (currentDirection) {
                case UP: currentDirection = Direction.LEFT; break;
                case DOWN: currentDirection = Direction.RIGHT; break;
                case RIGHT: currentDirection = Direction.UP; break;
                case LEFT: currentDirection = Direction.DOWN; break;

                default:
                    throw new IllegalStateException(
                        String.format("Robot has invalid direction: %s", currentDirection));
            }
        } else if (rotation == Rotate.RIGHT) {
            switch (currentDirection) {
                case UP: currentDirection = Direction.RIGHT; break;
                case DOWN: currentDirection = Direction.LEFT; break;
                case RIGHT: currentDirection = Direction.DOWN; break;
                case LEFT: currentDirection = Direction.UP; break;

                default:
                    throw new IllegalStateException(
                        String.format("Robot has invalid direction: %s", currentDirection));
            }
        }
    }
}
