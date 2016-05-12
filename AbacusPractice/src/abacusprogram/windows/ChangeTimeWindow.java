package abacusprogram.windows;

import abacusprogram.quiz.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created on 4/16/16.
 */
public class ChangeTimeWindow extends JFrame {

    private Settings settings;

    double[] times = {0.2, 0.5, 0.7, 1, 1.5, 2, 3};
    JButton[] buttons;

    public ChangeTimeWindow(Settings settings) throws IOException
    {
        this.settings = settings;
        buttons = new JButton[times.length];
        setContentPane(new ContainerWithBackground(
                ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("resources/timeBackground.jpg"))
        ));
        setLayout(null);

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, times.length));
        for(int i = 0; i < times.length; i++)
        {
            buttons[i] = new JButton(times[i] + "");
            buttons[i].addActionListener(new TimeSettingActionListener(times[i]));
            pane.add(buttons[i]);
        }
        pane.setBounds(
                35,
                35,
                50*times.length,
                150-90
        );
        pane.setOpaque(false);
        add(pane);
        setSize(70 + 50 * times.length, 150 + 35);

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    private class TimeSettingActionListener implements ActionListener{
        private final double time;

        public TimeSettingActionListener(double time) {
            this.time = time;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setTimePerQuestion(time);
            System.out.println(settings.getTimePerQuestion());
            ChangeTimeWindow.this.dispose();
        }
    }

}
