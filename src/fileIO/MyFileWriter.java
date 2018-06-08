
package fileIO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MyFileWriter {

	BufferedWriter bw;


	public MyFileWriter(String PATH){
		try {
			bw = new BufferedWriter(new FileWriter(PATH));
		}
		catch(IOException e){
			System.err.println("error" + e);
			System.exit(1);
		}
	}

	public MyFileWriter(String PATH, boolean append){
		try {
			bw = new BufferedWriter(new FileWriter(PATH, append));
		}
		catch(IOException e){
			System.err.println("error" + e);
			System.exit(1);
		}
	}

	public void writeDoubleMatrix(double[][] data){

		for(int i=0;i<data.length;i++){
			String textline ="";
			for(int j = 0;j<data[i].length;j++){
				if(j == 0) textline = "" + data[i][j];
				else textline =textline + "\t" + data[i][j];
			}
			try {
				bw.write(textline);
				bw.newLine();
			}
			catch(IOException e){
				System.err.println("error" + e);
				System.exit(1);
			}

		}
	}

	public void writeDoubleMatrix(int [][] data){

		for(int i=0;i<data.length;i++){
			String textline ="";
			for(int j = 0;j<data[i].length;j++){
				if(j == 0) textline = "" + data[i][j];
				else textline =textline + "\t" + data[i][j];
			}
			try {
				bw.write(textline);
				bw.newLine();
			}
			catch(IOException e){
				System.err.println("error" + e);
				System.exit(1);
			}

		}
	}

	public void writeALine(String inputfile){
		try {
			bw.write(inputfile);
			bw.newLine();
		}
		catch(IOException e){
			System.err.println("error" + e);
			System.exit(1);
		}

	}

	public void close(){
		try{
			bw.close();
		}
		catch(IOException e){
			System.err.println("error" + e);
			System.exit(1);
		}
	}


	/**
	 * @param args
	 */
	public static void test(String[] args) {
		//?�g?�?�?�?�
		String filename ="./output/testtest.txt";
		MyFileWriter mfw = new MyFileWriter(filename);
		mfw.writeALine("nobita");
		mfw.close();
	}

}
