package de.hm.edu.carads.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.Model;

public abstract class EntityControllerImpl<E extends Model> {
	
	protected DatabaseController dbController;
	protected Gson gson;
	private Class<E> modelClass;
	
	public EntityControllerImpl(Class<E> model, DatabaseController database){
		this.dbController = database;
		this.modelClass = model;
		gson = new Gson();
	}
	
	public Collection<E> getAllEntities(){
		List<DBObject> entities = dbController.getAllEntities(modelClass);
		List<E> smallList = new ArrayList<E>();
		
		Iterator<DBObject> it = entities.iterator();
		
		while(it.hasNext()){
			smallList.add(makeEntityFromBasicDBObject((BasicDBObject)it.next()));
		}
		
		return smallList;
	}
	public E getEntity(String id) throws NoContentException{
		BasicDBObject dbObj = dbController.getEntity(modelClass, id);
		if(dbObj == null)
			throw new NoContentException("Driver not found");
		return makeEntityFromBasicDBObject(dbObj);
	}
	
	public abstract E addEntity(E entity) throws Exception;
	
	protected E makeEntityFromBasicDBObject(BasicDBObject dbObj){
		E model = (E) gson.fromJson(dbObj.toJson(), modelClass);
		model.setId(dbObj.getString("_id"));
		return model;
	}
	
	

}
