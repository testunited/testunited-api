package org.testunited.api;

import static org.hamcrest.CoreMatchers.startsWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
	static Properties prop = new Properties();
	static final String FILE_PREFIX = "application";
	static final String FILE_SUFFIX = ".properties";

	static private void loadFile(String env) throws Exception {

		String envQualifier;

		if (env == null || env == "") {
			System.out.println("Loading environment neutral property file");
			envQualifier = "";
		} else {
			System.out.println("Loading property file for environment: " + env);
			envQualifier = "." + env;
		}

		String propFileName = FILE_PREFIX + envQualifier + FILE_SUFFIX;

		var inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
			inputStream.close();
		}

		System.out.println("-----------PROPERTIES------------");
		for (var p : prop.keySet())
			System.out.printf("\t%s:%s\n", p, prop.get(p));
		System.out.println("---------------------------------");
	}

	static {
		try {
			loadFile(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (System.getProperty("env") != null)
			try {
				loadFile(System.getProperty("env"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		prop.putAll(System.getenv());

		System.out.println("-----------PROPERTIES WITH ENV------------");
		for (var p : prop.keySet())
			System.out.printf("\t%s:%s\n", p, prop.get(p));
		System.out.println("---------------------------------");

	}

	public static String getPropValue(String key) {
		return prop.getProperty(key);
	}
}
