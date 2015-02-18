package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image img;

	public ImagePanel(String img) {
		this(new ImageIcon(img).getImage());
	}

	public ImagePanel(Image img) {
		this.img = img;
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setBorder(null);
		setLayout(null);
	}

	public void setPic(Image img) {
		this.img = img;
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}

}