package votingmachine;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Priyam
 */
public class VotingMachine extends JFrame{
    
    public VotingMachine(){
        initcomponents();
    }
    
    private void initcomponents(){
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(400,20,600,700); //left area, top area, width, height
        this.setResizable(false);
        this.setTitle("RoyVotingZone");    
        ImageIcon icon=new ImageIcon(getClass().getResource("vot.jpg"));
        this.setIconImage(icon.getImage());          
    }
    
    

    public static void main(String[] args) {
        VotingMachine VM=new VotingMachine();
        MainPanel mpanel=new MainPanel(VM);
        VM.add(mpanel);
    }
}
