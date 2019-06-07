package button;

import static main.BLV.curDir;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;





import javax.swing.JButton;
import javax.swing.SwingConstants;

import rs232c.CommunicatorKIKUSUI;


public class MyCCDButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IntervalCCDTextBox ictb;
	CapCCD cccd;
	CommunicatorKIKUSUI c;
	ConfirmState cs;
	
	public int state = 0;
	
	
	public MyCCDButton(IntervalCCDTextBox ictb, CommunicatorKIKUSUI c, ConfirmState cs){
		//super(new ImageIcon(curDir+"/pic/starDDLD.png"));
		this.setText("CCD");
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.ictb = ictb;
		this.c = c;
		this.cs = cs;
		this.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {			 
				if(state == 0) CCDON();
				else CCDOFF();
			 }
		});

	}
	
	public void CCDON(){
		int interval = (int)(1000*60*mytextToDouble(ictb.getText()));
		if(interval > 1000*59){
			this.setText("REC_now");
			cccd = new CapCCD(interval, c, cs);
			cccd.setInitialDelay(0);
			cccd.start();
			this.state = 1;
		}else{
			System.out.println("Please set interval more than 1.0 min");
			ictb.setText("Please set interval more than 1.0 min");
		}
	}
	
	public void CCDOFF(){
		if(cccd.state==true){
			cccd.stop();
			this.setText("CCD");
			this.state = 0;
			cccd.state = false;
		}
	}
	
	private double mytextToDouble(String x){
	 	   double ans=0;
	 	   try{ans = Double.parseDouble(x);}
	 	   catch(Exception e){
	 		   System.out.println("Current setting Error");
	 	   }
	 	   
	 	   return ans;
	    }
	
}
