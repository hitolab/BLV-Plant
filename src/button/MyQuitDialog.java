package button;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import rs232c.CommunicatorPhotomul;


public class MyQuitDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MyQuitDialog(Frame owner, final CommunicatorPhotomul c) {
		super(owner);
		getContentPane().setLayout(new FlowLayout());
		JButton btn = new JButton("OK");
		btn.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	c.portclose();	    		   
		    	myStopButton.rewriteEXPNO();
		 		 System.exit(0);
		       }
		    });
		
		getContentPane().add(btn);

		setTitle("MyDialog");
		setSize(100, 70);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		
	}

}
