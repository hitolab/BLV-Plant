package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.Timer;

import org.jfree.data.xy.XYSeries;

import rs232c.CommunicatorKIKUSUI;


public class AnyWave extends Timer implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CommunicatorKIKUSUI c;
	XYSeries dataseries;
	long startTime;
	double LightCurrent = 1;
	public double currentArray[];
	public int currentArrayTime[];
	int counter=0;
	int noisecounter=0; //Timerの判定用
	int flag=0;  //Timerの判定用
	DelayLDTimeTextBox dlttb;
	ConfirmState cs;
	public boolean state; // working: true, waiting: false;
	
	
	AnyWave(int interval,double[] currentArray, int[] currentArrayTime, CommunicatorKIKUSUI c, XYSeries dataseries, long startTime, ConfirmState cs) {
		super(interval, null);
		this.c=c;
		this.dataseries = dataseries;
		this.startTime=startTime;
		addActionListener(this);
		this.currentArray= currentArray;
		this.currentArrayTime = currentArrayTime;
		this.state=false;
		this.cs = cs;
	}
	
	
	
	public double turnONLight(double current){
		this.state = true;
		double ans;
		c.init();
		c.setI(current, "NOISE");
		c.OUTPUTON();
		ans = c.getI();
		c.portclose();
		return ans;
	}
	
	public double turnOFFLight(){
		state = false;
		double ans;
		c.init();
		c.OUTPUTOFF("NOISE");
		ans = c.getI();
		c.portclose();
		return ans;
	}
	
	//------------こっからテスト--------------
	public double changeLight(double current){
		this.state = true;
		double ans;
		c.init();
		if(cs.ccdState==false){
			c.setVI(24, current, "NOISE"); //今はsetVだが後でsetIにする
		}
		c.OUTPUTON();
		ans = current;
		c.portclose();
		cs.bright = true;
		cs.current = current;
		return ans;
		
	}
	//--------------ここまで--------------
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(flag == 1)this.noisecounter -= 1; //初回以外実行
		if(this.noisecounter<=0 || flag==0){ //初回またはカウンターが０以下になったら動作
			if(flag==0)flag=1;
			double data =-1, olddata= -1;
			int index = counter % currentArray.length;
			if(counter != 0) olddata = changeLight(currentArray[index-1]);
			data = changeLight(currentArray[index]);
			
			//グラフ描画の為の記述
			long dateinms = new Date().getTime()-startTime;
			double now = (double) dateinms /1000.0/60.0/60.0;
			if(olddata >= 0) dataseries.add(now, olddata);
			dataseries.add(now, data);
			this.noisecounter = currentArrayTime[counter];
			this.counter++;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
