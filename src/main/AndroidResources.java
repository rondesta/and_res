package main;

import gui.ImageButton;
import gui.ImageLabel;
import gui.ImagePanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.ConfigManager;
import utils.Utils;

public class AndroidResources {
	private Utils utils = new Utils();
	private JFrame frame;
	private JPanel pnlStatus = new JPanel();
	private JLabel lblStatusCounter = new JLabel();
	private ImageButton btnArrange;
	private Point mouseDownCompCoords;
	private ImagePanel panel = new ImagePanel(new ImageIcon(getClass()
			.getResource("drop_area_off.png").getFile()).getImage());
	private boolean isDragOn = true;
	private DropTarget dropTarget = new DropTarget() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public synchronized void dragEnter(DropTargetDragEvent dtde) {
			toggleDragArea(isDragOn);
			// Change cursor...
			Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
			frame.setCursor(cursor);
		}

		@Override
		public synchronized void dragExit(DropTargetEvent dtde) {
			this.changeToNormal();
		}

		private void changeToNormal() {
			// Set cursor to default.
			Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
			frame.setCursor(cursor);
			toggleDragArea(!isDragOn);

		}

		public synchronized void drop(DropTargetDropEvent evt) {
			try {
				this.changeToNormal();
				evt.acceptDrop(DnDConstants.ACTION_COPY);
				@SuppressWarnings("unchecked")
				List<File> droppedFiles = (List<File>) evt.getTransferable()
						.getTransferData(DataFlavor.javaFileListFlavor);

				for (File file : droppedFiles) {
					/*
					 * NOTE: When I change this to a println, it prints the
					 * correct path
					 */

					ConfigManager.instance.set(ConfigManager.PATH,
							file.getAbsolutePath());
					break;
				}
				// btnArrange.setFont(btnArrange.getFont().deriveFont(Font.BOLD));
				evt.dropComplete(true);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AndroidResources window = new AndroidResources();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AndroidResources() {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		mouseDownCompCoords = null;
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.setShape(new RoundRectangle2D.Double(10, 10, 800, 600, 50, 50));
		frame.setSize(886, 631);
		frame.setBounds(
				Integer.parseInt(ConfigManager.instance.get(ConfigManager.X)),
				Integer.parseInt(ConfigManager.instance.get(ConfigManager.Y)),
				886, 631);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		panel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String path = utils.chooseDialog(false, frame,
						utils.getDesktopPath(), "");
				if (path != null && !path.equals("")) {
					ConfigManager.instance.set(ConfigManager.PATH, path);
				}

			}
		});

		panel.setDropTarget(dropTarget);
		panel.setBounds(264, 101, 318, 294);

		frame.getContentPane().add(panel);

		BufferedImage buffImgClose = ImageIO.read(getClass().getResource(
				"Close@2x.png"));
		Image imgClose = buffImgClose.getScaledInstance(60, 60,
				Image.SCALE_SMOOTH);
		ImageIcon imgConClose = new ImageIcon(imgClose);

		ImageButton btnClose = new ImageButton(imgConClose);
		btnClose.setBounds(29, 24, 60, 60);
		btnClose.setBorderPainted(false);
		try {
			Image img = ImageIO.read(getClass().getResource("Close@2x.png"));
			btnClose.setIcon(new ImageIcon(img));
		} catch (IOException ex) {
		}
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		frame.getContentPane().add(btnClose);

		JLabel lblStatus = new JLabel("Android Resources");
		lblStatus.setForeground(new Color(128, 128, 128));
		lblStatus.setFont(new Font("Helvetica Neue", Font.PLAIN, 24));
		lblStatus.setBounds(535, 24, 238, 25);
		frame.getContentPane().add(lblStatus);

		pnlStatus.setBackground(new Color(204, 204, 204));
		pnlStatus.setBounds(0, 479, 886, 152);
		frame.getContentPane().add(pnlStatus);
		pnlStatus.setLayout(null);

		BufferedImage img = ImageIO.read(getClass().getResource("button.png"));
		img = resize(img, 269, 61);
		ImageIcon imgCon2 = new ImageIcon(img);

