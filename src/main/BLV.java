package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import button.BasePlace;
import button.ConfirmState;
import button.CurrentTextBox;
import button.DataDownButton;
import button.DataUpButton;
import button.DelayLDTimeTextBox;
import button.DisplayCharacters;
import button.DisplayInterval;
import button.DisplayLDTime;
import button.DisplayMeasure;
import button.DisplayNo;
import button.FilenameTextBox;
import button.GoToMeasuringChart;
import button.IntervalCCDTextBox;
//import button.JudgeMeasurement;
import button.LDCycleCounterTextBox;
import button.MyCCDButton;
import button.MyDDDDDLButton;
import button.MyLDButton;
import button.MyLightButton;
import button.MyLightNoiseButton;
import button.MyLightOFFButton;
import button.SampleIntervalTimeTextBox;
import button.SampleMeasureTimeTextBox;
import button.myMeasureButton;
import button.myPrintButton;
import button.myStopButton;
import rs232c.Communicator;
import rs232c.CommunicatorArduino;
import rs232c.CommunicatorKIKUSUI;
import rs232c.CommunicatorPhotomul;
import rs232c.CommunicatorT10;

public class BLV extends JFrame {


	/**
	 * ver1.0 植物チャンバー3並列用に大改造。
	 */
	private static final long serialVersionUID = -2508829707600345047L;

	//Parameter
	public static String version = "1.0";// 2018年8月17日
	private static boolean testMode = true;
	public static String curDir;
	public static int SamplingTime = 3; // sampling every 3.0[s]
	public int MACHINENO=0;

	public JPanel panel;
	protected GridBagConstraints constraints;
//	public int expno1,expno2,expno3,expno4;
	GridBagLayout gridbag;
	public CommunicatorPhotomul cp1,cp2,cp3;
	public CommunicatorT10 ct;
	public CommunicatorKIKUSUI ck;
	public CommunicatorArduino ca;
	public myMeasureButton cmb1,cmb2,cmb3,cmb4;
	public BasePlace cbase1,cbase1_1,spacer1,cbase2,cbase2_1,spacer2,cbase3,cbase3_1,spacer3,cbase4,cbase4_1,spacer4,cbase2_0,cbase3_0,cbase4_0,cbase5_0,cbase5_1,cbase5,spacer5;
	public ChartPanel cchart1,cchart2,cchart3;
	public myStopButton csb1,csb2,csb3,csb4;
	public myPrintButton cpb1,cpb2,cpb3,cpb4;
	public DataUpButton dub1,dub2,dub3,dub4;
	public MyLightButton lonb;
	public MyLightOFFButton loffb;
	public MyLightNoiseButton lnb;
	public CurrentTextBox ctb;
	public DelayLDTimeTextBox dlttb;
	public MyLDButton ldb;
	public DisplayLDTime ldt;
	public DataDownButton ddb1,ddb2,ddb3,ddb4;
	public GoToMeasuringChart gtmc1,gtmc2,gtmc3,gtmc4;
	public SampleIntervalTimeTextBox sittb;
	public SampleMeasureTimeTextBox smttb;
	public FilenameTextBox resultftb, ldftb;
	public MyDDDDDLButton ddddldb;
	public MyCCDButton arb;
	public IntervalCCDTextBox ictb;
	public ConfirmState cs;
	//public JudgeMeasurement jm;
	public LDCycleCounterTextBox ldcctb;

	XYSeries dataseries1 = new XYSeries(1),
	dataseries2 = new XYSeries(2),
	dataseries3 = new XYSeries(3);
	XYSeriesCollection dataset1 = new XYSeriesCollection(),
			dataset2 = new XYSeriesCollection(),
			dataset3 = new XYSeriesCollection();
	JFreeChart chart1,chart2,chart3;
	long startTime1=0, startTime2=0, startTime3=0;
	public DisplayNo displayNo1,displayNo2,displayNo3;

	boolean isWindows = false;


