import java.util.*;

public class Lost {
    public final static int GRASS = 0;
    public final static int OBSTACLE = 1;
    public final static int WATER = 2;
    public final static int MAGIC_WHEEL = 3;
    public final static int EXIT = 4;
    public static final int UNREACHABLE = Integer.MAX_VALUE;
    public static final int LOST_IN_TIME = Integer.MIN_VALUE;
    public static final int CAN_SWIM = 0;
    public static final int CAN_USE_WHEEL = 1;
    private final static int WATER_COST = 2;
    private final static int GRASS_COST = 1;
    private static final int START_PLACE = 0;
    private static final int END_PLACE = 1;
    private static final int PLACE_COST = 2;
    private final int[][] island;
    private final List<int[]> normalPaths;
    private final List<int[]> waterPaths;
    private final List<int[]> magicWheelPaths;
    private final int numRows;
    private final int numCols;
    private final Map<Integer, Integer> magicWheels;
    private final Map<Integer, Integer> places;
    private int numPlaces;
    private int EXIT_POS;
    private int numMagicWheels;

    public Lost(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.normalPaths = new LinkedList<>();
        island = new int[numRows][numCols];
        magicWheels = new HashMap<>();
        places = new HashMap<>(numRows * numCols);
        numMagicWheels = 0;
        numPlaces = 0;
        waterPaths = new LinkedList<>();
        magicWheelPaths = new LinkedList<>();
    }

    private int[] intToPos(int pos) {
        int x = pos / 100;
        int y = pos % 100;
        return new int[]{x, y};
    }

    public void addIslandPosition(int x, int y, int type) {
        island[y][x] = type;
        int start = posToInt(x, y);
        if (type == EXIT) {
            EXIT_POS = numPlaces;
        }
        places.put(start, numPlaces++);
        if (y > 0&& type != OBSTACLE) { //has up
            int up = island[y-1][x];
            int upPos = posToInt(x, y - 1);
            addPaths(type, start, up, upPos);
        }
        if (x > 0 && type != OBSTACLE) { //has left
            int left = island[y][x-1];
            int leftPos = posToInt(x - 1, y);
            addPaths(type, start, left, leftPos);
        }

        if (type == MAGIC_WHEEL) {
            magicWheels.put(numMagicWheels++, start);
        }
    }

    private void addPaths(int type, int start, int end, int leftPos) {
        int[] edgeFrom = new int[]{start, leftPos, cost(type)};
        int[] edgeTo = new int[]{leftPos, start, cost(end)};
        if(type == WATER || end == WATER){
            waterPaths.add(edgeFrom);
            waterPaths.add(edgeTo);
        } else if (end != OBSTACLE) {
            //if is not an obstacle add path to left, bidirecional
            normalPaths.add(edgeFrom);
            normalPaths.add(edgeTo);
        }
    }

    private int posToInt(int x, int y) {
        return x * 100 + y;
    }

    private int cost(int type) {
        return type == GRASS || type == MAGIC_WHEEL ? GRASS_COST : WATER_COST;
    }

    public void addMagicWheel(int i, int x, int y, int cost) {
        int start = magicWheels.get(i);
        int end = posToInt(x, y);
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
        if(playerType[CAN_SWIM]) {
            changes = updateLengthsInSubPaths(lengths, waterPaths);
        }
        if(playerType[CAN_USE_WHEEL]){
            changes = updateLengthsInSubPaths(lengths, magicWheelPaths);
        }
        return changes;
    }

    private boolean updateLengthsInSubPaths(int[] lengths, List<int[]> paths) {
        boolean changes = false;
        for (int[] path : paths) {
            int tail = places.get(path[START_PLACE]);
            int head = places.get(path[END_PLACE]);
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
