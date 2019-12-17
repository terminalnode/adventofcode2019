import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;


public class Robot {
    private IntcodeComputer brain;
    private final Queue<Long> brainOutput;
    private final Queue<Long> brainInput;
    private final Map<Coordinate, CoordinateType> map;
    private final Set<Coordinate> unvisitedSet;
    private Coordinate currentPosition;
    private Coordinate oxygen;

    private class Direction {
        public static final long NORTH = 1;
        public static final long SOUTH = 2;
        public static final long WEST = 3;
        public static final long EAST = 4;
    }

    private class Status {
        public static final int HIT_WALL = 0;
        public static final int MOVED = 1;
        public static final int OXYGEN_SYSTEM = 2;
    }

    private class Sprites {
        public static final String UNKNOWN = "░";
        public static final String EMPTY = " ";
        public static final String WALL = "█";
        public static final String ORIGIN = "◈";
        public static final String OXYGEN = "●";
    }

    public Robot(long[] application) {
        brain = new IntcodeComputer(application);

        brain.reinitialize();
        brainOutput = brain.getOutput();
        brainInput = new LinkedList<>();
        brain.setInput(brainInput);

        map = new HashMap<>();
        unvisitedSet = new HashSet<>();

        currentPosition = new Coordinate(0,0);
        map.put(currentPosition, CoordinateType.ORIGIN);
        addNeighbours(currentPosition);

        oxygen = null;
    }

    public void move(long direction) {
        /* INTCODE PROGRAM SPECIFICATION
         * 1. Reads movement instruction as input (1, 2, 3 or 4).
         * 2. Outputs status code (0, 1 or 2)
         * */
        brainInput.add(direction);
        Coordinate endPosition = null;

        if (direction == Direction.NORTH) {
            endPosition = currentPosition.getNorth();
        } else if (direction == Direction.SOUTH) {
            endPosition = currentPosition.getSouth();
        } else if (direction == Direction.EAST) {
            endPosition = currentPosition.getEast();
        } else if (direction == Direction.WEST) {
            endPosition = currentPosition.getWest();
        }

        brain.run();
        unvisitedSet.remove(endPosition);
        long status = brainOutput.poll();
        switch ((int) status) {
            case Status.HIT_WALL:
                map.put(endPosition, CoordinateType.WALL);
                break;
            case Status.MOVED:
                map.put(endPosition, CoordinateType.EMPTY);
                currentPosition = endPosition;
                addNeighbours(endPosition);
                break;
            case Status.OXYGEN_SYSTEM:
                map.put(endPosition, CoordinateType.OXYGEN);
                currentPosition = endPosition;
                addNeighbours(endPosition);
                oxygen = endPosition;
                break;
            default:
                throw new IllegalArgumentException("Robot returned invalid status response.");
        }
    }

    public void exploreMap() {
        while (!unvisitedSet.isEmpty()) {
            Coordinate goal = unvisitedSet.iterator().next();
            List<Long> instructions = findShortestPath(goal);
            for (Long l : instructions) {
                move(l);
            }
        }
        map.put(new Coordinate(0,0), CoordinateType.ORIGIN);
    }

    public int gasMap() {
        if (oxygen == null) {
            exploreMap();
        }

        Set<Coordinate> remainingCoordinates = map.keySet()
            .stream()
            .filter(x -> map.get(x).equals(CoordinateType.EMPTY))
            .collect(Collectors.toSet());
        List<Coordinate> activeCoordinates = new ArrayList<>();
        activeCoordinates.add(oxygen);

        int steps = 0;
        while (!remainingCoordinates.isEmpty()) {
            List<Coordinate> newActiveCoordinates = new ArrayList<>();
            steps++;
            for (Coordinate coordinate : activeCoordinates) {
                for (Coordinate neighbour : coordinate.getNeighbours()) {
                    if (remainingCoordinates.contains(neighbour)) {
                        remainingCoordinates.remove(neighbour);
                        newActiveCoordinates.add(neighbour);
                    }
                }
            }
            activeCoordinates = newActiveCoordinates;
        }
        return steps;
    }

    public int pathLengthFromOriginToOxygen() {
        if (oxygen == null) {
            exploreMap();
        }
        return findShortestPath(new Coordinate(0, 0), oxygen).size();
    }

    private List<Long> findShortestPath(Coordinate goal) {
        return findShortestPath(currentPosition, goal);
    }

