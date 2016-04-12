package de.hm.edu.carads.db;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.NotFoundException;

public class PropertiesLoader {
	private Properties properties;
	public PropertiesLoader() throws Exception{
		properties = new Properties();
		BufferedInputStream stream;
		String dbConfigPath = getConfigFile();
		
		try{
			stream = new BufferedInputStream(new FileInputStream(dbConfigPath));
			properties.load(stream);
			stream.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String getConfigFile() throws Exception{
		Properties config = new Properties();
		BufferedInputStream stream;
		try {
			//Get file from resources folder
			stream = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("config.properties"));
			config.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			config = null;
		}
		if(config == null){
			throw new IllegalAccessException();
		}else if(config.getProperty("PATH_TO_SERVER_CONFIG").isEmpty()){
			throw new NotFoundException();
		}
		return config.getProperty("PATH_TO_SERVER_CONFIG");
	}
	
	public String getPropertyString(String id){
		return properties.getProperty(id);
	}
}
