import java.util.*;

public class Lost {
    //Types of places in the island
    public final static int GRASS = 0;
    public final static int OBSTACLE = 1;
    public final static int WATER = 2;
    public final static int MAGIC_WHEEL = 3;
    public final static int EXIT = 4;
    //Paths
    public static final int UNREACHABLE = Integer.MAX_VALUE;
    public static final int LOST_IN_TIME = Integer.MIN_VALUE;
    //Type of players
    public static final int CAN_SWIM = 0;
    public static final int CAN_USE_WHEEL = 1;
    //Cost of paths from cells in island
    private final static int WATER_COST = 2;
    private final static int GRASS_COST = 1;
    //Paths structure
    private static final int START_PLACE = 0;
    private static final int END_PLACE = 1;
    private static final int PLACE_COST = 2;
    /**
     * Types of cells in the island positions
     */
    private final int[][] island;
    /**
     * Paths from to a Grass cells
     */
    private final List<int[]> normalPaths;
    /**
     * Paths to and from a water cell
     */
    private final List<int[]> waterPaths;
    /**
     * paths from the magic wheel
     */
    private final List<int[]> magicWheelPaths;

    /**
     * magic wheels key is the number presented and value is the magic wheel codification position
     */
    private final Map<Integer, Integer> magicWheels;
    /**
     * all cells of the island. key is the (x,y) value and the value is the codification position
     * (from 0 to numRows*numCols-1, the total number of cells
     */
    private final Map<Integer, Integer> places;
    private final int numRows;
    private final int numCols;
    private int numPlaces;
    /**
     * Codification of the exit cell
     */
    private int EXIT_POS;

    public Lost(int numRows, int numCols, int numMagicWheels) {
        this.numRows = numRows;
        this.numCols = numCols;

        normalPaths = new LinkedList<>();
        waterPaths = new LinkedList<>();
        magicWheelPaths = new LinkedList<>();

        island = new int[numRows][numCols];
        magicWheels = new HashMap<>(numMagicWheels);
        places = new HashMap<>(numRows * numCols);
        numPlaces = 0;

    }

    private int[] intToPos(int pos) {
        int x = pos / 100;
        int y = pos % 100;
        return new int[]{x, y};
    }

    public void addIslandPosition(int x, int y, int type, int magicWheel) {
        island[y][x] = type;
        int start = posToInt(x, y);
        int v = numPlaces++;
        places.put(start, v);
        if (y > 0 && type != OBSTACLE) { //has up
            int up = island[y - 1][x];
            int upPos = places.get(posToInt(x, y - 1));
            addPaths(type, v, up, upPos);
        }
        if (x > 0 && type != OBSTACLE) { //has left
            int left = island[y][x - 1];
            int leftPos = places.get(posToInt(x - 1, y));
            addPaths(type, v, left, leftPos);
        }

        if (type == MAGIC_WHEEL) {
            magicWheels.put(magicWheel, v);
        }
        if (type == EXIT) {
            EXIT_POS = v;
        }
    }

    private int posToInt(int x, int y) {
        return x * 100 + y;
    }

    private void addPaths(int startType, int startPos, int endType, int endPos) {
        int[] edgeFrom = new int[]{startPos, endPos, cost(startType)};
        int[] edgeTo = new int[]{endPos, startPos, cost(endType)};

        if (startType == WATER || endType == WATER) {
            //is is a path connected to a water slot
            if (startType != EXIT) {
                waterPaths.add(edgeFrom);
            }
            if (endType != EXIT) {
                waterPaths.add(edgeTo);
            }
        } else if (endType != OBSTACLE) {
            //if is not an obstacle add path to left, bidirecional
            if (startType != EXIT) {
                normalPaths.add(edgeFrom);
            }
            if (endType != EXIT) {
                normalPaths.add(edgeTo);
            }
        }
    }

    private int cost(int type) {
        return type == GRASS || type == MAGIC_WHEEL ? GRASS_COST : WATER_COST;
    }

    public void addMagicWheel(int i, int x, int y, int cost) {
        int start = magicWheels.get(i);
        int end = places.get(posToInt(x, y));
        int[] edge = new int[]{start, end, cost};
        magicWheelPaths.add(edge);

    }

    public int solution(int originX, int originY, boolean[] playerType) {
        int[] lengths = new int[numRows * numCols];

        Arrays.fill(lengths, UNREACHABLE);

        int origin = places.get(posToInt(originX, originY));
        lengths[origin] = 0;
        boolean changes = false;
        for (int i = 1; i < lengths.length; i++) {
            changes = updateLength(lengths, playerType);
            if (!changes) {
                break;
            }
        }

        //Detect negative-weight cycles
        if (changes && updateLength(lengths, playerType)) {
            lengths[EXIT_POS] = LOST_IN_TIME;
        }


        return lengths[EXIT_POS];
    }

    private boolean updateLength(int[] lengths, boolean[] playerType) {
        boolean changes = updateLengthsInSubPaths(lengths, normalPaths);
        if (playerType[CAN_SWIM]) {
            changes = updateLengthsInSubPaths(lengths, waterPaths);
        }
        if (playerType[CAN_USE_WHEEL] && magicWheels.size() > 0) {
            changes = updateLengthsInSubPaths(lengths, magicWheelPaths);
        }
        return changes;
    }

    private boolean updateLengthsInSubPaths(int[] lengths, List<int[]> paths) {
        boolean changes = false;
        for (int[] path : paths) {
            int tail = path[START_PLACE];
            int head = path[END_PLACE];
            int cost = path[PLACE_COST];
            if (lengths[tail] < Integer.MAX_VALUE) {
                int newCost = lengths[tail] + cost;
                if (newCost < lengths[head]) {
                    lengths[head] = newCost;
                    //continue cycle because there are changes in the length vector
                    changes = true;
                }
            }
        }
        return changes;
    }
}
