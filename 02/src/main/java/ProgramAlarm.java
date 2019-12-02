import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;

public class ProgramAlarm {
    public static void main(String[] args) throws IOException {
        int[] input = getInputAsStream();
        input[1] = 12;
        input[2] = 2;

        // For part 1 we run the program normally after having replaced
        // input[1] and input[2] with the above values.
        System.out.println("Part #1 solution:");
        System.out.println(runProgram(input));

        // For part two we want to find the values for input[1] and input[2]
        // that produces the desired return value.
        int output = 0;
        int desired = 19690720;
        int result = 0;
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                input = getInputAsStream();
                input[1] = noun;
                input[2] = verb;
                output = runProgram(input);
                if (output == desired) {
                    result = 100 * noun + verb;
                    break;
                }
            }
            if (output == desired) {
                break;
            }
        }
        System.out.println("Part #2 solution:");
        System.out.println(result);

    }

    private static int runProgram(int[] input) {
        for (int i = 0; i < input.length; i += 4) {
            if (input[i] == 99) {
                break;
            }
            int pos1 = input[i+1];
            int pos2 = input[i+2];
            int pos3 = input[i+3];

            switch (input[i]) {
                case 1:
                    input[pos3] = input[pos1] + input[pos2];
                    break;
                case 2:
                    input[pos3] = input[pos1] * input[pos2];
                    break;
                default:
                    throw new IndexOutOfBoundsException(String.format("Instruction at position %s was %s", i, input[i]));
            }
        }
        return input[0];
    }

    private static int[] getInputAsStream() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("input");
        if (is != null) {
            String[] allDigits = new String(is.readAllBytes())
                    .strip()
                    .split(",");
            return Arrays.stream(allDigits)
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }
        // Well, this isn't going to work
        throw new MissingResourceException(
                "Could not find input file",
                ProgramAlarm.class.getName(),
                "");
    }
}
