import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.MissingResourceException;

class Main {
    public static void main(String[] args) throws IOException {
        partOne("input");
        partTwo("input");
    }

    private static void partTwo(String fileName) throws IOException {
        long[] application = getInput(fileName);
        Robot robot = new Robot(application);
        robot.forcePaint(1);
        while(!robot.isHalted()) {
            robot.run();
        }

        Collection<Panel> hullPanels = robot.getHull().values();
        int minX = hullPanels.stream()
            .mapToInt(x -> x.getX())
            .min()
            .getAsInt();
        // Repeat for other max and min
        int maxX = hullPanels.stream().mapToInt(x -> x.getX()).max().getAsInt();
        int minY = hullPanels.stream().mapToInt(y -> y.getY()).min().getAsInt();
        int maxY = hullPanels.stream().mapToInt(y -> y.getY()).max().getAsInt();

        String[][] hullArray = new String[maxY - minY + 1][maxX - minX + 1];
        for (Panel panel : hullPanels) {
            int x = panel.getX();
            int y = panel.getY();
            long color = panel.getColor();
            if (color == 0) {
                hullArray[y - minY][x - minX] = " ";
            } else if (color == 1) {
                hullArray[y - minY][x - minX] = "█";
            } else {
                throw new IllegalStateException(String.format("Panel %s has an invalid color!", panel));
            }
        }
        System.out.println("Part two:");
        printHull(hullArray);
    }

    private static void printHull(String[][] hull) {
        for (int i = hull.length - 1; i >= 0; i--) {
            String[] row = hull[i];

            for (String cell : row) {
                if (cell == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(cell);
                }
            }
            System.out.println();
        }

    }

    private static void partOne(String fileName) throws IOException {
        long[] application = getInput(fileName);
        Robot robot = new Robot(application);
        while(!robot.isHalted()) {
            robot.run();
        }
        int answer = robot.getHull().size();
        System.out.printf("Part one: %s\n", answer);
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
