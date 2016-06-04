package abacusprogram.quiz.question.generator;

import abacusprogram.quiz.Settings;

/**
 * TODO; garentee that settings with negative upperRange will not get here.
 * TODO: really have the running quiz data it seriously will help deal with the multiple sums variables
 */
public class RandomGenerator implements QuestionGenerator {
    protected Settings settings;
    private int sum = 0;

    public RandomGenerator(Settings settings) {
        this.settings = settings;
    }

    /*
    @return: returns a positive integer between the lowerrange and the upper range that does not equal oldValue
        getNextValue(x) != x
        will always be true;
        and will always make sure sum +num will not become a negative number
     */
    @Override
    public int getNextValue(int oldValue) {
        sum += oldValue;
        //this CANNOT be getNextValue bc inheritance.
        int num;
        do {

            num = (int) (settings.getLowerRange() + (settings.getUpperRange() - settings.getLowerRange() + 1) * Math.random());
        }while(num == 0 || num == oldValue || sum + num < 0);
        return num;
    }


    /*
    @return: returns a positive integer between the lower range and the upper range.
     */
    @Override
    public int getNextValue() {
        sum = 0;
        int num;
        do {
            num = (int) (settings.getLowerRange() + (settings.getUpperRange() - settings.getLowerRange() + 1) * Math.random());
        }while(num <= 0 ); //assuming no negative on first trial
        return num;
    }
}
