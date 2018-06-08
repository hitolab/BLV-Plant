package button;

import java.awt.Color;

import javax.swing.JLabel;

public class DisplayCharacters extends JLabel {
	
	public DisplayCharacters(String str){
	super(str);
	this.setOpaque(true);
	this.setBackground(Color.white);
	this.setHorizontalAlignment(JLabel.LEFT);
	}

}
