import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

class AmplificationCircuit {
    public static void main(String[] args) throws IOException {
        int[] controllerSoftware;

        // Test1 should return 43210 from input 4,3,2,1,0
        //controllerSoftware = getInput("test1");

        // Test2 should return 54321 from input 0,1,2,3,4
        //controllerSoftware = getInput("test2");

        // Test3 should return 65210 from input 1,0,4,3,2
        //controllerSoftware = getInput("test3");

        // The real input
        controllerSoftware = getInput("input");

        int[] phaseSettings = new int[]{0,0,0,0,-1};
        int maxResult = 0;
        int[] bestArray = null;

        while (incrementArray(phaseSettings)) {
            int inputSignal = 0;

            for (int phaseSetting : phaseSettings) {
                // Create input stream to be used by the IntcodeComputer's scanner
                InputStream is = new ByteArrayInputStream(
                    String.format("%s\n%s", phaseSetting, inputSignal)
                        .getBytes("UTF-8"));

                // Instantiate a new IntcodeComputer for the next amplifier
                IntcodeComputer cpu = new IntcodeComputer(controllerSoftware, is);
                inputSignal = cpu.run().get(0);
            }

            if (phaseSettings[0] == 1 && phaseSettings[1] == 0 && phaseSettings[2] == 4 && phaseSettings[3] == 3 && phaseSettings[4] == 2) {
                prettyPrintArray(phaseSettings);
                System.out.println(inputSignal);
            }

            if (inputSignal > maxResult) {
                prettyPrintArray(phaseSettings);
                System.out.println(phaseSettings[0]);
                maxResult = inputSignal;
                bestArray = phaseSettings.clone();
            }
        }
        System.out.println("Result");
        prettyPrintArray(bestArray);
        System.out.println(maxResult);
    }

    private static void prettyPrintArray(int[] array) {
        System.out.printf(
            "[%s,%s,%s,%s,%s]\n",
            array[0],
            array[1],
            array[2],
            array[3],
            array[4]);
    }

    private static boolean incrementArray(int[] phaseSettings) {
        phaseSettings[phaseSettings.length - 1]++;
        for (int i = phaseSettings.length - 1; i > 0 ;i--) {
            if (phaseSettings[i] == 5) {
                phaseSettings[i] = 0;
                phaseSettings[i - 1]++;
            } else {
                break;
            }
        }
        if (phaseSettings[0] == 5) {
            return false;
        } else {
            // Check that we have no duplicates
            int uniqueInts = Arrays
                .stream(phaseSettings)
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toSet())
                .size();
            if (uniqueInts != phaseSettings.length) {
                return incrementArray(phaseSettings);
            }
        }
        return phaseSettings[0] != 5;
    }

    private static int[] getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            return Arrays.stream(
                    new String(is.readAllBytes())
                        .strip()
                        .split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        }

        throw new MissingResourceException(
                "Could not find input file",
                AmplificationCircuit.class.getName(),
                fileName);
    }
}
