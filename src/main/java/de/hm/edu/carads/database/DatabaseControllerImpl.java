package de.hm.edu.carads.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import de.hm.edu.carads.models.Driver;

public class DatabaseControllerImpl implements DatabaseController{
	private MongoClient mongoClient;
	private DB db;
	
	public DatabaseControllerImpl(String host, int port, String dbName){
		try {
			mongoClient = new MongoClient(host, port);
			db = mongoClient.getDB(dbName);
			mongoClient.setWriteConcern(WriteConcern.JOURNALED);
		} catch (UnknownHostException e) {
			System.out.println("Hoppls");
			e.printStackTrace();
		}
	}

	@Override
	public List<DBObject> getDrivers() {
		DBCollection driverCollection = db.getCollection("driver");
		DBObject driver = driverCollection.findOne();
		
		List<DBObject> all = new ArrayList<DBObject>();
		all.add(driver);
		return all;
	}

	@Override
	public Driver getDriver(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Driver addDriver(BasicDBObject driver) {
		DBCollection driverCollection = db.getCollection("driver");
		driverCollection.save(driver);
		return null;
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

}
