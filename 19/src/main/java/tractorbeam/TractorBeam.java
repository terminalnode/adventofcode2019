package tractorbeam;

import java.util.LinkedList;
import java.util.Queue;

public class TractorBeam {
    IntcodeComputer brain;
    Queue<Long> brainIn;
    Queue<Long> brainOut;

    public TractorBeam(long[] application) {
        brain = new IntcodeComputer(application);
        brainIn = new LinkedList<>();
        brainOut = brain.getOutput();
        brain.setInput(brainIn);
        brain.reinitialize();
    }

    public int testFifty() {
        int sum = 0;
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                sum += testCoordinate(x, y);
            }
        }
        return sum;
    }

    public long testCoordinate(long x, long y) {
        brain.reinitialize();
        brainIn.add(x);
        brainIn.add(y);
        brain.run();

        return brainOut.poll();
    }
}
