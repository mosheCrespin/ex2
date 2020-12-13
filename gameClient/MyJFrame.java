package gameClient;

import javax.swing.*;

public class MyJFrame extends JFrame {
    private Arena arena;
    private MyPanel panel;
    public MyJFrame(String title,Arena arena){
        super(title);
        this.arena=arena;
        this.panel=new MyPanel(arena);
        this.add(panel);
        this.setVisible(true);
    }

}
