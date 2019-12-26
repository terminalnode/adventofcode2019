import java.util.ArrayList;
import java.util.List;

public class LivingMap {
    private final String[] map;
    private int x, y;
    private int maxX, maxY;
    private char direction;

    public LivingMap(String map) {
        this.map = map.split("\n");
        x = -1;
        y = -1;
        direction = '?';
        maxY = this.map.length - 1;
        maxX = this.map[0].length() - 1;

        for (int y = 0; y < this.map.length; y++) {
            for (int x = 0; x < this.map[y].length(); x++) {
                switch (this.map[y].charAt(x)) {
                    case '.': break;
                    case '#': break;
                    case Direction.UP:
                        this.x = x;
                        this.y = y;
                        direction = Direction.UP;
                        break;
                    case Direction.DOWN:
                        this.x = x;
                        this.y = y;
                        direction = Direction.DOWN;
                        break;
                    case Direction.RIGHT:
                        this.x = x;
                        this.y = y;
                        direction = Direction.RIGHT;
                        break;
                    case Direction.LEFT:
                        this.x = x;
                        this.y = y;
                        direction = Direction.LEFT;
                        break;
                    default: break;
                }

                // Break x-loop if location is found
                if (this.x != -1 && this.y != -1 && direction != '?') {
                    break;
                }
            }
            // Break y-loop if location is found
            if (this.x != -1 && this.y != -1 && direction != '?') {
                break;
            }
        }
    }

    public List<String> autoMove() {
        List<String> result = new ArrayList<>();
        char mostRecentTurn = autoTurn();

        do {
            result.add(Character.toString(mostRecentTurn));
            result.add(Integer.toString(runToEdge()));

            mostRecentTurn = autoTurn();
        } while (mostRecentTurn != 'X');

        return result;
    }

    private char autoTurn() {
        char leftTile = getTileLeft();
        char rightTile = getTileRight();
        char aboveTile = getTileAbove();
        char belowTile = getTileBelow();

        if (direction == Direction.UP || direction == Direction.DOWN) {
            if (leftTile == '#') {
                return makeFaceLeft();
            }

            if (rightTile == '#') {
                return makeFaceRight();
            }

        } else if (direction == Direction.RIGHT || direction == Direction.LEFT) {
            if (aboveTile == '#') {
                return makeFaceUp();
            }

            if (belowTile == '#') {
                return makeFaceDown();
            }
        }

        return 'X';
    }

    private int runToEdge() {
        int stepsTaken = 0;

        while (nextTileIsTraversable()) {
            stepsTaken++;
            moveForward();
        }

        //System.out.printf("Took %s steps (%s,%s)\n", stepsTaken, x, y);
        return stepsTaken;
    }

    private void moveForward() {
        switch (direction) {
            case Direction.UP:    y--; break;
            case Direction.DOWN:  y++; break;
            case Direction.RIGHT: x++; break;
            case Direction.LEFT:  x--; break;
            default: throw new UnsupportedOperationException(String.format("Invalid direction: %s", direction));
        }
    }

    private boolean nextTileIsTraversable() {
        switch (direction) {
            case Direction.UP:
                //System.out.println("nextTileIsTraversable(): Direction is UP");
                if (y == 0) {
                    return false;
                } else {
                    return map[y - 1].charAt(x) == '#';
                }
            case Direction.DOWN:
                //System.out.println("nextTileIsTraversable(): Direction is DOWN");
                if (y >= maxY) {
                    return false;
                } else {
                    return map[y + 1].charAt(x) == '#';
                }
            case Direction.RIGHT:
                //System.out.println("nextTileIsTraversable(): Direction is RIGHT");
                if (x >= maxX) {
                    return false;
                } else {
                    return map[y].charAt(x + 1) == '#';
                }
            case Direction.LEFT:
                //System.out.println("nextTileIsTraversable(): Direction is LEFT");
                if (x == 0) {
                    return false;
                } else {
                    return map[y].charAt(x - 1) == '#';
                }
            default:
                throw new UnsupportedOperationException(String.format("Invalid direction: %s", direction));
        }
    }

    private char getTileAbove() {
        return getTile(x, y - 1);
    }

    private char getTileBelow() {
        return getTile(x, y + 1);
    }

    private char getTileLeft() {
        return getTile(x - 1, y);
    }

    private char getTileRight() {
        return getTile(x + 1, y);
    }

    private char getTile(int x, int y) {
        if (x < 0 || y < 0 || x > maxX || y > maxY) {
            return '.';
        } else {
            return map[y].charAt(x);
        }

    }

    private char makeFaceUp() {
        switch (direction) {
            case Direction.RIGHT:
                direction = Direction.UP;
                return 'L';
            case Direction.LEFT:
                direction = Direction.UP;
                return 'R';
            default:
                throw new UnsupportedOperationException(String.format("Can not face UP from direction: %s", direction));
        }
    }

    private char makeFaceDown() {
        switch (direction) {
            case Direction.RIGHT:
                direction = Direction.DOWN;
                return 'R';
            case Direction.LEFT:
                direction = Direction.DOWN;
                return 'L';
            default:
                throw new UnsupportedOperationException(String.format("Can not face DOWN from direction: %s", direction));
        }
    }

    private char makeFaceRight() {
        switch (direction) {
            case Direction.UP:
                direction = Direction.RIGHT;
                return 'R';
            case Direction.DOWN:
                direction = Direction.RIGHT;
                return 'L';
            default:
                throw new UnsupportedOperationException(String.format("Can not face RIGHT from direction: %s", direction));
        }
    }

    private char makeFaceLeft() {
        switch (direction) {
            case Direction.UP:
                direction = Direction.LEFT;
                return 'L';
            case Direction.DOWN:
                direction = Direction.LEFT;
                return 'R';
            default:
                throw new UnsupportedOperationException(String.format("Can not face LEFT from direction: %s", direction));
        }
    }

    private class Direction {
        public static final char UP = '^';
        public static final char DOWN = 'v';
        public static final char RIGHT = '>';
        public static final char LEFT = '<';
    }

}
