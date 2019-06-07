package button;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import rs232c.CommunicatorKIKUSUI;

public class MyLightOFFButton extends JButton{

	ConfirmState cs;
		
		public MyLightOFFButton(String filename, final CommunicatorKIKUSUI c, final ConfirmState cs){
			
				super(new ImageIcon(filename));
				this.setHorizontalAlignment(SwingConstants.CENTER);
				this.setHorizontalTextPosition(SwingConstants.CENTER);
				this.setVerticalTextPosition(SwingConstants.BOTTOM);
				this.setFocusPainted(false);
				this.setForeground(new Color(255, 0, 0));
				this.cs = cs;
				this.addActionListener(new ActionListener() {
				       public void actionPerformed(ActionEvent e) {
				 		 c.init();
				 		 c.OUTPUTOFF("D");
				 		 c.portclose();
				 		 cs.bright = false;
				       }
				    });
			}
}
