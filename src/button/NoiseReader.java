package button;
import fileIO.MatrixFileReader;

public class NoiseReader {
	String[][] noise;
	boolean isWindows;
	
	
	public NoiseReader(){
		MatrixFileReader mfr;
		osCheck();
		if(isWindows == true) mfr= new MatrixFileReader("C:\\Users\\alskd\\eclipse-workspace\\BLV\\input\\noise.txt");
		else mfr = new MatrixFileReader("/Applications/BLV/input/noise.txt");
		this.noise = mfr.readStringMatrix();
		
	}
	
	public double getNoise(int i){
		return Double.parseDouble(noise[i][1]);
	}
	
	public double getNoiseTime(int i){
		return Integer.parseInt(noise[i][0]);
	}

	public double[] getAllNoise(){
		int i = 0;
		double[] allNoise = new double[noise.length];
		for(;i<noise.length;i++){
			allNoise[i] = getNoise(i);
		}
		return allNoise;
	}
	
	public int[] getAllNoiseTime(){
		int i = 0;
		int[] allNoiseTime = new int[noise.length];
		for(;i<noise.length;i++){
			allNoiseTime[i] = (int) getNoiseTime(i);
		}
		return allNoiseTime;
		
	}
	
	public static void main(String[] args) {
		NoiseReader test = new NoiseReader();
		test.getAllNoise();
	}
	
	private void osCheck(){
		String os = System.getProperty("os.name");
		if(os.contains("Windows")) isWindows =true;
		else isWindows = false;
	}
	
}
