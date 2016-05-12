package abacusprogram.windows;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 4/2/16.
 */
public class ContainerWithBackground extends JComponent {

    private Image img;

    public ContainerWithBackground(Image img) {
        this.img = img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);

    }
}
