import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int numTestCases = Integer.parseInt(input.readLine());
        for (int i = 0; i < numTestCases; i++) {
            Lost problem = processProblem(input);
            String[] playersPositions = input.readLine().split(" ");
            int xJ = Integer.parseInt(playersPositions[0]);
            int yJ = Integer.parseInt(playersPositions[1]);
            int xK = Integer.parseInt(playersPositions[2]);
            int yK = Integer.parseInt(playersPositions[3]);
            int solutionJohn = problem.solution(xJ, yJ, new boolean[]{false, true});
            int solutionKate = problem.solution(xK, yK, new boolean[]{true, false});
            System.out.println("Case #" + (i+1));
            String john = solutionString(solutionJohn);
            String kate = solutionString(solutionKate);
            System.out.println("John " + john);
            System.out.println("Kate " + kate);
        }


    }

    private static Lost processProblem(BufferedReader input) throws IOException {
        String[] problemInfo = input.readLine().split(" ");
        int numRows = Integer.parseInt(problemInfo[0]);
        int numCols = Integer.parseInt(problemInfo[1]);
        int numMagicWheels = Integer.parseInt(problemInfo[2]);
        Lost problem = new Lost(numRows, numCols);
        for (int y = 0; y < numRows; y++) {
            String row = input.readLine();
            for (int x = 0; x < numCols; x++) {
                int type = positionType(row.charAt(x));
                problem.addIslandPosition(x, y, type);
            }
        }
        for (int i = 0; i < numMagicWheels; i++) {
            String[] magicWheel = input.readLine().split(" ");
            int x = Integer.parseInt(magicWheel[0]);
            int y = Integer.parseInt(magicWheel[1]);
            int cost = Integer.parseInt(magicWheel[2]);
            problem.addMagicWheel(i, x, y, cost);
        }

        return problem;
    }

    private static String solutionString(int solutionJohn) {
        String string;
        if (solutionJohn == Lost.UNREACHABLE) {
            string = "Unreachable";

        } else if (solutionJohn == Lost.LOST_IN_TIME) {
            string = "Lost in Time";
        } else {
            string = String.valueOf(solutionJohn);
        }
        return string;
    }

    private static int positionType(char charAt) {
        int type;
        if (charAt == 'G') {
            type = Lost.GRASS;
        } else if (charAt == 'O') {
            type = Lost.OBSTACLE;
        } else if (charAt == 'W') {
            type = Lost.WATER;
        } else if (charAt == 'X') {
            type = Lost.EXIT;
        } else {
            type = Lost.MAGIC_WHEEL;
        }
        return type;
    }
}
