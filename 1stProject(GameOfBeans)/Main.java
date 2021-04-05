import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) throws IOException {

        //Tests, Piles, Depth
        //int T, P, D;
        GameOfBeans game;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        int numTests = Integer.parseInt(in.readLine());

        for (int i = 0; i < numTests; i++) {
            //Get P and D
            String[] P_D = in.readLine().split(" ");
            int numPiles = Integer.parseInt(P_D[0]);
            int depth = Integer.parseInt(P_D[1]);

            String[] piles = in.readLine().split(" ");
            int[] aux = new int[numPiles];
            for (int j = 0; j < numPiles; j++) {
                aux[j] = Integer.parseInt(piles[j]);
            }
            String player = in.readLine();
            game = new GameOfBeans(depth, aux, player);
            int solution = game.bestJabaScore();
            System.out.println(solution);

        }


    }


}