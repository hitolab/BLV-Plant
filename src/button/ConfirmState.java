package button;

public class ConfirmState {
	boolean bright=false;
	boolean ccdState=false;
	double current = 0;
	int count = 0;
	
	public ConfirmState(){
		
	}
	
	public void setLightState(boolean x){
		this.bright = x;
	}
	
	public void setCCDState(boolean x){
		this.ccdState = x;
	}
	
	public boolean getLightState(){
		return this.bright;
	}
	
	public boolean getCCDState(){
		return this.bright;
	}
	
	public double getCurrent(){
		return this.current;
	}
}
