package button;

import java.awt.Color;
import java.util.Date;

import javax.swing.JLabel;

public class DisplayLDTime extends JLabel {
	
	long endTime;
	boolean bright=false;
	int cycleTime;

	public DisplayLDTime(){
		super("00:00:00");
		this.setOpaque(true);
		this.setBackground(Color.white);
		this.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void setcycleTime(int time){
		this.cycleTime = time;
	}
	public void resetTime(){
		long timems = endTime - this.getNow();

		int ms = (int)timems % 1000;
		int sec = (int) (timems -ms)/1000 % 60;
		int min = (int) (timems -ms -sec*1000)/(1000*60) % 60;
		int hour = (int)(timems -ms -sec*1000 - min*1000*60)/(1000*60*60);
		if(bright) super.setText("L "+hour+":"+min+":"+sec);
		else super.setText("D "+hour+":"+min+":"+sec);
	}
	
	public void setDDDDD(){
		this.cycleTime = 1000*60*60*12*5;
		this.setLightState(false);
		this.setEndTime();
	}
	
	public void setLD(){
		this.cycleTime = 1000*15;
		//this.cycleTime = 1000*60*60*12*5;
		this.setLightState(true);
		this.setEndTime();
	}
	
	public void setLightState(boolean x){
		this.bright = x;
	}
	
	public void setEndTime(){
		endTime = new Date().getTime()+cycleTime;
	}
	
	private long getNow(){
		return  new Date().getTime();
	}
	
	public void clear(){
		super.setText("00:00:00");
	}
}
