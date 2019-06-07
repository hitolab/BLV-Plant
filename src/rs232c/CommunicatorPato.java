package rs232c;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;

public class CommunicatorPato extends Communicator {
	
	CommPortIdentifier portId;
	SerialPort port;
	static OutputStream outputStream;


	
	public CommunicatorPato(int n){
		super(n);
	}
	@Override
	public boolean checkConnection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean init() {
		boolean ans=false;
		openSerialPort(super.comport);
		setSerialPort();
		ans = true;
		
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

	@Override
	public double measureOnce() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void portclose() {
		// TODO Auto-generated method stub

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
	
	public void turnON1and2(){
		write(this.message(true, true, true),true);
		
	}
	
	public void turnOFF1and2(){
		write(this.message(false, true, true),true);
		
	}
	
	public byte[] messageBody (String command, String data1, String data2){
		byte ans[] = new byte[7];
		ans[0] ="@".getBytes()[0];//Header
		ans[1] ="?".getBytes()[0];
		ans[2] ="?".getBytes()[0];
		ans[3] =command.getBytes()[0];
		ans[4] =data1.getBytes()[0];
		ans[5] =data2.getBytes()[0];
		ans[6] ="!".getBytes()[0];
		return ans;
	}
	
	public byte[] message(boolean on, boolean ch1, boolean ch2){
		byte[] ans;
		String command,data1,data2;
		if(on) command="1";
		else command="0";
		int bitdata=0;
		if(ch1) bitdata= bitdata+1;
		if(ch2) bitdata= bitdata+2;
		data1="0";
		data2=""+bitdata;
		ans = messageBody(command,data1,data2);
		
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
	
	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CommunicatorPato cp = new CommunicatorPato(6);
		cp.init();
		cp.turnON1and2();
		cp.wait(5000);
		cp.turnOFF1and2();
		cp.wait(5000);
		cp.turnON1and2();
		
	}

}
