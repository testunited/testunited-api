package org.testunited.api;

import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

public class TestBundlePropertyReader {
	Properties prop = new Properties();
	static final String FILE_PREFIX = "testbundle";
	static final String FILE_SUFFIX = ".properties";
	
	Logger logger = Logger.getLogger(getClass().getName());

	private void loadFile(Class<?> consumer, String env) throws Exception {
		
		String envQualifier;
		
		if(env == null || env == "") {
			System.out.printf("Loading default property file for \'%s\'\n", consumer.getName());
			envQualifier = "";
		}
		else {
			System.out.printf("Loading env specific [%s] property file for \'%s\'\n", env , consumer.getName());
			envQualifier = "." + env;
		}
		
		String propFileName = FILE_PREFIX + envQualifier + FILE_SUFFIX;

		URL file = new URL("jar:" + consumer.getProtectionDomain().getCodeSource().getLocation().toString() + "!/"
				+ propFileName);

		var inputStream = file.openStream();

		if (inputStream != null) {
			prop.load(inputStream);
			inputStream.close();
		}
		
		System.out.println("-----------PROPERTIES------------");
		for(var p:prop.keySet()) System.out.printf("\t%s:%s\n",p, prop.get(p));
		System.out.println("---------------------------------");
	}

	public TestBundlePropertyReader(Class<?> consumer) {
		
		try {
			this.loadFile(consumer, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(System.getProperty("env") != null) {
			try {
				this.loadFile(consumer, System.getProperty("env"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		prop.putAll(System.getenv());

		System.out.println("-----------PROPERTIES WITH ENV------------");
		for (var p : prop.keySet())
			System.out.printf("\t%s:%s\n", p, prop.get(p));
		System.out.println("---------------------------------");

	}

	public String getPropValue(String key) {
		return prop.getProperty(key);
	}
}
