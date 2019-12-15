import java.util.LinkedList;
import java.util.Queue;

public class ArcadeCabinet {
    private final Queue<Long> brainInput;
    private final Queue<Long> brainOutput;
    private final IntcodeComputer brain;

    private class TileType {
        public static final long EMPTY = 0;
        public static final long WALL = 1;
        public static final long BLOCK = 2;
        public static final long PADDLE = 3;
        public static final long BALL = 4;
    }

    public ArcadeCabinet(long[] application) {
        brainInput = new LinkedList<>();

        brain = new IntcodeComputer(application);
        brain.setInput(brainInput);
        brain.reinitialize();

        brainOutput = brain.getOutput();
    }

    public void run() {
        while (!brain.isHalted()) {
            brain.run();
        }
    }

    public void renderScreen() {
        run();
        int blockTiles = 0;
        while (brainOutput.peek() != null) {
            long x = brainOutput.poll();
            long y = brainOutput.poll();
            long tile = brainOutput.poll();

            if (tile == TileType.BLOCK) {
                blockTiles++;
            }
        }
        System.out.println(blockTiles);
    }
}
