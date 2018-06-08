package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class DDDDDLCountingTimer extends Timer implements ActionListener {

	DisplayLDTime d;
	int numberOfDark=0;
	
	
	public DDDDDLCountingTimer(int interval, final DisplayLDTime d){
		super(interval, null);
		this.d =d;
		addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		numberOfDark++;
		if(numberOfDark==1) d.setLD();
		else if(numberOfDark==6) d.setLD();
		else if(numberOfDark>=7) d.resetTime();
	}

}
