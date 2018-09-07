package rs232c;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TooManyListenersException;

import org.jfree.data.xy.XYSeries;

import fileIO.MyFileWriter;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class CommunicatorPhotomul extends Communicator{


	int n;
	XYSeries dataseries;
	long starttime;
	int countingNumber=1; //


	public CommunicatorPhotomul(int n, XYSeries dataseries) {
		super(n);
		this.n=n;
		this.dataseries = dataseries;
	}

	public CommunicatorPhotomul(int n) {
		super(n);
		this.n=n;
	}

	MyFileWriter mfw;
	CommPortIdentifier portId;
	SerialPort port;
	OutputStream outputStream;
	String outputfile;
	double sumlum=0;
	int countNow=0;

	static int PMT = 1000; //voltage of photomul

	public int getN(){
		return this.n;
	}

	public int getPort(){
		return this.n;
	}


	public void setOutputfile(String outputfile){
		this.outputfile = outputfile;
	}

	boolean openSerialPort(int n) {
		try {
			portId = CommPortIdentifier.getPortIdentifier("COM"+n);
			port = (SerialPort)portId.open("PMdetector", 2000);
		} catch (NoSuchPortException e) {
			e.printStackTrace();
			return false;
		} catch (PortInUseException e) {
			e.printStackTrace();
			return false;
		}

	    try {
			outputStream = port.getOutputStream();
		    } catch (IOException e) {}

		return true;
	}

	boolean setSerialPort() {
		try {
			port.setSerialPortParams(
					9600,                   // �ʐM���x[bps]
					SerialPort.DATABITS_8,   // �f�[�^�r�b�g��
					SerialPort.STOPBITS_1,   // �X�g�b�v�r�b�g
					SerialPort.PARITY_NONE   // �p���e�B
			);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
			return false;
		}
		port.setDTR(true);
		port.setRTS(false);

	    try {
	    	port.notifyOnOutputEmpty(true);
	    } catch (Exception e) {
		System.out.println("Error setting event notification");
		System.out.println(e.toString());
		System.exit(-1);
	    }

		return true;
	}

	public void write(byte[] comand) {
		try {
			outputStream = port.getOutputStream();
			outputStream.write(comand);

		    } catch (IOException e) {
		    }

		    int hex = byteToint(comand);
		    String str = byteToString(comand);
		    System.out.println("Comand:"+Integer.toHexString(hex)+":"+str);
	}
	private String byteToString(byte[] x){
		String ans="";
		if(x.length==1) ans="\\r";
		else if(x.length==2) ans=(new String(x)).substring(0,1)+"\\r";
		else {
			ans = (new String(x)).substring(0,1);
			int digit=1;
			int num=0;
			for(int i=x.length-2;i>0;i--){
				num = num + x[i]*digit;
				digit= digit*256;
			}
			ans = ans+num+"\\r";
		}

		return ans;
	}
	private int byteToint(byte[]x){
		int ans=0;
		int digit=1;
		for(int i=x.length-1;i>-1;i--){

			ans = ans + x[i]*digit;
			digit=digit*256;
		}

		return ans;
	}




	public String readMessage(){
		String ans="";
		InputStream in=null;

		try {
			in = port.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] buffer = new byte[4];
		while (true) {
			int numRead=0;
			try {
				numRead = in.read(buffer);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (numRead == -1) {
				break;
			} else if (numRead == 0) {
				break;
//				try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					// ���荞�܂�Ă��������Ȃ�
//				}
			}
			// buffer����ǂݏo������
			if(numRead ==4){
				System.out.println(count(buffer));
				ans = ans+new String(buffer);
			}
			else if(numRead==2){
				ans = ans + new String(buffer);
				System.out.println("Repley:"+new String(buffer));
			}
			else{
				System.out.println("Readed byte:"+numRead);
				ans = ans+new String(buffer);
			}

		}
		System.out.println("readed:"+ans);
		return ans;

	}

	public double readCount(){
		double ans=0;
		InputStream in=null;

		try {
			in = port.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] buffer = new byte[4];
		while (true) {
			int numRead=0;
			try {
				numRead = in.read(buffer);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (numRead == -1) {
				break;
			} else if (numRead == 0) {
				break;
//				try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					// ���荞�܂�Ă��������Ȃ�
//				}
			}
			// buffer����ǂݏo������
			if(numRead ==4){
				ans = count(buffer);
//				ans = ans+new String(buffer);
			}
			else if(numRead==2){
				System.out.println("Repley:"+new String(buffer));
			}
			else{
				System.out.println("Readed byte:"+numRead);
//				ans = ans+new String(buffer);
			}

		}
		return ans;

	}

	public int read() {
		InputStream in=null;
		int ans =-1;
		try {
			in = port.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buffer = new byte[4];
		while (true) {
			int numRead=0;
			try {
				numRead = in.read(buffer);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (numRead == -1) {
				break;
			} else if (numRead == 0) {
				break;
//				try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					// ���荞�܂�Ă��������Ȃ�
//				}
			}
			// buffer����ǂݏo������
			if(numRead ==4){
				System.out.println(count(buffer));
				ans = count(buffer);
			}
			else if(numRead==2){
				System.out.println("Reply:"+new String(buffer));
			}
			else{
				System.out.println("Readed byte:"+numRead);
			}

		}

		return ans;

	}

	public int count(byte[] buf){
		int ans=0;
		int b0=(buf[0] & 0xff)*256*256*256;
		int b1 =(buf[1] & 0xff)*256*256;
		int b2 = (buf[2] & 0xff)*256;
		int b3 = (buf[3] & 0xff);
		System.out.println(b3);
		ans = b0+b1+b2+b3;
		if(b0>127){
			ans = -100000;
			System.out.println("Overflow!!");
		}
		return ans;
	}
	public void portclose(){

	    try {
		       Thread.sleep(2000);  // Be sure data is xferred before closing
		       outputStream.close();
		    } catch (Exception e) {}
		    if(port!=null){
		    	port.close();
		    	System.out.println("Photomul(COM"+n+") Port closed.");
		    }


//		    System.exit(1);

	}

	public void portfastclose(){
	    try {
		       Thread.sleep(1000);  // Be sure data is xferred before closing
		       outputStream.close();
		    } catch (Exception e) {}
		    if(port!=null){
		    	port.close();
//		    	System.out.println("Port closed.");
		    }
	}


	class SerialPortListener implements SerialPortEventListener {
		long startTime;
		public SerialPortListener(){}
		public SerialPortListener(long starttime){
			startTime =starttime;
		}
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				wait(500);
				double lum =readCount();
				if(lum >0) {
					countNow++;
					System.out.println("c"+countNow);
					sumlum = sumlum+lum;
					if(countingNumber == countNow){
						if(outputfile != null)writefile(sumlum);
						long dateinms = new Date().getTime()-startTime;
						double now = (double) dateinms /1000.0/60.0/60.0;
						if(dataseries != null) dataseries.add(now, sumlum);
						sumlum=0;
						countNow=0;
					}
				}
			}
		}

		public void writefile(double val){
			mfw = new MyFileWriter(outputfile,true);
			mfw.writeALine(createNow()+"	"+val);
			mfw.close();
		}

		public void wait(int time){
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

    void enableListener() {
        try {
            port.addEventListener(new SerialPortListener());
            port.notifyOnDataAvailable(true);
        } catch (TooManyListenersException e) {
            // �G���[����
        }
    }

    void enableListener(long starttime) {
        try {
            port.addEventListener(new SerialPortListener(starttime));
            port.notifyOnDataAvailable(true);
        } catch (TooManyListenersException e) {
            // �G���[����
        }
    }




	public byte[] command (String a, int n){
		byte[] nb = intTobyte(n);
		byte ans[] =new byte[nb.length+2];
		ans[0] = a.getBytes()[0];
		int cnt=1;
		for(int i=0;i<ans.length-2;i++){
			ans[cnt] = nb[i];
			cnt++;
		}

		ans[ans.length-1] = "\r".getBytes()[0];
		return ans;
	}

	public byte[] command (String a){
		byte ans[] =new byte[2];
		if(a.equals("")){
			ans =new byte[1];
			ans[0] = "\r".getBytes()[0];
		}
		else{
			ans[0] = a.getBytes()[0];
			ans[1] = "\r".getBytes()[0];
		}


		return ans;
	}


	public byte[] intTobyte(int n){
		byte[] ans = new byte[1];
		if (n > 255){
			ans= new byte[2];
			ans[1] = (byte)(n >>> 0 );
			ans[0] = (byte)(n >>> 8 );
		}
		else{
			ans[0] = (byte)(n >>> 0);
		}


		return ans;

	}

	public void wait(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendcommand(String text, int n){
		byte[] com =this.command(text,n);
		this.wait(1000);
		this.write(com);
		this.wait(1000);


	}

	public void sendcommand(String text){
		byte[] com =this.command(text);
		this.write(com);

	}

	public boolean init(){
		boolean ans=false;
		openSerialPort(super.comport);
		setSerialPort();
		ans = isOpenPortConnection();
		if(ans){
			starttime = new Date().getTime();
			enableListener(starttime);
			sendcommand("D");
			sendcommand("V",PMT);
			setMeasureParameter(100,countingNumber);
		}

		return ans;
	}

	public boolean init(int measureTime){
		this.countingNumber = measureTime;
		boolean ans=false;
		openSerialPort(super.comport);
		setSerialPort();
		ans = isOpenPortConnection();
		if(ans){
			starttime = new Date().getTime();
			enableListener(starttime);
			sendcommand("D");
			sendcommand("V",PMT);
			setMeasureParameter(100,countingNumber);
		}

		return ans;
	}

	public void measure(int time){
		mfw = new MyFileWriter(outputfile,true);
		sendcommand("C");
		wait(time);
		sendcommand("");
		mfw.close();

	}
	public void measure(){
		sendcommand("C");
	}

	public void setMeasureParameter(int integraltime, int times){
		sendcommand("P",integraltime);
		sendcommand("R", times);
	}
	public double measureOnce(){
		double ans;
		sendcommand("S");
		wait(1500);
		ans = readCount();
		/*
		mfw = new MyFileWriter(outputfile,true);
		mfw.writeALine(createNow()+"	"+ans);
		mfw.close();
		*/
		return ans;
	}

	public void measureOnce2(){
		sendcommand("S");
	}

	public void measureStop(){
		sendcommand("");
	}

	private String createNow(){
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		return fmt.format(cal.getTime());

	}

	public String sendAndListen(String mes){
		String ans="";
		sendcommand(mes);
		wait(1000);
		ans = readMessage();
		return ans;
	}

	public boolean checkConnection(){
		boolean ans=false;
		openSerialPort(super.comport);
		setSerialPort();
		String text="1";
		text = sendAndListen("D");
		if(!text.equals("1")) {
			ans =true;
		}
		this.portfastclose();
		System.out.println(ans);
		return ans;
	}

	public boolean isOpenPortConnection(){
		boolean ans=false;
		String text = sendAndListen("D");
		boolean getText = false;
		if(text.length()!=0) getText = true;
		
		while(!getText) {
			text = sendAndListen("D");
			if(text.length()!=0) getText = true;
		}
			text = text.substring(0, 2);
		System.out.println("checkPort:");
		if(text.equals("VA")){
			System.out.print("Port Open!");
			ans = true;
		}
		return ans;
	}
	public static void main(String[] args) {
		CommunicatorPhotomul c = new CommunicatorPhotomul(1);
		c.init();
		c.setOutputfile("./result/test/test.txt");
		c.measureOnce();
//		c.measure(10000);
//		String text = c.sendAndListen("D");
//		System.out.println(text+"teststest");
		c.portclose();

	}



}


