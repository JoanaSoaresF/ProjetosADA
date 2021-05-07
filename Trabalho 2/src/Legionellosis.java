/*
 * Ada Trabalho 2 - Legionellosis
 *
 * @author Joana Soares Faria n55754
 * @author Goncalo Martins Lourenco n55780
 */

public class Legionellosis {

    private final int numLocations;
    //number of sick person that passed in which location
    private int[] locationsCheck;


    public Legionellosis(int numLocations) {
        this.numLocations = numLocations;
        locationsCheck = new int[numLocations];
    }

    /**
     * Adds a connection between to locations
     *
     * @param l1 first location
     * @param l2 second location
     */
    public void addConnection(int l1, int l2) {

    }

    /**
     * Adds tha information of a sick person
     *
     * @param home     location of the of of the sick person
     * @param distance maximum distance from home the patient has been on the days before the
     *                 first symptoms
     */
    public void addSick(int home, int distance) {

    }

    /**
     * Computes the perilous locations
     *
     * @return an array with all the identified perilous locations
     */
    public int[] perilousLocations() {
        return null;
    }
}
