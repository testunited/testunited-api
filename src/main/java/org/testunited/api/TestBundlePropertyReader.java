package org.testunited.api;

import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBundlePropertyReader {
	Properties prop = new Properties();
	static final String FILE_PREFIX = "testbundle";
	static final String FILE_SUFFIX = ".properties";
	static final String PROFILE_KEY = "testunited.profiles.active";
	String activeProfile;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private void loadFile(Class<?> consumer, String profile) throws Exception {
		
		String profileQualifier;
		
		if(profile == null || profile == "") {
			logger.info("Loading default property file for \'{}\'\n", consumer.getName());
			profileQualifier = "";
		}
		else {
			logger.info("Loading env specific [{}] property file for \'{}\'\n", profile , consumer.getName());
			profileQualifier = "." + profile;
		}
		
		String propFileName = FILE_PREFIX + profileQualifier + FILE_SUFFIX;

		URL file = new URL("jar:" + consumer.getProtectionDomain().getCodeSource().getLocation().toString() + "!/"
				+ propFileName);

		var inputStream = file.openStream();

		if (inputStream != null) {
			prop.load(inputStream);
			inputStream.close();
		}
		
		if(logger.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("\n-----------PROPERTIES------------\n");
			for (var p : prop.keySet())
				builder.append(String.format("\t%s:%s\n", p, prop.get(p)));
			builder.append("\n---------------------------------\n");
			logger.debug(builder.toString());
		}
	}

	public TestBundlePropertyReader(Class<?> consumer) {
		
		this.activeProfile = System.getProperty(PROFILE_KEY);
		
		try {
			this.loadFile(consumer, null);
		} catch (Exception e) {
			logger.info("Default property file not found for {}.", consumer.getName());
			
			if(logger.isDebugEnabled()) {
				e.printStackTrace();
			}
		}
		
		if(this.activeProfile != null) {
			try {
				this.loadFile(consumer, this.activeProfile);
			} catch (Exception e) {
				logger.info("Environment specific [{}] property file not found for {}.", consumer.getName());
				
				if(logger.isDebugEnabled()) {
					e.printStackTrace();
				}
			}
		}
		
		prop.putAll(System.getenv());

		if(logger.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("\n-----------PROPERTIES WITH ENV------------\n");
			for (var p : prop.keySet())
				builder.append(String.format("\t%s:%s\n", p, prop.get(p)));
			builder.append("\n---------------------------------\n");
			logger.debug(builder.toString());
		}

	}

	public String getPropValue(String key) {
		return prop.getProperty(key);
	}
}
