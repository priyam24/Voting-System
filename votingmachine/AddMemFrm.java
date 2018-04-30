package votingmachine;

import com.mysql.jdbc.StringUtils;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddMemFrm extends JFrame{
    private VotingMachine ParentContainer;
    private JPanel panel;
    private JLabel label,name,cand_id,place,contact,upload;
    private JTextField nametf,idtf,contacttf;
    private JComboBox combox;
    private JButton submit,upld;
    private Font f;
    private AddMemFrm curr;
    private JFileChooser fc;
    private FileInputStream fin;
    private File file;
    
    public AddMemFrm(VotingMachine v){
        curr=this;
        ParentContainer=v;
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(475,100,450,500); //left area, top area, width, height
        this.setResizable(false);
        this.setTitle("Add Member");  
        try{
        ImageIcon img = new ImageIcon(ImageIO.read(new File("C:\\Users\\Priyam\\Documents\\NetBeansProjects\\VotingMachine\\src\\votingmachine\\addmem.jpg")).getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));      
        this.setContentPane(new JLabel(img));
        }catch(Exception ex){System.out.println(ex);}         
        initcomponents();
    }
    private void initcomponents(){
        f=new Font("style",Font.BOLD+Font.ITALIC,20);
        
        
        this.setLayout(null);
        label=new JLabel("CREATE NEW CANDIDATE");
        label.setFont(f);
        label.setForeground(Color.white);
        label.setBounds(100,10,300,50);
        this.add(label);
        
        submit=new JButton("Submit");
        submit.setBounds(150,350,150,50);
        submit.setFont(f);
        submit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String n=nametf.getText();
                String id=idtf.getText().trim();
                String plc=combox.getSelectedItem().toString();
                String cont=contacttf.getText();
                ////////////////// Checking if the image file chosen is an image or not /////////////////
                String filename=file.getName();
                String extension=filename.substring(filename.lastIndexOf("."));
                try{
                if(!(extension.equals(".jpg")||extension.equals(".png")||extension.equals(".jpeg"))){
                    JOptionPane.showMessageDialog(submit.getParent(), "Please choose an image file!");
                    return;
                }
                }catch(Exception e1){}
                /////////////////////////////////////////////////////////////////////////////////////////                
                
                if(n.isEmpty()||id.isEmpty()||cont.isEmpty()){
                    JOptionPane.showMessageDialog(submit.getParent(), "Please enter all the fields!");
                }
                else if(!StringUtils.isStrictlyNumeric(id)||!StringUtils.isStrictlyNumeric(cont)){
                    JOptionPane.showMessageDialog(submit.getParent(), "Only numeric characters allowed for Candidate ID and Contact!");
                }
                else if(cont.length()!=10){
                     JOptionPane.showMessageDialog(submit.getParent(), "Please enter a 10 digit phone number!");
                }
                else{
                    Connection con=null;
                    PreparedStatement ps=null;
                    ResultSet rs=null;
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        con=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vote","root","03324682425");
                        ps=con.prepareStatement("select id from candidates where id=?");
                        ps.setInt(1,Integer.parseInt(id));
                        rs=ps.executeQuery();
                        if(rs.next()){
                            JOptionPane.showMessageDialog(submit.getParent(), "Candidate already registered", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                        else{
                            ps=con.prepareStatement("insert into candidates values(?,?,?,?,?)");
                            ps.setInt(1,Integer.parseInt(id));
                            ps.setString(2, n);
                            ps.setString(3, plc);
                            ps.setString(4, cont);
                            ps.setBlob(5, fin);
                            if(ps.executeUpdate()>0){
                                JOptionPane.showMessageDialog(submit.getParent(), "Candidate registered successfully", "Message", JOptionPane.PLAIN_MESSAGE);
                                curr.dispose();
                                ParentContainer.setEnabled(true);
                            }
                        }
                    } catch (Exception ex) {
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
            
        });         
        this.add(submit);        
        
        panel=new JPanel();
        panel.setBounds(95,100,260,200);
        this.add(panel);
        panel.setBackground(Color.green);
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
                c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(5,5,5,5);
		c.anchor = GridBagConstraints.EAST;
                //c.fill = GridBagConstraints.HORIZONTAL;
        
        name=new JLabel("Name :");
        name.setFont(f);
        name.setForeground(Color.red);
        panel.add(name,c);      
        
        cand_id=new JLabel("Candidate ID :");
        c.gridy=1;
        cand_id.setFont(f);
        cand_id.setForeground(Color.red);
        panel.add(cand_id,c);
        
        place=new JLabel("Place :");
        c.gridy=2;
        place.setFont(f);
        place.setForeground(Color.red);
        panel.add(place,c);
        
        contact=new JLabel("Contact :");
        c.gridy=3;
        contact.setFont(f);
        contact.setForeground(Color.red);
        panel.add(contact,c);

        upload=new JLabel("Prof. Image :");
        c.gridy=4;
        upload.setFont(f);
        upload.setForeground(Color.red);
        panel.add(upload,c);        
        
        nametf=new JTextField();
        c.anchor = GridBagConstraints.WEST;
        c.gridy=0;
        c.gridx=1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nametf,c);  

        idtf=new JTextField();
        c.gridy=1;
        panel.add(idtf,c);
        
        String []dest={"Kolkata","Behala","Jadavpur","Dumdum","Narendrapur"};
        combox=new JComboBox(dest);         
        c.gridy=2;
        panel.add(combox,c);
        
        contacttf=new JTextField();
        c.gridy=3;
        panel.add(contacttf,c);
        
        upld=new JButton("Choose");
        c.gridy=4;
        panel.add(upld,c);      
        
        upld.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    fc=new JFileChooser();
                    int i=fc.showSaveDialog(null);
                    if(i==JFileChooser.APPROVE_OPTION){
                        file=fc.getSelectedFile();
                        fin=new FileInputStream(file);
                        upld.setText("Done");
                    }
                }catch(IOException io){}
            }
        });
               
    }
}
