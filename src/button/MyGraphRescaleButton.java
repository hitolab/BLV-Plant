package button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;


public class MyGraphRescaleButton extends JButton{

	private static final long serialVersionUID = 1L;

	public MyGraphRescaleButton(String filename, final JFreeChart[] chartArray){
		
		super(new ImageIcon(filename));
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.addActionListener(new ActionListener() {

		       public void actionPerformed(ActionEvent e) {
		 		 for(int i=0;i<chartArray.length;i++){
		 			XYPlot plot =  (XYPlot) chartArray[i].getPlot();
		 			plot.getDomainAxis().setLowerBound(0);
		 			plot.getDomainAxis().setUpperBound(240);
		 		 }
		    	   
		       }
		       
		     
		    });
	}


}
