package button;

import java.awt.Color;
import java.io.File;

import javax.swing.JLabel;

public class DisplayEXPNO extends JLabel{
	
	public int expno;
	int ch;
	String curDir;

	private static final long serialVersionUID = -3552045707710273405L;

	public DisplayEXPNO(int ch){
		super("EXP  ");
		osCheck();
		this.ch = ch;
		this.expno = readEXPNO2(ch);
		this.setText("EXP  "+expno);
		
		this.setOpaque(true);
		this.setBackground(Color.white);
		this.setHorizontalAlignment(JLabel.CENTER);
	}
	
	public void expnoAdd1(){
		this.expno++;
		this.setText("EXP  "+expno);
	}
	
	public int refresh(){
		this.expno = readEXPNO2(ch);
		this.setText("EXP  "+expno);
		return expno;
	}
	
	public int readEXPNO2(int ch){
		int ans=0;
		String path = curDir+"/result/"+ch;
		File dir = new File(path);
		String[] files = dir.list();
		if(files == null|| files.length==0)ans=1;
		
		else{
			String last = files[files.length-1];
			ans = Integer.parseInt(last.substring(3, 7))+1;
		}
		
		return ans;
	}
	
	private void osCheck(){
		boolean isWindows=false;
		String os = System.getProperty("os.name");
		if(os.contains("Windows")) isWindows =true;
		if(isWindows) curDir = "C:\\Program Files\\BLV";
		else curDir = "/Applications/BLV";
	}
	
	
}
