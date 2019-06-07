package rs232c;

public abstract class Communicator {
	int comport=-1;
	String comportStr;
	public Communicator(int n){
		comport = n;
	}
	
	public Communicator(String str){
		comportStr = str;
	}
	abstract public double measureOnce();
	abstract public void portclose();
	abstract public boolean init();
	abstract public void setOutputfile(String outputfile);
	abstract public boolean checkConnection();
	

}
