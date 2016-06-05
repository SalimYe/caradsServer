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

/**
 * Diese Klasse repreasentiert die Steuerungsinstanz fuer die Entitaeten Werbender, Fahrer und Realm.
 * 
 * @author BK
 *
 * @param <E>
 */
public class AbstractEntityControllerImpl<E extends Person> implements AbstractEntityController<E>{
	
	/**
	 * Die Verbindung zur Datenbank.
	 */
	protected DatabaseController dbController;
	
	/**
	 * Der Konverter fuer JSON Objekte.
	 */
	protected Gson gson;
	
	/**
	 * Die tastaechliche Klasse der Entitaet.
	 */
	private ModelCollection modelClass;
	
	/**
	 * Logger.
	 */
	final static Logger logger = Logger.getLogger(AbstractEntityControllerImpl.class);
	
	/**
	 * Der Konstruktor empfaengt die Collection und die Datenbankschnittstelle.
	 * @param model
	 * @param database
	 */
	public AbstractEntityControllerImpl(ModelCollection model, DatabaseController database){
		this.dbController = database;
		this.modelClass = model;
		gson = new Gson();
		
	}
	
	/**
	 * Alle Entitaeten werden zurueck gegeben.
	 */
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
	
	/**
	 * Ein Objekt wird anhand der ID zurzeck gegeben.
	 */
	@Override
	public E getEntity(String id) throws NoContentException{
		BasicDBObject dbObj = dbController.getEntity(modelClass, id);
		if(dbObj == null){
			logger.error("Entity not found with id "+id);
			throw new NoContentException("Entity not found");
		}
		return makeEntityFromBasicDBObject(dbObj);
	}
	
	/**
	 * Ein Objekt wird anhand der E-Mail(eindeutig) zurueck gegbeen.
	 */
    @Override
    public E getEntityByMail(String mail) throws NoContentException {
    	BasicDBObject dbObj = dbController.getEntityByKeyValue(modelClass, "email", mail);
        if(dbObj == null){
        	logger.info("Entity not found with e-mail "+mail);
        	throw new NoContentException("Entity not found");
        }
        	
		return makeEntityFromBasicDBObject(dbObj);
    }
	
    /**
     * Eine Entitaet wird mittels der ID geloescht.
     */
	@Override
	public void deleteEntity(String id) throws NoContentException{
		//Innerhalb von deleteEntity wird gecheckt ob das Dokument existiert		
		dbController.deleteEntity(modelClass, id);
		logger.info("Entity "+id+" deleted");
	}
	
	/**
	 * Die Anhzahl aller enthaltenen Entitaeten wird zurueck gegeben.
	 */
	@Override
	public long getEntityCount() {
		return dbController.getCollectionCount(modelClass);
	}
	
	/**
	 * Eine Entitaet wird ueberschrieben.
	 * @param id der Entitaet
	 * @param entityData das neue Objekt.
	 */
	@Override
	public void changeEntity(String id, E entityData) throws Exception{

		if(!EntityValidator.isEntityValid((entityData))){
			logger.error("Can not save: Invalid Attributes for Entity: " + entityData.getId());
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
	
	/**
	 * Eine neue Entitaet wird hinzugefuegt.
	 * @param entity
	 */
	@Override
	public E addEntity(E entity) throws Exception{
		if(!EntityValidator.isEntityValid(entity))
			throw new InvalidAttributesException("Entity is not valid");
		
		try{
			getEntityByMail(entity.getEmail());
			logger.error("Mail (" + entity.getEmail() + ") already exists. Can not save this entity");
			throw new AlreadyExistsException();
		}catch(NoContentException e){
			logger.info("Email not registred yet. Saving Entity.");
		}
		entity.renewMetaInformation();
		BasicDBObject dbObj = dbController.addEntity(modelClass, (BasicDBObject) JSON.parse(gson.toJson(entity)));
		return makeEntityFromBasicDBObject(dbObj);
	}
	
	/**
	 * Aus dem Datenbankobjekt wird das tastaechliche Model generiert.
	 * @param dbObj
	 * @return
	 */
	protected E makeEntityFromBasicDBObject(BasicDBObject dbObj){
		if(dbObj == null)
			return null;
		
		E model = (E) gson.fromJson(JSON.serialize(dbObj), getModelClass(modelClass));
		model.setId(dbObj.getString("_id"));
		return model;
	}
	
	/**
	 * Diese Hilfsmethode gibt die tastaechlichen Klassen aus einer ModellCollection zurueck.
	 * @param collection
	 * @return Repreasentant der Collection
	 */
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
