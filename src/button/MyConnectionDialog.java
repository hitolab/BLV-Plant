package button;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class MyConnectionDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MyConnectionDialog(Frame owner) {
		super(owner);
		getContentPane().setLayout(new FlowLayout());

		JButton btn = new JButton("OK");
		btn.addActionListener(this);
		getContentPane().add(btn);

		setTitle("MyDialog");
		setSize(100, 70);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		
	}

}
