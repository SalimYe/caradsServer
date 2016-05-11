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
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.models.util.Model;

public abstract class AbstractEntityControllerImpl<E extends Model> implements AbstractEntityController<E>{
	
	protected DatabaseController dbController;
	protected Gson gson;
	private ModelCollection modelClass;
	
	
	public AbstractEntityControllerImpl(ModelCollection model, DatabaseController database){
		this.dbController = database;
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
    public E getEntityByMail(String mail) throws NoContentException {
    	BasicDBObject dbObj = dbController.getEntityByKeyValue(modelClass, "email", mail);
        if(dbObj == null)
        	throw new NoContentException("Entity not found");
		return makeEntityFromBasicDBObject(dbObj);
    }
	
	@Override
	public void deleteEntity(String id) throws Exception{
		//Innerhalb von deleteEntity wird gecheckt ob das Dokument existiert		
		dbController.deleteEntity(modelClass, id);
		
		if(dbController.existEntityByKeyValue(modelClass, "_id", id))
			throw new Exception("entity not deleted");
	}
	
	@Override
	public long getEntityCount() {
		return dbController.getCollectionCount(modelClass);
	}
	
	@Override
	public E changeEntity(String id, E entityData) throws Exception{

		if(!EntityValidator.isEntityValid((entityData)))
			throw new InvalidAttributesException();
		
		try {
			E oldEntity = getEntity(id);
			entityData.update(oldEntity.getMetaInformation());
			
			//Achtung: updateEntity gibt nicht das aktualisierte Object zurück.
			dbController.updateEntity(modelClass, id, BasicDBObject.parse(gson.toJson(entityData)));
			BasicDBObject dbObj = dbController.getEntity(modelClass, id);
			return makeEntityFromBasicDBObject(dbObj);
		} catch (NoContentException e) {
			throw new NoContentException("Entity not found");
		}
	}
	
	@Override
	public E addEntity(E entity) throws Exception{
		if(!EntityValidator.isEntityValid(entity))
			throw new InvalidAttributesException("Entity is not valid");
		
		BasicDBObject dbObj = dbController.addEntity(modelClass, BasicDBObject.parse(gson.toJson(entity)));

		return makeEntityFromBasicDBObject(dbObj);
	}
	
	
	protected E makeEntityFromBasicDBObject(BasicDBObject dbObj){
		if(dbObj == null)
			return null;
		E model = (E) gson.fromJson(dbObj.toJson(), getModelClass(modelClass));
		model.setId(dbObj.getString("_id"));
		return model;
	}
	
	private Class getModelClass(ModelCollection collection){
		if(collection.equals(ModelCollection.ADVERTISER))
			return Advertiser.class;
		if(collection.equals(ModelCollection.DRIVER))
			return Driver.class;
		if(collection.equals(ModelCollection.REALM))
			return User.class;
		
		return null;
	}
	
	

}
