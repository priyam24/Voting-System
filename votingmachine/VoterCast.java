package votingmachine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class VoterCast extends JPanel{
    private VotingMachine ParentContainer;
    private MainPanel mainpanel;
    private VoterCast curr;
    private Image img;
    private Font f;
    private JLabel elec,cand,instruction,plist,pdetails;
    private JScrollPane boxscroll,tablescroll;
    private ArrayList<String> party,candit;
    private String SessionID,CandidateID;
    private ButtonGroup grp;
    private JCheckBox none;
    private DefaultTableModel model;
    private JTable table;
    private int bscroll,tscroll;
    private JButton submit,goBack;
    
    public VoterCast(VotingMachine v,MainPanel vm,String Session,String CandidId){
        SessionID=Session;
        CandidateID=CandidId;
        ParentContainer=v;
        mainpanel=vm;
        bscroll=30;
        tscroll=20;
        img=new ImageIcon(getClass().getResource("votecast.jpg")).getImage();
        curr=this;
        this.setLayout(null);
        
        f=new Font("style",Font.BOLD+Font.ROMAN_BASELINE,25);  

        elec=new JLabel("Welcome to "+getSessName(Session)+" Election");
        elec.setFont(f);
        elec.setForeground(Color.red);
        f=new Font("style",Font.BOLD+Font.ITALIC,20); 
        cand=new JLabel("Dear "+getCanName(CandidId)+", please cast your Vote!");
        cand.setFont(f);
        cand.setForeground(Color.black);
        instruction=new JLabel("Please go ahead and choose a party and NONE if you are Neutral!");
        instruction.setForeground(Color.black); 
        f=new Font("style",Font.BOLD+Font.ITALIC,15);  
        instruction.setFont(f);
        plist=new JLabel("Party List :");
        f=new Font("style",Font.BOLD+Font.HANGING_BASELINE,20);
        plist.setFont(f);
        plist.setForeground(Color.black);
        pdetails=new JLabel("Party Details :");
        pdetails.setFont(f);
        pdetails.setForeground(Color.black);
        
        elec.setBounds(90,20,500,50);
        cand.setBounds(10,100,400,30);
        instruction.setBounds(10,150,480,30);
        plist.setBounds(60,200,200,30);
        pdetails.setBounds(400,200,200,30);
        
        this.add(elec);
        this.add(cand);
        this.add(instruction);
        this.add(plist);
        this.add(pdetails);
        
        initPartyList();
        
        JCheckBox[] partycheck=new JCheckBox[party.size()];
	grp=new ButtonGroup();	
        Box optionBox = Box.createVerticalBox();
        f=new Font("style",Font.BOLD+Font.ITALIC,20); 
        for(int i=0;i<party.size();i++){
            partycheck[i]=new JCheckBox(party.get(i));
            partycheck[i].setFont(f);
            partycheck[i].setActionCommand(party.get(i));
            grp.add(partycheck[i]);
            optionBox.add(partycheck[i]);
            if(bscroll<180)
                bscroll+=30;
        }
        none=new JCheckBox("NONE");
        none.setActionCommand("NONE");
        none.setFont(f);
        none.setSelected(true);
        grp.add(none);
        
        optionBox.add(none);       
        
        
        optionBox.setBorder(BorderFactory.createTitledBorder("Party List"));  
        boxscroll=new JScrollPane(optionBox);
        boxscroll.setBounds(10,250,220,bscroll);
        this.add(boxscroll);      
        
        table=new JTable();
        table.setBackground(Color.white);
        table.setForeground(Color.black);
        table.getTableHeader().setBackground(Color.blue);
        table.getTableHeader().setForeground(Color.white);  
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        model=new DefaultTableModel();
        model.addColumn("Party");
        model.addColumn("Candidate");
        Iterator<String> itrp=party.iterator();
        Iterator<String> itrc=candit.iterator();
        while(itrp.hasNext()&&itrc.hasNext()){
            String[] temp={itrp.next(),itrc.next()};
            model.addRow(temp);
            if(tscroll<180)
                tscroll+=20;
        }
        table.setModel(model);
        tablescroll=new JScrollPane(table);
        //tablescroll.add(table);
        tablescroll.setBounds(370,250,200,tscroll);
        this.add(tablescroll);
        
        Handler handler=new Handler();
        
        submit=new JButton("SUBMIT");
        submit.setForeground(Color.white);
        submit.setBackground(Color.blue);
        submit.setBorder(BorderFactory.createLineBorder(Color.blue));    
        submit.addActionListener(handler);
        
        goBack=new JButton("LOGOUT");
        goBack.setForeground(Color.white);
        goBack.setBackground(Color.red);
        goBack.setBorder(BorderFactory.createLineBorder(Color.red));    
        goBack.addActionListener(handler);    
        
        submit.setBounds(50,600,120,50);
        goBack.setBounds(420,600,120,50);
        
        this.add(submit);
        this.add(goBack);
    }
    private class Handler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==submit){
                if(castVote()){
                    JOptionPane.showMessageDialog(curr,"Congratulations, your vote has been counted!","Congratulations!",JOptionPane.PLAIN_MESSAGE);
                    ParentContainer.remove(curr);
                    ParentContainer.add(mainpanel);
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
    private boolean castVote(){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        party=new ArrayList();
        candit=new ArrayList();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
            ps=con.prepareStatement("select place from candidates where id=?");
            ps.setInt(1, Integer.parseInt(CandidateID));
            rs=ps.executeQuery();
            rs.next();
            String Place=rs.getString("place");
            ps=con.prepareStatement("insert into Sess"+SessionID+"V values(?,?,?)");
            ps.setString(1, CandidateID);
            ps.setString(2, grp.getSelection().getActionCommand());
            ps.setString(3, Place);
            if(ps.executeUpdate()>0){
                return true;
            }
            else{
                JOptionPane.showMessageDialog(curr, "Vote not recorded!","Warning",JOptionPane.WARNING_MESSAGE);
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
    private void initPartyList(){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        party=new ArrayList();
        candit=new ArrayList();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
            ps=con.prepareStatement("select * from Sess"+SessionID+"P");
            rs=ps.executeQuery();
            while(rs.next()){
                party.add(rs.getString("party"));
                candit.add(rs.getString("cand"));
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(curr, e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
        }
        finally{
            try{
                if(con!=null) con.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException ex){}
        }         
    }
    private String getSessName(String s){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
            ps=con.prepareStatement("select sessName from session_store where sessID=?");
            ps.setString(1, s);
            rs=ps.executeQuery();
            if(rs.next()){
                return rs.getString("sessName");
            }
            else{
                JOptionPane.showMessageDialog(curr, "Failed to load database!","Warning",JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(curr, e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            return null;
        }
        finally{
            try{
                if(con!=null) con.close();
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException ex){}
        } 
    }
    private String getCanName(String s){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
            ps=con.prepareStatement("select nam from candidates where id=?");
            ps.setInt(1, Integer.parseInt(s));
            rs=ps.executeQuery();
            if(rs.next()){
                return rs.getString("nam");
            }
            else{
                JOptionPane.showMessageDialog(curr, "Failed to load database!","Warning",JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(curr, e.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            return null;
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
