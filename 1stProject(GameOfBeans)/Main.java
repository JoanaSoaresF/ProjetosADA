import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) throws IOException{

        //Tests, Piles, Depth
        //int T, P, D;
        GameOfBeans gb = new GameOfBeans();

        BufferedReader in = new BufferedReader( new InputStreamReader(System.in));

        int T = Integer.parseInt(in.readLine());

        for(int i = 0; i < T; i++){
            //Get P and D
            String[] P_D = in.readLine().split(" ");
            int P = Integer.parseInt(P_D[0]);
            int D = Integer.parseInt(P_D[1]);

            String[] Piles = in.readLine().split(" ");
            int[] aux = new int[P];

            for(int j = 0; j < P; j++){
                aux[j] = Integer.parseInt(Piles[j]);
            }



        }




    }









}