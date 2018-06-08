package rs232c;
import fileIO.MatrixFileReader;

public class Test {

	public static void main(String[] args) {

		CommunicatorArduino ca = new CommunicatorArduino(5);
		CommunicatorKIKUSUI ck = new CommunicatorKIKUSUI(7);
		CommunicatorPhotomul cp = new CommunicatorPhotomul(1);
		
		MatrixFileReader mfr;
		mfr= new MatrixFileReader("./input/noise.txt");

		ck.init();
		ck.wait(5000);
		ck.OUTPUTOFF("Dark");
		ck.portclose();

		ca.init();
		ca.wait(2000);
		ca.sendcommand("o");
		ca.portclose();


		cp.init();
		cp.wait(2000);
		System.out.println(cp.measureOnce());
		cp.wait(2000);

		System.out.println(cp.measureOnce());
		cp.wait(2000);

		System.out.println(cp.measureOnce());
		cp.wait(2000);


		cp.portclose();


		ca.init();
		ca.wait(6000);
		ca.sendcommand("p");
		ca.portclose();


		ck.init();
		ck.wait(4000);
		//ck.setV(24);
		ck.setVI(24,0.2,"Light");
		ck.OUTPUTON();
		ck.portclose();

		/////開発終わったら以下消す。
		ck.init();
		ck.wait(3000);
		ck.OUTPUTOFF("Dark");
		ck.portclose();

		//ca.init();
		//ca.wait(1000);//これ大事かも
		/*
		ck.init();


		//1.camber Light off

		ck.wait(5000);
		ck.OUTPUTOFF("Dark");


		//2.photomul on
		ca.init();
		ca.wait(1000);
		ca.sendcommand("o");

		//3.measureOnce RUN
	  // cp.measureOnce2();

		//4.photomul off
		ca.wait(3000);
		ca.sendcommand("p");
		ca.portclose();

		//5.camber Light ON
		ck.wait(10000);
		ck.setV(24);
		ck.setVI(24,0.2,"Light");
		ck.OUTPUTON();
		ck.portclose();







		//ca.wait(1000);
		//ca.sendcommand("p");
		//ca.wait(1000);
		//ca.sendcommand("o");

//		c.measure(10000);
//		String text = c.sendAndListen("D");
//		System.out.println(text+"teststest");
		//ca.portclose();


*/


	}

}
