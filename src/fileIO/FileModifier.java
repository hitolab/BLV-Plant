package fileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileModifier {
	
	
	public static void filecopy(String srcPath, String destPath) 
    throws IOException {
    
    FileChannel srcChannel = new
        FileInputStream(srcPath).getChannel();
    FileChannel destChannel = new
        FileOutputStream(destPath).getChannel();
    try {
        srcChannel.transferTo(0, srcChannel.size(), destChannel);
    } finally {
        srcChannel.close();
        destChannel.close();
    }
    

}
	
	public static void filedelete(String path){
		File f = new File(path);
		f.delete();
	}
	
	public static void filemove(String srcPath,String destPath) throws IOException{
		filecopy(srcPath,destPath);
		if(new File(destPath).exists()) filedelete(srcPath); 
		
	}
	

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		FileModifier fm = new FileModifier();
		
		
//		FileModifier.filecopy("./test/a.txt","./test/b.txt");
//		FileModifier.filedelete("./test/b.txt");
		FileModifier.filemove("./test/a.txt", "./test/e.txt");

	}

}
