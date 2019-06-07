package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Timer;

import org.jfree.data.xy.XYSeries;

import rs232c.Communicator;
import rs232c.CommunicatorKIKUSUI;

public class DDDDDLCycle extends Timer implements ActionListener {

	CommunicatorKIKUSUI c;
	XYSeries dataseries;
	long startTime;
	double LightCurrent = 1.4;
	public boolean state;
	DisplayLDTime ldt;
	CurrentTextBox ctb;
	public int dtimes=0; //D‚ð‰½‰ñŒoŒ±‚µ‚½‚©B
	ConfirmState cs;

	DDDDDLCycle(int interval, CommunicatorKIKUSUI c, XYSeries dataseries, long startTime, DisplayLDTime ldt, CurrentTextBox ctb, ConfirmState cs) {
		super(interval, null);
		this.c=c;
		this.dataseries = dataseries;
		this.startTime=startTime;
		addActionListener(this);
		state=false;
		this.ldt = ldt;
		this.ctb = ctb;
		this.cs = cs;

	}



	public void turnONLight(){
		double data =0;
		c.init();
		ctb.setText(""+LightCurrent);
		if(cs.ccdState=false){
			c.setVI(24,LightCurrent, "DDDDDL");
		}
		cs.bright = true;
		cs.current = LightCurrent;
		//c.setV(mytextToDouble(ctb.getText()));
		c.OUTPUTON();
		data = c.getI();
		c.portclose();


		long dateinms = new Date().getTime()-startTime;
		double now = (double) dateinms /1000.0/60.0;
		dataseries.add(now, data);

	}

	public void turnOFFLight(){
		double data=0;
		c.init();
		c.OUTPUTOFF("DDDDDL");
		c.portclose();
		
		cs.bright = false;

		long dateinms = new Date().getTime()-startTime;
		double now = (double) dateinms /1000.0/60.0;
		dataseries.add(now, data);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		dtimes++;
		if(dtimes==1) ldt.setDDDDD();
		if(dtimes>=6){
			ldt.setcycleTime(1000*60*60*12);
			ldt.setEndTime();
			if(!state) {
				ldt.setLightState(true);
				turnONLight();	
			}
			else {
				ldt.setLightState(false);
				turnOFFLight();
			}
			if(state) state = false;
			else state = true;
		}
	}
}
