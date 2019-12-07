import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class IntcodeComputer {
    private final Scanner scanner;
    private final int[] application;
    private List<Integer> output;

    public IntcodeComputer(int[] application, InputStream is) {
        this.scanner = new Scanner(is);
        this.application = application;
        this.output = new ArrayList<>();
    }

    public List<Integer> run() {
        Integer index = 0;
        int[] application = this.application.clone();

        while (index != null) {
            index = intcodeProcessor(index, application, scanner);
        }

        return output;
    }

    private static class OpCodes {
        public static final int ADDITION = 1;
        public static final int MULTIPLICATION = 2;
        public static final int INPUT = 3;
        public static final int OUTPUT = 4;
        public static final int JUMP_IF_TRUE = 5;
        public static final int JUMP_IF_FALSE = 6;
        public static final int LESS_THAN = 7;
        public static final int EQUALS = 8;
        public static final int EXIT = 99;
    }

    private Integer intcodeProcessor(int index, int[] program, Scanner scanner) {
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
            case OpCodes.ADDITION:
                args = decodeParams(2, index, paramMode, program);
                program[program[index + 3]] = args[0] + args[1];
                return index + 4;

            case OpCodes.MULTIPLICATION:
                args = decodeParams(2, index, paramMode, program);
                program[program[index + 3]] = args[0] * args[1];
                return index + 4;

            case OpCodes.INPUT:
                // System.out.println("Awaiting input...");
                program[program[index + 1]] = Integer.parseInt(scanner.nextLine());
                return index + 2;

            case OpCodes.OUTPUT:
                args = decodeParams(1, index, paramMode, program);
                // System.out.printf("IntComputer output: %s\n", args[0]);
                output.add(args[0]);
                return index + 2;

            case OpCodes.JUMP_IF_TRUE:
                args = decodeParams(2, index, paramMode, program);
                if (args[0] != 0) {
                    return args[1];
                }
                return index + 3;
            case OpCodes.JUMP_IF_FALSE:
                args = decodeParams(2, index, paramMode, program);
                if (args[0] == 0) {
                    return args[1];
                }
                return index + 3;

            case OpCodes.LESS_THAN:
                args = decodeParams(2, index, paramMode, program);
                if (args[0] < args[1]) {
                    program[program[index + 3]] = 1;
                } else {
                    program[program[index + 3]] = 0;
                }
                return index + 4;

            case OpCodes.EQUALS:
                args = decodeParams(2, index, paramMode, program);
                if (args[0] == args[1]) {
                    program[program[index + 3]] = 1;
                } else {
                    program[program[index + 3]] = 0;
                }
                return index + 4;

            case OpCodes.EXIT:
                // System.out.println("Encountered exit code 99, shutting down.");
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
}
