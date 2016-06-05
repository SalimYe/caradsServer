package de.hm.edu.carads.db.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Diese Klasse kontrolliert den Zugriff auf die Konfigurationsdateien.
 * Es handelt sich hierbei um ein Singleton-Pattern. Dadurch werden 
 * die Konfigurationsdateien nur einmal ausgelesen.
 * @author BK
 *
 */
public class PropertieController {
	/**
	 * Die Konfigurationseigenschaften.
	 */
	private static Properties properties;
	
	/**
	 * Die Instanz der Konfiguration.
	 */
	private static PropertieController pLoader = new PropertieController();
	
	/**
	 * Der private Konstruktor liest die Dateien ein.
	 */
	private PropertieController(){
		properties = new Properties();
		//Die Konfigurationsdatei innerhalb des ressource Ordners wird ausgelesen.
		properties.putAll(getProperties(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("config.properties"))));
		
		//Gibt es einen Pfad zu einer weiteren Konfiguration? Wenn ja, wird ausgelesen.
		String pathToOtherConfig = properties.getProperty("PATH_TO_SERVER_CONFIG");
		if(!pathToOtherConfig.isEmpty()){
			try {
				properties.putAll(getProperties(new BufferedInputStream(new FileInputStream(pathToOtherConfig))));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Eine Konfigurationsdatei wird eigelesen.
	 * @param inputStream
	 * @return Eigenschaften aus der Datei
	 */
	private Properties getProperties(BufferedInputStream inputStream){
		BufferedInputStream stream = inputStream;
		Properties props = new Properties();
		try{
			stream = new BufferedInputStream(inputStream);
			props.load(stream);
			stream.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}		
		return props;
	}
	
	/**
	 * Gibt die Instanz zurueck.
	 * @return singleton
	 */
	public static PropertieController getInstance(){
		return pLoader;
	}
	
	/**
	 * Die Eigenschaft wird aus den eingelesenen Konfigurationsdaten geladen.
	 * @param key
	 * @return Wert der Eigenschaft
	 */
	public static String getPropertyString(String key){
		return properties.getProperty(key);
	}
}
