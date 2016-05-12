package abacusprogram.windows;

import abacusprogram.quiz.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created on 2/29/16.
 */
public class SettingsModifierButton extends JRadioButtonMenuItem implements ActionListener {

    private Settings oldSettings; //reference to main programs settings
    private Settings settings;     //settings the button represents

    public SettingsModifierButton(String title, Settings oldSettings, Settings settings) {
        super(title);
        this.settings = settings;
        this.oldSettings = oldSettings;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        settings.setFontSize(oldSettings.getFontSize());
        oldSettings.copy(settings);
        try {
            new ChangeTimeWindow(oldSettings);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
