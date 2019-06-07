package button;

import static main.BLV.curDir;
import static main.BLV.SamplingTime;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;

import rs232c.*;

import main.*;


public class myMeasureButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int state =0; //0 waiting, 1 measuring, 2stop
	int interval =10000; // measure every 10000[ms].
	int measureTime = 30;// measure during 30[s]
	Timer t = new Timer(interval,null);
	Timer ti = new Timer(500,null);
//	JPanel p;
//	BLV f;
	CommunicatorPhotomul c;
	ChartPanel chart;
	XYSeries dataseries;
	long startTime;
	int ch;
	SampleIntervalTimeTextBox sittb;
	SampleMeasureTimeTextBox smttb;
	DisplayEXPNO en;
	int MachineNo;
	FilenameTextBox ftb;

	
	
	public myMeasureButton(DisplayEXPNO en, CommunicatorPhotomul c, ChartPanel chart, XYSeries dataseries, int photomalNo, SampleIntervalTimeTextBox sittb, SampleMeasureTimeTextBox smttb, int MachineNo, FilenameTextBox ftb){
		super(new ImageIcon(curDir+"/pic/waitingForSaisei.png"));
		this.MachineNo = MachineNo;
		this.en = en;
		this.c =c;
		this.sittb = sittb;
		this.smttb = smttb;
		this.dataseries =dataseries;
//		this.p = fx.panel;
		this.chart = chart;
		this.ch = photomalNo;
		this.ftb = ftb;
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	   if(state==0) waitingTomeasure();
		    	   else if(state==1) measuringToStop();
		    	   else stopToMeasuring();

		       }
		    });
	}

    private int mytextToInt(String x){
 	   int ans=0;
 	   try{ans = Integer.parseInt(x);}
 	   catch(Exception e){
 		   System.out.println("SamplingInterval setting Error");
 	   }
 	   
 	   return ans;
    }
	
	private void waitingTomeasure(){
		this.state=1;
		int newexpno = en.refresh();

		if(sittb !=null) interval = mytextToInt(sittb.getText())*1000*60;
		if(smttb !=null) measureTime = mytextToInt(smttb.getText());
		
		c.init(measureTime);
		String optionalFilename = ftb.getText(); //‚±‚±‚Åƒtƒ@ƒCƒ‹–¼‚ðŽæ“¾
		
		c.setOutputfile(curDir+"/result/"+ch+"/EXP"+String.format("%04d", newexpno)+"_"+ch+"_"+MachineNo+"_"+optionalFilename+".txt");
		this.startTime = new Date().getTime();
		dataseries.clear();
		c.measureOnce2();
		t = new Measure(interval,c,dataseries,startTime);
		t.start();
//		t.schedule(new Measure(f,c,chart),0,SamplingTime*1000);
        ti = new BrightningIcon(500,this);
        ti.start();
	}
	
	
	private void measuringToStop(){
        t.stop();
        ti.stop();
        this.setImage(curDir+"/pic/pause.png");
		this.state=2;
	}
	private void stopToMeasuring(){
		t.restart();
		ti.restart();
//		t = new Measure2(1000,c,dataseries);
//		ti = new java.util.Timer();
 //       t.schedule(new Measure(f,c,chart),0,1000);
//        ti.schedule(new BrightningIcon(this),0,500);
		this.state=1;
	}
	
	public void setImage(String text){
		super.setIcon(new ImageIcon(text));
	}
	


}


