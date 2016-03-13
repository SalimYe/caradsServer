package de.hm.edu.carads.database;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DBTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PropertiesLoader pLoader = new PropertiesLoader();
		try {
			MongoClient mongoClient = new MongoClient( pLoader.getPropertyString("DB_HOST") , Integer.parseInt(pLoader.getPropertyString("DB_PORT")));
			DB db = mongoClient.getDB( pLoader.getPropertyString("DB_NAME") );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
