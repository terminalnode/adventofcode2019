import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VacuumRobot {
    private final IntcodeComputer brain;
    private final Queue<Long> brainInput;
    private String asciiMap;

    public VacuumRobot(long[] application) {
        brain = new IntcodeComputer(application);
        brainInput = new LinkedList<>();
        brain.setInput(brainInput);
        brain.reinitialize();
        asciiMap = null;
    }

    public void updateMap() {
        brain.run();
        StringBuilder sb = new StringBuilder();
        while (!brain.getOutput().isEmpty()) {
            sb.append((char) Math.toIntExact(brain.getOutput().poll()));
        }
        asciiMap = sb.toString().strip();
    }

    public List<int[]> getAlignmentParameters() {
        if (asciiMap == null) {
            updateMap();
        }

        List<int[]> result = new ArrayList<>();

        String[] mapRows = asciiMap.split("\n"); 
        for (int y = 1; y < mapRows.length - 2; y++) {
            for (int x = 1; x < mapRows[y].length() - 2; x++) {
               if (mapRows[y].charAt(x) == '#') {
                    char above = mapRows[y-1].charAt(x);
                    char below = mapRows[y+1].charAt(x);
                    char right = mapRows[y].charAt(x+1);
                    char left  = mapRows[y].charAt(x-1);
                    if (above == '#' && below == '#' && right == '#' && left == '#') {
                        result.add(new int[]{x, y});
                    }
               }
            }
        }

        return result;
    }

    public String getAsciiMap() {
        return asciiMap;
    }
}
