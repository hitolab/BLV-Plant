package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.swing.Timer;

import static main.BLV.curDir; 

public class BrightningIcon extends Timer implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final myMeasureButton b;
	boolean light;
	
	public BrightningIcon(int interval, final myMeasureButton b){
		super(interval, null);
		this.b =b;
		light = false;
		addActionListener(this);
	}
	


	public void actionPerformed(ActionEvent e) {
		if(light == false){
			b.setImage(curDir+"/pic/saisei.png");
			light = true;
		}
		else{
			b.setImage(curDir+"/pic/waitingForSaisei.png");
			light = false;
		}
		
	}
}