    private List<Long> findShortestPath(Coordinate start, Coordinate goal) {
        Map<Coordinate, List<Long>> walkInstructions = new HashMap<>();
        walkInstructions.put(start, new ArrayList<>());

        Set<Coordinate> unvisitedSet = map.keySet()
            .stream()
            .filter(x -> !map.get(x).equals(CoordinateType.WALL)).collect(Collectors.toSet());
        unvisitedSet.remove(start);

        boolean found = false;
        List<Long> bestList = null;

        while (!found) {
            Map<Coordinate, List<Long>> newInstructions = new HashMap<>();

            for (Coordinate c : walkInstructions.keySet()) {
                List<Long> cInstructions = walkInstructions.get(c);
                Coordinate cNorth = c.getNorth();
                Coordinate cSouth = c.getSouth();
                Coordinate cWest = c.getWest();
                Coordinate cEast = c.getEast();
                List<Long> newInstructionList;

                if (unvisitedSet.contains(cNorth)) {
                    unvisitedSet.remove(cNorth);
                    newInstructionList = new ArrayList<>(cInstructions);
                    newInstructionList.add(Direction.NORTH);
                    newInstructions.put(cNorth, newInstructionList);
                    if (cNorth.equals(goal)) {
                        found = true;
                        bestList = newInstructionList;
                        break;
                    }
                }

                if (unvisitedSet.contains(cSouth)) {
                    unvisitedSet.remove(cSouth);
                    newInstructionList = new ArrayList<>(cInstructions);
                    newInstructionList.add(Direction.SOUTH);
                    newInstructions.put(cSouth, newInstructionList);
                    if (cSouth.equals(goal)) {
                        found = true;
                        bestList = newInstructionList;
                        break;
                    }
                }

                if (unvisitedSet.contains(cWest)) {
                    unvisitedSet.remove(cWest);
                    newInstructionList = new ArrayList<>(cInstructions);
                    newInstructionList.add(Direction.WEST);
                    newInstructions.put(cWest, newInstructionList);
                    if (cWest.equals(goal)) {
                        found = true;
                        bestList = newInstructionList;
                        break;
                    }
                }

                if (unvisitedSet.contains(cEast)) {
                    unvisitedSet.remove(cEast);
                    newInstructionList = new ArrayList<>(cInstructions);
                    newInstructionList.add(Direction.EAST);
                    newInstructions.put(cEast, newInstructionList);
                    if (cEast.equals(goal)) {
                        found = true;
                        bestList = newInstructionList;
                        break;
                    }
                }
            }
            walkInstructions = newInstructions;
        }
        return bestList;
    }

    public void printMap() {
        Set<Coordinate> allCoordinates = map.keySet();
        int[] allX = map.keySet()
            .stream()
            .mapToInt(x -> x.getX())
            .toArray();
        int[] allY = map.keySet()
            .stream()
            .mapToInt(y -> y.getY())
            .toArray();

        int maxX = Arrays.stream(allX).max().getAsInt();
        int minX = Arrays.stream(allX).min().getAsInt();
        int maxY = Arrays.stream(allY).max().getAsInt();
        int minY = Arrays.stream(allY).min().getAsInt();

        String[][] graphicMap = new String[maxY - minY + 1][maxX - minX + 1];

        for (int y = minY; y <= maxY; y++) {
            int mapY = y - minY;

            for (int x = minX; x <= maxX; x++) {
                int mapX = x - minX;

                CoordinateType type = map.get(new Coordinate(x,y));
                if (type == null) {
                    graphicMap[mapY][mapX] = Sprites.UNKNOWN;
                    continue;
                }

                switch (type) {
                    case EMPTY: graphicMap[mapY][mapX] = Sprites.EMPTY; break;
                    case WALL: graphicMap[mapY][mapX] = Sprites.WALL; break;
                    case UNKNOWN: graphicMap[mapY][mapX] = Sprites.UNKNOWN; break;
                    case ORIGIN: graphicMap[mapY][mapX] = Sprites.ORIGIN; break;
                    case OXYGEN: graphicMap[mapY][mapX] = Sprites.OXYGEN; break;
                    default: graphicMap[mapY][mapX] = Sprites.UNKNOWN;
                }
            }
        }

        for (String[] row : graphicMap) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }

    }

    private void addNeighbours(Coordinate coordinate) {
        addToUnvisitedSet(coordinate.getNorth());
        addToUnvisitedSet(coordinate.getSouth());
        addToUnvisitedSet(coordinate.getWest());
        addToUnvisitedSet(coordinate.getEast());
    }

    private void addToUnvisitedSet(Coordinate coordinate) {
        if (!map.containsKey(coordinate)) {
            unvisitedSet.add(coordinate);
            map.put(coordinate, CoordinateType.UNKNOWN);
        }
    }

    public Map<Coordinate, CoordinateType> getMap() {
        return map;
    }
}
