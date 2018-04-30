package votingmachine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdminMain extends JPanel{
    private VotingMachine ParentContainer;
    private AdminMain curr;
    private MainPanel mainpanel;
    private JButton AddMember,CreateSession,DisplayMembers,SeeResult,DeleteSession,returnToMainPanel;
    private JLabel adminlabel;
    private Font f;
    private Image img;
    
    public AdminMain(VotingMachine v,MainPanel mp){
        img=new ImageIcon(getClass().getResource("admain.jpg")).getImage();
        curr=this;
        ParentContainer=v;
        mainpanel=mp;
        this.setLayout(null);
        
        f=new Font("style",Font.BOLD+Font.ITALIC,20);
        
        AddMember=new JButton("ADD MEMBER");
        CreateSession=new JButton("CREATE SESSION");
        DisplayMembers=new JButton("DISPLAY MEMBERS");
        DeleteSession=new JButton("CEASE SESSION");
        SeeResult=new JButton("SEE RESULT");
        returnToMainPanel=new JButton("LOGOUT");
        adminlabel=new JLabel("WELCOME TO ADMIN PAGE");
        adminlabel.setFont(f);
        adminlabel.setForeground(Color.red);
        
        AddMember.setBounds(100,200,200,70);
        CreateSession.setBounds(310,200,200,70);
        DisplayMembers.setBounds(100,290,200,70);
        SeeResult.setBounds(200,455,200,50);
        DeleteSession.setBounds(310,290,200,70);
        returnToMainPanel.setBounds(250,600,100,40);
        adminlabel.setBounds(150,100,300,50);

        AddMember.setBackground(Color.LIGHT_GRAY);
        AddMember.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        CreateSession.setBackground(Color.LIGHT_GRAY);
        CreateSession.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        DisplayMembers.setBackground(Color.LIGHT_GRAY);
        DisplayMembers.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        SeeResult.setForeground(Color.white);
        SeeResult.setBackground(Color.blue);
        SeeResult.setBorder(BorderFactory.createLineBorder(Color.blue));
        DeleteSession.setBackground(Color.LIGHT_GRAY);
        DeleteSession.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));        
        returnToMainPanel.setBackground(Color.red);
        returnToMainPanel.setForeground(Color.white);
        returnToMainPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        adminlabel.setBackground(Color.LIGHT_GRAY);        
        
        Handler handler=new Handler();
        
        AddMember.addActionListener(handler);
        CreateSession.addActionListener(handler);
        DisplayMembers.addActionListener(handler);
        DeleteSession.addActionListener(handler);
        SeeResult.addActionListener(handler);
        returnToMainPanel.addActionListener(handler);
        
        
        this.add(adminlabel);
        this.add(AddMember);
        this.add(CreateSession);
        this.add(DisplayMembers);
        this.add(DeleteSession);
        this.add(SeeResult);
        this.add(returnToMainPanel);
    }
    private class Handler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==AddMember){
                ParentContainer.setEnabled(false);
                AddMemFrm amf=new AddMemFrm(ParentContainer);
                amf.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e) {
                        ParentContainer.setEnabled(true);
                    }                    
                });
            }
            else if(e.getSource()==CreateSession){
                ParentContainer.setEnabled(false);
                CreateSess ses=new CreateSess(ParentContainer);
                ses.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e) {
                        ParentContainer.setEnabled(true);
                    }                    
                });                
            }
            else if(e.getSource()==DisplayMembers){
                ParentContainer.setEnabled(false);
                DispMemFrm disp=new DispMemFrm();
                disp.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e) {
                        ParentContainer.setEnabled(true);
                    }                    
                });                
            }
            else if(e.getSource()==DeleteSession){
                ParentContainer.setEnabled(false);
                DelSesFrm dels=new DelSesFrm();
                dels.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e) {
                        ParentContainer.setEnabled(true);
                    }                    
                });                 
            }
            else if(e.getSource()==SeeResult){
                    ParentContainer.remove(curr);                    
                    ResultMain resultmain=new ResultMain(ParentContainer,curr);
                    ParentContainer.add(resultmain);
                    ParentContainer.revalidate();
                    ParentContainer.repaint();               
            }
            else if(e.getSource()==returnToMainPanel){
                ParentContainer.remove(curr);
                ParentContainer.add(mainpanel);
                ParentContainer.revalidate();
                ParentContainer.repaint();
            }
        }
        
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img,0,0,getWidth(),getHeight(),null);
    }      
}
