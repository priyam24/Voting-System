package votingmachine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class CreateSess extends JFrame{
    private VotingMachine ParentContainer;
    private CreateSess curr;
    private JLabel label,sess_code,name;
    private JTextField sesstf,nametf;
    private JButton add,submit,del;
    private Font f;
    private Panel panel;
    private JTable table;
    private JScrollPane scroll;
    private ArrayList<CandidInfo> list;
    private DefaultTableModel dmodel;
    private ListSelectionModel model;
    private int scroll_ht,row_index;
    private int party_count;
    
    class CandidInfo{
        private final String party;
        private final String can;
        public CandidInfo(String p,String c){
            this.party=p;
            this.can=c;
        }
        public String getParty(){
            return this.party;
        }
        public String getCan(){
            return this.can;
        }
    }
    
    public CreateSess(VotingMachine v){
        row_index=-1;
        scroll_ht=20;
        party_count=0;
        curr=this;
        ParentContainer=v;
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(475,100,450,520); //left area, top area, width, height
        this.setResizable(false);
        this.setTitle("Create Session");  
        try{
        ImageIcon img = new ImageIcon(ImageIO.read(new File("C:\\Users\\Priyam\\Documents\\NetBeansProjects\\VotingMachine\\src\\votingmachine\\sess1.jpg")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));      
        this.setContentPane(new JLabel(img));
        }catch(Exception ex){System.out.println(ex);}        
        initcomponents();
        
    }
    private void initcomponents(){
        list=new ArrayList();
        this.setLayout(null);
        f=new Font("style",Font.BOLD+Font.TRUETYPE_FONT,30);
        label=new JLabel("Create a Voting Session");
        label.setBounds(10,10,450,35);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        label.setFont(f);
        this.add(label);        
        
        f=new Font("style",Font.BOLD+Font.TYPE1_FONT,20);
        sess_code=new JLabel("Session Code :");
        sess_code.setBounds(10,70,150,20);
        sess_code.setForeground(Color.YELLOW);
        sess_code.setFont(f);
        this.add(sess_code);  
        
        name=new JLabel("Session Name:");
        name.setBounds(10,120,150,20);
        name.setForeground(Color.YELLOW);
        name.setFont(f);
        this.add(name);         
        
        sesstf=new JTextField();
        sesstf.setBounds(170,70,100,20);
        this.add(sesstf); 
        
        nametf=new JTextField();
        nametf.setBounds(170,120,100,20);
        this.add(nametf);        
        
        Handler handler=new Handler();
        
        add=new JButton("Add Party");
        add.setBounds(80,170,100,30);
        add.setForeground(Color.white);
        add.setBackground(Color.red);
        add.setBorder(BorderFactory.createLineBorder(Color.red));
        add.addActionListener(handler);
        this.add(add);  
        
        del=new JButton("Delete selected row");
        del.setBounds(190,170,135,30);
        del.setForeground(Color.white);
        del.setBackground(Color.red);
        del.setBorder(BorderFactory.createLineBorder(Color.red));
        del.addActionListener(handler);
        this.add(del);          
        
        submit=new JButton("SUBMIT");
        submit.setBounds(160,440,100,45);
        submit.setForeground(Color.white);
        submit.setBackground(Color.blue);
        submit.setBorder(BorderFactory.createLineBorder(Color.blue));
        submit.addActionListener(handler);
        this.add(submit);    
        
        panel=new Panel();
        panel.setLayout(null);
        panel.setBounds(10,225,420,200);
        table=new JTable();
        table.setBackground(Color.yellow);
        table.setForeground(Color.red);
        table.getTableHeader().setBackground(Color.green);
        table.getTableHeader().setForeground(Color.blue);        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String[] cols={"Party","Candidate"};
        dmodel=new DefaultTableModel(null,cols);
        table.setModel(dmodel);       
        
        model=table.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                row_index=table.getSelectedRow();
            }
        });        
        scroll=new JScrollPane(table);
        
        this.add(panel);
    }
    class Handler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==add){
                curr.setEnabled(false);
                JFrame frame=new JFrame();
                frame.setVisible(true);
                frame.getContentPane().setBackground(Color.red);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setBounds(550,200,300,200); //left area, top area, width, height
                frame.setResizable(false);
                frame.setTitle("Party Information");
                frame.setLayout(null);
                
                JLabel partyname=new JLabel("Party Name :");
                partyname.setBounds(10,10,100,20);
                partyname.setForeground(Color.white);
                frame.add(partyname);
                
                JTextField partytf=new JTextField();
                partytf.setBounds(120,10,150,20);
                frame.add(partytf);       

                JLabel Candidatename=new JLabel("Candidate Name :");
                Candidatename.setBounds(10,50,100,20);
                Candidatename.setForeground(Color.white);
                frame.add(Candidatename); 
                
                JTextField cantf=new JTextField();
                cantf.setBounds(120,50,150,20);
                frame.add(cantf);     
                
                JButton sub=new JButton("Submit");
                sub.setBounds(100,105,100,45);
                frame.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e) {
                        curr.setEnabled(true);
                    }                    
                });        
                sub.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(partytf.getText().isEmpty()||cantf.getText().isEmpty()){
                            JOptionPane.showMessageDialog(frame, "Please enter all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        Iterator<CandidInfo> it=list.iterator();
                        while(it.hasNext()){
                            CandidInfo ci=it.next();
                            if(ci.getParty().equals(partytf.getText())){
                                JOptionPane.showMessageDialog(frame, "Party already included!", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            if(ci.getCan().equals(cantf.getText())){
                                JOptionPane.showMessageDialog(frame, "Candidate already representing a different party!", "Warning", JOptionPane.WARNING_MESSAGE);
                                return;
                            }                            
                        }
                        String[] t={partytf.getText(),cantf.getText()};
                        list.add(new CandidInfo(partytf.getText(),cantf.getText()));
                        frame.dispose();
                        curr.setEnabled(true);
                        
                         dmodel.addRow(t);
                         if(scroll_ht<180){
                             scroll_ht+=16;
                         }
                         panel.remove(scroll);
                         scroll.setBounds(90,10,200,scroll_ht);
                         panel.add(scroll);
                         panel.revalidate();                                                  
                         panel.repaint();
                         party_count++;
                    }
                });
                frame.add(sub);
                
            }
            else if(e.getSource()==del){
                if(row_index!=-1){
                    list.remove(row_index);
                    dmodel.removeRow(row_index);
                    scroll_ht-=16;
                    scroll.setBounds(90,10,200,scroll_ht);
                    panel.revalidate();
                    panel.add(scroll);
                    panel.repaint();
                    
                }                
            }
            else if(e.getSource()==submit){
                if(sesstf.getText().isEmpty()||nametf.getText().isEmpty()){
                    JOptionPane.showMessageDialog(submit.getParent(), "Please enter all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(party_count<2){
                    JOptionPane.showMessageDialog(submit.getParent(), "Please include at least 2 parties", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                PreparedStatement ps=null;
                Connection con=null;
                ResultSet rs=null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                    ps=con.prepareStatement("commit"); //commit the current state
                    ps.execute();
                    ps=con.prepareStatement("select sessID from session_store where sessID=?");
                    ps.setString(1, sesstf.getText());
                    rs=ps.executeQuery();
                    if(rs.next()){
                        JOptionPane.showMessageDialog(submit.getParent(), "Session already exists", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    else{
                        ps=con.prepareStatement("insert into session_store values(?,?,?)");
                        ps.setString(1, sesstf.getText());
                        ps.setString(2, nametf.getText());
                        ps.setInt(3, 1);
                        if(ps.executeUpdate()>0){
                            curr.dispose();
                            ParentContainer.setEnabled(true);
                            ps=con.prepareStatement("create table "+"Sess"+sesstf.getText()+"V"+" (canID varchar(10) PRIMARY KEY, party varchar(100), dist varchar(100))");
                            if(!ps.execute()){
                                ps=con.prepareStatement("create table "+"Sess"+sesstf.getText()+"P"+" (party varchar(100) PRIMARY KEY, cand varchar(100))");
                                if(!ps.execute()){
                                    for(int i=0;i<list.size();i++){
                                        ps=con.prepareStatement("insert into "+"Sess"+sesstf.getText()+"P"+" values(?,?)");
                                        ps.setString(1, list.get(i).getParty());
                                        ps.setString(2, list.get(i).getCan());
                                        ps.executeUpdate();
                                    }
                                    JOptionPane.showMessageDialog(submit.getParent(), "Session created successfully", "Message", JOptionPane.PLAIN_MESSAGE);
                                }     
                                else{
                                    JOptionPane.showMessageDialog(submit.getParent(), "Problem occured while session creation", "Warning", JOptionPane.WARNING_MESSAGE);
                                }
                            }    
                            else{
                                JOptionPane.showMessageDialog(submit.getParent(), "Problem occured while session creation", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(submit.getParent(), "Problem occured while session creation", "Warning", JOptionPane.WARNING_MESSAGE);
                        }                        
                    }
                }catch(Exception ex){
                    try{
                        ps=con.prepareStatement("rollback");
                        ps.execute();
                    }catch(SQLException ee){}
                    JOptionPane.showMessageDialog(submit.getParent(), ex.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
                }
                finally{
                    try{
                        if(con!=null) con.close();
                        if(ps!=null) ps.close();
                        if(rs!=null) rs.close();
                    }catch(SQLException es){}
                }
            }
        }
        
    }
class Panel extends JPanel{
    private Image imag;
    
    public Panel(){
        imag=new ImageIcon(getClass().getResource("sess2.jpg")).getImage();
        this.setLayout(new BorderLayout()); 
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(imag,0,0,getWidth(),getHeight(),null);
    }      
}    
}
