package votingmachine;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.sql.Blob;
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

public class DispMemFrm extends JFrame{
    private DispMemFrm curr;
    private JLabel label,imglabel,sesslabel,canI,canN,canP,canC;
    private JButton sessbtn;
    private JTable table;
    private JTextField tf;
    private JScrollPane scroll;
    private Font f;
    private int scrollcn;
    private Image im;
    
    public DispMemFrm(){
        curr=this;
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(410,100,580,470);
        this.setResizable(false);
        this.setTitle("Display Members");  
        try{
        ImageIcon img = new ImageIcon(ImageIO.read(new File("C:\\Users\\Priyam\\Documents\\NetBeansProjects\\VotingMachine\\src\\votingmachine\\dispmem.jpg")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));      
        this.setContentPane(new JLabel(img));
        }catch(Exception ex){System.out.println(ex);}         
        initcomponents();
    }
    private void initcomponents(){
        this.setLayout(null);
        im=null;
        f=new Font("style",Font.BOLD+Font.ITALIC,30);
        label=new JLabel("Members in Voter List");
        label.setForeground(Color.RED);
        label.setFont(f);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(10,10,560,40);
        this.add(label);
        
        imglabel=new JLabel();
        imglabel.setBounds(340,70,200,200);
        this.add(imglabel);
        
        f=new Font("style",Font.ITALIC+Font.BOLD,15);
        
        canI=new JLabel();
        canI.setFont(f);
        canI.setForeground(Color.blue);
        canI.setBounds(340,290,200,30);
        this.add(canI);  
        
        canN=new JLabel();
        canN.setFont(f);
        canN.setForeground(Color.blue);
        canN.setBounds(340,320,200,30);
        this.add(canN);

        canP=new JLabel();
        canP.setFont(f);
        canP.setForeground(Color.blue);
        canP.setBounds(340,350,200,30);
        this.add(canP);

        canC=new JLabel();
        canC.setFont(f);
        canC.setForeground(Color.blue);
        canC.setBounds(340,380,200,30);
        this.add(canC);        

        sesslabel=new JLabel("Enter Session Code of Candidate to delete :");
        f=new Font("style",Font.ITALIC+Font.BOLD,15);
        sesslabel.setFont(f);
        sesslabel.setBounds(10,70,350,30);
        this.add(sesslabel);
        
        tf=new JTextField();
        tf.setBounds(10,110,200,30);
        this.add(tf);        
        
        sessbtn=new JButton("DELETE");
        sessbtn.setForeground(Color.yellow);
        sessbtn.setBackground(Color.red);
        sessbtn.setBorder(BorderFactory.createLineBorder(Color.red));        
        sessbtn.setFont(f);
        sessbtn.setBounds(10,150,100,30);
        sessbtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!tf.getText().isEmpty()&&deleteRow(tf.getText())>0){
                    canI.setText("");
                    canN.setText("");
                    canP.setText("");
                    canC.setText("");
                    imglabel.setIcon(null);
                    JOptionPane.showMessageDialog(curr, "Candidate deleted successfully!");
                    curr.remove(scroll);
                    curr.add(createTable());
                    curr.revalidate();
                    curr.repaint();
                }
                else{
                    JOptionPane.showMessageDialog(curr, "Recheck Input!");
                }
            }
        });
        this.add(sessbtn);
        this.add(createTable());
               
    }
    private JScrollPane createTable(){
        scrollcn=20;
        String []col_headings={"ID","Name","Place","Contact"};
        String[][]rows=updateTable();
        table=new JTable(rows,col_headings);
        table.setBackground(Color.yellow);
        table.setForeground(Color.red);
        table.getTableHeader().setBackground(Color.blue);
        table.getTableHeader().setForeground(Color.white);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(Color.lightGray);
        table.setEnabled(true);
        ListSelectionModel model=table.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row_index=table.getSelectedRow();
                String _id=table.getModel().getValueAt(row_index, 0).toString();
                String nm=table.getModel().getValueAt(row_index, 1).toString();
                String pl=table.getModel().getValueAt(row_index, 2).toString();
                String cnt=table.getModel().getValueAt(row_index, 3).toString();
                tf.setText(_id);
                canI.setText("Cand Id. : "+_id );
                canN.setText("Name : "+nm);
                canP.setText("Place : "+pl);
                canC.setText("Contact : "+cnt);
                updatePhoto(_id);
                imglabel.setIcon(new ImageIcon(im));
            }
        });
        
        scroll=new JScrollPane(table);
        scroll.setBounds(10,200,300,scrollcn);
        return scroll;
    }
    private void updatePhoto(String cnId){
        ResultSet rs=null;
        Connection con=null;
        PreparedStatement ps=null;
        try{
                Class.forName("com.mysql.jdbc.Driver");
                con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                ps=con.prepareStatement("select img from candidates where id=?");
                ps.setInt(1, Integer.parseInt(cnId));
                rs=ps.executeQuery();
                rs.next();
                Blob b=rs.getBlob("img");
                BufferedInputStream fin=new BufferedInputStream(b.getBinaryStream()); 
                im=ImageIO.read(fin).getScaledInstance(imglabel.getWidth(), imglabel.getHeight(), Image.SCALE_SMOOTH);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        } 
                finally{
                    try{
                        if(con!=null) con.close();
                        if(ps!=null) ps.close();
                    }catch(SQLException es){}
                }            
    }
    private String[][] updateTable(){
        class CandidInfo{
            private String canid,name,place,contact;
            public CandidInfo(String id,String n,String p,String c){
                this.canid=id;
                this.name=n;
                this.place=p;
                this.contact=c;
            }
            public String getid(){
                return canid;
            }
            public String getname(){
                return name;
            }
            public String getplace(){
                return place;
            }
            public String getcontact(){
                return contact;
            }            
        }   
        ArrayList<CandidInfo> list=new ArrayList();                
        try{
            ResultSet rs=getCandidList();
            while(rs.next()){
                String cnid=((Integer)rs.getInt("id")).toString();
                String nm=rs.getString("nam");
                String pl=rs.getString("place");
                String cnt=rs.getString("contact");
                CandidInfo can=new CandidInfo(cnid,nm,pl,cnt);
                list.add(can);
                if(scrollcn<240)
                    scrollcn+=20;
            }
            rs.close();
        }catch(Exception ex){}
        
        int size=list.size();
        String[][] row=new String[size][4];
        int counter=-1;
        Iterator<CandidInfo> itr=list.iterator();
        while(itr.hasNext()){
            CandidInfo element=itr.next();
            counter++;
            row[counter][0]=element.getid();
            row[counter][1]=element.getname();
            row[counter][2]=element.getplace();
            row[counter][3]=element.getcontact();
        }
        return row;
    }
    private ResultSet getCandidList(){
        ResultSet rs=null;
        try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                PreparedStatement ps=con.prepareStatement("select * from candidates");
                rs=ps.executeQuery();
                return rs;
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            return rs;
        }       
    }
    private int deleteRow(String _id){
        Connection con=null;
        PreparedStatement ps=null;
        try{
                Class.forName("com.mysql.jdbc.Driver");
                con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                ps=con.prepareStatement("delete from candidates where id=?");
                ps.setInt(1, Integer.parseInt(_id));
                return ps.executeUpdate();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            return 0;
        }
                finally{
                    try{
                        if(con!=null) con.close();
                        if(ps!=null) ps.close();
                    }catch(SQLException es){}
                }        
    }
}
