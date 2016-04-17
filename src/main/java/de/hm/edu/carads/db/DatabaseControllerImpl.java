package de.hm.edu.carads.db;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.hm.edu.carads.db.util.DatabaseFactory;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.Image;

public class DatabaseControllerImpl implements DatabaseController {
	private DB db;
	private static final String COLLECTION_DRIVER = "driver";
	private static final String COLLECTION_CAR = "car";
	private static final String COLLECTION_CAMPAIGN = "campaign";
	private static final String COLLECTION_IMAGE = "image";
	private static final String COLLECTION_ADVERTISER = "advertiser";

	public DatabaseControllerImpl(String environment) {
		db = DatabaseFactory.getInstanceDB(environment);
	}

	@Override
	public BasicDBObject addEntity(Class collectionClass, BasicDBObject entity) {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionClass));
		collection.insert(entity);
		return entity;
	}

	private String getCollectionName(Class c) {
		if (c == Driver.class)
			return COLLECTION_DRIVER;
		else if (c == Car.class)
			return COLLECTION_CAR;
		else if (c == Image.class)
			return COLLECTION_IMAGE;
		else if (c == Advertiser.class)
			return COLLECTION_ADVERTISER;
		else if (c == Campaign.class)
			return COLLECTION_CAMPAIGN;
		return "";
	}

	@Override
	public boolean existEntityByKeyValue(Class collectionClass, String key,
			String value) {
		BasicDBObject theOne = getEntityByKeyValue(collectionClass, key, value);
		if (theOne != null) {
			System.out.println("Collection' "
					+ getCollectionName(collectionClass)
					+ "' enthaelt bereits: " + theOne.toString());
			return true;
		}
		return false;
	}

	@Override
	public BasicDBObject getEntityByKeyValue(Class collectionClass, String key,
			String value) {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionClass));
		return (BasicDBObject) collection
				.findOne(new BasicDBObject(key, value));
	}

	@Override
	public BasicDBObject getEntity(Class collectionClass, String id)
			throws NoContentException {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionClass));
		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			throw new NoContentException("id not found");
		}

		return (BasicDBObject) collection.findOne(query);
	}

	@Override
	public List<DBObject> getAllEntities(Class collectionClass) {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionClass));
		List<DBObject> list = collection.find().toArray();
		// DBCursor cursor = collection.find();

		return list;
	}

	@Override
	public BasicDBObject updateEntity(Class collectionClass, String id,
			BasicDBObject newEntity) throws NoContentException {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionClass));
		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			throw new NoContentException("id not found");
		}
	
		return (BasicDBObject) collection.findAndModify(query, newEntity);
	}

	@Override
	public void deleteEntity(Class collectionClass, String id)
			throws NoContentException {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionClass));

		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			throw new NoContentException("id not found");
		}

		collection.remove(query);
	}

	@Override
	public long getCollectionCount(Class collectionClass) {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionClass));
		return collection.count();
	}

}
