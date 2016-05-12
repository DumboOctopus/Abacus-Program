package abacusprogram.quiz.question;

/**
 * Created on 4/2/16.
 */
public class Range {
    private int lower;
    private int high;

    public Range(int lower, int high) {
        this.lower = lower;
        this.high = high;
    }

    //====================GETTERS===============================//
    public int getLower() {
        return lower;
    }

    public int getHigher() {
        return high;
    }

    //=================Setters==============================//

    public void setLower(int lower) {
        this.lower = lower;
    }

    public void setHigher(int high) {
        this.high = high;
    }
}
