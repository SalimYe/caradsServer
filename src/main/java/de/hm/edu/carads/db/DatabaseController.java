package de.hm.edu.carads.db;

import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
/**
 * Die Schnittstelle zur Datenbank.
 * @author BK
 *
 */
public interface DatabaseController {
		
	public BasicDBObject getEntity(ModelCollection collection, String id) throws NoContentException;
	public BasicDBObject addEntity(ModelCollection collection, BasicDBObject entity);
	public BasicDBObject addEntity(ModelCollection collection, BasicDBObject entity, String id);
	public void updateEntity(ModelCollection collection, String id, BasicDBObject newEntity) throws NoContentException;
	public void deleteEntity(ModelCollection collection, String id) throws NoContentException;
	public long getCollectionCount(ModelCollection collection);
	public boolean existEntityByKeyValue(ModelCollection collection, String key, String value);
	public BasicDBObject getEntityByKeyValue(ModelCollection collection, String key, String value);
	public List<DBObject> getAllEntities(ModelCollection collection);
	public String getNewId();
}
