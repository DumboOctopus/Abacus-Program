package abacusprogram.quiz;

import abacusprogram.quiz.question.generator.PositiveContinuallyGenerator;
import abacusprogram.quiz.question.generator.QuestionGenerator;
import abacusprogram.quiz.question.generator.RequiredQuestionsGenerator;
import abacusprogram.windows.TimedIntegerDialogListener;
import abacusprogram.quiz.question.Question;
import abacusprogram.quiz.question.RequiredQuestion;
import abacusprogram.windows.AbacusPracticeWindow;
import abacusprogram.windows.ScoreDialog;
import abacusprogram.windows.TimedIntegerDialog;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

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


    //RunningQuizData idea
    private RunningQuizData data;
    private QuestionGenerator questionGenerator;

    private static URL nextQuestionSoundURL;


    //============================CONSTRUCTOR==============================//
    public QuizManager (AbacusPracticeWindow window) {
        nextQuestionSoundURL = window.nextQuestionSound;
        settings = window.getSettings();
        mainLabel = window.getMainLabel();
        questionNumberLabel = window.getQuestionNumberLabel();
        this.window = window;

        questionsAsked = new Question[settings.getNumberOfQuestions()];
        requiredQuestions = RequiredQuestion.copyArray(settings.getRequiredNumbers());


        data = new RunningQuizData(window.getSettings());
        questionGenerator = new RequiredQuestionsGenerator(window.getSettings());


        createQuestionAndRun();

    }

    //===================METHODS============///

    public int nextQuestion()
    {
        int newNum;



        if(data.getCurrQuestionInSetNumber() == 1) {
            newNum = questionGenerator.getNextValue();
        }else {
            newNum = questionGenerator.getNextValue(
                    Integer.parseInt(mainLabel.getText())
            );
        }
        mainLabel.setText("" + newNum);
        mainLabel.setFont(new Font("Serif", Font.PLAIN, settings.getFontSize()));

        data.incrementCurrQuestion();
        return newNum;
    }
    public void createQuestionAndRun()
    {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(nextQuestionSoundURL);
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(stream);
            clip.start();
            //clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            //requiredQuestions = RequiredQuestion.copyArray(settings.getRequiredNumbers());
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
            data.goToNextQuestionSet(questionsAsked[questionNum-1]);

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
