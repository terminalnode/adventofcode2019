import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws IOException {
        partOne("input");
        partTwo("input");
    }

    private static void partTwo(String fileName) throws IOException {
        long[] application = getInput(fileName);
        VacuumRobot robot = new VacuumRobot(application);
        IntcodeComputer brain = robot.getBrain();

        LivingMap livingMap = new LivingMap(robot.getAsciiMap());
        List<String> stepsToTake = livingMap.autoMove();
        // TODO: implement zip/LZ77/LZ78 algorithm
        // https://en.wikipedia.org/wiki/LZ77_and_LZ78
        //
        // Manual solution:
        // A = L10,L12,R6
        // B = R10,L4,L4,L12
        // C = L10,R10,R6,L4
        //
        // A,B,A,B,A,C,B,C,A,C
        // 65 44 66 44 6
        robot.setBotToMovementMode();
        char[] mainRoutine = "A,B,A,B,A,C,B,C,A,C\n".toCharArray();
        char[] aRoutine = "L,10,L,12,R,6\n".toCharArray();
        //char[] aRoutine = "R,10,R,12,L,6\n".toCharArray();
        char[] bRoutine = "R,10,L,4,L,4,L,12\n".toCharArray();
        //char[] bRoutine = "L,10,R,4,R,4,R,12\n".toCharArray();
        char[] cRoutine = "L,10,R,10,R,6,L,4\nn\n".toCharArray();
        //char[] cRoutine = "R,10,L,10,L,6,R,4\nn\n".toCharArray();
        char[][] fullInput = new char[][]{mainRoutine, aRoutine, bRoutine, cRoutine};

        for (char[] routine : fullInput) {
            robot.run();
            brain.getOutput().clear();
            for (char character : routine) {
                robot.addInput((long) character);
            }
        }
        robot.run();

        long lastOutput = 0;
        while (brain.getOutput().peek() != null) {
            lastOutput = brain.getOutput().poll();
        }
        System.out.printf("Part two: %s\n", lastOutput);
    }

    private static void printOutput(IntcodeComputer cpu) {
        Queue<Long> output = cpu.getOutput();
        while (output.peek() != null) {
            System.out.print((char) Math.toIntExact(output.poll()));
        }
        System.out.println();
    }

    private static void partOne(String fileName) throws IOException {
        long[] application = getInput(fileName);
        VacuumRobot robot = new VacuumRobot(application);
        robot.reinitialize();
        robot.updateMap();
        int sum = robot.getAlignmentParameters()
            .stream()
            .mapToInt(x -> x[0] * x[1])
            .sum();
        System.out.println("Part one: " + sum);
    }

    private static long[] getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            return Arrays.stream(
                    new String(is.readAllBytes())
                        .strip()
                        .split(","))
                    .mapToLong(Long::parseLong)
                    .toArray();
        }

        throw new MissingResourceException(
                "Could not find input file",
                Main.class.getName(),
                fileName);
    }
}
