package de.hm.edu.carads.db.util;

import java.io.IOException;
import java.util.EmptyStackException;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;
import de.hm.edu.carads.db.PropertiesLoader;


public class DatabaseFactory {
	public static String INST_TEST = "test";
	public static String INST_PROD = "live";
	
    private static DB testDB;
    private static DB prodDB;

    
    public static DB getInstanceDB(String instance){
    	if(instance.equalsIgnoreCase(INST_TEST)){
    		if(testDB == null){
    			testDB = createNewTestMongoDB();
    		}
    		return testDB;
    	}
    	else if(instance.equalsIgnoreCase(INST_PROD)){
    		if(prodDB == null){
    			try {
					prodDB = createNewProductiveMongoDB();
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
    		return prodDB;
    	}
    	
    	throw new EmptyStackException();
    }
    
    private static DB createNewProductiveMongoDB() throws Exception{
    	MongoClient mongoClient;
		PropertiesLoader pLoader;
		pLoader = PropertiesLoader.getInstance();
		mongoClient = new MongoClient(pLoader.getPropertyString("DB_HOST"), Integer.parseInt(pLoader.getPropertyString("DB_PORT")));
		mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		return mongoClient.getDB(pLoader.getPropertyString("DB_NAME"));
    }
    
    private static DB createNewTestMongoDB() {
        try {
            MongodForTestsFactory factory = new MongodForTestsFactory();
            Mongo mongo = factory.newMongo();
            return factory.newDB(mongo);
        }
        catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
    
    public static void dropTestDB(){
    	if(testDB!=null)
    		testDB.dropDatabase();
    }
}
