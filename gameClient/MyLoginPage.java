package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyLoginPage extends JFrame implements ActionListener , MouseListener {
    JTextField textFieldForId;
    JTextField textFieldForLevel;
    JLabel ID;
    JLabel LevelNum;
    JLabel message;
    JButton button;
    boolean userSuccessfullyConnected;
    boolean user_entered_id;
    int id_num;
    int level_num;

    /**
     * this method declare how the login page look. the size of the page the labels and where they will show
     * the size the text will show..
     */
    public MyLoginPage(){
        super();
        userSuccessfullyConnected =false;
        user_entered_id=false;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textFieldForId=new JTextField();
        textFieldForId.setBounds(60,50,150,20);
        textFieldForId.setText("Optional");
        textFieldForId.addMouseListener(this);
        textFieldForId.addActionListener(this);
        textFieldForLevel=new JTextField();
        textFieldForLevel.setBounds(60,90,150,20);
        this.ID=new JLabel("ID: ");
        this.ID.setBounds(textFieldForId.getX()-30,textFieldForId.getY(),50,20);
        this.LevelNum=new JLabel("*Level: ");
        this.LevelNum.setBounds(textFieldForLevel.getX()-50,textFieldForLevel.getY(),50,20);
        button=new JButton("Start Game");
        button.setBounds(50,150,200,30);
        this.message=new JLabel();
        this.message.setBounds(textFieldForLevel.getX(), textFieldForLevel.getY()+20,200,50);
        this.add(message);
        this.add(textFieldForId);
        this.add(textFieldForLevel);
        this.add(button);
        this.add(ID);
        this.add(LevelNum);
        this.setResizable(false);
        button.addActionListener(this);
        setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(screenSize.height/2,screenSize.width/10,300,300);
        setVisible(true);
    }


    /**
     * this method check if the data the user give in the Labels is legal,
     * if so he move to the game page else in the login show the reason why the data isn't legal.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String id_str=this.textFieldForId.getText();
        String level_str=this.textFieldForLevel.getText();
        if(id_str.length()>10||level_str.length()>10)
        {
            message.setForeground(Color.red);
            message.setText("please insert an integer number!");
            this.textFieldForId.setText("");
            this.textFieldForLevel.setText("");
            return;
        }
        if(level_str.length()==0)
        {
            message.setForeground(Color.red);
            message.setText("level number is required!");
            return;
        }
        boolean IdIsNumeric = id_str.chars().allMatch( Character::isDigit );
        boolean LevelIsNumeric=level_str.chars().allMatch(Character::isDigit);
        boolean OptionalStay=this.textFieldForId.getText().equals("Optional");

        if(LevelIsNumeric&&(IdIsNumeric||OptionalStay)){
            if(id_str.length()!=0&&!OptionalStay)
                this.id_num=Integer.parseInt(id_str);
            this.level_num=Integer.parseInt(level_str);
            if(level_num<0||level_num>23){
                message.setForeground(Color.red);
                this.message.setText("insert level at the range of 0-23!");
                textFieldForLevel.setText("");
                return;
            }
            if(id_str.length()!=0&&!OptionalStay)
                this.user_entered_id=true;
            this.userSuccessfullyConnected =true;
            message.setForeground(Color.green);
            this.message.setText("entering the game! \n good luck!");
        }
        else {
            message.setForeground(Color.red);
            this.message.setText("please insert valid input!");
            this.textFieldForId.setText("");
            this.textFieldForLevel.setText("");
        }

    }
    public boolean get_user_successfully_connected(){return this.userSuccessfullyConnected;}
    public boolean get_user_entered_id(){return this.user_entered_id;}
    public int getId_num(){return this.id_num; }
    public int getLevel_num(){return this.level_num;}

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * This method listen to the press the user press on the button
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(this.textFieldForId.getText().equals("Optional"))
             this.textFieldForId.setText("");
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
