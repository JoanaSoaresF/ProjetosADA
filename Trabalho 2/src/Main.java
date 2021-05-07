/*
 * Ada Trabalho 2 - Legionellosis
 *
 * @author Joana Soares Faria n55754
 * @author Goncalo Martins Lourenco n55780
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String[] inputLine = input.readLine().split(" ");
        int numLocations = Integer.parseInt(inputLine[0]);
        int numConnections = Integer.parseInt(inputLine[1]);


        Legionellosis problem = new Legionellosis(numLocations);
        for (int i = 0; i < numConnections; i++) {
            String[] connection = input.readLine().split(" ");
            int l1 = Integer.parseInt(connection[0]);
            int l2 = Integer.parseInt(connection[1]);
            problem.addConnection(l1, l2);
        }

        int numSick = Integer.parseInt(input.readLine());
        for (int i = 0; i < numSick; i++) {
            String[] sick = input.readLine().split(" ");
            int home = Integer.parseInt(sick[0]);
            int distance = Integer.parseInt(sick[1]);
            problem.addSick(home, distance);
        }

        int[] perilousLoc = problem.perilousLocations();

        for (int loc : perilousLoc) {
            System.out.printf("%d ", loc);
        }
        System.out.println();

    }
}
