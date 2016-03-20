package de.hm.edu.carads.database;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import de.hm.edu.carads.models.Driver;

public interface DatabaseController {
		
	public BasicDBObject addEntity(Class collectionClass, BasicDBObject entity);
	public boolean existEntityByEmail(Class collectionClass, String email);
	public BasicDBObject getEntity(Class collectionClass, String id);
	
	public List<DBObject> getEntities(Class collectionClass);
}
