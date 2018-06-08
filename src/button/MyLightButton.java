package button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartPanel;

import rs232c.CommunicatorKIKUSUI;

public class MyLightButton extends JButton{
	
	ConfirmState cs;
	
	public MyLightButton(String filename, final CurrentTextBox ctb, final CommunicatorKIKUSUI c, final ConfirmState cs){
		
		super(new ImageIcon(filename));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.cs = cs;
		this.addActionListener(new ActionListener() {
			boolean status;
		       public void actionPerformed(ActionEvent e) {
		 		 c.init();
		 		 
		 		 double setCurrent = mytextToDouble(ctb.getText());
		 		 if(setCurrent <=2.4){
		 			 if(cs.ccdState==false){
		 				 c.setVI(24,setCurrent, "L");
		 				 //c.setI(setCurrent, "Light");
		 			 }
		 			 c.OUTPUTON();
		 			 c.portclose();
		 			 cs.bright = true;
		 			 cs.current = setCurrent;
		 		 }
		 		 else ctb.setText("err");
		       }
		       
		       private double mytextToDouble(String x){
		    	   double ans=0;
		    	   status =true;
		    	   try{ans = Double.parseDouble(x);}
		    	   catch(Exception e){
		    		   status = false;
		    		   System.out.println("Current setting Error");
		    	   }
		    	   
		    	   return ans;
		       }
		    });
	}


}
