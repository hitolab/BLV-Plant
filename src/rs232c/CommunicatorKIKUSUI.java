package rs232c;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CommunicatorKIKUSUI extends Communicator {
	
	CommPortIdentifier portId;
	SerialPort port;
	static OutputStream outputStream;
	
	public CommunicatorKIKUSUI(int n){
		super(n);
	}
	
	public CommunicatorKIKUSUI(String str){
		super(str);
	}

	@Override
	public boolean checkConnection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean init() {
		boolean ans=false;
		if(super.comport >=0) openSerialPort(super.comport);
		else openSerialPort(super.comportStr);
		setSerialPort();
		ans = true;
		this.sendMessage(this.setNODE(), true);
		this.sendMessage(this.setChannel(), true);
		
		return ans;
	}
	
	boolean setSerialPort() {
		try {
			port.setSerialPortParams(
					9600,                   // 通信速度[bps]
					SerialPort.DATABITS_8,   // データビット数
					SerialPort.STOPBITS_1,   // ストップビット
					SerialPort.PARITY_NONE   // パリティ
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
	
	boolean openSerialPort(int x) {
		try {	
			System.out.println(x);
			portId = CommPortIdentifier.getPortIdentifier("COM"+x);
			port = (SerialPort)portId.open("pato", 2000);
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
	
	boolean openSerialPort(String str) {
		System.out.println(str);
		try {
			
			portId = CommPortIdentifier.getPortIdentifier(str);
			port = (SerialPort)portId.open("pato", 2000);
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

	@Override
	public double measureOnce() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void portclose(){
		
	    try {
		       Thread.sleep(2000);  // Be sure data is xferred before closing
		       outputStream.close();
		    } catch (Exception e) {}
		    if(port!=null){
		    	port.close();
		    	System.out.println("KIKUSUI Port closed.");
		    }
		    
		    
//		    System.exit(1);

	}

	@Override
	public void setOutputfile(String outputfile) {
		// TODO Auto-generated method stub

	}
	
	
	public void write(byte[] command,boolean printMessage) {

		try {
			outputStream.write(command);
			
		    } catch (IOException e) {}
		    if(printMessage) System.out.print("Send:"+byteArrayToString(command));
		    
	}
	
	public String byteArrayToString(byte[]x){
		String ans="";
		for(int i=0;i<x.length;i++){
			ans = ans + Character.toString((char)x[i]);
		}
		
		return ans;
	}
	
	public byte[] testMessage(){
		byte ans[];

		ans ="*IDN?\n".getBytes();
		
		return ans;
	}
	
	public byte[] setChannel(){
		byte[] ans;
		ans = "CH 1\n".getBytes();		
		return ans;
	}
	public void OUTPUTON(){
		byte[] mes;
		mes = ("OUT 1\n").getBytes();		

		this.sendMessage(mes, false);
		System.out.println("OUTPUT ON!");
	}
	
	public void OUTPUTOFF(String kind){
		byte[] mes;
		mes = ("OUT 0\n").getBytes();		
		//ログファイルを書く
		File currentlog = new File("C:\\Program Files\\BLV\\currentlog.txt");
				try {
					if (checkBeforeWritefile(currentlog)){
						FileWriter filewriter;
						Date now = new Date();
						filewriter = new FileWriter(currentlog, true);
						filewriter.write("0.0	"+kind+"	"+now+"\r\n");
						filewriter.close();
					}else{
						System.out.println("ファイルに書き込めません");
					}
				}catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//ログファイルここまで

		this.sendMessage(mes, false);
		System.out.println("OUTPUT OFF!");
	}
	

	
	private String doubleToString4digit(double x, int dig){
		String ans;
		DecimalFormat df=new DecimalFormat();
		df.applyPattern("0");
        df.setMaximumFractionDigits(dig);
        df.setMinimumFractionDigits(dig);
        ans=df.format(new Double (x));	
		return ans;
	}
	
	public void setV(double vol){
		byte[] mes;
		if(vol<0 || vol >60) System.out.println("Strange voltage setting.");
		String volstr = doubleToString4digit(vol,2);
		mes = ("VSET "+volstr +"\n").getBytes();			
		this.sendMessage(mes, false);
		System.out.println("Set voltage "+volstr+" [V]");
	}
	
	public void setI(double cur, String kind){
		byte[] mes;
		if(cur<0 || cur >6) System.out.println("Strange current setting.");
		String curstr = doubleToString4digit(cur,3);
		mes = ("ISET "+curstr +"\n").getBytes();			
		this.sendMessage(mes, false);
		System.out.println("Set current "+curstr+" [A]");
		//ログファイルを書く
		File currentlog = new File("C:\\Program Files\\BLV\\currentlog.txt");
		try {
			if (checkBeforeWritefile(currentlog)){
				FileWriter filewriter;
				Date now = new Date();
				filewriter = new FileWriter(currentlog, true);
				filewriter.write(""+cur+"	"+kind+"	"+now+"\r\n");
				filewriter.close();
			}else{
				System.out.println("ファイルに書き込めません");
			}
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//ログファイルここまで
	}
	
	public double getI(){
		double ans=0;
		byte[] mes;
		mes = ("IOUT?"+"\n").getBytes();
		try{
		ans = Double.parseDouble(this.byteArrayToString(this.sendMessage(mes, false)));
		}
		catch(Exception e){
			ans =0;
		}
		return ans;
	}
	
	public double getV(){
		double ans=0;
		byte[] mes;
		mes = ("VOUT?"+"\n").getBytes();
		
		ans = Double.parseDouble(this.byteArrayToString(this.sendMessage(mes, false)));
		return ans;
	}
	
	public byte[] setNODE(){
		byte[] ans;
		ans = "NODE 5\n".getBytes();
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
	
	public String sendTestMessage(){
		String ans;
 		this.write(testMessage(), true);
// 		wait(5000);
		ans = this.byteArrayToString(this.read(true));
		return ans;
	}
	
	public byte[] sendMessage(byte[] mes, boolean displayReply){
		byte[] ans={0};
		this.write(mes, displayReply);
//		ans = this.read(displayReply);
		
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
		int testdata=100;
		System.out.println(testdata);
		try{
		testdata = in.available();
		}catch(IOException e){}
		System.out.println(testdata);
		
		byte[] buffer = new byte[12];
		int readedByte=0;
		while (true) {
			int numRead=0;
			try {
				System.out.println(numRead);
				numRead = in.read(buffer);
				System.out.println(numRead);

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
		if(printMessage)System.out.println("\n--------------------");
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
	
	public void setVI(double V, double I, String kind){
		this.setV(V);
		this.setI(I, kind);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	//	CommunicatorKIKUSUI ck = new CommunicatorKIKUSUI(3);
		CommunicatorKIKUSUI ck = new CommunicatorKIKUSUI("/dev/cu.UC-232AC");
		
		
		ck.init();
		ck.setV(12.46725);
		ck.OUTPUTON();
		ck.portclose();
//		System.out.println(ck.sendTestMessage());
//		ck.wait(1000);
//		System.out.println(ck.getI()+1);
//		ck.portclose();
/*			
		ck.wait(1000);
		ck.setI(1.46725);
		ck.wait(1000);
		System.out.println(ck.getV());
		ck.wait(1000);
		ck.OUTPUTOFF();
		ck.portclose();
		
*/
	}
	
	private static boolean checkBeforeWritefile(File file){
		if (file.exists()){
			if (file.isFile() && file.canWrite()){
		    	  return true;
			}
		}
		return false;
	}

}
