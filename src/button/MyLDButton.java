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

public class MyLDButton  extends JButton{
	
	private static final long serialVersionUID = 1L;
	public int state =0; //0 waiting, 1 LDcycle
	int cycleTime =1000*60*60*12; // cycle 12 [hours]
//	int cycleTime =1000*60*3;
	//int cycleTime =1000*30; //テストモード
	LDCycle t;
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
	LDCycleCounterTextBox ldcctb;
	
	public MyLDButton(final  BLV fx, CommunicatorKIKUSUI c, ChartPanel chart, XYSeries dataseries,CurrentTextBox ctb, DisplayLDTime ldt, DelayLDTimeTextBox dlttb, ConfirmState cs, LDCycleCounterTextBox ldcctb){
		super(new ImageIcon(curDir+"/pic/starLD.png"));
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
		this.ldcctb =ldcctb;
		this.ldt.setcycleTime(cycleTime);
		this.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	   if(state==0) waitingToLDcycle();
		    	   else LDcycleToStop();

		       }
		    });
	}
		
		private void waitingToLDcycle(){
			this.setImage(curDir+"/pic/starLDsaisei.png");
			this.ldt.setEndTime();
			this.state=1;
			this.startTime = new Date().getTime();
//			System.out.println("ss:"+startTime);
			t = new LDCycle(cycleTime,c,dataseries,startTime,ldt,ctb,cs,ldcctb);
			//t.turnOFFLight();
			int delay = (int)(1000*60*60*mytextToDouble(dlttb.getText()));
			System.out.println("D "+dlttb.getText());
			System.out.println("D "+mytextToDouble(dlttb.getText()));
			System.out.println("D "+delay);
			t.setInitialDelay(delay);
			t.start();
//			t.schedule(new Measure(f,c,chart),0,SamplingTime*1000);
	        ti = new LDCountingTimer(1000,ldt);
			ti.setInitialDelay(delay);
	        ti.start();
		}
		
		private void LDcycleToStop(){
	        t.stop();
	        if(t.state) t.turnOFFLight();
	        ti.stop();
	        this.setImage(curDir+"/pic/starLD.png");
			this.state=0;
			this.dataseries.clear();
			this.ldt.setText("00:00:00");
			t.state = false;
			ldt.setLightState(false);
			
		}
		
		private double mytextToDouble(String x){
		 	   double ans=0;
		 	   //書き換え（伊東）160623
		 	  System.out.println("x="+x);
		 	   x =  x.substring(0, x.length() - 4);
		 	  System.out.println("x="+x);
		 	   //ここまで
		 	   try{ans = Double.parseDouble(x);}
		 
		 	   catch(Exception e){
		 		   System.out.println("Current setting Error");
		 	   }
		 	   //書き換え(伊東)160623
		 	  System.out.println("ans="+ans);
		 	  //ここまで
		 	   return ans;
		}
		
		public void setImage(String text){
			super.setIcon(new ImageIcon(text));
		}
		
	}
	
	
	
