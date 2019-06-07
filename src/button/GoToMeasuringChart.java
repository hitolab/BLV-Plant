package button;

import static main.BLV.curDir;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;

import fileIO.MatrixFileReader;

public class GoToMeasuringChart extends JButton {
	
	
	public GoToMeasuringChart(String filename, final myMeasureButton mmb, final XYSeries dataseries,final int portNo){
		super(new ImageIcon(filename));
		
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.addActionListener(new ActionListener() {
	       public void actionPerformed(ActionEvent e) {
	 		 if(mmb.state==0){ //Not Measuring
	 			 dataseries.clear();
	 		 }
	 		 else{ //Measuring
		    	   dataseries.clear();
		    	   int nowEXPNO =readEXPNO()-1;
		    	   String datafilename=curDir+"\\result\\"+portNo+"\\EXP"+String.format("%04d", nowEXPNO)+"_"+portNo+".txt";
		    	   MatrixFileReader mfr = new MatrixFileReader(datafilename);
		    	   String[][] data = mfr.readStringMatrix();
		    	   new TimeSeriesMaker(data,""+nowEXPNO,dataseries);
	 			
	 			 
	 		 }
	 		 
	 		 
	 		 
	       }
	       
	      	private int readEXPNO(){
				int ans=0;
				String path = curDir+"/result/"+portNo+"/";
				File dir = new File(path);
				String[] files = dir.list();
				if(files.length==0)ans=1;
				else{
					String last = files[files.length-1];
					ans = Integer.parseInt(last.substring(3, 7))+1;
				}
				
				return ans;
			}
	    });

	}

}
