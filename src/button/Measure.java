package button;

import static main.BLV.curDir;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Timer;

import org.jfree.data.xy.XYSeries;

import fileIO.MyFileWriter;
import rs232c.CommunicatorArduino;
import rs232c.CommunicatorKIKUSUI;
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
	CommunicatorPhotomul cp1,cp2,cp3;
	CommunicatorArduino ca;
	CommunicatorKIKUSUI ck;
	MyFileWriter mfl;
	JudgeMeasurement jm;
	XYSeries dataseries1,dataseries2,dataseries3;
	FilenameTextBox ftb;
	AnyWave tld;
	long startTime;

	Measure(int interval,CommunicatorPhotomul cp1, CommunicatorPhotomul cp2, CommunicatorPhotomul cp3, CommunicatorArduino ca,CommunicatorKIKUSUI ck,
			XYSeries dataseries1, XYSeries dataseries2, XYSeries dataseries3,long startTime, JudgeMeasurement jm, FilenameTextBox ftb, AnyWave tld) {
		super(interval, null);
		this.cp1=cp1;
		this.cp2=cp2;
		this.cp3=cp3;
		this.ca=ca;
		this.ck=ck;
		this.ftb=ftb;
		this.dataseries1 = dataseries1;
		this.dataseries2 = dataseries2;
		this.dataseries3 = dataseries3;
		this.startTime=startTime;
		this.jm = jm;
		this.tld = tld;
		addActionListener(this);
	}

	public void waitTimer(long ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	public void actionPerformed(ActionEvent event) {
		//CommunicatorKIKUSUI ck = new CommunicatorKIKUSUI(7);
		//CommunicatorPhotomul cp = new CommunicatorPhotomul(1);
		//jm.keisokuchu = true;
		ck.init();
		ck.wait(1000);
		ck.OUTPUTOFF("Dark");
		ck.wait(1000);
		ck.portclose();


		jm.keisokuchu = true;
		//ck.wait(5000);
		//ダークカウントの上昇を防ぐためにポート操作はしないようにする
		ca.wait(2000);

		Date date = new Date();
		date = new Date();//時刻表示用
		System.out.println("いまから測定準備。"+date.toString());//測定準備入り時刻の記録

		int timeOfFadeAwayPhotons =300000; //LEDの蓄光が無くなるまでの待機時間 5min

		System.out.println("LEDが「完全に」消えるのを待っています");
		ca.wait(timeOfFadeAwayPhotons);//LEDの蛍光消えるの待っている状態
		System.out.println("ca.waitを抜けました");

		date = new Date();
		System.out.println("測定開始"+date.toString());//測定開始時刻の記録

		ca.sendcommand("o");//全フォトマル通電

		
		measurePlotWrite(cp1,dataseries1,1);
		measurePlotWrite(cp2,dataseries2,2);
		measurePlotWrite(cp3,dataseries3,3);
		
		cp1.portclose();
		cp2.portclose();
		cp3.portclose();

		ca.wait(4000);
		ca.sendcommand("p");
		//ca.portclose();//ここではポート操作はしない

		jm.keisokuchu = false;

		tld.noisecounter = tld.noisecounter - 5; //タイマー停止の減算処理.ウェイト時間によるズレを帳尻あわせする為。
		//アイデアメモ。tld.waitTimer(xxxx);xxxx msec 遅らせることができる。

if(jm.makasareta == true){
	    ck.init();
		ck.wait(2000);
		ck.setVI(24,jm.nextCurrent,"Light");

		date = new Date();
		System.out.println("任されたタイミングで電流変えました"+date.toString());

		ck.OUTPUTON();
		ck.portclose();
		jm.makasareta = false;


		}
else{
	 ck.init();
		ck.wait(2000);
		ck.setVI(24,jm.beforeCurrent,"Light");

		date = new Date();
		System.out.println("復帰で電流変えました"+date.toString());
		ck.OUTPUTON();
		ck.portclose();

}

	}
	
	private void measurePlotWrite(CommunicatorPhotomul cp, XYSeries dataseries, int photomulNumber) {
		System.out.println("starts measureing for Chamber" + photomulNumber);//測定開始
		//計測～プロット
		cp.init();
		double data = cp1.measureOnce();//光量測定
		long dateinms = new Date().getTime()-startTime;//計測時間
		double now = (double) dateinms /1000.0/60.0/60.0;
		dataseries.add(now, data);
		System.out.println("Chamber" + photomulNumber+"; time:" + now+" data:"+data);
		
		//計測～プロットここまで
		//書き出し機能
		String fileName = ftb.getText();
		//.txtがついていればはぎとる
		if(fileName.substring(fileName.length()-4, fileName.length()).equals(".txt")) fileName = fileName.substring(0, fileName.length()-4);
		String outputpath;
		
		if(fileName.equals("")) {
			outputpath = curDir+"/result/"+"result_"+photomulNumber+".txt";//テキストボックスに何もなければresult_1.txt
			System.out.println("the output file name is not specified");
		}
		else outputpath = curDir+"/result/"+fileName+"_"+photomulNumber+".txt";//テキストボックスに何か書いてあればそれがファイル名
		MyFileWriter mfw = new MyFileWriter(outputpath,true);//trueを書くと追加していくようになる
		mfw.writeALine(now+" "+data);
		mfw.close();
		//書き出しここまで

		System.out.println("completed measureing for Chamber" + photomulNumber);//測定終了
	}
}
