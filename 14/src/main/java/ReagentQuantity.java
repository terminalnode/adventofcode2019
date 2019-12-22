class ReagentQuantity {
    private final String name;
    private final int quantity;

    public ReagentQuantity(String formula) {
        quantity = Integer.parseInt(formula.split(" ")[0]);
        name = formula.split(" ")[1];
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("%s %s", quantity, name);
    }
}
