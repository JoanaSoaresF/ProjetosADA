import java.util.LinkedList;
import java.util.List;

public class Lost {
    public final static int GRASS = 0;
    public final static int OBSTACLE = 1;
    public final static int WATER = 2;
    public final static int MAGIC_WHEEL = 3;
    public final static int EXIT = 4;
    private final static int WATER_COST = 2;
    private final static int GRASS_COST = 1;
    private List<Integer[]> paths;

    public Lost() {
        this.paths = new LinkedList<>();
    }

    public void addIslandPosition(int x, int y, int type) {

    }

    public void addMagicWheel(int x, int y, int cost) {

    }

    public int solution(int originX, int originY) {
        return 0;
    }
}
