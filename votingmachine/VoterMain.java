package votingmachine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VoterMain extends JPanel{
    private VotingMachine ParentContainer;
    private VoterMain curr;
    private MainPanel mainpanel;
    private JLabel sessLabel,canLabel;
    private JTextField tf1,tf2;
    private JButton Submit,goBack;
    private JLabel voterlabel;
    private Font f;
    private Image img;
        public VoterMain(VotingMachine v,MainPanel mp){
        img=new ImageIcon(getClass().getResource("voteAuth.jpg")).getImage();
        curr=this;
        ParentContainer=v;
        mainpanel=mp;
        this.setLayout(null);
        
        f=new Font("style",Font.BOLD+Font.ITALIC,30);  
        
        voterlabel=new JLabel("Candidate Login");
        voterlabel.setFont(f);
        sessLabel=new JLabel("Enter Session Code :");
        f=new Font("style",Font.BOLD+Font.TRUETYPE_FONT,20);
        sessLabel.setFont(f);
        canLabel=new JLabel("Enter Candidate ID :");
        canLabel.setFont(f);
        tf1=new JTextField();
        tf2=new JTextField();
        Submit=new JButton("SUBMIT");
        goBack=new JButton("GO BACK");
        
        voterlabel.setBounds(150,50,300,50);
        sessLabel.setBounds(15,150,200,30);
        canLabel.setBounds(15,250,200,30);
        tf1.setBounds(15,190,200,30);
        tf2.setBounds(15,280,200,30);
        Submit.setBounds(70,340,100,40);
        goBack.setBounds(250,600,100,40);
        
        sessLabel.setForeground(Color.white);
        canLabel.setForeground(Color.white);
        Submit.setForeground(Color.red);
        Submit.setBackground(Color.yellow);
        Submit.setBorder(BorderFactory.createLineBorder(Color.yellow));
        goBack.setForeground(Color.white);
        goBack.setBackground(Color.black);
        goBack.setBorder(BorderFactory.createLineBorder(Color.black));         
        
        Handler handler=new Handler();
        
        Submit.addActionListener(handler);
        goBack.addActionListener(handler);
        
        this.add(voterlabel);
        this.add(sessLabel);
        this.add(canLabel);
        this.add(tf1);
        this.add(tf2);
        this.add(Submit);
        this.add(goBack);
                
        }
    private class Handler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==Submit){
                if(check(tf1.getText(),tf2.getText())){
                    ParentContainer.remove(curr);                    
                    VoterCast votercast=new VoterCast(ParentContainer,mainpanel,tf1.getText(),tf2.getText());
                    ParentContainer.add(votercast);
                    ParentContainer.revalidate();
                    ParentContainer.repaint();
                }
            }
            else if(e.getSource()==goBack){
                ParentContainer.remove(curr);
                ParentContainer.add(mainpanel);
                ParentContainer.revalidate();
                ParentContainer.repaint();
            }
        }
        
    }
    private boolean check(String ses,String cn){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
            ps=con.prepareStatement("select sessID from session_store where sessID=? and stat=?");
            ps.setString(1, ses);
            ps.setInt(2, 1);
            rs=ps.executeQuery();
            if(rs.next()){
                ps=con.prepareStatement("select id from candidates where id=?");
                ps.setInt(1, Integer.parseInt(cn));
                rs=ps.executeQuery();
                if(rs.next()){
                    ps=con.prepareStatement("select canID from sess"+ses+"V where canID=?");
                    ps.setString(1, cn);
                    rs=ps.executeQuery();
                    if(!rs.next()){
                        JOptionPane.showMessageDialog(curr, "Authorization Successful!","Message",JOptionPane.WARNING_MESSAGE);
                        return true;
                    }
                    else{
                        JOptionPane.showMessageDialog(curr, "Sorry, you have already casted your Vote!","Warning",JOptionPane.WARNING_MESSAGE);
                        return false;                        
                    }
                }
                else{
                    JOptionPane.showMessageDialog(curr, "Please Re-check your ID!","Warning",JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
            else{
                JOptionPane.showMessageDialog(curr, "Invalid Session Code!","Warning",JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(curr, e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            return false;
        }
        finally{
            try{
                if(con!=null) con.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException ex){}
        } 
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img,0,0,getWidth(),getHeight(),null);
    }          
}
