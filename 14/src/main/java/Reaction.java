import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Reaction {
    private final ReagentQuantity endProduct;
    private final List<ReagentQuantity> reagents;

    public Reaction(String formula) {
        endProduct = new ReagentQuantity(formula.replaceAll(".*=> ", ""));
        reagents = Arrays
            .stream(formula.replaceAll(" =>.*", "").split(", "))
            .map(x -> new ReagentQuantity(x))
            .collect(Collectors.toList());
    }

    public ReagentQuantity getEndProduct() {
        return endProduct;
    }

    public List<ReagentQuantity> getReagents() {
        return reagents;
    }

    @Override
    public String toString() {
        List<String> reagentStrings = reagents.stream()
            .map(x -> x.toString())
            .collect(Collectors.toList());
        String leftSide = String.join(", ", reagentStrings);

        return String.format("%s => %s", leftSide, endProduct.toString());
    }
}
