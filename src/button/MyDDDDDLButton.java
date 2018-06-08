package button;

import static main.BLV.curDir;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import main.BLV;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;

import rs232c.Communicator;
import rs232c.CommunicatorKIKUSUI;

public class MyDDDDDLButton  extends JButton{
	
	//DDDDDLDL...と続く。
	
	private static final long serialVersionUID = 1L;
	public int state =0; //0 waiting, 1 LDcycle
	int cycleTime = 1000*60*60*12; // cycle 12 [hours].
	//int cycleTime =1000*15; //テストモード 15秒の切り替え
	DDDDDLCycle t;
	LDCountingTimer ti;
	JPanel p;
	BLV f;
	CommunicatorKIKUSUI c;
	ChartPanel chart;
	XYSeries dataseries;
	DisplayLDTime ldt;
	long startTime;
	CurrentTextBox ctb;
	DelayLDTimeTextBox dlttb;
	ConfirmState cs;
	
	public MyDDDDDLButton(final  BLV fx, CommunicatorKIKUSUI c, ChartPanel chart, XYSeries dataseries,CurrentTextBox ctb, DisplayLDTime ldt, DelayLDTimeTextBox dlttb, ConfirmState cs){
		super(new ImageIcon(curDir+"/pic/starDDLD.png"));
		this.c =c;
		this.dataseries =dataseries;
		this.p = fx.panel;
		this.f = fx;
		this.chart = chart;
		this.ldt = ldt;
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.ctb = ctb;
		this.dlttb = dlttb;
		this.cs = cs;
		this.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	   if(state==0) waitingToDDDDDLcycle();
		    	   else LDcycleToStop();

		       }
		    });
	}
		
		private void waitingToDDDDDLcycle(){
			this.setImage(curDir+"/pic/starLDsaisei.png");
			this.state=1;
			this.startTime = new Date().getTime();
			//System.out.println("ss:"+startTime);
			t = new DDDDDLCycle(cycleTime,c,dataseries,startTime,ldt,ctb, cs);
			int delay = (int)(1000*60*60*mytextToDouble(dlttb.getText()));
			t.setInitialDelay(delay);
			t.turnOFFLight();
			t.start();
	        ti = new LDCountingTimer(1000,ldt);
	        ti.start();
		}
		
		private void LDcycleToStop(){
	        t.stop();
	        t.dtimes = 0;
	        if(t.state) t.turnOFFLight();
	        ti.stop();
	        this.setImage(curDir+"/pic/starDDLD.png");
			this.state=0;
			this.ldt.setText("00:00:00");
			t.state = false;
			ldt.setLightState(false);	
		}
		
		public void setImage(String text){
			super.setIcon(new ImageIcon(text));
		}
		
		private double mytextToDouble(String x){
		 	   double ans=0;
		 	   try{ans = Double.parseDouble(x);}
		 	   catch(Exception e){
		 		   System.out.println("Current setting Error");
		 	   }
		 	   
		 	   return ans;
		}
	}
	
	
