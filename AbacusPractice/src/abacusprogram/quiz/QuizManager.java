package abacusprogram.quiz;

import abacusprogram.windows.TimedIntegerDialogListener;
import abacusprogram.quiz.question.Question;
import abacusprogram.quiz.question.RequiredQuestion;
import abacusprogram.windows.AbacusPracticeWindow;
import abacusprogram.windows.ScoreDialog;
import abacusprogram.windows.TimedIntegerDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * dude that Integer.parseInt(mainLabel.getText()) tho...
 *
 *
 * Also all those fields are disorganized. We must factor them out into seperate classes. That will be more happy.
 *
 *
 * SO MANY ANONYMOUS CLASSES UGGHHH. EWW THATS GROSSS
 */
public class QuizManager {

    private int questionNum = 1;
    private Timer questionTimer;

    //for running total quiz
    private Question[] questionsAsked;

    //other
    private Settings settings;
    private JLabel mainLabel;
    private JLabel questionNumberLabel;
    private AbacusPracticeWindow window;

    //runner
    private QuestionTimerHandler questionTimerHandler;

    //generate questions
    private RequiredQuestion[] requiredQuestions;
    private int numberInQuestionSet;

    //=============CONSTRUCTOR
    public QuizManager (AbacusPracticeWindow window) {
        settings = window.getSettings();
        mainLabel = window.getMainLabel();
        questionNumberLabel = window.getQuestionNumberLabel();
        this.window = window;

        questionsAsked = new Question[settings.getNumberOfQuestions()];
        requiredQuestions = RequiredQuestion.copyArray(settings.getRequiredNumbers());
        numberInQuestionSet = 0;

        createQuestionAndRun();
    }

    //===================METHODS============///

    public int nextQuestion()
    {
        int newNum =(int) (settings.getLowerRange() + (settings.getUpperRange() - settings.getLowerRange() + 1) * Math.random());


        double prob = ((double)numberInQuestionSet + requiredQuestions.length)/settings.getNumberOfNumbersPerQuestions();
        System.out.println("prob: "+prob);
        System.out.println("numberInQuestionSet: "+numberInQuestionSet);
        for(RequiredQuestion requiredQuestion: requiredQuestions)
        {
            System.out.println("\thi: "+requiredQuestion.hasBeenAsked());
            if(!requiredQuestion.hasBeenAsked())
                if(Math.random() <= prob) {
                    newNum = requiredQuestion.getValue();
                    System.out.println("set: "+newNum);
                }
            //do not set hasBeenAsked = true yet because it might be filtered
        }

        while((newNum+"").equals(mainLabel.getText()) || newNum == 0) {
            newNum = (int) (settings.getLowerRange() + (settings.getUpperRange() - settings.getLowerRange() + 1) * Math.random());
        }
        System.out.println("num:"+newNum);

        for(RequiredQuestion requiredQuestion: requiredQuestions)
        {
            if(newNum == requiredQuestion.getValue()) {
                requiredQuestion.asked();
                System.out.println(requiredQuestion.getValue() + " asked");
            }
        }


        mainLabel.setText("" + newNum);
        mainLabel.setFont(new Font("Serif", Font.PLAIN, settings.getFontSize()));

        numberInQuestionSet++;
        return newNum;
    }
    public void createQuestionAndRun()
    {
        questionNumberLabel.setText("Question #" + questionNum);
        nextQuestion();

        questionTimer = new Timer(
                (int)( settings.getTimePerQuestion() * 1000),
                (questionTimerHandler = new QuestionTimerHandler())
        );
        questionTimer.start();
    }

    public void cancelQuiz()
    {
        questionTimer.stop();
        questionTimerHandler.stop();
        //populate the rest of questionsAsked
        for (int i = questionNum - 1; i < questionsAsked.length; i++) {
            questionsAsked[i] = new Question("!");
            questionsAsked[i].setCorrect(false);
            questionsAsked[i].setAnswer("!");
        }
        try {
            new ScoreDialog(window, questionsAsked);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //===================================INNNER CLASSES==========================//
    private class QuestionTimerHandler implements ActionListener, TimedIntegerDialogListener
    {
        private int numberOfQuestionOn = 0;
        private String compiledQuestion = "" + mainLabel.getText();
        private int calculatedAnswer = Integer.parseInt(mainLabel.getText());
        private Timer fTimer;
        private TimedIntegerDialog dialog;

        //============================FROM ACTION LISTENER================================//
        public void actionPerformed(ActionEvent e) {
            numberOfQuestionOn ++;
            if(numberOfQuestionOn >= settings.getNumberOfNumbersPerQuestions())
            {
                onEndOfQSet();
            } else
            {
                int nextNum = nextQuestion();
                calculatedAnswer = (int) settings.getOperation().doOperation(calculatedAnswer, nextNum);
                compiledQuestion += settings.getOperation().toString() + "("+nextNum+")";
            }
        }

        //=====================================REFACTORING===========================//

        private void onEndOfQSet() {
            mainLabel.setText("");
            questionTimer.stop();
            questionsAsked[questionNum - 1] = new Question( compiledQuestion);
            numberInQuestionSet = 0;
            requiredQuestions = RequiredQuestion.copyArray(settings.getRequiredNumbers());
            dialog = new TimedIntegerDialog(
                    window,
                    "Please Answer",
                    settings.getTimeToAnswer(),
                    this
            );
            mainLabel.setFont(new Font("Serif", Font.PLAIN, 100));
            mainLabel.setText("Waiting for response");
        }

        public void stop()
        {
            if(dialog != null) dialog.stop();
            if(fTimer != null) fTimer.stop();
        }


        //=======================FROM TimedIntegerDialogListener=====================//

        @Override
        public void onExit(boolean byUser, int userInput) {
            if(!byUser)
            {
                questionsAsked[questionNum - 1].setAnswer("NO RESPONSE");
                questionsAsked[questionNum -1].setCorrect(false);

            }else {
                questionsAsked[questionNum - 1].setAnswer(userInput + "");
                if (userInput == calculatedAnswer) {
                    questionsAsked[questionNum - 1].setCorrect(true);

                } else {
                    questionsAsked[questionNum - 1].setCorrect(false);

                }
            }

            //mainLabel.setFont(new Font("Serif", Font.PLAIN, 100));
            mainLabel.setText("Get Ready");
            fTimer = new Timer(
                2000,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(questionNum >= settings.getNumberOfQuestions())
                        {
                            mainLabel.setFont(new Font("Serif", Font.PLAIN, 100));
                            mainLabel.setText("jkjk that's all the questions ;)");
                            window.onQuizFinish(); //TODO: replace with listener ArrayList
                            try{
                                new ScoreDialog(window, questionsAsked);
                            }
                            catch(Exception ee){
                                ee.printStackTrace();
                            }
                        }else {
                            questionNum ++;
                            createQuestionAndRun();
                        }
                        fTimer.stop();
                    }
                }
            );
            fTimer.start();
        }

    }
}
