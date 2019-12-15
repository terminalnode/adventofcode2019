import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ArcadeCabinet {
    private final Queue<Long> brainInput;
    private final Queue<Long> brainOutput;
    private final IntcodeComputer brain;
    private long playerScore;
    private String[][] screen;
    private long paddleX;
    private long ballX;

    private class TileType {
        public static final int EMPTY = 0;
        public static final int WALL = 1;
        public static final int BLOCK = 2;
        public static final int PADDLE = 3;
        public static final int BALL = 4;
    }

    private class TileSprites {
        public static final String EMPTY = " ";
        public static final String WALL = "█";
        public static final String BLOCK = "▄";
        public static final String PADDLE = "▂";
        public static final String BALL = "◉";
    }

    public ArcadeCabinet(long[] application) {
        brainInput = new LinkedList<>();

        brain = new IntcodeComputer(application);
        brain.setInput(brainInput);
        brain.reinitialize();

        brainOutput = brain.getOutput();

        screen = null;
        playerScore = 0;
        paddleX = 0;
        ballX = 0;
    }

    public void setIndexTo(long index, long value) {
        brain.getApplication().put(index, value);
    }

    public void run() {
        brain.run();
    }

    public long getPlayerScore() {
        return playerScore;
    }

    public boolean isHalted() {
        return brain.isHalted();
    }

    public int fakeScreenRender() {
        run();
        int blockTiles = 0;
        while (brainOutput.peek() != null) {
            brainOutput.poll();
            brainOutput.poll();
            if (brainOutput.poll() == TileType.BLOCK) {
                blockTiles++;
            }
        }
        return blockTiles;
    }

    public void printScreen() {
        if (screen == null) {
            firstScreenRender();
        }
        System.out.printf("Current score: %s\n", playerScore);

        for (String[] row : screen) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void updateScreen() {
        if (ballX > paddleX) {
            updateScreen(1);
        } else if (ballX < paddleX) {
            updateScreen(-1);
        } else {
            updateScreen(0);
        }
    }

    public void updateScreen(long joystickInput) {
        brainInput.add(joystickInput);
        run();
        while (brainOutput.peek() != null) {
            long x = brainOutput.poll();
            long y = brainOutput.poll();
            long tile = brainOutput.poll();

            if (x == -1 && y == 0) {
                playerScore = tile;
            } else {
                editTiles(x, y, tile);
            }
        }
    }

    public void firstScreenRender() {
        run();
        Map<Long, Map<Long,Long>> firstPass = new HashMap<>();

        while (brainOutput.peek() != null) {
            long x = brainOutput.poll();
            long y = brainOutput.poll();
            long tile = brainOutput.poll();

            if (x == -1 && y == 0) {
                playerScore = tile;
            } else {
                if (!firstPass.containsKey(y)) {
                    firstPass.put(y, new HashMap<>());
                }
                firstPass.get(y).put(x, tile);
            }
        }

        int height = firstPass.size();
        int width = firstPass.get(Long.valueOf(0)).size();
        screen = new String[height][width];
        for (Long ly : firstPass.keySet()) {
            for (Long lx : firstPass.get(ly).keySet()) {
                editTiles(lx, ly, firstPass.get(ly).get(lx));
            }
        }
    }

    private void editTiles(long lx, long ly, long tile) {
        int x = (int) lx;
        int y = (int) ly;

        switch ((int) tile) {
            case TileType.EMPTY:
                screen[y][x] = TileSprites.EMPTY;
                break;
            case TileType.WALL:
                screen[y][x] = TileSprites.WALL;
                break;
            case TileType.BLOCK:
                screen[y][x] = TileSprites.BLOCK;
                break;
            case TileType.PADDLE:
                screen[y][x] = TileSprites.PADDLE;
                paddleX = lx;
                break;
            case TileType.BALL:
                screen[y][x] = TileSprites.BALL;
                ballX = lx;
                break;
            default:
                throw new IllegalStateException(String.format("Invalid tile: %s", tile));
        }

    }
}
