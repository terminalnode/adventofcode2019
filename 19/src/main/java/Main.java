import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;

import tractorbeam.TractorBeam;

public class Main {
    public static void main(String[] args) throws IOException {
        partOne("input");
    }

    private static void partOne(String fileName) throws IOException {
        long[] application = getInput(fileName);
        TractorBeam tractorBeam = new TractorBeam(application);
        int result = tractorBeam.testFifty();
        System.out.printf("Part one (using %s): %s\n", fileName, result);
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
                Main.class.getName(),
                fileName);
    }
}
