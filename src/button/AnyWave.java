package button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	int noisecounter=0; //Timer�̔���p
	int flag=0;  //Timer�̔���p
	DelayLDTimeTextBox dlttb;
	ConfirmState cs;
	JudgeMeasurement jm;
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

	AnyWave(int interval,double[] currentArray, int[] currentArrayTime, CommunicatorKIKUSUI c, XYSeries dataseries, long startTime,JudgeMeasurement jm) {
		super(interval, null);
		this.c=c;//comKIKUSUI
		this.dataseries = dataseries;
		this.startTime=startTime;
		addActionListener(this);
		this.currentArray= currentArray;
		this.currentArrayTime = currentArrayTime;
		this.state=false;
		this.jm = jm;

	}
	AnyWave(int interval,double[] currentArray, int[] currentArrayTime, CommunicatorKIKUSUI c, XYSeries dataseries, long startTime) {
		super(interval, null);
		this.c=c;//comKIKUSUI
		this.dataseries = dataseries;
		this.startTime=startTime;
		addActionListener(this);
		this.currentArray= currentArray;
		this.currentArrayTime = currentArrayTime;
		this.state=false;

	}

	public void waitTimer(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public double turnONLight(double current){
		Date date = new Date();
		System.out.println("いまからL入り"+date.toString());
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
		Date date = new Date();
		System.out.println("いまからD入り"+date.toString());
		state = false;
		double ans;
		c.init();
		c.OUTPUTOFF("NOISE");
		ans = c.getI();
		c.portclose();
		return ans;
	}

	//------------��������e�X�g--------------
	public double changeLight(double current){
		Date date = new Date();
		System.out.println("いまから"+current+"[A]にかえます。"+date.toString());
		this.state = true;
		double ans;
		c.init();
		//if(cs.ccdState==false){
			c.setVI(24, current, "NOISE"); //����setV�������setI�ɂ���
		//}
		c.OUTPUTON();
		ans = current;
		c.portclose();
		//cs.bright = true;
		//cs.current = current;

		return ans;

	}
	//--------------�����܂�--------------

	@Override
	public void actionPerformed(ActionEvent e) {
		Date date = new Date();//時刻表示用
		System.out.println("tld（Anywave)のタイマー発動。刺激変更まであと"+this.noisecounter+"分:　"+date.toString());
		int overshoot = 0;//tldが減算処理した結果0をしたまわって引きすぎの量
		if(flag == 1)this.noisecounter -= 1; //何もしないときはnoisecounterを1減らす
		if(this.noisecounter<=0 || flag==0){ //noisecounterが0になったら、菊水に通信
			overshoot = Math.abs(this.noisecounter);
			if(flag==0)flag=1;
			double data =-1;
			//double olddata= -1;
			int index = counter % currentArray.length;



//ここからｔとtldによって変える
		if(jm.keisokuchu == true){
			jm.makasareta = true;
			jm.nextCurrent = currentArray[index];
			jm.beforeCurrent = currentArray[index];
			//jm.makasareta = true;
			date = new Date();//時刻表示用
			System.out.println("keisokucyu==trueなので、任せました"+date.toString());

		}

		else{
		/*if(flag == 1)this.noisecounter -= 1; //何もしないときはnoisecounterを1減らす
		if(this.noisecounter<=0 || flag==0){ //noisecounterが0になったら、菊水に通信
			if(flag==0)flag=1;
			double data =-1;
			//double olddata= -1;
			 */

			//if(counter != 0) olddata = changeLight(currentArray[index-1]);
			data = changeLight(currentArray[index]);
			jm.beforeCurrent = currentArray[index];
			date = new Date();//時刻表示用
			System.out.println("任せずに電流変えました"+date.toString());

			//電流変化をぐらふに記述する
			/*
			long dateinms = new Date().getTime()-startTime;
			double now = (double) dateinms /1000.0/60.0/60.0;
			if(olddata >= 0) dataseries.add(now, olddata);
			dataseries.add(now, data);
			*/
		}
			this.noisecounter = currentArrayTime[index]-overshoot;
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
