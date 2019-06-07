package button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;

import rs232c.*;

import main.*;
import fileIO.FileModifier;
import fileIO.MatrixFileReader;
import fileIO.MyFileWriter;
import static main.BLV.curDir;

public class myStopButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Communicator c;


	public myStopButton(final myMeasureButton mb,final BLV f,final Communicator c, final ChartPanel chart,final XYSeries dataseries,final DisplayEXPNO en){
		super(new ImageIcon(curDir+"/pic/stop.png"));
		this.c =c;
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	   if(mb.t.isRunning()) mb.t.stop();
		    	   if(mb.ti.isRunning()) mb.ti.stop();
		    	   if(mb.state ==0){}
		    	   else{
		    		   c.portclose();	    		   
		    		   rewriteEXPNO2(en);
		    		   dataseries.clear();
			    	   mb.state=0;
	    			   mb.setImage(curDir+"/pic/waitingForSaisei.png");

	    			   
		       }
		       }
		    });
	}
	
	private String createNow(){
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmm");
		return fmt.format(cal.getTime());

	}
	
	static public void rewriteEXPNO2(DisplayEXPNO en){
		en.expnoAdd1();
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

