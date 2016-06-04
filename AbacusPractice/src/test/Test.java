package test;

import abacusprogram.quiz.Settings;
import abacusprogram.quiz.question.Operation;
import abacusprogram.quiz.question.Range;
import abacusprogram.quiz.question.RequiredQuestion;
import abacusprogram.quiz.question.generator.PositiveContinuallyGenerator;
import abacusprogram.quiz.question.generator.QuestionGenerator;
import abacusprogram.quiz.question.generator.RandomGenerator;

/**
 * Created on 5/31/16.
 */
public class Test {
    public static void main(String[] args)
    {
        Settings s = new Settings(
                10,
                new Range(-10,10),
                4,
                0,
                0,
                0,
                Operation.ADDITION,
                new RequiredQuestion[0]
        );

        QuestionGenerator generator = new PositiveContinuallyGenerator( new RandomGenerator(s), 4);
        int oldValue = 0;
        int sum = 0;
        for(int i = 0; i < 103; i++)
        {
            if(i % 4 == 0)
            {
                System.out.println("sum: "+sum);
                sum = 0;
                oldValue = generator.getNextValue();
                sum += oldValue;
                System.out.println("next:\n" + oldValue);
            } else
            {
                oldValue = generator.getNextValue(oldValue);
                sum += oldValue;
                System.out.println(oldValue);
            }

        }

    }
}
