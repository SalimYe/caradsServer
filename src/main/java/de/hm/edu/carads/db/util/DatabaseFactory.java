package de.hm.edu.carads.db.util;

import java.io.IOException;
import java.util.EmptyStackException;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import de.flapdoodle.embed.mongo.tests.MongodForTestsFactory;

/**
 * Diese Klasse stellt zur Laufzeit eine Datenbank zur Verfuegung. 
 * @author BK
 *
 */
public class DatabaseFactory {
	/**
	 * Static String fuer die Testinstanz.
	 */
	public static String INST_TEST = "test";
	
	/**
	 * Static String fuer die Produktivinstanz.
	 */
	public static String INST_PROD = "live";
	
	/**
	 * Die Testdatenbank.
	 */
    private static DB testDB;
    
    /**
     * Die Produktivdatenbank.
     */
    private static DB prodDB;
    
    /**
     * Eine MongoDB wird zurueck gegeben. Ob Test oder Produktiv
     * haengt vom Parameter ab.
     * 
     * @param instance Name der angeforderten Instanz
     * @return MongoDB
     */
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
    
    /**
     * Eine neue MongoDB-Verbindung wird erstellt.
     * @return
     * @throws Exception
     */
    private static DB createNewProductiveMongoDB() throws Exception{
    	MongoClient mongoClient;
		PropertieController pLoader;
		pLoader = PropertieController.getInstance();
		String host = pLoader.getPropertyString("DB_HOST");
		int port = Integer.parseInt(pLoader.getPropertyString("DB_PORT"));
		
		mongoClient = new MongoClient(pLoader.getPropertyString("DB_HOST"), port);
		mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		return mongoClient.getDB(pLoader.getPropertyString("DB_NAME"));
    }
    
    /**
     * Eine neue Testverbindung zu einer lokalen Mongo-Instanz wird fuer
     * Testzwecke erstellt.
     * @return
     */
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
    
    /**
     * Die Datenbank wird geleert und geloescht.
     */
    public static void dropTestDB(){
    	if(testDB!=null)
    		testDB.dropDatabase();
    }
}
