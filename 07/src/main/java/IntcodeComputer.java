import java.util.LinkedList;
import java.util.Queue;

class IntcodeComputer {
    private final int[] originalApplication;
    private final Queue<Integer> output;
    private int[] application;
    private boolean halted;
    private boolean paused;
    private Integer index;
    private Queue<Integer> input;

    public IntcodeComputer(int[] application) {
        this.originalApplication = application;
        this.output = new LinkedList<>();
        this.halted = false;
        this.paused = false;
        this.input = null;
        this.application = null;
        this.index = null;
    }

    public Queue<Integer> getInput() {
        return input;
    }

    public Queue<Integer> getOutput() {
        return output;
    }

    public void setInput(Queue<Integer> newInput) {
        input = newInput;
    }

    public void addInput(int value) {
        input.add(value);
    }

    public boolean isHalted() {
        return halted;
    }

    public void reinitialize() {
        output.clear();
        input.clear();
        application = originalApplication.clone();
        index = 0;
        halted = false;
    }

    public void run() {
        if (halted) {
            return;
        }
        
        paused = false;

        while (!halted && !paused) {
            intcodeProcessor();
        }
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

    private static class Modes {
        public static final int POSITION = 0;
        public static final int IMMEDIATE = 1;
    }

    private void intcodeProcessor() {
        // Convert OP code + mode to string
        String opCodeAndMode = Integer.toString(application[index]);
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

        int first, second;
        switch (opCode) {
            case OpCodes.ADDITION:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                application[application[index + 3]] = first + second;
                index += 4;
                break;

            case OpCodes.MULTIPLICATION:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                application[application[index + 3]] = first * second;
                index += 4;
                break;

            case OpCodes.INPUT:
                if (!input.isEmpty()) {
                    application[application[index + 1]] = input.poll();
                    index += 2;
                } else {
                    paused = true;
                }
                break;

            case OpCodes.OUTPUT:
                first = getParam(1, index, paramMode);
                output.add(first);
                index += 2;
                break;

            case OpCodes.JUMP_IF_TRUE:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                if (first != 0) {
                    index = second;
                    break;
                }
                index += 3;
                break;

            case OpCodes.JUMP_IF_FALSE:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                if (first == 0) {
                    index = second;
                    break;
                }
                index += 3;
                break;

            case OpCodes.LESS_THAN:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                if (first < second) {
                    application[application[index + 3]] = 1;
                } else {
                    application[application[index + 3]] = 0;
                }
                index += 4;
                break;

            case OpCodes.EQUALS:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                if (first == second) {
                    application[application[index + 3]] = 1;
                } else {
                    application[application[index + 3]] = 0;
                }
                index += 4;
                break;

            case OpCodes.EXIT:
                // System.out.println("Encountered exit code 99, shutting down.");
                halted = true;
                break;

            default:
                throw new IllegalArgumentException(String.format("Unrecognised OP code: %s", opCode));
        }
    }

    private int getParam(int paramNum, int forIndex, int[] modes) {
        int mode = modes[paramNum - 1];
        int immediateValue = application[forIndex + paramNum];
        switch (mode) {
            case Modes.POSITION:
                return application[immediateValue];
            case Modes.IMMEDIATE:
                return immediateValue;
            default:
                throw new IllegalArgumentException(String.format("Illegal mode: %s", mode));
        }
    }
}
