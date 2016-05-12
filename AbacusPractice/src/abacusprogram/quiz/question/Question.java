package abacusprogram.quiz.question;

/**
 * Created on 2/20/16.
 */
public class Question {
    private String question;
    private String answer;
    private boolean correct;

    public Question(String question) {
        this.question = question;
    }

    //===================GETTERS=======================//
    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public boolean isCorrect() {
        return correct;
    }

    //=====================SETTERS=======================//

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
