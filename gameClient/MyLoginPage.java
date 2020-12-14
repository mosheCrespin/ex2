package gameClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyLoginPage extends JFrame implements ActionListener {
    JTextField textFieldForId;
    JTextField textFieldForLevel;
    JLabel ID;
    JLabel LevelNum;
    JLabel message;
    JButton button;
    boolean userSuccessfulyConnected;
    boolean user_entered_id;
    int id_num;
    int level_num;

    public MyLoginPage(){
        super();
        userSuccessfulyConnected=false;
        user_entered_id=false;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textFieldForId=new JTextField();
        textFieldForId.setBounds(60,50,150,20);
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
        this.setSize(300,300);
        setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String id_str=this.textFieldForId.getText();
        String level_str=this.textFieldForLevel.getText();
        if(level_str.length()==0)
        {
            message.setText("you have to choose level");
            return;
        }
        boolean IdIsNumeric = id_str.chars().allMatch( Character::isDigit );
        boolean LevelIsNumeric=level_str.chars().allMatch(Character::isDigit);
        if(IdIsNumeric&&LevelIsNumeric) {
            if(id_str.length()!=0)
                this.id_num=Integer.parseInt(id_str);
            this.level_num=Integer.parseInt(level_str);
            if(level_num<0||level_num>23){
                this.message.setText("enter level at the range of 0-23");
                textFieldForLevel.setText("");
                return;
            }
            if(id_str.length()!=0)
                this.user_entered_id=true;
            this.userSuccessfulyConnected=true;
            this.message.setText("entering the game! \n good luck!");
        }
        else {
            this.message.setText("please enter valid input!");
            this.textFieldForId.setText("");
            this.textFieldForLevel.setText("");
        }

    }
    public boolean get_user_successfuly_connected(){return this.userSuccessfulyConnected;}
    public boolean get_user_enterd_id(){return this.user_entered_id;}
    public int getId_num(){return this.id_num; }
    public int getLevel_num(){return this.level_num;}
}
