package abacusprogram.windows;

import abacusprogram.quiz.question.Question;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created on 2/20/16.
 */
public class ScoreDialog extends JDialog {
    private Question[] questions;
    private Image background;

    public ScoreDialog(Frame owner, Question[] questions) throws Exception {
        super(owner, "Score");
        this.questions = questions;
        background = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/scoreBackground2.jpg"));


        setUpBackgroundImage();
        SpringLayout layout = new SpringLayout();
        Container contentpane = getContentPane();
        getContentPane().setLayout(layout);
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(0, 3));

        JLabel correct = new JLabel("Status");
        JLabel question = new JLabel("Question");
        JLabel questionNumber = new JLabel("Question #");
        pane.add(questionNumber);
        pane.add(question);
        pane.add(correct);
        for (int i = 0; i < questions.length; i++) {
            Question q = questions[i];

            correct = new JLabel(q.isCorrect() ? "Correct" : "Incorrect");
            question = new JLabel(q.getQuestion() + " = " + q.getAnswer());
            questionNumber = new JLabel(1+ i + "");

            pane.add(questionNumber);
            pane.add(question);
            pane.add(correct);
        }
        pane.setBounds(40,40,500, questions.length * 30);
        pane.setOpaque(false);

        add(pane);
        //spring layout stufff
        layout.putConstraint(
                SpringLayout.NORTH,
                pane,
                10,
                SpringLayout.NORTH,
                contentpane
        );
        layout.putConstraint(
                SpringLayout.EAST,
                pane,
                10,
                SpringLayout.EAST,
                contentpane
        );
        layout.putConstraint(
                SpringLayout.SOUTH,
                pane,
                10,
                SpringLayout.SOUTH,
                contentpane
        );
        layout.putConstraint(
                SpringLayout.WEST,
                pane,
                10,
                SpringLayout.WEST,
                contentpane
        );



        //end of spring layout stufffs
        setSize(
                background.getWidth(null)/3,
                100+ questions.length * 30
        );
        setBounds(
                owner.getWidth() / 2 - getWidth()/2,
                owner.getHeight() / 2 - getHeight()/2,
                getWidth(),
                getHeight()
        );
        setVisible(true);
    }

    private void setUpBackgroundImage() throws IOException {
        setContentPane(new ContainerWithBackground(background));
    }


}
