package de.hm.edu.carads.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.Model;

public abstract class AbstractEntityControllerImpl<E extends Model> implements AbstractEntityController<E>{
	
	protected DatabaseController dbController;
	protected Gson gson;
	private Class<E> modelClass;
	
	
	public AbstractEntityControllerImpl(Class<E> model, DatabaseController database){
		this.dbController = database;
		this.modelClass = model;
		this.modelClass = model;
		gson = new Gson();
	}
	
	@Override
	public Collection<E> getAllEntities(){
		List<DBObject> entities = dbController.getAllEntities(modelClass);
		List<E> smallList = new ArrayList<E>();
		
		Iterator<DBObject> it = entities.iterator();
		
		while(it.hasNext()){
			smallList.add(makeEntityFromBasicDBObject((BasicDBObject)it.next()));
		}
		
		return smallList;
	}
	
	@Override
	public E getEntity(String id) throws NoContentException{
		BasicDBObject dbObj = dbController.getEntity(modelClass, id);
		if(dbObj == null)
			throw new NoContentException("Entity not found");
		return makeEntityFromBasicDBObject(dbObj);
	}
	
	@Override
	public void deleteEntity(String id) throws Exception{
		//E entity = getEntity(id);
		
		dbController.deleteEntity(modelClass, id);
		
		if(dbController.existEntityByKeyValue(modelClass, "_id", id))
			throw new Exception("entity not deleted");
	}
	
	@Override
	public long getEntityCount() {
		return dbController.getCollectionCount(modelClass);
	}
	
	@Override
	public abstract E changeEntity(String id, E updatedEntity) throws Exception;
	
	@Override
	public abstract E addEntity(E entity) throws Exception;
	
	
	protected E makeEntityFromBasicDBObject(BasicDBObject dbObj){
		E model = (E) gson.fromJson(dbObj.toJson(), modelClass);
		model.setId(dbObj.getString("_id"));
		return model;
	}
	
	

}