	public BLV(){

		osCheck();
		if(testMode) curDir= new File(".").getAbsoluteFile().getParent();
		else if (isWindows)curDir = "C:\\Users\\circa\\Documents\\GitHub\\blvplant-test"; //ここ現状にあわせて変えてください！！(山崎君)
		else curDir = "/Users/hito/Documents/GitHub/BLV-Plant/tree/develop/";


		dlttb = new DelayLDTimeTextBox();
	//	jm = new JudgeMeasurement();

		//シリアルポートの準備
		this.cp1 = new CommunicatorPhotomul(4,dataseries1);//comportを指定
		this.cp2 = new CommunicatorPhotomul(2,dataseries2);//comportを指定
		this.cp3 = new CommunicatorPhotomul(6,dataseries3);//comportを指定
		if(isWindows)this.ck = new CommunicatorKIKUSUI(7); //KIKUSUIとの接続 3は危険。
		else this.ck = new CommunicatorKIKUSUI("/dev/cu.UC-232AC");
		if(isWindows)this.ca = new CommunicatorArduino(5); //Arduinoとの接続、COM=5
		//else this.ar = new CommunicatorArduino("/dev/cu.UC-232AC");

		//Chartを作る。
		setDatasets();
		chart1 = createScatterPlot(dataset1,"Biolumi. [cnt]");
		chart2 = createScatterPlot(dataset2,"Biolumi. [cnt]");
		chart3 = createScatterPlot(dataset3,"Biolumi. [cnt]");
		setChartPanel();

		//Panelの作成
		gridbag = new GridBagLayout();
		constraints = new GridBagConstraints();
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(gridbag);

		//Buttons in the first line
		int row=2;
		DisplayInterval di = new DisplayInterval();
		DisplayCharacters dmin = new DisplayCharacters("min");
		DisplayCharacters dfilename = new DisplayCharacters("    Result file");
		sittb = new SampleIntervalTimeTextBox();
		sittb.setPreferredSize(new Dimension(20000, 3000));
		resultftb = new FilenameTextBox();
		ldftb = new FilenameTextBox();

		constraints.fill = GridBagConstraints.NONE;
		setConstraints(panel,gridbag,di,0,0,       0,row-2,  1,1);
		setConstraints(panel,gridbag,sittb,0,0,    1,row-2,  1,1);
		setConstraints(panel,gridbag,dmin,0,0,     2,row-2,  1,1);
		setConstraints(panel,gridbag,dfilename,0,0,3,row-2,  1,1);
		setConstraints(panel,gridbag,resultftb,0,0,4,row-2,  6,1);

		//Buttons in the second line
		DisplayMeasure dm = new DisplayMeasure();
		DisplayCharacters dsec = new DisplayCharacters("sec");
		DisplayCharacters dldfile = new DisplayCharacters("    LD file");
		//JFreeChart[] chartArrays = {chart1,chart2, chart3,chart4,chart5};
		//MyGraphRescaleButton mrb = new MyGraphRescaleButton(curDir+"/pic/back.png",chartArrays);

		smttb = new SampleMeasureTimeTextBox();
		arb = new MyCCDButton(ictb, ck, cs);
		ictb = new IntervalCCDTextBox();

		setConstraints(panel,gridbag,dm,0,0,      0,row-1, 1,1);
		setConstraints(panel,gridbag,smttb,0,0,   1,row-1, 1,1,7,0);
		setConstraints(panel,gridbag,dsec,0,0,    2,row-1, 1,1);
		setConstraints(panel,gridbag,dldfile,0,0, 3,row-1, 1,1);
		setConstraints(panel,gridbag,ldftb,0,0,   4,row-1, 6,1);


		//Buttons in first photomul
		cs = new ConfirmState();
		cmb1= new myMeasureButton(cp1,cp2,cp3,ca,ck,cchart1,dataseries1,dataseries2,dataseries3,1,sittb,smttb,this.MACHINENO,resultftb,dlttb,ldftb);
		cbase1 = new BasePlace();
		csb1 = new myStopButton(cmb1,this,cp1,cp2,cp3,dataseries1,dataseries2,dataseries3);
		ddb1 = new DataDownButton(chart1,dataseries1,1,cmb1,dub1);
		dub1 = new DataUpButton(chart1,dataseries1,1,cmb1,ddb1);
		gtmc1 = new GoToMeasuringChart(curDir+"/pic/back.png",cmb1,dataseries1,1);
		cbase1_1 = new BasePlace();
		spacer1 = new BasePlace();
		//cbase1_1.setBackground(Color.pink);
		//spacer1.setBackground(Color.ORANGE);


		setConstraints(panel,gridbag,cmb1,0,0,       0,                          row+0,                      1,1);
		setConstraints(panel,gridbag,csb1,0,0,       0,                          row+1,                      1,1);
		setConstraints(panel,gridbag,gtmc1,0,0,      0,                          row+2,                      1,1);
		setConstraints(panel,gridbag,dub1,0,0,       0,                          row+3,                      1,1);
		setConstraints(panel,gridbag,ddb1,0,0,       0,                          row+4,                      1,1);
		setConstraints(panel,gridbag,cbase1_1,0,0.6,   0,                          row+5,                      1,1);
		setConstraints(panel,gridbag,spacer1,0,2.3,    0,                          row+6,                      1,                           GridBagConstraints.REMAINDER);
		setConstraints(panel,gridbag,cbase1,10,100,  1,                          row+0,                      1,                           GridBagConstraints.REMAINDER);
		setConstraints(panel,gridbag,cchart1,100,0,3,                          row+0,                      GridBagConstraints.REMAINDER,7);
		setConstraints(panel,gridbag,cchart2,100,1,3,                          GridBagConstraints.RELATIVE,GridBagConstraints.REMAINDER,GridBagConstraints.RELATIVE);
		setConstraints(panel,gridbag,cchart3,100,1,3,                          GridBagConstraints.RELATIVE,GridBagConstraints.REMAINDER,GridBagConstraints.REMAINDER);

		makeFlame(panel);

	}

