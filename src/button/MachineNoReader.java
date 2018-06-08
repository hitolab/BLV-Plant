package button;
import fileIO.MatrixFileReader;

public class MachineNoReader {

	int no=0;

	public static String curDir = "C:\\Eclipce\\pleiades\\workspace\\BLVPlant\\";
	boolean isWindows = false;

	public MachineNoReader(){
		osCheck();
		String path = curDir+"machineno.txt";
		MatrixFileReader mfr = new MatrixFileReader(path);
		no = Integer.parseInt(mfr.readString()[0]);

	}

	private void osCheck(){
		String os = System.getProperty("os.name");
		if(os.contains("Windows")) isWindows =true;
		if(isWindows == false)  curDir = "/Applications/BLV/input/";
	}

	public int getMachineNo(){
		return no;
	}

	public static void main(String args[]){
		MachineNoReader mnr = new MachineNoReader();
		System.out.println(mnr.no);

	}

}
