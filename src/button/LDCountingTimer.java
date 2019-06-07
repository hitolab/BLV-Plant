package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class LDCountingTimer extends Timer implements ActionListener {

	DisplayLDTime d;
	
	
	public LDCountingTimer(int interval, final DisplayLDTime d){
		super(interval, null);
		this.d =d;
		addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		d.resetTime();

	}

}
