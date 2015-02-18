package utils;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

public class Utils {
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

	/**
	 * Some general utilities.
	 */
	public Utils() {
	}

	/**
	 * Splits given string.
	 * 
	 * @param strToSplit
	 *            The string.
	 * @param sbstr
	 *            Specific substring to look for.
	 * @return List of strings.
	 */
	public ArrayList<String> splitString(String strToSplit, String sbstr) {
		String[] linesArray = strToSplit.split("\n");
		ArrayList<String> retVal = new ArrayList<String>();
		for (String string : linesArray) {
			if (sbstr != null) {
				if (string.contains(sbstr)) {
					retVal.add(string);
				}
			} else {
				retVal.add(string);
			}
		}
		return retVal;
	}

	/**
	 * Copies file
	 * 
	 * @param sourceFile
	 *            The source file.
	 * @param destFile
	 *            The destination.
	 * @throws IOException
	 *             when there is an error while copying file.
	 */
	public void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	/**
	 * Executes a command line
	 * 
	 * @param commnd
	 *            The command
	 * @return The output of the execution.
	 */
	public String execCmd(String[] commnd) {
		String result = Arrays.toString(commnd).replace(",", "").replace("[", "").replace("]", "");
		// System.out.println(result);
		try {

			String sendback = "";
			Process proc = Runtime.getRuntime().exec(commnd);
			InputStream istr = proc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(istr));
			String str;
			while ((str = br.readLine()) != null) {
				str = str.trim();
				if (str.length() > 0)
					sendback = sendback + "\n" + str.trim();
			}
			proc.waitFor();
			result += "\nCommand result: " + sendback;
			br.close();
		} catch (IOException er) {
			// TODO Auto-generated catch block
			er.printStackTrace();
		} catch (InterruptedException er) {
			// TODO Auto-generated catch block
			er.printStackTrace();
		} catch (Exception er) {
			// TODO Auto-generated catch block
			er.printStackTrace();
		}
		return result.replaceAll("(?m)^[ \t]*\r?\n", "");
	}

	/**
	 * Removes files and folders recursively.
	 * 
	 * @param prefix
	 *            The path.
	 */
	public void removeFiles(String prefix) {
		execCmd(new String[] { "sh", "-c", "rm -rf " + prefix });
	}

	/**
	 * Reads one line from a file.
	 * 
	 * @param path
	 *            The path of the file.
	 * @return The line.
	 */
	public String readLine(String path) {
		InputStream fis;
		BufferedReader br = null;
		String line = "";
		System.out.println(path);
		try {
			fis = new FileInputStream(path);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			line = br.readLine();
		} catch (FileNotFoundException e) {
			LOGGER.info(path + " was not found");
		} catch (IOException e) {
			LOGGER.info(path + " was not found");
		} finally {
			try {
				br.close();
				br = null;
				fis = null;
			} catch (IOException e) {
				LOGGER.info(path + " was not found");
			}

		}
		return line;
	}

	public List<String> readLines(String path) {
		List<String> results = new ArrayList<String>();
		File fileToRead = new File(path);
		if (fileToRead.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(fileToRead);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				while ((line = br.readLine()) != null) {
					results.add(line);
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
			}
		} else {
			LOGGER.info(path + " is missing.");
		}
		return results;
	}

	/**
	 * Executes command line.
	 * 
	 * @param command
	 *            The command.
	 * @param hiddenText
	 *            - In case of passwords.
	 * @return The output of the command.
	 */
	public String executeCommand(String command, String hiddenText) {
		if (hiddenText.equals("")) {
			System.out.println(command);
		} else {
			System.out.println(command.replace(hiddenText, "**********"));
		}

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command, null, new File("/tmp"));
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() > 0)
					output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString().replaceAll("(?m)^[ \t]*\r?\n", "");
	}

	/**
	 * Executes command line.
	 * 
	 * @param command
	 *            The command
	 * @return The output of the command.
	 */
	public String executeCommand(String command) {
		return executeCommand(command, "");
	}

	/**
	 * Executes command line.
	 * 
	 * @param cmdArgs
	 *            The command
	 * @return The output of the command.
	 */
	public String executeCommand(ArrayList<String> cmdArgs) {
		String[] stockArr = new String[cmdArgs.size()];
		stockArr = cmdArgs.toArray(stockArr);
		return execCmd(stockArr);
	}

	/**
	 * 
	 * @return The path to user's desktop.
	 */
	public String getDesktopPath() {
		return getHomeDir() + "/Desktop";
	}

	/**
	 * 
	 * @return The path to user's home directory.
	 */
	public String getHomeDir() {
		return FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
	}

	/**
	 * Displays file chooser dialog.
	 * 
	 * @param isFile
	 *            Is file or folder.
	 * @param frame
	 *            The fram to display on the dialog.
	 * @param defaultPath
	 *            Initial path.
	 * @param fileType
	 *            The type of the file
	 * @return The path to the chosn file or folder.
	 */
	public String chooseDialog(boolean isFile, JFrame frame, String defaultPath, String fileType) {
		// http://stackoverflow.com/a/16468926/839097
		String description = "Choose a file";
		if (isFile) {
			System.setProperty("apple.awt.fileDialogForDirectories", "false");
		} else {
			System.setProperty("apple.awt.fileDialogForDirectories", "true");
			description = "Choose a folder";
		}

		FileDialog fd = new FileDialog(frame, description, FileDialog.LOAD);
		if (isFile) {
			fileType = "*." + fileType;
			// System.out.println(fileType);
			fd.setFile(fileType);
		}
		fd.setDirectory(defaultPath);
		fd.setVisible(true);
		if (fd.getFile() == null)
			return "";

		String filename = fd.getDirectory() + fd.getFile();
		return filename;
	}

	/**
	 * Searches for a string and replace it.
	 * 
	 * @param path
	 *            The path of the file.
	 * @param string
	 *            The string to look for.
	 * @param newVal
	 *            The new value to replace.
	 */
	public void findReplace(String path, String string, String newVal) {
		System.out.println(path);
		BufferedReader brFile = null;
		FileOutputStream fosFile = null;
		try {
			brFile = new BufferedReader(new FileReader(path));

			// input the file content to the String "input"
			String line;
			String input = "";
			boolean isChanged = false;
			while ((line = brFile.readLine()) != null) {
				System.out.println(line);
				if (line.contains(string)) {
					line = newVal;
					System.out.println(line);
					isChanged = true;
				}
				input += line + '\n';
			}

			// write the new String with the replaced line OVER the same file
			if (isChanged)
				fosFile = new FileOutputStream(path);
			fosFile.write(input.getBytes());

		} catch (FileNotFoundException e) {
			System.out.println("Problem reading file.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				brFile.close();
				fosFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * Same as @see {@link #findReplace(String, String, String)} but the file is
	 * Waze's preferences file. For example: GPS.Show: yes --> GPS.Show: no
	 * 
	 * @param filePath
	 *            The path of the file.
	 * @param preference
	 *            The key to the preference.
	 * @param optionA
	 *            The first option.
	 * @param optionB
	 *            The second option
	 */
	public void togglePreference(String filePath, String preference, String optionA, String optionB) {
		System.out.println(preference);
		BufferedReader brFile = null;
		FileOutputStream fosFile = null;
		try {
			brFile = new BufferedReader(new FileReader(filePath));

			String line;
			String input = "";
			boolean isChanged = false;
			while ((line = brFile.readLine()) != null) {
				if (line.contains(preference)) {
					if (line.contains(optionA)) {
						line = line.replace(optionA, optionB);
						isChanged = true;
					} else if (line.contains(optionB)) {
						line = line.replace(optionB, optionA);
						isChanged = true;
					}
				}
				input += line + '\n';
			}

			// write the new String with the replaced line OVER the same file
			if (isChanged)
				fosFile = new FileOutputStream(filePath);
			fosFile.write(input.getBytes());

		} catch (FileNotFoundException e) {
			System.out.println("Problem reading file.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				brFile.close();
				fosFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * Set a specific value to a prefrence line.
	 * 
	 * @param filePath
	 *            The path of the file.
	 * @param preference
	 *            The key to the preference.
	 * @param option
	 *            The new value to set.
	 */
	public void togglePreference(String filePath, String preference, String option) {
		System.out.println(preference);
		BufferedReader brFile = null;
		FileOutputStream fosFile = null;
		try {
			brFile = new BufferedReader(new FileReader(filePath));

			String line;
			String input = "";
			String currenValue = "";
			while ((line = brFile.readLine()) != null) {
				if (line.contains(preference)) {
					currenValue = line.split(preference)[1];
					line = line.replace(currenValue, option);
				}
				input += line + '\n';
			}
			// write the new String with the replaced line OVER the same file
			fosFile = new FileOutputStream(filePath);
			fosFile.write(input.getBytes());

		} catch (FileNotFoundException e) {
			System.out.println("Problem reading file.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				brFile.close();
				fosFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void writeFile(String filePath, String linesToWrite) {
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(filePath);
			out = new BufferedWriter(fstream);
			out.write(linesToWrite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Search the newest file in a folder.
	 * 
	 * @param path
	 *            The path of the folder.
	 * @param fileType
	 *            The file type.
	 * @return The path to the newest file in the folder.
	 */
	public String getNewestFile(String path, String fileType) {
		String command = "ls -t ";

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command, null, new File(path));
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.endsWith("." + fileType)) {
					output.append(line);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		LOGGER.info(output.toString());
		return path + output.toString();
	}

	/**
	 * Create symbolic link
	 * 
	 * @param path
	 *            The original file.
	 * @param symlinkPath
	 *            The path to shortcut.
	 */
	public void createSymlink(String path, String symlinkPath) {
		String[] command = { "ln", "-s", path, symlinkPath };
		execCmd(command);
	}
}
