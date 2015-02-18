package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageLabel(ImageIcon imageIcon) {
		this.setIcon(imageIcon);
		this.setForeground(new Color(255, 255, 255));
		this.setFont(new Font("Helvetica Neue", Font.PLAIN, 24));
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.CENTER);
		this.setIcon(imageIcon);
	}

	public ImageLabel(String string) {
		super(string);
	}
}
