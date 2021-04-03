
public class GameOfBeans {

    // MARK: Variables

    private int[][] bestScores;
    private int depth;
    private int[] piles;
    private String firstPlayer;
    private static final boolean JABA = true;
    //private static final boolean PIETON = false;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    public GameOfBeans(int depth, int[] piles, String firstPlayer) {
        this.depth = depth;
        this.piles = piles;
        this.firstPlayer = firstPlayer;
        bestScores = new int[piles.length + 1][piles.length + 1];
    }

    private int[] pietonPlay(int i, int j) {
        int maxScore = Integer.MIN_VALUE;
        int[] answer = { 0, 0 };

        for (int k = 1; k <= depth && (i + k <= j + 1); k++) {
            int sum = 0;

            for (int c = 0; c < k; c++) {
                sum += piles[i + c - 1];
            }

            if (sum > maxScore) {
                maxScore = sum;
                answer = new int[] { k, 0 };
            }
        }

        for (int k = 1; k <= depth && (j - k + 1 >= i); k++) {
            int sum = 0;

            for (int c = 0; c < k; c++) {
                sum += piles[j - c - 1];
            }

            if (sum > maxScore) {
                maxScore = sum;
                answer = new int[] { 0, k };
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
        boolean player = firstPlayer.toLowerCase().equals("jaba");
        // base cases = only one pile left (i=j)
        for (int i = 1; i < piles.length; i++) {
            bestScores[i][i] = piles[i - 1];
        }

        // general case. P is the difference of indices of piles
        for (int p = 1; p < piles.length; p++) {
            for (int i = 1; i < piles.length - p; i++) { // i is the left index
                int j = i + p; // j is the right index
                int maxScore = Integer.MIN_VALUE;
                int maxToRemove = Integer.min(depth, p + 1);
                if (player == JABA) {
                    // it is Jaba's turn to play
                    for (int k = 1; k <= maxToRemove; k++) { // k is the number of piles to remove
                        int score = score(k, i, j) + bestScores[i - k][j];
                        if (score > maxScore) {
                            maxScore = score;
                        }

                    }

                } else {
                    // it is Pieton's turn to play
                    int[] play = pietonPlay(i, j);
                    maxScore = bestScores[i - play[LEFT]][j - play[RIGHT]];

                }
                // player turn changes
                player = !player;
                bestScores[i][j] = maxScore;

            }

        }

        // solution is the best possible way for Jaba to score with the piles from 1 to
        // the last
        return bestScores[1][piles.length];
    }

    public int biDimensionalAnswer() {
        boolean player = firstPlayer.toLowerCase().equals("jaba");
        // base cases = only one pile left (i=j)
        if (player == JABA) {
            for (int i = 1; i <= piles.length; i++) {
                bestScores[i][i] = piles[i - 1];
            }
        } else {
            for (int i = 1; i <= piles.length; i++) {
                bestScores[i][i] = 0;
            }
        }

        // general case. P is the difference of indices of piles
        for (int p = 1; p <= piles.length; p++) {
            for (int i = 1; i < piles.length + 1 - p; i++) { // i is the left index
                
                int j = i + p; // j is the right index
                int maxScore = Integer.MIN_VALUE;
                int maxToRemove = Integer.min(depth, p + 1);
                
                if (player == JABA) {
                    // it is Jaba's turn to play first
                    for (int k = 1; k <= maxToRemove; k++) { // k is the number of piles to remove
                        int[] play = pietonPlay(i + k, j);
                        int score;
                        if ((i + k + play[LEFT]) <= (j - play[RIGHT])) {
                            score = score(k, i, j) + bestScores[i + k + play[LEFT]][j - play[RIGHT]];
                        } else {
                            score = score(k, i, j);
                        }
                        if (score > maxScore) {
                            maxScore = score;
                        }
                    }

                    for (int k = maxToRemove; k > 0; k--) { // k is the number of piles to remove
                        int[] play = pietonPlay(i, j - k);
                        int score;
                        if ((i + play[LEFT]) <= (j - k - play[RIGHT])) {
                            score = score(-k, i, j) + bestScores[i + play[LEFT]][j - k - play[RIGHT]];
                        } else {
                            score = score(-k, i, j);
                        }
                        if (score > maxScore) {
                            maxScore = score;
                        }
                    }

                } else {
                    // it is Pieton's turn to play first
                    int[] play = pietonPlay(i, j);
                    // maxScore = bestScores[i-play[LEFT]][j-play[RIGHT]];
                    if ((i + play[LEFT]) >= (j - play[RIGHT])) {
                        for (int k = 1; k <= maxToRemove; k++) { // k is the number of piles to remove
                            int score;
                            if ((i + play[LEFT] + k) <= j - play[RIGHT]) {
                                score = score(k, i + play[LEFT], j - play[RIGHT]) + bestScores[i + play[LEFT] + k][j - play[RIGHT]];
                            } else {
                                score = score(k, i + play[LEFT], j - play[RIGHT]);
                            }
                            if (score > maxScore) {
                                maxScore = score;
                            }
                        }

                        for (int k = maxToRemove; k > 0; k--) { // k is the number of piles to remove
                            int score;
                            if ((i + play[LEFT] + k) <= j - play[RIGHT]) {
                                score = score(-k, i + play[LEFT], j - play[RIGHT]) + bestScores[i + play[LEFT]][j - play[RIGHT] - k];
                            } else {
                                score = score(-k, i + play[LEFT], j - play[RIGHT]);
                            }
                            if (score > maxScore) {
                                maxScore = score;
                            }
                        }
                    } else {
                        maxScore = 0;
                    }

                }

                bestScores[i][j] = maxScore;

            }

        }

        // solution is the best possible way for Jaba to score with the piles from 1 to
        int nilo = 1+1;// the last
        if(nilo>0){nilo++;}
        return bestScores[1][piles.length];
    }

}



/*

1
5 1
10 5 -3 6 -2
jaba

1
7 2
3 -1 20 -1 -10 -1 2 
Pieton


1
3 1
1 4 8
jaba


*/