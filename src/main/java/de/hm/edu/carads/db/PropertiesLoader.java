package de.hm.edu.carads.db;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
	private Properties properties;
	public PropertiesLoader(){
		properties = new Properties();
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(PropertiesLoader.class.getResourceAsStream("db.properties"));
			properties.load(stream);
			stream.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public String getPropertyString(String id){
		return properties.getProperty(id);
	}
}
