package org.testunited.api;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyReader {
	static Properties prop = new Properties();
	static final String FILE_PREFIX = "application";
	static final String FILE_SUFFIX = ".properties";
	static final String PROFILE_KEY = "testunited.profiles.active";
	static String activeProfile;
	private final static Logger logger = LoggerFactory.getLogger(PropertyReader.class);

	static private void loadFile(String profile) throws Exception {

		String profileQualifier;

		if (profile == null || profile == "") {
			logger.info("Loading environment neutral property file");
			profileQualifier = "";
		} else {
			logger.info("Loading property file for environment: " + profile);
			profileQualifier = "." + profile;
		}

		String propFileName = FILE_PREFIX + profileQualifier + FILE_SUFFIX;

		var inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
			inputStream.close();
		}

		if (logger.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("\n-----------PROPERTIES------------\n");
			for (var p : prop.keySet())
				builder.append(String.format("\t%s:%s\n", p, prop.get(p)));
			builder.append("\n---------------------------------\n");
			logger.debug(builder.toString());
		}
	}

	static {

		activeProfile = System.getProperty(PROFILE_KEY);

		try {
			loadFile(null);
		} catch (Exception e) {
			logger.info("Default property file not found.");

			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
		}

		if (activeProfile != null) {
			try {
				loadFile(activeProfile);
			} catch (Exception e) {
				logger.info("Environment specific property file [{}] not found.", activeProfile);

				if (logger.isDebugEnabled()) {
					e.printStackTrace();
				}
			}
		}
		prop.putAll(System.getenv());

		if (logger.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("\n-----------PROPERTIES WITH ENV------------\n");
			for (var p : prop.keySet())
				builder.append(String.format("\t%s:%s\n", p, prop.get(p)));
			builder.append("\n---------------------------------\n");
			logger.debug(builder.toString());
		}
	}

	public static String getPropValue(String key) {
		return prop.getProperty(key);
	}
}
