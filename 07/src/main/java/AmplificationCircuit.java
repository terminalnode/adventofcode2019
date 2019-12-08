import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;


class AmplificationCircuit {
    public static void main(String[] args) throws IOException {
        // Test1 should return 43210 from input 4,3,2,1,0
        //partOne("test1");

        // Test2 should return 54321 from input 0,1,2,3,4
        //partOne("test2");

        // Test3 should return 65210 from input 1,0,4,3,2
        //partOne("test3");

        // The real input
        partOne("input");
    }

    public static void partOne(String fileName) throws IOException {
        int[] application = getInput(fileName);
        int[] maxArray = null;
        int[] phaseSettings = new int[]{0,0,0,0,-1};
        int maxResult = 0;


        // Create all the amplifiers and connect inputs and outputs
        List<IntcodeComputer> amplifiers = createAmplifiers(phaseSettings.length, application);

        while (incrementArray(phaseSettings)) {
            // Initialize or reset the amplifiers, then add phase settings
            // and initialize the first amplifier with 0 as input.
            amplifiers.forEach(x -> x.reinitialize());
            for (int i = 0; i < phaseSettings.length; i++) {
                amplifiers.get(i).addInput(phaseSettings[i]);
            }
            amplifiers.get(0).addInput(0);

            amplifiers.forEach(x -> x.run());

            int result = amplifiers.get(amplifiers.size() - 1).getOutput().peek();
            if (result > maxResult) {
                maxResult = result;
                maxArray = phaseSettings.clone();
            }
        }
        prettyPrintArray(maxArray);
        System.out.println(maxResult);
    }

    public static List<IntcodeComputer> createAmplifiers(int num, int[] application) {
        List<IntcodeComputer> amps = new ArrayList<>();
        IntcodeComputer lastAmp = null;
        for (int i = 0; i < num; i++) {
            IntcodeComputer newAmp = new IntcodeComputer(application);
            if (lastAmp != null) {
                newAmp.setInput(lastAmp.getOutput());
            }
            amps.add(newAmp);
            lastAmp = newAmp;
        }
        amps.get(0).setInput(amps.get(4).getOutput());
        return amps;
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

        return true;
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
