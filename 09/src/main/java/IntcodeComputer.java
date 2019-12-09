import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

class IntcodeComputer {
    private final long[] originalApplication;
    private final Queue<Long> output;
    private Map<Long, Long> application;
    private boolean halted;
    private boolean paused;
    private Long index;
    private Queue<Long> input;
    private long relativeBase;

    public IntcodeComputer(long[] application) {
        this.originalApplication = application;
        this.output = new LinkedList<>();
        this.halted = false;
        this.paused = false;
        this.input = null;
        this.application = null;
        this.index = null;
        this.relativeBase = 0;
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
        public static final int ADD_RELATIVE_BASE = 9;
        public static final int EXIT = 99;
    }

    private static class Modes {
        public static final int POSITION = 0;
        public static final int IMMEDIATE = 1;
        public static final int RELATIVE = 2;
    }

    public Queue<Long> getInput() {
        return input;
    }

    public Queue<Long> getOutput() {
        return output;
    }

    public Map<Long, Long> getApplication() {
        return application;
    }

    public void setInput(Queue<Long> newInput) {
        input = newInput;
    }

    public void addInput(long value) {
        input.add(value);
    }

    public boolean isHalted() {
        return halted;
    }

    private void loadApplication() {
        application = new HashMap<>();
        for (long i = 0; i < originalApplication.length; i++) {
            application.put(i, (long) originalApplication[(int) i]);
        }
    }

    public void reinitialize() {
        if (input != null) {
            input.clear();
        } else {
            input = new LinkedList<>();
        }

        output.clear();
        loadApplication();
        index = (long) 0;
        halted = false;
        relativeBase = 0;
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

    private void intcodeProcessor() {
        // Convert OP code + mode to string
        String opCodeAndMode = application.get(index).toString();
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

        long first, second, outputTo;
        switch (opCode) {
            case OpCodes.ADDITION:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                outputTo = getIndex(3, index, paramMode);

                application.put(outputTo, first + second);
                index += 4;
                break;

            case OpCodes.MULTIPLICATION:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                outputTo = getIndex(3, index, paramMode);

                application.put(outputTo, first * second);
                index += 4;
                break;

            case OpCodes.INPUT:
                outputTo = getIndex(1, index, paramMode);

                if (!input.isEmpty()) {
                    application.put(outputTo, input.poll());
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
                outputTo = getIndex(3, index, paramMode);

                if (first < second) {
                    application.put(outputTo, (long) 1);
                } else {
                    application.put(outputTo, (long) 0);
                }
                index += 4;
                break;

            case OpCodes.EQUALS:
                first = getParam(1, index, paramMode);
                second = getParam(2, index, paramMode);
                outputTo = getIndex(3, index, paramMode);

                if (first == second) {
                    application.put(outputTo, (long) 1);
                } else {
                    application.put(outputTo, (long) 0);
                }
                index += 4;
                break;

            case OpCodes.ADD_RELATIVE_BASE:
                first = getParam(1, index, paramMode);
                relativeBase += first;

                index += 2;
                break;

            case OpCodes.EXIT:
                System.out.println("Encountered exit code 99, shutting down.");
                halted = true;
                break;

            default:
                throw new IllegalArgumentException(String.format("Unrecognised OP code: %s", opCode));
        }
    }

    private Long getIndex(int paramNum, long forIndex, int[] modes) {
        int mode = modes[paramNum - 1];
        long immediateValue = forIndex + paramNum;

        switch (mode) {
            case Modes.POSITION:
                return application.get(immediateValue);

            case Modes.RELATIVE:
                return application.get(immediateValue) + relativeBase;

            case Modes.IMMEDIATE:
                return immediateValue;

            default:
                throw new IllegalArgumentException(String.format("Illegal mode: %s", mode));
        }
    }

    private Long getParam(int paramNum, long forIndex, int[] modes) {
        Long index = getIndex(paramNum, forIndex, modes);
        Long value = application.get(index);
        if (value == null) {
            return (long) 0;
        } else {
            return value;
        }
    }
}

