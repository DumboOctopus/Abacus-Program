package abacusprogram.quiz;

import abacusprogram.quiz.question.Question;

/**
 * this class wraps all the data for the whole quiz durration
 */
public class RunningQuizData {
    private int currQuestionInSetNumber; //represents in one set
    private Question[] questionsAsked;
    private int currSetNumber;  //ei. set 0 set 1, set2
    private Settings settings;

    public RunningQuizData(Settings settings) {
        this.settings = settings;

        this.currSetNumber = 0; //counts from zero in internal system
        questionsAsked = new Question[settings.getNumberOfQuestions()];
        currQuestionInSetNumber = 0; //counts from zero in internal system
    }


    //entire set of question operations
    public void goToNextQuestionSet(Question previousQuestion)
    {
        questionsAsked[currSetNumber] = previousQuestion;
        currQuestionInSetNumber = 0;
        currSetNumber ++;
    }
    public void cancelQuiz()
    {
        for (int i = 0; i < questionsAsked.length; i++) {
            if(questionsAsked[i] == null) { //sometimes currSetNumber goes ahead of whats its supposed to
                Question tmp = new Question("!");
                tmp.setCorrect(false);
                tmp.setAnswer("!");
                questionsAsked[i] = tmp;
                System.out.println(tmp);
            }
        }
    }

    //questions asked
    public Question[] getQuestionsAsked() {
        return questionsAsked;
    }



    //inside a set operations
    public void incrementCurrQuestion()
    {
        currQuestionInSetNumber++;
    }
    public int getCurrQuestionInSetNumber()
    {
        return currQuestionInSetNumber + 1;
    }
}
