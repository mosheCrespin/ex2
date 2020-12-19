package gameClient;

import javax.swing.*;

/**
 * this method make the frame to the login page and game page.
 */
public class MyJFrame extends JFrame {
    private Arena arena;
    public MyJFrame(String title,Arena arena){
        super(title);
        this.arena=arena;
    }

}
