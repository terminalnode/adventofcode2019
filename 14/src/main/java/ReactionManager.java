import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;

class ReactionManager {
    private final Map<String, Reaction> reactionMap;
    private Map<String, Long> inventory;
    private BigInteger oreConsumed;

    public ReactionManager(Map<String, Reaction> reactions) {
        reactionMap = reactions;
        oreConsumed = BigInteger.valueOf(0L);
        inventory = reactionMap
            .keySet()
            .stream()
            .collect(Collectors.toMap(x -> x, x -> 0L));
    }

    public long binarySearch() {
        BigInteger trillion = BigInteger.valueOf(1000000000000L);
        long maxValue = 1000000L;
        long minValue = 0L;
        long testValue = 0L;

        produce(maxValue);
        while (true) {
            produce(maxValue);
            maxValue *= 2;

            if (oreConsumed.compareTo(trillion) < 0) {
                minValue = maxValue;
            } else {
                break;
            }
        }

        System.out.println("stage two");
        while (maxValue - minValue > 1000000L) {
            reset();
            testValue = (maxValue - minValue) / 2 + minValue;
            produce(testValue);

            int comparison = oreConsumed.compareTo(trillion);
            if (comparison < 0) {
                // too low
                minValue = testValue;
            } else if (comparison > 0) {
                // too high
                maxValue = testValue;
            } else {
                // just right
                break;
            }

            // So we can see that it's not dead...
            System.out.println("Max value: " + maxValue);
            System.out.println("Min value: " + minValue);
        }

        if (oreConsumed.compareTo(trillion) > 0) {
            reset();
            produce(minValue);
        }

        while (oreConsumed.compareTo(trillion) < 0) {
            produce(1);
        }

        return inventory.get("FUEL") - 1;
    }

    public void produce(long quantity) {
        produce("FUEL", quantity);
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
                oreConsumed = oreConsumed.add(BigInteger.valueOf(rq.getQuantity() * multiplier));

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
        oreConsumed = BigInteger.valueOf(0L);
        inventory = reactionMap
            .keySet()
            .stream()
            .collect(Collectors.toMap(x -> x, x -> 0L));
    }

    public BigInteger getOreConsumed() {
        return oreConsumed;
    }
}
