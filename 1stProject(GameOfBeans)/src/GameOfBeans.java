/**
 * Ada Trabalho 1 - Game of beans
 *
 * @author Joana Soares Faria n55754
 * @author Goncalo Martins Lourenco n55780
 */

public class GameOfBeans {

    private static final int JABA = 0;
    private static final int PIETON = 1;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private final int[][][] bestScores;
    private final int depth;
    private final int[] piles;
    private final String firstPlayer;


    public GameOfBeans(int depth, int[] piles, String firstPlayer) {
        this.depth = depth;
        this.piles = piles;
        this.firstPlayer = firstPlayer;
        bestScores = new int[piles.length + 1][piles.length + 1][2];
    }

    /**
     * Computes  Pieton's play.
     *
     * @param i left index(-1) of the pile
     * @param j right index(-1) of the pile
     * @return the number of piles removed from which side
     */
    private int[] pietonPlay(int i, int j) {
        int maxScore = Integer.MIN_VALUE;
        int[] answer = {0, 0};

        //left play
        for (int k = 1; k <= depth && (i + k <= j + 1); k++) { // k is the number of piles to remove
            int sum = 0;

            for (int c = 0; c < k; c++) { // c is a counter to sum all the piles to remove
                sum += piles[i + c - 1];
            }

            if (sum > maxScore) {
                maxScore = sum;
                answer = new int[]{k, 0};
            }
        }

        //right play
        for (int k = 1; k <= depth && (j - k + 1 >= i); k++) {
            int sum = 0;

            for (int c = 0; c < k; c++) {
                sum += piles[j - c - 1];
            }

            if (sum > maxScore) {
                maxScore = sum;
                answer = new int[]{0, k};
            }
        }

        return answer;
    }


    /**
     * Computes the Jabas's score from removing k piles from the piles i to j
     * k>0 means we should augment i, k<0 means we should move j backwards
     *
     * @param k piles to remove
     * @param i left index(-1) of the pile
     * @param j right index(-1) of the pile
     * @return the score from removing k piles
     */
    private int score(int k, int i, int j) {
        int score = 0;

        if (k < 0) {
            for (int counter = 0; counter < -k; counter++) {
                score += piles[j - 1 - counter];
            }
        } else {
            for (int counter = 0; counter < k; counter++) {
                score += piles[i - 1 + counter];
            }
        }

        return score;
    }

    /**
     * Computes the max score for Jaba, solves the problem
     *
     * @return the maximum Jaba score
     */
    public int bestJabaScore() {
        int player = firstPlayer.equalsIgnoreCase("jaba") ? JABA : PIETON;
        //base cases = only one pile left (i=j)
        for (int i = 1; i <= piles.length; i++) {
            bestScores[i][i][PIETON] = 0;
            bestScores[i][i][JABA] = piles[i - 1];
        }

        //general case. P is the difference of indices of piles
        for (int p = 1; p < piles.length; p++) {
            for (int i = 1; i <= piles.length - p; i++) { //i is the left index
                int j = i + p; //j is the right index
                int maxScoreJaba = Integer.MIN_VALUE;
                int maxToRemove = Integer.min(depth, p + 1);

                //it is Jaba 's turn to play
                for (int k = 1; k <= maxToRemove; k++) { //k is the number of piles to remove
                    //remove from left
                    int scoreLeft = score(k, i, j);
                    if (k + i <= piles.length)// does not empty the piles
                        scoreLeft += bestScores[i + k][j][PIETON];
                    maxScoreJaba = Integer.max(scoreLeft, maxScoreJaba);
                    //remove from right
                    int scoreRight = score(-k, i, j);
                    if (j - k > 0)// does not empty the piles
                        scoreRight += bestScores[i][j - k][PIETON];
                    maxScoreJaba = Integer.max(scoreRight, maxScoreJaba);
                }

                bestScores[i][j][JABA] = maxScoreJaba;

                //it is Pieton 's turn to play
                int[] play = pietonPlay(i, j);
                int maxScorePieton = 0; //Jaba's best score if is Pieton's play
                //does not empty the piles
                if (play[LEFT] + i <= piles.length && j - play[RIGHT] > 0) {
                    maxScorePieton = bestScores[i + play[LEFT]][j - play[RIGHT]][JABA];
                }
                bestScores[i][j][PIETON] = maxScorePieton;

            }

        }
        //solution is the best possible way for Jaba to score with the piles from 1 to the last
        return bestScores[1][piles.length][player];
    }

}