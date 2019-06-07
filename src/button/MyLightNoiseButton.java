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

import rs232c.CommunicatorKIKUSUI;
import fileIO.MatrixFileReader;


public class MyLightNoiseButton extends JButton{
	
	private static final long serialVersionUID = 1L;
	public int state =0; //0 waiting, 1 Noise
	//int measureTime =10000; // measure every 10000[ms].
	NoiseReader nr = new NoiseReader();;
	String filename;
	AnyWave t;
	Timer ti = new Timer(500,null);
	JPanel p;
	BLV f;
	CommunicatorKIKUSUI c;
	ChartPanel chart;
	XYSeries dataseries;
	DelayLDTimeTextBox dlttb;
	ConfirmState cs;
	long startTime;
	
	
	public MyLightNoiseButton(final  BLV fx, CommunicatorKIKUSUI c, ChartPanel chart, XYSeries dataseries,CurrentTextBox ctb, DelayLDTimeTextBox dlttb,ConfirmState cs){
		super(new ImageIcon(curDir+"/pic/starhalf.png"));
		this.c =c;
		this.dataseries = dataseries;
		this.p = fx.panel;
		this.f = fx;
		this.chart = chart;
		this.dlttb = dlttb;
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.filename = "./CurrentInput/test.txt";
		this.cs = cs;
		

		this.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {    	   
		    	   if(state==0) changingAnyWave();
		    	   else AnyWaveToStop();

		       }
		    });
	}
	/*
	public void waitingToAnyWave(){
		this.setImage(curDir+"/pic/starhalfsaisei.png");
		this.state=1;
		this.startTime = new Date().getTime();
		double[] currentArray = getCurrentInput(filename);	
		t = new AnyWave(measureTime,currentArray,c,dataseries,startTime);
		t.start();
	}
	*/
	
	public void changingAnyWave(){
		double[] AllNoise = nr.getAllNoise();
		int[] AllNoiseTime = nr.getAllNoiseTime();
		this.setImage(curDir+"/pic/starhalfsaisei.png");
		this.state=1;
		this.startTime = new Date().getTime();
		//double currentArray = nr.getNoise();
		t = new AnyWave(1*60*1000, AllNoise, AllNoiseTime, c, dataseries, startTime, cs);
		int delay = (int)(1000*60*60*mytextToDouble(dlttb.getText()));
		t.setInitialDelay(delay);
		t.start();
		
		System.out.println("changinganyButton");
	}
	
	private void AnyWaveToStop(){
        t.stop();
        if(t.state == true){
        	t.turnOFFLight();
        	System.out.println("turnOFFLight");
        }
        cs.bright = false;
//        ti.stop();
        this.setImage(curDir+"/pic/starhalf.png");
        System.out.println("anywavetostop");
		this.state=0;
	}
	
	public void setImage(String text){
		super.setIcon(new ImageIcon(text));
	}
	
	public double[] getCurrentInput(String filename){
		double [][] tmp;
		double [] ans;
		tmp = new MatrixFileReader(filename).read();
		ans = new double[tmp.length];
		for(int i=0;i<tmp.length;i++){
			ans[i]=tmp[i][0];
		}
		return ans;
	}
	
	private double mytextToDouble(String x){
	 	   double ans=0;
	 	   try{ans = Double.parseDouble(x);}
	 	   catch(Exception e){
	 		   System.out.println("Current setting Error");
	 	   }
	 	   
	 	   return ans;
	}
	
	/*
	public static void main(String args[]){
		double[][] ans;
		MatrixFileReader mfr = new MatrixFileReader("./CurrentInput/test.txt");
		ans = mfr.read();
		
		for(int i=0;i<ans.length;i++){
			System.out.println(ans[i][0]);
		}
		
		
	}
	*/
	
	
}
