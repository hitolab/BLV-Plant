package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Timer;

import org.jfree.data.xy.XYSeries;

import rs232c.Communicator;
import rs232c.CommunicatorKIKUSUI;

public class LDCycle extends Timer implements ActionListener {
	
	CommunicatorKIKUSUI c;
	XYSeries dataseries;
	long startTime;
	double LightCurrent = 1.8;
	public boolean state;
	DisplayLDTime ldt;
	CurrentTextBox ctb;
	DelayLDTimeTextBox dlttb;
	ConfirmState cs;
	LDCycleCounterTextBox ldcctb;
	int setldtimes;

	LDCycle(int interval, CommunicatorKIKUSUI c, XYSeries dataseries, long startTime, DisplayLDTime ldt, CurrentTextBox ctb, ConfirmState cs, LDCycleCounterTextBox ldcctb) {
		super(interval, null);
		this.c=c;
		this.dataseries = dataseries;
		this.startTime=startTime;
		addActionListener(this);
		state=false;
		this.ldt = ldt;
		this.ctb = ctb;
		this.cs = cs;
		
		//test
		this.ldcctb = ldcctb;
		setldtimes = mytextToInt(ldcctb.getText());
	}
	

	
	
	
	

	public void turnONLight(){
		double data =0;
		double current =0;
		//èââÒÇ‡ÇµÇ≠ÇÕÅACCDState=falseÇ»ÇÁÇkÇ…Ç∑ÇÈ
		current = mytextToDouble(ctb.getText());
		c.init();
		
		if(cs.ccdState=false || cs.count == 0){
			c.setVI(24,current, "LD");
		}
		c.OUTPUTON();
		data = c.getI();
		c.portclose();
		
		if(cs.count == 0){
			cs.count = 1;
		}
			//c.setV(mytextToDouble(ctb.getText()));


		cs.current = current;
		cs.bright = true;
		

		long dateinms = new Date().getTime()-startTime;
		double now = (double) dateinms /1000.0/60.0;
		dataseries.add(now, data);

	}
	
	public void turnOFFLight(){
		double data=0;
		c.init();
		c.OUTPUTOFF("LD");
		c.portclose();
		
		cs.bright = false;
		
		long dateinms = new Date().getTime()-startTime;
		double now = (double) dateinms /1000.0/60.0;
		dataseries.add(now, data);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ldt.setEndTime();
		
		if(setldtimes != 0){
		
			if(!state) {
				//ldt.setLightState(true);
				//turnONLight();
				ldt.setLightState(false);
				turnOFFLight();
			}
			else {
				//ldt.setLightState(false);
				//turnOFFLight();
				ldt.setLightState(true);
				turnONLight();
				setldtimes = setldtimes-1;
			}
			if(state) state = false;
			else state = true;

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
	
	
	private int mytextToInt(String x){
	 	   int ans=0;
	 	   try{ans = Integer.parseInt(x);}
	 	   catch(Exception e){
	 		   System.out.println("LDtimes setting Error");
	 	   }
	 	   
	 	   return ans;
	    }
}
