package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Timer;

import org.jfree.data.xy.XYSeries;

import rs232c.Communicator;
import rs232c.CommunicatorPhotomul;

public class Measure extends Timer implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	* Constructor.
	*
	* @param interval the interval (in milliseconds)
	*
	*/
	CommunicatorPhotomul c;
	XYSeries dataseries;
	long startTime;
	Measure(int interval,CommunicatorPhotomul c, XYSeries dataseries, long startTime) {
		super(interval, null);
		this.c=c;
		this.dataseries = dataseries;
		this.startTime=startTime;
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent event) {
//		double data = c.measureOnce();
//		long dateinms = new Date().getTime()-startTime;
//		double now = (double) dateinms /1000.0/60.0/60.0;
		c.measureOnce2();
		
//		dataseries.add(now, data);

	}
}
