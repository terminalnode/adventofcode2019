import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;

public class Main {
    public static void main(String[] args) throws IOException {
        partOne("input");
        partTwo("input");
    }

    private static void partTwo(String fileName) throws IOException {
        long[] application = getInput(fileName);
        ArcadeCabinet arcadeCabinet = new ArcadeCabinet(application);
        arcadeCabinet.setIndexTo(0, 2);
        arcadeCabinet.firstScreenRender();
        while (!arcadeCabinet.isHalted()) {
            arcadeCabinet.updateScreen();
        }
        System.out.printf("Part two (with %s): %s\n", fileName, arcadeCabinet.getPlayerScore());
    }

    public static void partOne(String fileName) throws IOException {
        long[] application = getInput(fileName);
        ArcadeCabinet arcadeCabinet = new ArcadeCabinet(application);

        int answer = arcadeCabinet.fakeScreenRender();
        System.out.printf("Part one (with %s): %s\n", fileName, answer);
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
