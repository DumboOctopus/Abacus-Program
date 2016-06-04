package abacusprogram.quiz.question.generator;

import abacusprogram.quiz.Settings;
import abacusprogram.quiz.question.Required1ColumnSum;
import abacusprogram.quiz.question.RequiredQuestion;

import java.awt.*;

/**
 * Simulates both types of Required Question
 *
 *
 * required 1'column sum:
 * there is 2 ways of doing this:
 *  probability:
 *          as numbers per set increase, the probability that the special sum will be implemented increase
 *          when it is decided that next move will be special sum, computer choose one of the special sum values
 *          it then sets questio to x, where x +∑ = the specail sum value choosen above
 *          then it sets question to the other special sum value
 *
 *  this can be done with pregeneration or continuous generation. I chose continuous
 *
 *
 */
public class RequiredQuestionsGenerator extends RandomGenerator {
    protected int numberInQuestionSet;

    private RequiredQuestion[] requiredQuestions;
    private int requiredQuestionsNumberInQuestionSetLength;

    private Required1ColumnSum curr1ColumnSum;
    private RequiredQuestion currNegativeReq;
    private int sum = 0;


    public RequiredQuestionsGenerator(Settings settings) {
        super(settings);
        this.numberInQuestionSet = 0;
        requiredQuestions = RequiredQuestion.copyArray(settings.getRequiredNumbers());
        int tmp = 0;
        for(RequiredQuestion requiredQuestion: requiredQuestions)
        {
            if(requiredQuestion instanceof Required1ColumnSum)
                tmp += 2; //we need 2 questions
            else if(requiredQuestion.getValue() < 0) //if it is negative
                tmp+= 2;
            else
                tmp ++;
        }
        requiredQuestionsNumberInQuestionSetLength = tmp;
    }

    @Override
    public int getNextValue(int oldValue) {

        int num = super.getNextValue(); //if this was super.getNextValue(int oldValue) then it


        if(curr1ColumnSum == null && currNegativeReq == null) {
            double prob = ((double) numberInQuestionSet + requiredQuestionsNumberInQuestionSetLength - 1) / settings.getNumberOfNumbersPerQuestions();
            System.out.println(prob);
            for (RequiredQuestion requiredQuestion : requiredQuestions) {
                if (!requiredQuestion.hasBeenAsked() && Math.random() <= prob) {
                    if (requiredQuestion instanceof Required1ColumnSum) {
                        curr1ColumnSum = (Required1ColumnSum) requiredQuestion;

                        //determining value heheh
                        int sumOnesPlace = sum % 10;

                        if (curr1ColumnSum.getValue() > sumOnesPlace) {
                            //means we just have to move up the difference.
                            num = curr1ColumnSum.getValue() - sumOnesPlace;
                        } else {
                            //that means we have to loop over to get down to getValue()
                            //we see how many we need to get to 0 (10 - sumOnesPlace)
                            //then add how many we have to move after 0 (getValue())
                            num = 10 - sumOnesPlace + curr1ColumnSum.getValue();
                        }
                        curr1ColumnSum.asked();

                        //lets say we can already do the required thing. ei. num = 0 or 10
                        if(num == 0 || num == 10)
                        {
                            num = curr1ColumnSum.getValue();
                            curr1ColumnSum = null;
                        }
                        break;
                    } else if (requiredQuestion.getValue() < 0) {
                        currNegativeReq = requiredQuestion;
                        int minAddition = -(sum + requiredQuestion.getValue());

                        int randAdjustment;
                        do {
                            randAdjustment = (int) ((settings.getUpperRange() - minAddition + 1) * Math.random());
                        }while (randAdjustment == minAddition);

                        num = (minAddition + randAdjustment);
                        currNegativeReq.asked();
                        break;
                    } else {
                        num = requiredQuestion.getValue();
                        requiredQuestion.asked();

                    }
                    //cuz some are not of this kind
//                    //see if it got asked in super.getNextValue(); or elsewhere
//                    if (num == requiredQuestion.getValue()) {
//                        requiredQuestion.asked();
//                    }
                }

            }
        } else if(curr1ColumnSum != null)
        {

            num = curr1ColumnSum.getSecondValue();
            curr1ColumnSum = null;
        } else //if(currNegativeReq != null)
        {
            num = currNegativeReq.getValue();
            currNegativeReq = null;
        }


        numberInQuestionSet++;
        sum += num;
        return num;
    }

    //called during FIRST number in set
    @Override
    public int getNextValue() {
        sum = 0;
        numberInQuestionSet = 2; //bc first already pased
        requiredQuestions = RequiredQuestion.copyArray(settings.getRequiredNumbers()); //reset this stuff


        int num = super.getNextValue();
        for (RequiredQuestion requiredQuestion : requiredQuestions) {
            if (!requiredQuestion.hasBeenAsked() && requiredQuestion.getValue() > 0) {
                if (Math.random() < 0.1) {
                    if(requiredQuestion instanceof Required1ColumnSum)
                    {

                        curr1ColumnSum = (Required1ColumnSum) requiredQuestion;
                        num = curr1ColumnSum.getValue();
                        curr1ColumnSum.asked();
                        break;
                    } else {

                        num = requiredQuestion.getValue();
                        requiredQuestion.asked();
                        break;
                    }
                }
            }
        }

        sum += num;
        return num;
    }
}
