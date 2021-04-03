public class GameOfBeans {

    private int[][][] bestScores;
    private int depth;
    private int[] piles;
    private String firstPlayer;
    private static final int JABA = 0;
    private static final int PIETON = 1;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;


    public GameOfBeans(int depth, int[] piles, String firstPlayer) {
        this.depth = depth;
        this.piles = piles;
        this.firstPlayer = firstPlayer;
        bestScores = new int[piles.length][piles.length][2];
    }

    private int[] pietonPlay(int i, int j) {
        int maxScore = Integer.MIN_VALUE;
        int[] answer = {0, 0};

        for (int k = 1; k <= depth && (i + k <= j); k++) {
            int sum = 0;

            for (int c = 0; c < k; c++) {
                sum += piles[i + c - 1];
            }

            if (sum > maxScore) {
                maxScore = sum;
                answer = new int[]{k, 0};
            }
        }

        for (int k = 1; k <= depth && (j - k >= i); k++) {
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

    // k>0 means we should augment i, k<0 means we should move j backwards
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


    public int bestJabaScore() {
        int player = firstPlayer.toLowerCase().equals("jaba") ? JABA : PIETON;
        //base cases = only one pile left (i=j)
        for (int i = 1; i < piles.length; i++) {
            bestScores[i][i][PIETON] = piles[i - 1];
            bestScores[i][i][JABA] = 0;
        }

        //general case. P is the difference of indices of piles
        for (int p = 1; p < piles.length; p++) {
            for (int i = 1; i < piles.length - p; i++) { //i is the left index
                int j = i + p; //j is the right index
                int maxScoreJaba = Integer.MIN_VALUE;
                int maxToRemove = Integer.min(depth, p + 1);

                //it is Jaba's turn to play
                for (int k = 1; k <= maxToRemove; k++) { //k is the number of piles to remove
                    //remove from left
                    int scoreLeft = score(k, i, j) + bestScores[i - k][j][PIETON];
                    maxScoreJaba = Integer.max(scoreLeft, maxScoreJaba);
                    //remove from right
                    int scoreRight = score(-k, i, j) + bestScores[i][j - k][PIETON];
                    maxScoreJaba = Integer.max(scoreRight, maxScoreJaba);
                }

                bestScores[i][j][JABA] = maxScoreJaba;

                //it is Pieton's turn to play
                int[] play = pietonPlay(i, j);
                int maxScorePieton = bestScores[i - play[LEFT]][j - play[RIGHT]][JABA];
                bestScores[i][j][JABA] = maxScorePieton;

            }

        }
        //solution is the best possible way for Jaba to score with the piles from 1 to the last
        return bestScores[1][piles.length][player];
    }

}