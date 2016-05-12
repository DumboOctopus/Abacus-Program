package abacusprogram.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created on 2/20/16.
 */
public class TimedIntegerDialog extends JDialog{
    private JFormattedTextField answerField;
    private JLabel timeLeftLabel;

    private TimedIntegerDialogListener listener;

    private int timeTotal;
    private Timer timer;


    public TimedIntegerDialog(Frame owner, String title,int timeTotal, TimedIntegerDialogListener listener) {
        super(owner, title);
        this.timeTotal = timeTotal;
        this.listener = listener;
        timeLeftLabel.setText("Time left to answer: " + timeTotal + " seconds"); //the only reason this works is because creatRootPane

        setBounds(owner.getWidth()/2 - 100, owner.getHeight()/2 - 50, 200, 100);
        setVisible(true);
    }

    @Override
    protected JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();
        rootPane.setLayout(new FlowLayout());

        JPanel timePanel = new JPanel();
        timeLeftLabel = new JLabel("");

        timePanel.add(timeLeftLabel);
        rootPane.add(timePanel);
        timer = new Timer(1000, new ActionListener() {
            int iterations = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                iterations++;
                timeLeftLabel.setText("Time left to answer: "+(timeTotal - iterations) + " seconds");
                if(iterations >= timeTotal)
                {
                    listener.onExit(false, 0); //0 because it is irrevant
                    TimedIntegerDialog.this.stop();
                }
            }
        });
        timer.start();

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        answerField = new JFormattedTextField(numberFormat);
        answerField.setColumns(15);
        answerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onExit(true, Integer.parseInt(answerField.getText()));
                TimedIntegerDialog.this.stop();
            }
        });
        rootPane.add(answerField);
        return rootPane;
    }

    public void stop()
    {
        setVisible(false);
        timer.stop();
        dispose();
    }
}
