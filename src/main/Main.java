package main;

import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utils.ConfigManager;
import utils.Utils;

public class Main {

	private JFrame frame;
	private JTextField txtPathSource;
	private JTextField txtPathOutput;
	private JTextArea textArea;
	private Utils utils = new Utils();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
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
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		DropTarget dropTarget = new DropTarget() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						/*
						 * NOTE: When I change this to a println, it prints the
						 * correct path
						 */
						txtPathOutput.setText(file.getAbsolutePath());
						txtPathSource.setText(file.getAbsolutePath());
						ConfigManager.instance.set(ConfigManager.PATH,
								file.getAbsolutePath());
						break;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		frame = new JFrame();
		frame.setTitle("DrPic - " + version());
		frame.setResizable(false);
		frame.setBounds(
				Integer.parseInt(ConfigManager.instance.get(ConfigManager.X)),
				Integer.parseInt(ConfigManager.instance.get(ConfigManager.Y)),
				698, 422);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setDropTarget(dropTarget);
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
		final JPanel panel = new JPanel();
		panel.setDropTarget(dropTarget);
		panel.setBorder(null);
		panel.setBounds(10, 6, 682, 102);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblFolder = new JLabel("Path:");
		lblFolder.setBounds(6, 12, 61, 16);
		panel.add(lblFolder);

		txtPathSource = new JTextField(
				ConfigManager.instance.get(ConfigManager.PATH));
		txtPathSource.setDropTarget(dropTarget);
		txtPathSource.setBounds(91, 6, 515, 28);
		panel.add(txtPathSource);
		txtPathSource.setEditable(false);
		txtPathSource.setColumns(10);

		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(606, 6, 70, 29);
		panel.add(btnBrowse);

		JLabel lblOutputPath = new JLabel("Output Path:");
		lblOutputPath.setBounds(6, 52, 103, 16);
		panel.add(lblOutputPath);

		txtPathOutput = new JTextField(
				ConfigManager.instance.get(ConfigManager.PATH));
		txtPathOutput.setBounds(91, 46, 515, 28);
		txtPathOutput.setDropTarget(dropTarget);
		panel.add(txtPathOutput);
		txtPathOutput.setEditable(false);
		txtPathOutput.setColumns(10);

		JButton button = new JButton("Browse");
		button.setBounds(606, 46, 70, 29);
		panel.add(button);

		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(6, 80, 61, 16);
		panel.add(lblType);
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(91, 76, 84, 27);
		panel.add(comboBox);
		comboBox.addItem("png");
		comboBox.addItem("jpeg");
		comboBox.addItem("jpg");
		comboBox.addItem("psd");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPathOutput.setText(utils.chooseDialog(false, frame,
						ConfigManager.instance.get(ConfigManager.PATH), ""));
			}
		});
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPathSource.setText(utils.chooseDialog(false, frame,
						ConfigManager.instance.get(ConfigManager.PATH), ""));
			}
		});

		JButton btnDoIt = new JButton("Do it!");
		btnDoIt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				movieFiles(txtPathSource.getText(), txtPathOutput.getText(),
						comboBox.getSelectedItem().toString());
			}
		});
		btnDoIt.setBounds(292, 110, 117, 36);
		frame.getContentPane().add(btnDoIt);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 148, 686, 245);
		frame.getContentPane().add(scrollPane);

		textArea = new JTextArea();
		textArea.setDropTarget(dropTarget);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
	}

	private String version() {
		return "0.8.5";
	}

	protected void movieFiles(String path, String dest, String picType) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getName();
				if (!fileName.contains(picType)) {
					continue;
				}
				System.out.println("File " + fileName);
				int start = fileName.lastIndexOf("_") + 1;
				int end = fileName.indexOf("." + picType);
				String quality = fileName.substring(start, end);

				String outPath = dest + "/" + quality;
				File newDir = new File(outPath);
				if (!newDir.exists()) {
					newDir.mkdir();
				}

				String sourcPath = path + "/" + fileName;
				File sourceFile = new File(sourcPath);

				String newFileName = fileName.replace("_" + quality, "");
				String outFilePath = outPath + "/" + newFileName;

				File destFile = new File(outFilePath);
				try {
					System.out.println("Moving " + sourceFile + " to "
							+ outFilePath);

					utils.copyFile(sourceFile, destFile);
					sourceFile.delete();
					textArea.setText(textArea.getText() + "Moved " + sourcPath
							+ " to " + outFilePath + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}
	}
}
