/*
 * ?�?�?�?�: 2006/09/05
 *
 * ?�?�?�̐�?�?�?�?�?�ꂽ?�R?�?�?�?�?�g?�̑}?�?�?�?�e?�?�?�v?�?�?�[?�g?�?�ύX?�?�?�邽?�?�
 * ?�E?�B?�?�?�h?�E > ?�ݒ� > Java > ?�R?�[?�h?�?�?�?� > ?�R?�[?�h?�ƃR?�?�?�?�?�g
 */
package fileIO;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MatrixFileReader {


	BufferedReader br;

	public MatrixFileReader(String PATH){
		try {
			br = new BufferedReader(new FileReader(PATH));
		}
		catch(IOException e){
			System.err.println("error" + e);
			System.exit(1);
		}
	}

	public String [][] readStringMatrix(){
		String [][] ans;
		String [] tmp;
		List<String> lines = new ArrayList<String>();
		String line;

		try {
			while ((line = br.readLine()) != null) {

				lines.add(line);
			}
		} catch (IOException e) {
			// TODO ?�?�?�?�?�?�?�?�?�?�?�ꂽ catch ?�u?�?�?�b?�N
			e.printStackTrace();
		}

		tmp = this.listToString(lines);
		ans = new String[tmp.length][];
		for(int i=0;i<ans.length;i++){
			ans[i] = tmp[i].split("	");
		}

		return ans;
	}

	public String [] readString(){
		String [] ans;
		List<String> lines = new ArrayList<String>();
		String line;

		try {
			while ((line = br.readLine()) != null) {

				lines.add(line);
			}
		} catch (IOException e) {
			// TODO ?�?�?�?�?�?�?�?�?�?�?�ꂽ catch ?�u?�?�?�b?�N
			e.printStackTrace();
		}

		ans = this.listToString(lines);

		return ans;
	}

	public int [][] intread(){

		double [][] x= read();
		int[][] ans = new int [x.length][];
		for(int i=0;i<ans.length;i++){
			ans[i] = new int [x[i].length];
			for(int j=0;j<ans[i].length;j++){
				ans[i][j] = (int) x[i][j];
			}
		}

		return ans;
	}
	public double [][] read(){
		double [][] ans;
		List<String[]> linesprit = new ArrayList<String[]>();
		String line;

		try {
			while ((line = br.readLine()) != null) {

				linesprit.add(line.split("\t"));
			}
		} catch (IOException e) {
			// TODO ?�?�?�?�?�?�?�?�?�?�?�ꂽ catch ?�u?�?�?�b?�N
			e.printStackTrace();
		}

		ans = this.listToDoubleMatrix(linesprit);

		return ans;
	}

	public double [][] readnext(){
		double [][] ans;
		boolean cnt = false;
		List<String[]> linesprit = new ArrayList<String[]>();
		String line;

		try {
			while ((line = br.readLine()) != null) {
				if (cnt == false){
					cnt = true;
				}
				else{
					linesprit.add(line.split("\t"));
				}
			}
		} catch (IOException e) {
			// TODO ?�?�?�?�?�?�?�?�?�?�?�ꂽ catch ?�u?�?�?�b?�N
			e.printStackTrace();
		}

		ans = this.listToDoubleMatrix(linesprit);

		return ans;
	}


	private String [] listToString(List<String> list){
		String[] ans = new String[list.size()];
		int cnt=0;
		Iterator<String> elements = list.iterator();
		while(elements.hasNext()){
			ans[cnt] = (String) elements.next();
			cnt++;
		}
		return ans;
	}

	private double [][] listToDoubleMatrix(List<String[]> list){
		double [][] ans;
		ans = new double[list.size()][];
		int cnt=0;
		Iterator<String[]> elements = list.iterator();

		while(elements.hasNext()){
			String [] line = (String []) elements.next();
			ans[cnt] = new double[line.length];
			for(int i=0;i<line.length;i++){
				ans[cnt][i] = Double.parseDouble(line[i]);
			}
			cnt++;
		}

		return ans;

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
//use example reading firat data of .txt file
		String filename = "./input/noise.txt";
		MatrixFileReader mfr = new MatrixFileReader(filename);
		String ans[] = mfr.readString();

		System.out.println(ans[0]);

		// TODO ?�?�?�?�?�?�?�?�?�?�?�ꂽ?�?�?�\?�b?�h?�E?�X?�^?�u

	}

}
