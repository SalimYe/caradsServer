package de.hm.edu.carads.database;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;


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

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.Image;
import de.hm.edu.carads.models.MetaInformation;


public class DatabaseControllerImpl implements DatabaseController{
	private MongoClient mongoClient;
	private DB db;
	private static final String COLLECTION_DRIVER = "driver";
	private static final String COLLECTION_CAR = "car";
	private static final String COLLECTION_CAMPAIGN = "campaign";
	private static final String COLLECTION_IMAGE = "image";
	
	public DatabaseControllerImpl(){
		PropertiesLoader pLoader = new PropertiesLoader();
		mongoClient = new MongoClient(pLoader.getPropertyString("DB_HOST"), Integer.parseInt(pLoader.getPropertyString("DB_PORT")));
		db = mongoClient.getDB(pLoader.getPropertyString("DB_NAME"));
		mongoClient.setWriteConcern(WriteConcern.ACKNOWLEDGED);
	}

	@Override
	public BasicDBObject addEntity(Class collectionClass, BasicDBObject entity) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));	
		collection.insert(entity);
		return entity;
	}

	private String getCollectionName(Class c) {
		if(c == Driver.class)
			return COLLECTION_DRIVER;
		else if(c == Car.class)
			return COLLECTION_CAR;
		else if(c == Image.class)
			return COLLECTION_IMAGE;
		return null;
	}

	@Override
	public boolean existEntityByKeyValue(Class collectionClass, String key, String value) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));	
		DBObject theOne = collection.findOne(new BasicDBObject(key, value));
		if(theOne!=null){
			System.out.println("Collection' " + getCollectionName(collectionClass) + "' enthaelt bereits: "+ theOne.toString());
			return true;
		}
		return false;
	}

	@Override
	public BasicDBObject getEntity(Class collectionClass, String id) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));	
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
		
		return (BasicDBObject) collection.findOne(query);
	}

	@Override
	public List<DBObject> getAllEntities(Class collectionClass) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));	
		List<DBObject> list = collection.find().toArray();
		DBCursor cursor = collection.find();
	
		return list;
	}

	@Override
	public BasicDBObject updateEntity(Class collectionClass, String id,
			BasicDBObject newEntity) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));	
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
		
		
		collection.findAndModify(query, newEntity);
		return null;
	}

	@Override
	public void deleteEntity(Class collectionClass, String id) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));
		
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(id));
		
		collection.remove(query);
	}

	@Override
	public long getCollectionCount(Class collectionClass) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));
		return collection.count();
	}
	

}
