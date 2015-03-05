package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the configuration. This class is a singleton.
 * 
 * @author rond
 *
 */
public class ConfigManager {
	private String configPath;
	private Map<String, String> configs;
	private Map<String, String> defualtConfigs;
	private Utils mUtils = new Utils();

	public static final ConfigManager instance = new ConfigManager();
	public static final String PATH = "path";
	public static final String X = "x";
	public static final String Y = "y";

	private ConfigManager() {
		String parenFolder = mUtils.getHomeDir()
				+ "/Library/Application Support/Android/Resources";
		new File(parenFolder).mkdirs();

		this.configPath = parenFolder + "and_res.cfg";
		this.configs = new HashMap<String, String>();

		this.defualtConfigs = new HashMap<String, String>();
		defualtConfigs.put(PATH, mUtils.getDesktopPath());
		defualtConfigs.put(X, "100");
		defualtConfigs.put(Y, "100");
		read();
	}

	/**
	 * 
	 * @return The path of the config file.
	 */
	public String getConfigPath() {
		return configPath;
	}

	/**
	 * 
	 * @param configPath
	 *            The path of the config file.
	 */
	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	/**
	 * @param key
	 *            The key of the configuration
	 * @return The key of the configuration
	 */
	public String get(String key) {
		return this.configs.get(key);
	}

	/**
	 * Adds new configuration to the file.
	 * 
	 * @param key
	 *            The key of the configuration
	 * @param value
	 *            The key of the configuration
	 */
	public void set(String key, String value) {
		this.configs.put(key, value);
		this.save();
	}

	/**
	 * Writes the configuration file.
	 */
	public void save() {
		File fout = new File(configPath);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for (Map.Entry<String, String> entry : configs.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				bw.write(key + "=" + value);
				bw.newLine();
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the configuration file.
	 */
	public void read() {
		List<String> lines = mUtils.readLines(this.configPath);
		if (lines.size() < 1) {
			this.configs.putAll(defualtConfigs);
			return;
		}
		for (String line : lines) {
			String key = line.split("=")[0];
			String value;
			try {
				value = line.split("=")[1];
			} catch (Exception e) {
				value = "";
			}
			this.configs.put(key, value);
		}
	}
}
