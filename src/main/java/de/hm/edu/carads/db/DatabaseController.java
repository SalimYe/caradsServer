package de.hm.edu.carads.db;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public interface DatabaseController {
		
	public BasicDBObject getEntity(Class collectionClass, String id) throws NoContentException;
	public BasicDBObject addEntity(Class collectionClass, BasicDBObject entity);
	public BasicDBObject updateEntity(Class collectionClass, String id, BasicDBObject newEntity) throws NoContentException;
	public void deleteEntity(Class collectionClass, String id) throws NoContentException;
	
	public long getCollectionCount(Class collectionClass);
	
	public boolean existEntityByKeyValue(Class collectionClass, String key, String value);
	public BasicDBObject getEntityByKeyValue(Class collectionClass, String key, String value);
	public List<DBObject> getAllEntities(Class collectionClass);
	
	public String getNewId();
}