		btnArrange = new ImageButton(imgCon2);
		btnArrange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				movieFiles(ConfigManager.instance.get(ConfigManager.PATH),
						ConfigManager.instance.get(ConfigManager.PATH), "png");
			}
		});
		btnArrange.setBorderPainted(false);
		btnArrange.setBounds(290, 406, 269, 61);
		frame.getContentPane().add(btnArrange);

		JLabel lblByRonDesta = new JLabel("by Ron Desta");
		lblByRonDesta.setForeground(new Color(169, 169, 169));
		lblByRonDesta.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		lblByRonDesta.setBounds(654, 46, 110, 25);
		frame.getContentPane().add(lblByRonDesta);

		JLabel lblV = new JLabel("v" + version());
		lblV.setForeground(new Color(128, 128, 128));
		lblV.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
		lblV.setBounds(744, 32, 39, 25);
		frame.getContentPane().add(lblV);
		allowMove();
	}

	public static BufferedImage resize(BufferedImage image, int width,
			int height) {
		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TRANSLUCENT);
		Graphics2D g2d = (Graphics2D) bi.createGraphics();
		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY));
		g2d.drawImage(image, 0, 0, width, height, null);
		g2d.dispose();
		return bi;
	}

	private void allowMove() {
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				ConfigManager.instance.set(ConfigManager.X, frame.getX() + "");
				ConfigManager.instance.set(ConfigManager.Y, frame.getY() + "");
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}

		});

		frame.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {
				mouseDownCompCoords = null;
			}

			public void mousePressed(MouseEvent e) {
				mouseDownCompCoords = e.getPoint();
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});

		frame.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
			}

			public void mouseDragged(MouseEvent e) {
				Point currCoords = e.getLocationOnScreen();
				try {
					frame.setLocation(currCoords.x - mouseDownCompCoords.x,
							currCoords.y - mouseDownCompCoords.y);
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				ConfigManager.instance.set(ConfigManager.X, frame.getX() + "");
				ConfigManager.instance.set(ConfigManager.Y, frame.getY() + "");
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}

		});
	}

	private String version() {
		return "0.9.5";
	}

	protected void movieFiles(String path, String dest, String picType) {
		try {
			int xStart = 120;
			int y = 52;
			int width = 82;
			int height = 30;
			int widthlbl = 20;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			BufferedImage img1;
			img1 = ImageIO.read(getClass().getResource("files.png"));

			pnlStatus.removeAll();
			pnlStatus.repaint();
			lblStatusCounter.setForeground(new Color(128, 128, 128));
			lblStatusCounter
					.setFont(new Font("Helvetica Neue", Font.PLAIN, 24));
			lblStatusCounter.setBounds(601, 40, 203, 53);

			JLabel lblStatus = new JLabel("Status");
			lblStatus.setForeground(Color.GRAY);
			lblStatus.setFont(new Font("Helvetica Neue", Font.PLAIN, 24));
			lblStatus.setBounds(33, 40, 82, 53);
			pnlStatus.add(lblStatus);

			Image dimg = img1.getScaledInstance(82, 27, Image.SCALE_SMOOTH);
			ImageIcon imageIcon = new ImageIcon(dimg);

			BufferedImage img2 = ImageIO.read(getClass().getResource(
					"check.png"));
			Image dimg2 = img2.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			ImageIcon imgCon = new ImageIcon(dimg2);

			Map<String, Boolean> doneQualities = new HashMap<String, Boolean>();
			int count = 0;
			int fCount = 1;

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String fileName = listOfFiles[i].getName();
					if (!fileName.contains(picType) || !fileName.contains("_")) {
						continue;
					}
					System.out.println("File " + fileName);
					int start = fileName.lastIndexOf("_") + 1;
					int end = fileName.indexOf("." + picType);
					String quality = fileName.substring(start, end);
					if (quality.equals("") || quality.contains(" ")) {
						continue;
					}

					boolean addLabal = false;
					try {
						addLabal = !doneQualities.get(quality);
					} catch (Exception e) {
						addLabal = true;
					}
					if (addLabal) {
						ImageLabel lblQuality = new ImageLabel(imageIcon);
						lblQuality.setText(quality.toUpperCase());
						lblQuality.setBounds(xStart + count
								* (width + widthlbl) + 15, y, width, height);
						pnlStatus.add(lblQuality);

						JLabel lblNewLabel_2 = new ImageLabel(imgCon);
						lblNewLabel_2.setBounds(xStart + (count + 1) * width
								+ count * widthlbl + 15, y, widthlbl, widthlbl);
						pnlStatus.add(lblNewLabel_2);
						System.out.println("added " + quality);
						pnlStatus.repaint();
						doneQualities.put(quality, true);
						count++;
					}
					String sep = File.separator;
					String outPath = dest + sep + quality;
					File newDir = new File(outPath);
					if (!newDir.exists()) {
						newDir.mkdir();
					}

					String sourcPath = path + sep + fileName;
					File sourceFile = new File(sourcPath);

					String newFileName = fileName.replace("_" + quality, "");
					String outFilePath = outPath + sep + newFileName;

					File destFile = new File(outFilePath);
					System.out.println("Moving " + sourceFile + " to "
							+ outFilePath);
					utils.copyFile(sourceFile, destFile);
					if (fCount == 1) {
						lblStatusCounter.setText(fCount + " File Updated");
					} else {
						lblStatusCounter.setText(fCount + " Files Updated");
					}
					pnlStatus.add(lblStatusCounter);

					fCount++;
					sourceFile.delete();
				} else if (listOfFiles[i].isDirectory()) {
					System.out.println("Directory " + listOfFiles[i].getName());
				}
			}
//			btnArrange.setFont(btnArrange.getFont().deriveFont(Font.PLAIN));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void toggleDragArea(boolean isDragOn) {
		try {
			Image img;
			if (isDragOn) {
				img = ImageIO.read(getClass().getResource("drop_area_on.png"));

			} else {
				img = ImageIO.read(getClass().getResource("drop_area_off.png"));
			}
			panel.setPic(img);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}