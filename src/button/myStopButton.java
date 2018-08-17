package button;

import static main.BLV.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import org.jfree.data.xy.XYSeries;
import fileIO.MatrixFileReader;
import fileIO.MyFileWriter;
import main.BLV;
import rs232c.Communicator;

public class myStopButton extends JButton{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Communicator c1,c2,c3;


	public myStopButton(final myMeasureButton mb,final BLV f,final Communicator c1,final Communicator c2,final Communicator c3, final XYSeries dataseries1, final XYSeries dataseries2, final XYSeries dataseries3){
		super(new ImageIcon(curDir+"/pic/stop.png"));
		this.c1 =c1;
		this.c2 =c2;
		this.c3 =c3;
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {//押されたとき
		    	  
		    	   if(mb.t.isRunning()) mb.t.stop();
		    	   if(mb.ti.isRunning()) mb.ti.stop();
		    	   if(mb.tld.isRunning()) mb.tld.stop();
		    	   if(mb.state ==0){}
		    	   else{
		    		   c1.portclose();
		    		   c2.portclose();
		    		   c3.portclose();
		    		   dataseries1.clear();
		    		   dataseries2.clear();
		    		   dataseries3.clear();
			    	   mb.state=0;
	    			   mb.setImage(curDir+"/pic/waitingForSaisei.png");
		       }
		       }
		    });
	}



	static public void rewriteEXPNO(){
		MatrixFileReader mfr = new MatrixFileReader(curDir+"/input/Preference.txt");
		String[] texts = mfr.readString();
		int newexpNo = Integer.parseInt(texts[0])+1;
		MyFileWriter mfw = new MyFileWriter(curDir+"/input/Preference.txt");
		for(int i=0;i<texts.length;i++){
			if(i==0) mfw.writeALine(""+newexpNo);
			else mfw.writeALine(texts[i]);
		}
		mfw.close();
	}


}

