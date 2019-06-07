package button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import static main.BLV.curDir; 

import main.BLV;

public class mySaveButton extends JButton{

	private static final long serialVersionUID = 1L;


	public mySaveButton(String filename, final BLV f, final DisplayEXPNO en){
		super(new ImageIcon(filename));

		final JFrame jx = f;
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setFocusPainted(false);
		this.setForeground(new Color(255, 0, 0));
		this.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {

		    	JFileChooser fc = new JFileChooser();
		    	fc.setSelectedFile(new File("exp"+en.expno+".txt"));
		    	
		    	
		    	int selected = fc.showSaveDialog(jx);
		    	if (selected == JFileChooser.APPROVE_OPTION){
		    	      File file = fc.getSelectedFile();
		    	      try {
						filecopy(curDir+"/input/tmp.txt",file.getAbsolutePath());

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}

		       }
		    });
		
	}
	
	
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
	
}

