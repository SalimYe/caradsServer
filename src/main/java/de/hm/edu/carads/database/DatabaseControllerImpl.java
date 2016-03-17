package de.hm.edu.carads.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import com.mongodb.bulk.WriteRequest;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import de.hm.edu.carads.models.Driver;


public class DatabaseControllerImpl implements DatabaseController{
	private MongoClient mongoClient;
	private DB db;
	private static final String COLLECTION_DRIVER = "driver";
	
	public DatabaseControllerImpl(String host, int port, String dbName){
		mongoClient = new MongoClient(host, port);
		db = mongoClient.getDB(dbName);
		mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
		System.out.println("passt");
	}

	@Override
	public List<DBObject> getDrivers() {
		
		//DBCollection driverCollection = db.getCollection("driver");
		//DBObject driver = driverCollection.findOne();
		
		List<DBObject> all = new ArrayList<DBObject>();
		//all.add(driver);
		return all;
	}

	@Override
	public Driver getDriver(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WriteResult addEntity(Class collectionClass, BasicDBObject entity) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));		
		return collection.insert(entity);
	}

	@Override
	public Driver changeDriver(Driver driver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDriver(String id) {
		// TODO Auto-generated method stub
		
	}

	private String getCollectionName(Class c) {
		if(c == Driver.class)
			return COLLECTION_DRIVER;
		return null;
	}

	@Override
	public boolean existEntity(Class collectionClass, String email) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));	
		DBCursor cursor = collection.find(new BasicDBObject("email", email));
		if(cursor.hasNext()){
			System.out.println(cursor.next());
			return true;
		}
		return false;
	}

}
