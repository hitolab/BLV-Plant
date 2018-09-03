package button;

import static main.BLV.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;

import rs232c.CommunicatorArduino;
import rs232c.CommunicatorKIKUSUI;
import rs232c.CommunicatorPhotomul;




public class myMeasureButton extends JButton{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public int state =0; //0 waiting, 1 measuring, 2stop
	int interval =10000; // measure every 10000[ms].
	int measureTime = 30;// measure during 30[s]
	Timer t,ti;
	AnyWave tld;
//	JPanel p;
//	BLV f;
	CommunicatorPhotomul cp1;
	CommunicatorPhotomul cp2;
	CommunicatorPhotomul cp3;
	CommunicatorArduino ca;
	CommunicatorKIKUSUI ck;
	ChartPanel chart;
	XYSeries dataseries1,dataseries2,dataseries3;
	long startTime;
	int ch;
	SampleIntervalTimeTextBox sittb;
	SampleMeasureTimeTextBox smttb;
	int MachineNo;
	FilenameTextBox ftb;
	DelayLDTimeTextBox dlttb;
	JudgeMeasurement jm;
	FilenameTextBox  noisefileTextbox;




	public myMeasureButton(CommunicatorPhotomul c1, CommunicatorPhotomul c2,CommunicatorPhotomul c3, CommunicatorArduino ca,
			CommunicatorKIKUSUI ck, ChartPanel chart, XYSeries dataseries1,  XYSeries dataseries2,  XYSeries dataseries3, int photomalNo,
			SampleIntervalTimeTextBox sittb, SampleMeasureTimeTextBox smttb, int MachineNo, FilenameTextBox ftb, DelayLDTimeTextBox dlttb, FilenameTextBox noisefileTextbox){
		super(new ImageIcon(curDir+"/pic/waitingForSaisei.png"));
		this.MachineNo = MachineNo;
		this.cp1 =c1;
		this.cp2 =c2;
		this.cp3 =c3;
		this.ca =ca;
		this.ck =ck;
		this.sittb = sittb; //SampleIntervalTimeTextBox
		this.smttb = smttb; //SampleMeasureTimeTextBox
		this.dataseries1 =dataseries1;
		this.dataseries2 =dataseries2;
		this.dataseries3 =dataseries3;
//		this.p = fx.panel;
		this.chart = chart;
		this.ch = photomalNo;
		this.ftb = ftb;//fileNameTextBox
		this.dlttb = dlttb;//DelayLDTimeTextBox
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));

		this.noisefileTextbox = noisefileTextbox;

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
		ck.init();
		ck.wait(2000);
		ck.OUTPUTOFF("Dark");
		ck.wait(5000);
		ca.init(); //portopenによるarduinoへの頻繁な通電を避けるため、ポートは開きっぱなしにする
		ca.wait(2000);
		ck.wait(5000);
		ck.portclose();
		String noiseFileName = noisefileTextbox.getText();
		if(noiseFileName.equals("")) noiseFileName = "noise.txt";//ノイズファイル名を指定しない場合のデフォルトノイズ
		else if(noiseFileName.length()<4){//テキストボックスに何か書いてあればそれがファイル名（.txtを含まないファイル名の場合）4字以下のファイル名の時
			noiseFileName = noiseFileName+".txt";
		}
		else{
			if(!noiseFileName.substring(noiseFileName.length()-4,noiseFileName.length()).equals(".txt")) {

			//テキストボックスに何か書いてあればそれがファイル名（.txtを含まないファイル名の場合）4字以上のファイル名の時
			noiseFileName = noiseFileName+".txt";
		}
		}
		System.out.println( "使用するノイズファイル： " + noiseFileName);//読み込んだノイズデータの確認表示
		NoiseReader nr = new NoiseReader(noiseFileName);
		double[] AllNoise = nr.getAllNoise();
		int[] AllNoiseTime = nr.getAllNoiseTime();
		this.state=1;

		if(sittb !=null) interval = mytextToInt(sittb.getText())*1000*60;
		if(smttb !=null) measureTime = mytextToInt(smttb.getText());

		//c.init(measureTime);
		//String optionalFilename = ftb.getText(); //�����Ńt�@�C�������擾
		//c.setOutputfile(curDir+"/result/"+ch+"/EXP"+String.format("%04d", newexpno)+"_"+ch+"_"+MachineNo+"_"+optionalFilename+".txt");
		this.startTime = new Date().getTime();
		dataseries1.clear();
		dataseries2.clear();
		dataseries3.clear();

		JudgeMeasurement jm = new JudgeMeasurement();

        //LightDarkサイクル関係のタイマー設置
		tld = new AnyWave(1*60*1000, AllNoise, AllNoiseTime, ck, dataseries1, startTime,jm);
		int delay = (int)(1000*60*60*mytextToDouble(dlttb.getText()));

		//フォトマル計測関係のタイマー設置
		t = new Measure(interval,cp1,cp2, cp3, ca, ck, dataseries1,dataseries2,dataseries3,startTime,jm,ftb,tld);
        ti = new BrightningIcon(500,this);
		tld.setInitialDelay(delay);

		t.start();
        ti.start();
		tld.start();

	}

	private double mytextToDouble(String x){
	 	   double ans=0;
	 	   try{ans = Double.parseDouble(x);}
	 	   catch(Exception e){
	 		   System.out.println("Current setting Error");
	 	   }

	 	   return ans;
	}
	private void measuringToStop(){//一時停止が押されたとき
		ca.portclose();//
        t.stop();

        ti.stop();
        tld.stop();
        //KIKUSUIの Aeduino(photomul)の機能を停止
        ck.init();
        ck.OUTPUTOFF("Dark");
        ck.portclose();
        ca.init();
        ca.sendcommand("p");
        ca.portclose();

        this.setImage(curDir+"/pic/pause.png");
		this.state=2;
	}
	private void stopToMeasuring(){
		//t.restart();
		//ti.restart();
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


