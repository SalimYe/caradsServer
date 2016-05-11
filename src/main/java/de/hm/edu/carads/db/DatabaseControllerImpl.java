package de.hm.edu.carads.db;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import de.hm.edu.carads.db.util.DatabaseFactory;

public class DatabaseControllerImpl implements DatabaseController {
	private DB db;

	public DatabaseControllerImpl(String environment) {
		db = DatabaseFactory.getInstanceDB(environment);
	}

	@Override
	public BasicDBObject addEntity(ModelCollection collectionC, BasicDBObject entity) {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionC));
		collection.insert(entity);
		return entity;
	}

	private String getCollectionName(ModelCollection collection) {
		switch(collection){
			case ADVERTISER: 	return "advertiser";
			case DRIVER:		return "driver";
			case REALM:			return "realm";
		}
		return "";
	}

	@Override
	public boolean existEntityByKeyValue(ModelCollection collection, String key,
			String value) {
		BasicDBObject theOne = getEntityByKeyValue(collection, key, value);
		if (theOne != null) {
			System.out.println("Collection' "
					+ getCollectionName(collection)
					+ "' enthaelt bereits: " + theOne.toString());
			return true;
		}
		return false;
	}

	@Override
	public BasicDBObject getEntityByKeyValue(ModelCollection collectionC, String key,
			String value) {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionC));
		return (BasicDBObject) collection
				.findOne(new BasicDBObject(key, value));
	}

	@Override
	public BasicDBObject getEntity(ModelCollection collectionC, String id)
			throws NoContentException {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionC));
		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			throw new NoContentException("id not found");
		}

		return (BasicDBObject) collection.findOne(query);
	}

	@Override
	public List<DBObject> getAllEntities(ModelCollection collectionC) {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionC));
		List<DBObject> list = collection.find().toArray();

		return list;
	}

	@Override
	public BasicDBObject updateEntity(ModelCollection collectionC, String id,
			BasicDBObject newEntity) throws NoContentException {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionC));
		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			throw new NoContentException("id not found");
		}

		return (BasicDBObject) collection.findAndModify(query, newEntity);
	}

	@Override
	public void deleteEntity(ModelCollection collectionC, String id)
			throws NoContentException {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionC));

		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			throw new NoContentException("id not found");
		}

		collection.remove(query);
	}

	@Override
	public long getCollectionCount(ModelCollection collectionClass) {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionClass));
		return collection.count();
	}

	@Override
	public String getNewId() {
		return new ObjectId().toString();
	}

}
