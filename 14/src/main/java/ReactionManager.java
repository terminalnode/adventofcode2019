import java.util.Map;
import java.util.stream.Collectors;

class ReactionManager {
    private final Map<String, Reaction> reactionMap;
    private final Map<String, Long> inventory;
    private long oreConsumed;

    public ReactionManager(Map<String, Reaction> reactions) {
        reactionMap = reactions;
        oreConsumed = 0;
        inventory = reactionMap
            .keySet()
            .stream()
            .collect(Collectors.toMap(x -> x, x -> 0L));
    }

    public void produce(String reagent, long quantity) {
        Reaction desiredReaction = reactionMap.get(reagent);
        ReagentQuantity endProduct = desiredReaction.getEndProduct();

        long multiplier = 1;
        while (endProduct.getQuantity() * multiplier < quantity) {
            multiplier++;
        }

        for (ReagentQuantity rq : desiredReaction.getReagents()) {
            long required = multiplier * rq.getQuantity();

            if (rq.getName().equals("ORE")) {
                oreConsumed += rq.getQuantity() * multiplier;

            } else {
                while (inventory.get(rq.getName()) < required) {
                    produce(rq.getName(), required - inventory.get(rq.getName()));
                }

            long newInventorySize = inventory.get(rq.getName()) - required;
            inventory.put(rq.getName(), newInventorySize);
            }
        }

        long newInventorySize = inventory.get(reagent) + endProduct.getQuantity() * multiplier;
        inventory.put(reagent, newInventorySize);
    }

    public void reset() {
        oreConsumed = 0;
    }

    public long getOreConsumed() {
        return oreConsumed;
    }
}
