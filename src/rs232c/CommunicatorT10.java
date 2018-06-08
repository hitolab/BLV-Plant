package rs232c;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import gnu.io.*;
import fileIO.*;

public class CommunicatorT10 extends Communicator{


	
	MyFileWriter mfw;
	CommPortIdentifier portId;
	SerialPort port;
	static OutputStream outputStream;
	String outputfile="";

	public CommunicatorT10(int n){
		super(n);
	}
	
	public void setOutputfile(String outputfile){
		this.outputfile = outputfile;
	}

	boolean openSerialPort(int x) {
		try {
			portId = CommPortIdentifier.getPortIdentifier("COM"+x);
			port = (SerialPort)portId.open("kodokei", 2000);
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
					9600,                   // 通信速度[bps]
					SerialPort.DATABITS_7,   // データビット数
					SerialPort.STOPBITS_1,   // ストップビット
					SerialPort.PARITY_EVEN   // パリティ
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

	public void write(byte[] command,boolean printMessage) {
		try {

			outputStream.write(command);
			
		    } catch (IOException e) {}
		    
		    if(printMessage) System.out.print("Send:"+byteArrayToString(command));
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
	

	public byte[] read(boolean printMessage) {
		InputStream in=null;
		byte[] ans =new byte [32];
		try {
			in = port.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] buffer = new byte[12];
		int readedByte=0;
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
//					// 割り込まれても何もしない
//				}
			}
			// bufferから読み出し処理
			ans = addBuffer(ans,buffer,numRead,readedByte);
			readedByte = readedByte + numRead;
			if(numRead ==14){
//				System.out.println("Reply:"+byteArrayToString(buffer));
				buffer = new byte[12];
				ans = buffer;
			}
			else{
//				System.out.println("Readed byte:"+numRead);
			}
			
		}
		
		if(printMessage)System.out.print("Reply:"+byteArrayToString(ans));
		if(printMessage)System.out.println("--------------------");
		return ans;
		
	}
	
	private byte[] addBuffer(byte[] x, byte[] buffer,int numRead,int readedByte){
		 byte[] ans = new byte[32];
		 for(int i=0;i<ans.length;i++){
			 if(i<readedByte) ans[i] = x[i];
			 else if(i< (readedByte+numRead) ) ans[i] = buffer[i-readedByte];
		 }
		 
		 return ans;
	}
	public int count(byte[] buf){
		int ans=0;
		ans = ans+ (buf[0] & 0xff)*16*16*16;
		ans = ans+ (buf[1] & 0xff)*16*16;
		ans = ans+ (buf[2] & 0xff)*16;
		ans = ans+ (buf[3] & 0xff);
		return ans;
	}
	public void portclose(){
		
	    try {
		       Thread.sleep(1000);  // Be sure data is xferred before closing
		       outputStream.close();
		    } catch (Exception e) {}
		    if(port!=null){
		    	port.close();
		    	System.out.println("Port closed.");
		    }
		    
		    
//		    System.exit(1);

	}
	
	public void portfastclose(){
		
	    try {
//		       Thread.sleep(1000);  // Be sure data is xferred before closing
		       outputStream.close();
		    } catch (Exception e) {}
		    if(port!=null){
		    	port.close();
//		    	System.out.println("Port closed.");
		    }
		    
		    
//		    System.exit(1);

	}

/*	class SerialPortListener implements SerialPortEventListener {
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				wait(500);
				int count = read();
				if(count>=0) {
					mfw = new MyFileWriter(outputfile,true);
					mfw.writeALine(createNow()+"	"+count);
					mfw.close();
				}
			}
			
			
			
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
            // エラー処理
        }
    }*/


	
	public byte[] message (String number, String command, String parameter){
		byte ans[] = new byte[14];
		ans[0] =2;//STX
		ans[1] =number.getBytes()[0];
		ans[2] =number.getBytes()[1];
		ans[3] =command.getBytes()[0];
		ans[4] =command.getBytes()[1];
		ans[5] =parameter.getBytes()[0];
		ans[6] =parameter.getBytes()[1];
		ans[7] =parameter.getBytes()[2];
		ans[8] =parameter.getBytes()[3];
		ans[9] =3;
		String BCC = getBCC(ans);
		ans[10] =BCC.getBytes()[0];
		ans[11] =BCC.getBytes()[1];
		ans[12] ="\r".getBytes()[0];
		ans[13] ="\n".getBytes()[0];
		return ans;
	}
	
	public String byteArrayToString(byte[]x){
		String ans="";
		for(int i=0;i<x.length;i++){
			ans = ans + Character.toString((char)x[i]);
		}
		
		return ans;
	}
	
	public String getBCC(byte[]x){
		String ans="";
		int xor = (x[1]^x[2]^x[3]^x[4]^x[5]^x[6]^x[7]^x[8]^x[9]);
		
		ans =intToHex2(xor);
		
		return ans;
	}
	
	private String intToHex2(int Value) {
	    char HEX2[]= {Character.forDigit((Value>>4) & 0x0F,16),
	    Character.forDigit(Value & 0x0F,16)};
	    String Hex2Str = new String(HEX2);
	    return Hex2Str.toUpperCase();
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
	
	public byte[] sendcommand(String number, String command, String parameter,boolean printMessage){
		byte[] ans = new byte[32];
		byte[] com =this.message(number, command, parameter);
		this.write(com,printMessage);
		this.wait(500);
		ans = this.read(printMessage);
		this.wait(500);
		
		return ans;

	}
	
	
	public boolean init(){
		boolean ans=false;
		openSerialPort(super.comport);
		setSerialPort();
		ans = changeToConnectionMode();
		if(ans){
			wait(1000);
			setMeasureCondition();
		}
		
		return ans;
	}

	private boolean changeToConnectionMode(){
		boolean ans=false;
		byte[] dataByte = sendcommand("00","54","1   ",true);
		ans=isResponsible(dataByte);
		wait(500);	
		return ans;
	}
	
	private boolean isResponsible(byte[] x){
		boolean ans=false;
		if(Character.toString((char)x[3]).equals("5")) ans=true;
		return ans;
	}
	private void setMeasureCondition(){
		sendcommand("00","10","0200",true);
	}
	

	public double measureOnce(){
		double ans=-1;
		byte[] dataByte = sendcommand("00","10","0200",false);
		if(checkError(dataByte)==0){
			ans = dataByteToInt(dataByte);
			mfw = new MyFileWriter(outputfile,true);
			mfw.writeALine(createNow()+"	"+ans);
			mfw.close();
		}
		else {
			System.out.println("Error type"+checkError(dataByte)+"occured.");
		}
		return ans;
	}
	private int checkError(byte[] x){
		int ans=0;
		String str1=Character.toString((char)x[6]);
		String str2=Character.toString((char)x[8]);
		if(str1.equals(" ") || str1.equals("7")) ans=0;
		if(str1.equals("1")) ans=1;
		if(str1.equals("2")) ans=2;
		if(str1.equals("3")) ans=3;
		if(str1.equals("5")) ans=5;
		if(str2.equals("1")) ans=11;
		if(str2.equals("3")) ans=33;		
		
		return ans;
	}
	private  double dataByteToInt(byte[] x){
		double ans=0;
		if(Character.toString((char)x[9]).equals("+")){
			String str1 = Character.toString((char)x[10]);
			String str2 = Character.toString((char)x[11]);
			String str3 = Character.toString((char)x[12]);
			String str4 = Character.toString((char)x[13]);
			String str5 = Character.toString((char)x[14]);
			int bit1=0,bit2=0,bit3=0,bit4=0,bit5=0;
			if(!str1.equals(" ")) bit1 = Integer.valueOf(str1).intValue();
			if(!str2.equals(" ")) bit2 = Integer.valueOf(str2).intValue();
			if(!str3.equals(" ")) bit3 = Integer.valueOf(str3).intValue();
			if(!str4.equals(" ")) bit4 = Integer.valueOf(str4).intValue();
			if(!str5.equals(" ")) bit5 = Integer.valueOf(str5).intValue();
			ans = (1000*bit1+100*bit2+10*bit3+bit4)*power10(bit5-4);
		}
		return ans;
	}
	private double power10(int n){
		double ans=1;
		if(n>0){
			for(int i=0;i<n;i++){
				ans = ans * 10.0;
			}
		}
		if(n<0){
			for(int i=0;i<n;i++){
				ans = ans / 10.0;
			}
		}
		
		return ans;
	}
	private String createNow(){
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
		return fmt.format(cal.getTime());

	}
	
	public boolean checkConnection(){
		boolean ans=false;
		openSerialPort(super.comport);
		setSerialPort();
		ans = changeToConnectionMode();
		return ans;
	}

	public static void main(String[] args) {
		Communicator c = new CommunicatorT10(4);
		c.setOutputfile("./result/test/test.txt");
		c.init();
		System.out.println(c.measureOnce());
		System.out.println(c.measureOnce());
		System.out.println(c.measureOnce());
		System.out.println(c.measureOnce());
		System.out.println(c.measureOnce());
		c.portclose();
		
		

	}

}


