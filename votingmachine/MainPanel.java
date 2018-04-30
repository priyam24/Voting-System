package votingmachine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Priyam
 */
public class MainPanel extends JPanel{
    private VotingMachine ParentContainer;
    private JButton voter,admin;
    private JLabel label;
    private Font f;
    private Image img;
    
    public MainPanel(VotingMachine v){
        img=new ImageIcon(getClass().getResource("mainframe.jpg")).getImage();
        ParentContainer=v;
        this.setLayout(null);
        //this.setOpaque(false);
        
        label=new JLabel("VOTING IS YOUR BIRTH RIGHT");
        label.setForeground(Color.white);
        label.setBounds(100,50,400,100);
        f=new Font("style",Font.BOLD+Font.ROMAN_BASELINE,25);
        label.setFont(f);
        this.add(label);
        
        voter=new JButton("VOTER");
        admin=new JButton("ADMIN");
        f=new Font("style",Font.BOLD+Font.ITALIC,20);
        voter.setFont(f);
        admin.setFont(f);
        voter.setBackground(Color.orange);
        voter.setBorder(BorderFactory.createLineBorder(Color.orange));
        voter.setForeground(Color.red);
        admin.setBackground(Color.orange);
        admin.setBorder(BorderFactory.createLineBorder(Color.orange));
        admin.setForeground(Color.red);
        voter.setBounds(200,250,200,70);
        admin.setBounds(200,350,200,70);
        admin.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String pass=JOptionPane.showInputDialog(admin.getParent(), "Enter Password :", "Admin", JOptionPane.PLAIN_MESSAGE);
                if(pass==null) return;
                if(pass.equals("007")){
                    ParentContainer.remove(admin.getParent());
                    
                    AdminMain adminmain=new AdminMain(ParentContainer,(MainPanel)admin.getParent());
                    ParentContainer.add(adminmain);
                    ParentContainer.revalidate();
                    ParentContainer.repaint();
                }
                else{
                    JOptionPane.showMessageDialog(admin.getParent(), "Wrong Password !");
                }
            }
        });
        voter.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                    ParentContainer.remove(admin.getParent());                    
                    VoterMain votermain=new VoterMain(ParentContainer,(MainPanel)admin.getParent());
                    ParentContainer.add(votermain);
                    ParentContainer.revalidate();
                    ParentContainer.repaint();
            }
        });
        this.add(voter);
        this.add(admin);     
        
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img,0,0,getWidth(),getHeight(),null);
    }    
}