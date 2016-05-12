package abacusprogram.quiz;

import abacusprogram.quiz.question.Operation;
import abacusprogram.quiz.question.Range;
import abacusprogram.quiz.question.RequiredQuestion;

/**
 * Created on 2/20/16.
 */
public class Settings {
    private int numberOfQuestions;
    private Range range;
    private int numberOfNumbersPerQuestions;
    private int timeToAnswer;
    private double timePerQuestion;
    private int fontSize;
    private Operation operation;
    private RequiredQuestion[] requiredNumbers;

    public Settings(int numberOfQuestions, Range range, int numberOfNumbersPerQuestions, int timeToAnswer, double timePerQuestion, int fontSize, Operation operation, RequiredQuestion[] requiredNumbers) {
        this.numberOfQuestions = numberOfQuestions;
        this.range = range;
        this.numberOfNumbersPerQuestions = numberOfNumbersPerQuestions;
        this.timeToAnswer = timeToAnswer;
        this.timePerQuestion = timePerQuestion;
        this.fontSize = fontSize;
        this.operation = operation;
        this.requiredNumbers = requiredNumbers;
    }

    //==================GETTERS=================//

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public int getLowerRange() {
        return range.getLower();
    }

    public int getUpperRange() {
        return range.getHigher();
    }

    public int getNumberOfNumbersPerQuestions() {
        return numberOfNumbersPerQuestions;
    }

    public int getTimeToAnswer() {
        return timeToAnswer;
    }

    public double getTimePerQuestion() {
        return timePerQuestion;
    }

    public int getFontSize() {
        return fontSize;
    }

    public Operation getOperation() {
        return operation;
    }

    public RequiredQuestion[] getRequiredNumbers() {
        return requiredNumbers;
    }


    //======================SETTERS===================//

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public void setLowerRange(int lowerRange) {
        range.setLower( lowerRange);
    }

    public void setUpperRange(int upperRange) {
        range.setHigher( upperRange);
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public void setNumberOfNumbersPerQuestions(int numberOfNumbersPerQuestions) {
        this.numberOfNumbersPerQuestions = numberOfNumbersPerQuestions;
    }

    public void setTimeToAnswer(int timeToAnswer) {
        this.timeToAnswer = timeToAnswer;
    }

    public void setTimePerQuestion(double timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setRequiredNumbers(RequiredQuestion[] requiredNumbers) {
        this.requiredNumbers = requiredNumbers;
    }

    public void copy(Settings newSettings)
    {
        setFontSize(newSettings.fontSize);
        setRange(newSettings.range);
        setNumberOfQuestions(newSettings.numberOfQuestions);
        setOperation(newSettings.operation);
        setNumberOfNumbersPerQuestions(newSettings.numberOfNumbersPerQuestions);
        setTimePerQuestion(newSettings.timePerQuestion);
        setTimeToAnswer(newSettings.timeToAnswer);
        requiredNumbers = newSettings.requiredNumbers;
    }
    @Override
    public String toString() {
        return "Settings[" +
                "numberOfQuestions=" + numberOfQuestions +
                ", lowerRange=" + range.getLower() +
                ", upperRange=" + range.getHigher() +
                ", numberOfNumbersPerQuestions=" + numberOfNumbersPerQuestions +
                ", timeToAnswer=" + timeToAnswer +
                ", timePerQuestion=" + timePerQuestion +
                ", fontSize=" + fontSize +
                ", operation=" + operation +
                ']';
    }
}