	class PortChecker extends Thread{
		Communicator c;
		BasePlace bp;
		public PortChecker(Communicator c, BasePlace bp){
			this.c =c;
			this.bp = bp;
		}

		public void run(){
			boolean ans = c.checkConnection();
			if(ans)setOKsignal(bp);
		}

	}
	private void setOKsignal(BasePlace x){
		x.setIcon(new ImageIcon(curDir+"/pic/cal.png"));
	}
	private void setConstraints(JPanel p, GridBagLayout gl, JComponent c, double weightx, double weighty, int gridx, int gridy, int gridwidth, int gridheight){
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = weightx;
		gc.weighty = weighty;
		gc.gridx = gridx;
		gc.gridy = gridy;
		gc.gridwidth = gridwidth;
		gc.gridheight = gridheight;
		gl.setConstraints(c, gc);
		p.add(c);
	}

	private void setConstraints(JPanel p, GridBagLayout gl, JComponent c, int weightx, int weighty, int gridx, int gridy, int gridwidth, int gridheight,int merginx,int merginy){
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = weightx;
		gc.weighty = weighty;
		gc.gridx = gridx;
		gc.gridy = gridy;
		gc.gridwidth = gridwidth;
		gc.gridheight = gridheight;
		gc.insets.bottom = merginy;
		gc.insets.left= merginx;
		gl.setConstraints(c, gc);
		p.add(c);
	}

	// XYDatasetからJFreeChartを作成
	//散布図の作成
	private static JFreeChart createScatterPlot(XYDataset dataset, String ylabel) {
		JFreeChart chart = ChartFactory.createScatterPlot(
				"", // タイトル
				"", // categoryAxisLabel （カテゴリ軸、横軸、X軸のラベル）
				ylabel, // valueAxisLabel（ヴァリュー軸、縦軸、Y軸のラベル）
				dataset, // data set
				PlotOrientation.VERTICAL, //方向
				true, // legend
				false, // tool tips
				false); // URLs

		//凡例を出力しない
		chart.getLegend().setVisible(false);
		// 出力される円グラフの透明度、境界線の色／有無などの設定を行う：クラスPlot（時系列グラフはCategoryPlot）
		XYPlot plot = (XYPlot) chart.getPlot();
		// 背景色 透明度
		plot.setBackgroundAlpha(0.5f);
		// 前景色 透明度
		plot.setForegroundAlpha(0.5f);
		// 横軸の最大値最小値設定
		plot.getDomainAxis().setLowerBound(0);
		plot.getDomainAxis().setUpperBound(240);
		// その他設定は XYPlotを参照
		ValueAxis vAxis = (ValueAxis) plot.getDomainAxis();
		TickUnits tickUnits = new TickUnits();
		//目盛り
		boolean memori =true;
		if(memori){
		int interval=24;
		TickUnit unit = new NumberTickUnit(interval);
		tickUnits.add(unit);
		vAxis.setStandardTickUnits(tickUnits);
		}
		//createChartPrintJob
		//

		return chart;
	}

	// XYDatasetからJFreeChartを作成

	public void makeFlame(JPanel panel){
		final JFrame frame =this;

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Object[] options = { "OK", "Cancel" };
				  int retValue = JOptionPane.showOptionDialog(frame,
				        "<html>Are you sure to exit?</html>",
				        "Exit Options",
				        JOptionPane.OK_CANCEL_OPTION,
				        JOptionPane.WARNING_MESSAGE, null, options, options[1]);
				  if(retValue ==0) {
					  if(cp1!=null) cp1.portfastclose();
					  if(cp2!=null) cp2.portfastclose();
					  if(cp3!=null) cp3.portfastclose();
					  System.exit(0);
				  }

				}
		});
		this.getContentPane().add( panel, BorderLayout.CENTER );
		this.pack();
		this.setSize(new Dimension(1000,740));
		this.setTitle("BLV-Plant "+version);
	}

	private void osCheck(){
		String os = System.getProperty("os.name");
		if(os.contains("Windows")) isWindows =true;
		if(isWindows) curDir = "C:\\Eclipce\\pleiades\\workspace\\BLVPlant";
		else curDir = "/Applications/BLV";
	}

	private void setDatasets(){
		dataset1.addSeries(dataseries1);
		dataset2.addSeries(dataseries2);
		dataset3.addSeries(dataseries3);
		///dataset4.addSeries(dataseries4);
		///dataset5.addSeries(dataseries5);
	}

	private void setChartPanel(){
		cchart1 = new ChartPanel(chart1);
		cchart2 = new ChartPanel(chart2);
		cchart3 = new ChartPanel(chart3);
		///cchart4 = new ChartPanel(chart4);
		///cchart5 = new ChartPanel(chart5);

	}


	public static void main(String[] args) {
		new BLV().setVisible(true);
	}

}
