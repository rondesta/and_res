package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ImageButton(ImageIcon imageIcon) {
		this.setIcon(imageIcon);
		this.setForeground(new Color(255, 255, 255));
		this.setFont(new Font("Helvetica Neue", Font.PLAIN, 24));
		this.setHorizontalTextPosition(JButton.CENTER);
		this.setVerticalTextPosition(JButton.CENTER);
		this.setIcon(imageIcon);
	}
}
