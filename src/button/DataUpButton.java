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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;

import fileIO.MatrixFileReader;

public class DataUpButton extends JButton {


	XYTextAnnotation graphNo;
	DataDownButton ddb;
	int showingData;
	int portNo;

	
	private static final long serialVersionUID = 1L;

	public DataUpButton(final JFreeChart chart, final XYSeries dataseries, final int portNo, final myMeasureButton mmb, final DataDownButton ddb){
		super(new ImageIcon(curDir+"/pic/up.png")); //�摜�t�@�C��
		
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.ddb = ddb;
		this.portNo = portNo;
		this.showingData = this.getFileNum()-1;
		
		this.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	   File[] files = getFilelists();

		    	   showingData =  ddb.showingData;
		    	   if (dataseries.isEmpty() == true && showingData == getFileNum()-1) showingData = showingData;
		    	   else if(showingData == getFileNum()-1) showingData = 0;
		    	   else showingData++;	    	   
		    	   ddb.showingData = showingData;
		    	   
		    	   dataseries.clear();
                    
		    	   String datafilename=""+files[showingData];
		    	   MatrixFileReader mfr = new MatrixFileReader(datafilename);
		    	   String[][] data = mfr.readStringMatrix();
		    	   new TimeSeriesMaker(data,""+showingData,dataseries);
		    	   XYPlot plot = (XYPlot) chart.getPlot();
		    	   ValueAxis rangeAxis = plot.getRangeAxis();
		    	   ValueAxis domainAxis = plot.getDomainAxis();
		    	   String displayName = (""+files[showingData]).substring(30);
		    	   
		    	   if(graphNo!=null) plot.removeAnnotation(graphNo);	 
			    	   double a = rangeAxis.getLowerBound();
			    	   double b = rangeAxis.getUpperBound();
			    	   double c = domainAxis.getLowerBound();
			    	   double d = domainAxis.getUpperBound();
		    		graphNo = new XYTextAnnotation(displayName,c+(d-c)*0.10,a+(b-a)*0.9);
		    	   	graphNo.setFont(new Font("Arial", Font.PLAIN, 15));
		    	   	plot.addAnnotation(graphNo);

		    	   
		       }
		       
		    private File[] getFilelists(){
		    	String path = curDir+"/result/"+portNo+"/";
		    	 File file = new File(path);
		         File files[] = file.listFiles();	         
		         return files;
		    }
		    
		    private int getFileNum(){
		    	String path = curDir+"/result/"+portNo+"/";
		    	 File dir = new File(path);
		         File files[] = dir.listFiles();
		         return files.length;    
		}
		    
		    });
		
	}
	
	    private int getFileNum(){
	    	String path = curDir+"/result/"+portNo+"/";
	    	 File dir = new File(path);
	         File files[] = dir.listFiles();
	         if(files == null) {return 0;}
	         return files.length;    
	}
	
}