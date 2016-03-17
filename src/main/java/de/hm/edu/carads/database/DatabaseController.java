package de.hm.edu.carads.database;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import de.hm.edu.carads.models.Driver;

public interface DatabaseController {
	public List<DBObject> getDrivers();
	public Driver getDriver(String id);
	
	public Driver changeDriver(Driver driver);
	public void deleteDriver(String id);
	
	public WriteResult addEntity(Class collectionClass, BasicDBObject entity);
	public boolean existEntity(Class collectionClass, String email);
}
