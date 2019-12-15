import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.MissingResourceException;

public class Main {
    public static void main(String[] args) throws IOException {
        long[] application = getInput("input");
        ArcadeCabinet arcadeCabinet = new ArcadeCabinet(application);
        arcadeCabinet.renderScreen();
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
