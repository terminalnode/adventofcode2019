import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;

class SensorBoost {
    public static void main(String[] args) throws IOException {
        solve(1);
        solve(2);
    }

    public static void solve(int firstInput) throws IOException {
        System.out.printf("Solving with %s as first input.", firstInput);

        long[] application = getInput("input");
        IntcodeComputer cpu = new IntcodeComputer(application);

        cpu.reinitialize();
        cpu.addInput(firstInput);
        while (!cpu.isHalted()) {
            cpu.run();
        }
        System.out.println(cpu.getOutput().poll());
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
                SensorBoost.class.getName(),
                fileName);
    }
}
