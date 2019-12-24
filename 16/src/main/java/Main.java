import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Stream;

class Main {
    public static void main(String[] args) throws IOException {
        partOne("test1", 24176176);
        partOne("test2", 73745418);
        partOne("test3", 52432133);
        partOne("input", 29795507);
        partTwo("test1", 0);
    }

    public static void partTwo(String fileName, int shouldBe) throws IOException {
        System.out.println("part two start");
        int[] input = repeatInput(getInput(fileName));
        /*
         * Way way way waaaaaay too slow
        for (int i = 0; i < 100; i++) {
            phaseTransformation(input);
        }
        */
    }

    public static void partOne(String fileName, int shouldBe) throws IOException {
        int[] input = getInput(fileName);
        for (int i = 0; i < 100; i++) {
            phaseTransformation(input);
        }
        int result = intifyArray(input, 0);
        String resultString = result == shouldBe ? "Correct!" : "Wrong!";
        System.out.printf("Part one (for %s): %s (%s)\n", fileName, result, resultString);
    }

    private static void phaseTransformation(int[] input) {
        int length = input.length;
        for (int i = 0; i < length; i++) {
            Iterator<Integer> iterator = getRepeatingPattern(i, length);
            int newValue = 0;
            for (int j : input) {
                newValue += j * iterator.next();
            }
            input[i] = Math.abs(newValue) % 10;
        }
    }

    private static int intifyArray(int[] input, int offset) {
        StringBuilder output = new StringBuilder();
        for (int i = offset; i < offset + 8; i++) {
            output.append(input[i]);
        }
        return Integer.parseInt(output.toString());
    }

    private static Iterator<Integer> getRepeatingPattern(int forPosition, int length) {
        int[] basePattern = new int[]{0, 1, 0, -1};
        List<Integer> nonRepeatingList = new ArrayList<>();

        for (int i : basePattern) {
            nonRepeatingList.addAll(Collections.nCopies(forPosition + 1, i));
        }

        Iterator<Integer> result = Stream
            .generate(() -> nonRepeatingList)
            .flatMap(List::stream)
            .limit(length + 1)
            .iterator();

        result.next(); // skip first value
        return result;
    }

    private static int[] repeatInput(int[] input) {
        return Stream.generate(() -> input)
            .flatMapToInt(Arrays::stream)
            .limit(10000 * input.length)
            .toArray();
    }

    private static int[] getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            return new String(is.readAllBytes())
                .strip()
                .chars()
                .mapToObj(Character::toString)
                .mapToInt(Integer::parseInt)
                .toArray();
        }

        throw new MissingResourceException(
                "Could not find input file",
                Main.class.getName(),
                fileName);
    }
}
