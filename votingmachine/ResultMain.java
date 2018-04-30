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
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class ResultMain extends JPanel{
    private VotingMachine ParentContainer;
    private AdminMain adminmain;
    private ResultMain curr;
    private Font f;
    private Image img;
    private JLabel label,sesslabel,plclabel;
    private JButton submit,goBack;
    private JTextField tf;
    private JComboBox combox;
    private ArrayList<PartyResult> parties;
    private ChartPanel chartpanel;
    private JTable table;
    private DefaultTableModel dmodel;
    private JScrollPane scroll;
    private int rscroll;
    
    class PartyResult{
        private String name;
        private int voteCount;
        public PartyResult(String n,int m){
            this.name=n;
            this.voteCount=m;
        }
        public String getName(){
            return this.name;
        }
        public int getCount(){
            return this.voteCount;
        }
    }
    
    public ResultMain(VotingMachine v,AdminMain am){
        ParentContainer=v;
        adminmain=am;
        curr=this;
        chartpanel=null;
        scroll=null;
        img=new ImageIcon(getClass().getResource("result.jpg")).getImage();
        this.setLayout(null);
        
        f=new Font("style",Font.BOLD+Font.PLAIN,30);
        
        label=new JLabel("CHECK RESULT");
        label.setForeground(Color.white);
        label.setFont(f);
        label.setBounds(200,20,400,50);
        
        sesslabel=new JLabel("Enter Session Code :");
        sesslabel.setForeground(Color.yellow);
        f=new Font("style",Font.BOLD+Font.ITALIC,20);
        sesslabel.setFont(f);
        sesslabel.setBounds(10,100,200,30);
        
        plclabel=new JLabel("Choose Zone :");
        plclabel.setForeground(Color.yellow);
        plclabel.setFont(f);
        plclabel.setBounds(10,190,200,30);   
        
        tf=new JTextField();
        tf.setBounds(10,140,200,30);
        
        String []dest={"Overall","Kolkata","Behala","Jadavpur","Dumdum","Narendrapur"};
        combox=new JComboBox(dest); 
        combox.setBounds(10,230,150,30);
        
        this.add(label);
        this.add(sesslabel);
        this.add(plclabel);
        this.add(tf);
        this.add(combox);
        
        f=new Font("style",Font.BOLD+Font.ITALIC,12);
        
        submit=new JButton("SHOW RESULT");
        submit.setForeground(Color.red);
        submit.setBackground(Color.yellow);
        submit.setBorder(BorderFactory.createLineBorder(Color.yellow));
        submit.setFont(f);

        goBack=new JButton("RETURN TO ADMIN PAGE");
        goBack.setForeground(Color.white);
        goBack.setBackground(Color.red);
        goBack.setBorder(BorderFactory.createLineBorder(Color.red));
        goBack.setFont(f);

        Handler handler=new Handler();
        
        submit.addActionListener(handler);
        goBack.addActionListener(handler);
        
        submit.setBounds(10,280,120,30);
        goBack.setBounds(175,620,250,40);
        
        this.add(submit);
        this.add(goBack);        
        
    }
    private class Handler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==submit){
                if(tf.getText().isEmpty()){
                    JOptionPane.showMessageDialog(curr,"Please enter all the fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Connection con=null;
                PreparedStatement ps=null;
                ResultSet rs=null;
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                    ps=con.prepareStatement("select * from session_store where sessID=?");
                    ps.setString(1, tf.getText());
                    rs=ps.executeQuery();
                    if(rs.next()){
                        if(rs.getInt("stat")==1){
                            JOptionPane.showMessageDialog(curr, "This Session is currently Active","Message",JOptionPane.PLAIN_MESSAGE);
                        }
                        initResult();
                        rscroll=20;
                        showTable();                        
                        showPieChart();
                    }
                    else{
                        JOptionPane.showMessageDialog(curr, "Please Re-check Session Code!","Warning",JOptionPane.WARNING_MESSAGE);
                    }
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(curr, ex,"Warning",JOptionPane.WARNING_MESSAGE);
                }
                finally{
                    try{
                        if(con!=null) con.close();
                        if(ps!=null) ps.close();
                        if(rs!=null) rs.close();
                    }catch(SQLException ex){}
                }                    
            }
            else if(e.getSource()==goBack){
                ParentContainer.remove(curr);
                ParentContainer.add(adminmain);
                ParentContainer.revalidate();
                ParentContainer.repaint();             
            }
        }
    }
    private void showTable(){
        if(scroll!=null){
            this.remove(scroll);
        }
        
        table=new JTable();
        table.setBackground(Color.white);
        table.setForeground(Color.black);
        table.getTableHeader().setBackground(Color.blue);
        table.getTableHeader().setForeground(Color.white);  
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        
        dmodel=new DefaultTableModel();
        dmodel.addColumn("Party");
        dmodel.addColumn("Votes");

        Iterator<PartyResult> itr=parties.iterator();

        while(itr.hasNext()){
            PartyResult obtemp=itr.next();
            Integer tt=obtemp.getCount();
            String[] temp={obtemp.getName(),tt.toString()};
            dmodel.addRow(temp);
            if(rscroll<180)
                rscroll+=18;
        }
        
        table.setModel(dmodel);
        scroll=new JScrollPane(table);
        scroll.setBounds(350,100,235,rscroll);
        this.add(scroll);       
        this.revalidate();
        this.repaint();
    }
    private void showPieChart(){
        DefaultPieDataset dataset=new DefaultPieDataset();
        for(int i=0;i<parties.size();i++){
            dataset.setValue(parties.get(i).getName(), parties.get(i).getCount());
        }
        JFreeChart chart=ChartFactory.createPieChart3D(combox.getSelectedItem().toString(), dataset,true,true,true);
        if(chartpanel!=null){
            this.remove(chartpanel);
        }
        chartpanel= new ChartPanel(chart);
        chartpanel.setBounds(10,320,575,280);
        this.add(chartpanel);
        this.revalidate();
        this.repaint();
        /*ChartFrame frm=new ChartFrame("Result",chart);
        frm.setBounds(475,100,450,450);
        frm.setVisible(true);      
        frm.getContentPane().setBackground(Color.red);
        frm.setLayout(null);*/
    }
    private void initResult(){
                Connection con=null;
                PreparedStatement ps1=null,ps2=null;
                ResultSet rs1=null,rs2=null;
                parties=new ArrayList();
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                    ps1=con.prepareStatement("select party from Sess"+tf.getText()+"P");
                    rs1=ps1.executeQuery();
                    while(rs1.next()){
                        String d=combox.getSelectedItem().toString();
                        String pt=rs1.getString("party");
                        if(d.equals("Overall")){
                            ps2=con.prepareStatement("select count(party) from Sess"+tf.getText()+"V where party=?");
                            ps2.setString(1, pt);
                        }
                        else{
                            ps2=con.prepareStatement("select count(party) from Sess"+tf.getText()+"V where party=? and dist=?");
                            ps2.setString(1, pt);
                            ps2.setString(2, d);
                        }
                        rs2=ps2.executeQuery();
                        rs2.next();
                        parties.add(new PartyResult(pt,rs2.getInt("count(party)")));
                    }
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(curr,ex,"Warning",JOptionPane.WARNING_MESSAGE);
                }
                finally{
                    try{
                        if(con!=null) con.close();
                        if(ps1!=null) ps1.close();
                        if(ps2!=null) ps2.close();
                        if(rs1!=null) rs1.close();
                        if(rs2!=null) rs2.close();
                    }catch(SQLException ex){}
                }           
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(img,0,0,getWidth(),getHeight(),null);
    }     
}
