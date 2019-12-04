import java.util.regex.Pattern;
import java.util.stream.IntStream;

class SecureContainer {
    public static void main(String[] args) {
        run(367479, 893698);
    }

    public static void run(int start, int stop) {
        System.out.println(String.format("Range start: %s", start));
        System.out.println(String.format("Range stop:  %s", stop));
        Pattern containsDoubleDigit = Pattern
            .compile("(\\d)\\1");
        Pattern isAscendingOrder = Pattern
            .compile("^0*1*2*3*4*5*6*7*8*9*$");

        // Part 1
        long partOneMatches = IntStream.range(start, stop + 1)
            .mapToObj(Integer::toString)
            .filter(x -> isAscendingOrder.matcher(x).find())
            .filter(x -> containsDoubleDigit.matcher(x).find())
            .count();
        System.out.println(String.format("Part 1: %s", partOneMatches));

        // Part 2
        long partTwoMatches = IntStream.range(start, stop + 1)
            .mapToObj(Integer::toString)
            .filter(x -> isAscendingOrder.matcher(x).find())
            .map(x -> x.replaceAll("(\\d)\\1\\1+", ""))
            .filter(x -> containsDoubleDigit.matcher(x).find())
            .count();
        System.out.println(String.format("Part 2: %s", partTwoMatches));
    }
}
