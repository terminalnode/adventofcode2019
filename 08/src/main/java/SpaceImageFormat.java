import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Queue;
import java.util.stream.Collectors;

class SpaceImageFormat {
    public static void main(String[] args) throws IOException {
        int height = 6;
        int width = 25;
        List<List<Integer>> layers = getInputLayers("input", height, width);
        partOne(layers);
        System.out.println();
        partTwo(layers, height, width);
    }

    private static void partTwo(List<List<Integer>> layers, int height, int width) {
        System.out.println("Part two:");
        int layerSize = height * width;

        for (int i = 0; i < layerSize; i++) {
            final int index = i;

            int[] pixelLayer = layers.stream()
                .mapToInt(x -> x.get(index))
                .dropWhile(x -> x == 2)
                .toArray();

            // Linebreak
            if (i % width == 0 && i > 0) {
                System.out.println();
            }

            // Pixel
            if (pixelLayer.length == 0) {
                // Shouldn't happen, but if it does we're prepared.
                System.out.print("2");
            } else {
                // Using spaces and # to make it easier to read
                int value = pixelLayer[0];
                if (value == 0) {
                    System.out.print(" ");
                } else {
                    System.out.print("#");
                }
            }
        }
    }

    private static void partOne(List<List<Integer>> layers) {
        int mostZeroes = Integer.MAX_VALUE;
        int result = -1;
        for (List<Integer> layer : layers) {
            int numZeroes = 0;
            int numOnes = 0;
            int numTwos = 0;
            for (int i : layer) {
                switch(i) {
                    case 0: numZeroes++; break;
                    case 1: numOnes++; break;
                    case 2: numTwos++; break;
                }
            }
            if (numZeroes < mostZeroes) {
                mostZeroes = numZeroes;
                result = numOnes * numTwos;
            }
        }
        System.out.printf("Part one: %s\n", result);
    }

    private static List<List<Integer>> getInputLayers(String fileName, int height, int width) throws IOException {
        int layerSize = height * width;
        Queue<Integer> input = new LinkedList<>(getInput(fileName));
        List<List<Integer>> layers = new ArrayList<>();

        int currentLayer = -1;
        while (input.size() > 0) {
            currentLayer++;
            layers.add(new ArrayList<>());
            for (int i = 0; i < layerSize; i++) {
                layers.get(currentLayer).add(input.poll());
            }
        }

        return layers;
    }

    private static List<Integer> getInput(String fileName) throws IOException {
        InputStream is = Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);

        if (is != null) {
            return new String(is.readAllBytes())
                .strip()
                .chars()
                .map(x -> Character.getNumericValue(x))
                .mapToObj(Integer::valueOf)
                .collect(Collectors.toList());
        }

        throw new MissingResourceException(
                "Could not find input file",
                SpaceImageFormat.class.getName(),
                fileName);
    }
}
