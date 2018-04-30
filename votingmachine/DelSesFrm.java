package votingmachine;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class DelSesFrm extends JFrame{
    private DelSesFrm curr;
    private JLabel label,deact,dell;
    private JTextField tf,tf1;
    private JButton dct,del;
    private Font f;
    private JTable table;
    private DefaultTableModel dmodel;
    private ListSelectionModel model;
    private JScrollPane scroll;
    private int row_index;
    private String _id,sname;
    private int statt;
    private int scroll_i;
    
    class SessClass{
        String sesid,name;
        int status;
        public SessClass(String id,String n,int sts){
            this.sesid=id;
            this.name=n;
            this.status=sts;
        }
        public String getId(){
            return this.sesid;
        }
        public String getName(){
            return this.name;
        }
        public int getStatus(){
            return this.status;
        }
    }

    public DelSesFrm(){
        row_index=-1;
        scroll_i=20;
        curr=this;
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(475,100,450,500);
        this.setResizable(false);
        this.setTitle("Cease Session");  
        try{
        ImageIcon img = new ImageIcon(ImageIO.read(new File("C:\\Users\\Priyam\\Documents\\NetBeansProjects\\VotingMachine\\src\\votingmachine\\del.jpg")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));      
        this.setContentPane(new JLabel(img));
        }catch(Exception ex){System.out.println(ex);}         
        initcomponents();  
    }
    private void initcomponents(){
        this.setLayout(null);
        f=new Font("hi",Font.BOLD+Font.ROMAN_BASELINE,30);
        label=new JLabel("Cease a Session");
        label.setFont(f);
        deact=new  JLabel("Enter Session Code to deactivate :");
        dell=new  JLabel("Enter Session Code to delete :");
        f=new Font("hi",Font.BOLD+Font.ROMAN_BASELINE,15);
        dell.setFont(f);
        deact.setFont(f);
        dct=new JButton("DEACTIVATE");
        tf=new JTextField();
        tf1=new JTextField();
        del=new JButton("DELETE");
        
        label.setForeground(Color.red);
        dell.setForeground(Color.blue);
        deact.setForeground(Color.blue);
        del.setForeground(Color.white);
        del.setBackground(Color.red);
        del.setBorder(BorderFactory.createLineBorder(Color.red));
        dct.setForeground(Color.white);
        dct.setBackground(Color.red);
        dct.setBorder(BorderFactory.createLineBorder(Color.red));        
        
        label.setBounds(10,10,430,50);
        label.setHorizontalAlignment(JLabel.CENTER);
        deact.setBounds(10,100,250,20);
        dell.setBounds(38,130,250,20);
        tf1.setBounds(260,100,80,20);
        tf.setBounds(260,130,80,20);
        dct.setBounds(345,100,80,20);
        del.setBounds(345,130,60,20);
        
        dct.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(row_index!=-1){
                    deactSession();
                }
            }
        });
        del.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(row_index!=-1){
                    delSession();
                }
            }
        });        
        
        table=new JTable();
        table.setBackground(Color.white);
        table.setForeground(Color.magenta);
        table.getTableHeader().setBackground(Color.green);
        table.getTableHeader().setForeground(Color.blue);  
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);    
        
        initTable();
        
        model=table.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                row_index=table.getSelectedRow();
                _id=table.getModel().getValueAt(row_index, 0).toString();
                sname=table.getModel().getValueAt(row_index, 1).toString();
                statt=Integer.parseInt(table.getModel().getValueAt(row_index, 2).toString());
                tf.setText(_id);
                tf1.setText(_id);
            }
        });   

        scroll=new JScrollPane(table);
        scroll.setBounds(90,180,270,scroll_i);
        
        this.add(label);
        this.add(deact);
        this.add(dct);
        this.add(tf1);        
        this.add(dell);
        this.add(del);
        this.add(tf);
        this.add(scroll);
    }
    private void initTable(){
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        dmodel=new DefaultTableModel();
        table.setModel(dmodel);   
        dmodel.addColumn("Session ID");
        dmodel.addColumn("Session Name");
        dmodel.addColumn("Status");
            try{
                Class.forName("com.mysql.jdbc.Driver");
                con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                ps=con.prepareStatement("select * from session_store");      
                rs=ps.executeQuery();
                String[] rows=new String[3];
                while(rs.next()){
                    String v1=rs.getString("sessID");
                    String v2=rs.getString("sessName");
                    Integer v3=rs.getInt("stat");
                    rows[0]=v1;
                    rows[1]=v2;
                    rows[2]=v3.toString();
                    dmodel.addRow(rows);
                    if(scroll_i<140){
                        scroll_i+=16;
                    }                    
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Failed to load Database!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
                finally{
                    try{
                        if(con!=null) con.close();
                        if(ps!=null) ps.close();
                        if(rs!=null) rs.close();
                    }catch(SQLException es){}
                }            
    }
    private void deactSession(){
        Connection con=null;
        PreparedStatement ps=null;
            try{
                Class.forName("com.mysql.jdbc.Driver");
                con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                ps=con.prepareStatement("commit");
                ps.execute();
                ps=con.prepareStatement("update session_store set stat=0 where sessID=?");
                ps.setString(1, _id);
                ps.executeUpdate();
                dmodel.setValueAt("0", row_index, 2);
                curr.remove(scroll);
                curr.add(scroll);
                curr.revalidate();
                curr.repaint();
                
            }catch(Exception ex){
                try{
                    ps=con.prepareStatement("rollback");
                    ps.execute();
                }catch(Exception ee){}
                JOptionPane.showMessageDialog(this, "Server Failed!\n"+ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }   
                finally{
                    try{
                        if(con!=null) con.close();
                        if(ps!=null) ps.close();
                    }catch(SQLException es){}
                }            
    }
    private void delSession(){
        Connection con=null;
        PreparedStatement ps=null;
            try{
                Class.forName("com.mysql.jdbc.Driver");
                con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                ps=con.prepareStatement("commit");
                ps.execute();
                ps=con.prepareStatement("delete from session_store where sessID=?");
                ps.setString(1, _id);
                if(ps.executeUpdate()==1){
                    ps=con.prepareStatement("drop table "+"sess"+_id+"V");
                    if(!ps.execute()){
                        ps=con.prepareStatement("drop table "+"sess"+_id+"P");
                        ps.execute();
                    }
                }
                                    
                curr.remove(scroll);
                if(scroll_i>20){
                    scroll_i-=16;
                }            
                try{dmodel.removeRow(row_index);}catch(ArrayIndexOutOfBoundsException ae){}
                scroll.setBounds(90,180,270,scroll_i);
                curr.add(scroll);
                curr.revalidate();
                curr.repaint();
            }
            catch(Exception ex){
                try{
                    ps=con.prepareStatement("rollback");
                    ps.execute();
                }catch(Exception ee){}
                JOptionPane.showMessageDialog(this, "Server Failed!\n"+ex, "Warning", JOptionPane.WARNING_MESSAGE);
            }
                finally{
                    try{
                        if(con!=null) con.close();
                        if(ps!=null) ps.close();
                    }catch(SQLException es){}
                }   
    }    
}
