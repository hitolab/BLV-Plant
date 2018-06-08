package rs232c;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class CommunicatorArduino extends Communicator{


	int n;


	public CommunicatorArduino(int n) {
		super(n);
		this.n=n;
	}

	CommPortIdentifier portId;
	SerialPort port;
	OutputStream outputStream;


	public int getPort(){
		return this.n;
	}

	boolean openSerialPort(int n) {
		try {
			portId = CommPortIdentifier.getPortIdentifier("COM"+n);
			port = (SerialPort)portId.open("Arduino", 5000);
		} catch (NoSuchPortException e) {
			e.printStackTrace();
			return false;
		} catch (PortInUseException e) {
			e.printStackTrace();
			return false;
		}

	    try {
			outputStream = port.getOutputStream();
			System.out.println("Port opens.");
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
			this.wait(1000);
			outputStream.write(comand);
		    System.out.println(comand[0]);

		    } catch (IOException e) {
			    System.out.println("Error!");
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
				System.out.println(byteToString(buffer));
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

	@Override
	public void portclose(){

	    try {
		       Thread.sleep(2000);  // Be sure data is xferred before closing
		       outputStream.close();
		    } catch (Exception e) {}
		    if(port!=null){
		    	port.close();
		    	System.out.println("Arduino(COM"+n+") Port is closed.");
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
		@Override
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				wait(500);
				String message =readMessage();
				System.out.println(message);

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

			ans =new byte[1];//test
			ans[0] = a.getBytes()[0];
			//ans[1] = "\r".getBytes()[0];
			System.out.println(ans[0]);
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

		return ans;
	}

	public static void main(String[] args) {
		CommunicatorArduino c = new CommunicatorArduino(5);
		c.init();
		c.wait(1000);
		c.sendcommand("o");
		c.wait(1000);
		c.sendcommand("p");
		c.wait(1000);
		c.sendcommand("o");

//		c.measure(10000);
//		String text = c.sendAndListen("D");
//		System.out.println(text+"teststest");
		c.portclose();

	}

	@Override
	public double measureOnce() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setOutputfile(String outputfile) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkConnection() {
		// TODO Auto-generated method stub
		return false;
	}



}


