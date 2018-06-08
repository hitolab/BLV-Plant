package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.Timer;

import org.jfree.data.xy.XYSeries;

import rs232c.CommunicatorArduino;
import rs232c.CommunicatorKIKUSUI;

public class CapCCD extends Timer implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	CommunicatorArduino ar;
	CommunicatorKIKUSUI c;
	ConfirmState cs;
	public boolean state; // working: true, waiting: false;
	long startTime;

	
	public CapCCD(int interval, CommunicatorKIKUSUI c, ConfirmState cs) {
		super(interval, null);
		addActionListener(this);
		this.state = false;
		this.c = c;
		this.cs = cs;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.state = true;
		CommunicatorArduino ar = new CommunicatorArduino(5);
		
		cs.ccdState=true;
		if(cs.bright==true){ //ñæÇÈÇ¢Ç»ÇÁà√Ç≠Ç∑ÇÈ
			c.init();
			c.setVI(0, 0, "ccdstart");
			c.portclose();
		}
		try {
			Thread.sleep(1000*47); //1000*17Ç≈ñÒÇQÇOïbÇÃÉ_Å[ÉNÅ®éBâe
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		ar.init();
		ar.wait(1000);
		ar.sendcommand("q"); //ArduinoÇ…CCDÉJÉÅÉâÇÇ¬ÇØÇÈÇÊÇ§Ç…éwé¶
		ar.wait(1000);
		ar.portclose();
		
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		
		if(cs.bright==true){		//Ç‡Çµcs.bright=trueÇ»ÇÁÇkÇ…Ç∑ÇÈ
			c.init();
			c.setVI(24, cs.current, "CCD");
			//c.setV(mytextToDouble(ctb.getText()));
			c.OUTPUTON();
			c.portclose();
		}
		
		cs.ccdState=false;
	}


}
