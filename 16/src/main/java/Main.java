import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
        partOne("input", 0);
    }

    public static void partOne(String fileName, int shouldBe) throws IOException {
        int[] input = getInput(fileName);
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
