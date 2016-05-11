package de.hm.edu.carads.db;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
	private static Properties properties;
	private static PropertiesLoader pLoader = new PropertiesLoader();
	private PropertiesLoader(){
		properties = new Properties();
		BufferedInputStream stream;
		
		try{
			stream = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("config.properties"));
			properties.load(stream);
			stream.close();
			
			//Gibt es ein weiteres Configfile?
			properties = getConfigFile(properties);
			
		}catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static PropertiesLoader getInstance(){
		return pLoader;
	}
	
	private Properties getConfigFile(Properties properties){
		Properties tmp = properties;
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream(properties.getProperty("PATH_TO_SERVER_CONFIG")));
			properties.load(stream);
			stream.close();
			return properties;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tmp;
	}
	
	public static String getPropertyString(String id){
		return properties.getProperty(id);
	}
}
