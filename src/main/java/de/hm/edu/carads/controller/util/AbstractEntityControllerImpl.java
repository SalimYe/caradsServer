package de.hm.edu.carads.controller.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.mongodb.DBObject;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.models.util.Person;

public class AbstractEntityControllerImpl<E extends Person> implements AbstractEntityController<E>{
	
	protected DatabaseController dbController;
	protected Gson gson;
	private ModelCollection modelClass;
	final static Logger logger = Logger.getLogger(AbstractEntityControllerImpl.class);
	
	
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
		if(dbObj == null){
			logger.error("Entity not found with id "+id);
			throw new NoContentException("Entity not found");
		}
		return makeEntityFromBasicDBObject(dbObj);
	}
	
    @Override
    public E getEntityByMail(String mail) throws NoContentException {
    	BasicDBObject dbObj = dbController.getEntityByKeyValue(modelClass, "email", mail);
        if(dbObj == null){
        	logger.info("Entity not found with e-mail "+mail);
        	throw new NoContentException("Entity not found");
        }
        	
		return makeEntityFromBasicDBObject(dbObj);
    }
	
	@Override
	public void deleteEntity(String id) throws NoContentException{
		//Innerhalb von deleteEntity wird gecheckt ob das Dokument existiert		
		dbController.deleteEntity(modelClass, id);
		logger.info("Entity "+id+" deleted");
	}
	
	@Override
	public long getEntityCount() {
		return dbController.getCollectionCount(modelClass);
	}
	
	@Override
	public void changeEntity(String id, E entityData) throws Exception{

		if(!EntityValidator.isEntityValid((entityData))){
			logger.error("Invalid Attributes for changed Entity");
			throw new InvalidAttributesException();
		}
		
		try{
			E person = getEntityByMail(entityData.getEmail());
			if(!person.getId().equals(id)){
				logger.error("Mail already exists. Can not save this entity");
				throw new AlreadyExistsException();
			}
		}catch(NoContentException e){
			logger.info("Email not registred yet. Saving changed Entity.");
		}
		
		E oldEntity = getEntity(id);
		entityData.update(oldEntity.getMetaInformation());
		
		//Achtung: updateEntity gibt nicht das aktualisierte Object zurück.
		dbController.updateEntity(modelClass, id, (BasicDBObject) JSON.parse(gson.toJson(entityData)));

	}
	
	@Override
	public E addEntity(E entity) throws Exception{
		if(!EntityValidator.isEntityValid(entity))
			throw new InvalidAttributesException("Entity is not valid");
		
		try{
			getEntityByMail(entity.getEmail());
			logger.error("Mail already exists. Can not save this entity");
			throw new AlreadyExistsException();
		}catch(NoContentException e){
			logger.info("Email not registred yet. Saving Entity.");
		}
		BasicDBObject dbObj = dbController.addEntity(modelClass, (BasicDBObject) JSON.parse(gson.toJson(entity)));
		return makeEntityFromBasicDBObject(dbObj);
	}
	
	
	protected E makeEntityFromBasicDBObject(BasicDBObject dbObj){
		if(dbObj == null)
			return null;
		
		E model = (E) gson.fromJson(JSON.serialize(dbObj), getModelClass(modelClass));
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
