package de.hm.edu.carads.db;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
	private Properties properties;
	public PropertiesLoader(){
		properties = new Properties();
		BufferedInputStream stream;
		try {
			//Get file from resources folder
			stream = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("config.properties"));
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(getPropertyString("DB_HOST") == null){
			if(getPropertyString("PATH_TO_SERVER_CONFIG") != null){
				try{
					stream = new BufferedInputStream(new FileInputStream(getPropertyString("PATH_TO_SERVER_CONFIG")));
					properties.load(stream);
					stream.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getPropertyString(String id){
		return properties.getProperty(id);
	}
}
