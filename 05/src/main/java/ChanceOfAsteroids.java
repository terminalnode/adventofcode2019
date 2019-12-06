import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;
import java.util.Scanner;

public class ChanceOfAsteroids {
    public static void main(String[] args) throws IOException {
        int[] input = getInput();

        // Equal: 1 if input equal to 8, otherwise 0
        // Position mode
        //input = new int[]{3,9,8,9,10,9,4,9,99,-1,8};
        // Immediate mode
        //input = new int[]{3,3,1108,-1,8,3,4,3,99};

        // Less than: 1 if input less than 8, otherwise 0
        // Position mode
        //input = new int[]{3,9,7,9,10,9,4,9,99,-1,8};
        // Immediate mode
        //input = new int[]{3,3,1107,-1,8,3,4,3,99};

        // Jumps: 0 if input 0, 1 if non-zero
        // Position mode
        //input = new int[]{3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9};
        // Immediate mode
        //input = new int[]{3,3,1105,-1,9,1101,0,0,12,4,12,99,1};

        Scanner scanner = new Scanner(System.in);
        Integer index = 0;
        while (index != null) {
            index = intcodeProcessor(index, input, scanner);
        }
    }

    private static Integer intcodeProcessor(int index, int[] program, Scanner scanner) {
        // Convert OP code + mode to string
        String opCodeAndMode = Integer.toString(program[index]);
        // Extract last two digits to retrieve the opcode.
        int opCode = Integer.parseInt(opCodeAndMode.replaceAll(".*(?=\\d\\d$)", ""));
        // Extract everything but the last two digits to retrieve the parameter modes.
        String paramModes = opCodeAndMode.replaceAll("\\d?\\d$", "");

        // Parse the parameter modes into digits
        // If string length isn't three that means we have one or more leading zeroes.
        int[] paramMode = new int[3];
        paramMode[0] = paramModes.length() >= 1 ?
            Integer.parseInt("" + paramModes.charAt(paramModes.length() - 1)) : 0;
        paramMode[1] = paramModes.length() >= 2 ?
            Integer.parseInt("" + paramModes.charAt(paramModes.length() - 2)) : 0;
        paramMode[2] = paramModes.length() >= 3 ?
            Integer.parseInt("" + paramModes.charAt(paramModes.length() - 3)) : 0;

        int[] args;
        switch (opCode) {
            case 1: // Addition
                args = decodeParams(2, index, paramMode, program);
                program[program[index + 3]] = args[0] + args[1];
                return index + 4;

            case 2: // Multiplication
                args = decodeParams(2, index, paramMode, program);
                program[program[index + 3]] = args[0] * args[1];
                return index + 4;

            case 3: // Input
                System.out.println("Awaiting input...");
                program[program[index + 1]] = Integer.parseInt(scanner.nextLine());
                return index + 2;

            case 4: // Output
                args = decodeParams(1, index, paramMode, program);
                System.out.printf("IntComputer output: %s\n", args[0]);
                return index + 2;

            case 5: // Jump if true
                args = decodeParams(2, index, paramMode, program);
                if (args[0] != 0) {
                    return args[1];
                }
                return index + 3;
            case 6: // Jump if false
                args = decodeParams(2, index, paramMode, program);
                if (args[0] == 0) {
                    return args[1];
                }
                return index + 3;

            case 7: // Less than
                args = decodeParams(2, index, paramMode, program);
                if (args[0] < args[1]) {
                    program[program[index + 3]] = 1;
                } else {
                    program[program[index + 3]] = 0;
                }
                return index + 4;

            case 8: // Equals
                args = decodeParams(2, index, paramMode, program);
                if (args[0] == args[1]) {
                    program[program[index + 3]] = 1;
                } else {
                    program[program[index + 3]] = 0;
                }
                return index + 4;

            case 99: // Exit program
                System.out.println("Encountered exit code 99, shutting down.");
                return null;

            default:
                throw new IllegalArgumentException(String.format("Unrecognised OP code: %s", opCode));
        }
    }

    private static int[] decodeParams(int numParams, int opCodeIndex, int[] paramModes, int[] program) {
        int[] decodedArgs = new int[numParams];

        for (int i = 1; i <= numParams; i++) {
            int mode = paramModes[i-1];
            int arg = program[opCodeIndex + i];
            switch (mode) {
                case 0: // Position mode
                    decodedArgs[i-1] = program[arg];
                    break;
                case 1: // Immediate mode
                    decodedArgs[i-1] = arg;
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Illegal mode: %s", mode));
            }
        }
        return decodedArgs;
    }

    private static int[] getInput() throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("input");

        if (is != null) {
            String[] input = new String(is.readAllBytes())
                    .strip()
                    .split(",");

            return Arrays.stream(input)
                .mapToInt(Integer::parseInt)
                .toArray();
        }

        // Well, this isn't going to work
        throw new MissingResourceException(
                "Could not find input file",
                ChanceOfAsteroids.class.getName(),
                "");
    }
}
