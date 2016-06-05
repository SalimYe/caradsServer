package de.hm.edu.carads.db;

import java.util.List;
import javax.ws.rs.core.NoContentException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import de.hm.edu.carads.db.util.DatabaseFactory;

/**
 * Die Verbindung zur Datenbank. Über diese Klasse können Entitäten aus 
 * der Datenbank gelesen, gespeichert, geändert und gelöscht werden.
 * 
 * @author BK
 *
 */
public class DatabaseControllerImpl implements DatabaseController {
	
	/**
	 * Das Datenbankobjekt.
	 */
	private DB db;
	
	/**
	 * Der Logger.
	 */
	final static Logger logger = Logger.getLogger(DatabaseControllerImpl.class);

	/**
	 * Der Konstruktor empfängt die Datenbankumgebung als String. Diser Parameter wird zur Unterscheidung benötigt
	 * weil alle Modul-Tests auf einer anderen DB laufen als sonst.
	 * @param environment
	 */
	public DatabaseControllerImpl(String environment) {
		db = DatabaseFactory.getInstanceDB(environment);
	}

	/**
	 * Eine neue Entität wird in die Datenbank geschrieben. Dabei wird anhand den Parametern
	 * entschieden in welche Collection geschrieben wird.
	 * @param collectionC Die Ziel-Collection
	 * @param entity Das Objekt welches hinzugefuegt werden soll.
	 * @return das Objekt wird wieder zurzueck gegeben.
	 */
	@Override
	public BasicDBObject addEntity(ModelCollection collectionC, BasicDBObject entity) {
		DBCollection collection = db.getCollection(getCollectionName(collectionC));
		collection.insert(entity);
		return entity;
	}

	/**
	 * Die Collectionenum wird in Strings uebersetzt. 
	 * @param collection
	 * @return Collectionname
	 */
	private String getCollectionName(ModelCollection collection) {
		switch(collection){
			case ADVERTISER: 	return "advertiser";
			case DRIVER:		return "driver";
			case REALM:			return "realm";
		}
		return "";
	}

	/**
	 * Diese Methode sucht in der angegebenen Collection nach dem key-value Paar und
	 * gibt den Wahrheitswert ueber die Exisitenz zurueck.
	 * @param collection Collection in welcher gesucht werden soll
	 * @param key Attributname
	 * @param value Wert nach dem gesucht werden soll.
	 * @return Wahrheitswert der Exisitenz.
	 */
	@Override
	public boolean existEntityByKeyValue(ModelCollection collection, String key,
			String value) {
		BasicDBObject theOne = getEntityByKeyValue(collection, key, value);
		if (theOne != null) {
			logger.info("Collection" + getCollectionName(collection) + " enthaelt "  + theOne.toString());
			return true;
		}
		return false;
	}

	/**
	 * Diese Methode sucht in der angegebenen Collection nach dem key-value Paar und
	 * gibt das Objekt zurueck.
	 * @param collection Collection in welcher gesucht werden soll
	 * @param key Attributname
	 * @param value Wert nach dem gesucht werden soll.
	 * @return Das gefundene Objekt
	 */
	@Override
	public BasicDBObject getEntityByKeyValue(ModelCollection collectionC, String key, String value) {
		DBCollection collection = db.getCollection(getCollectionName(collectionC));
		return (BasicDBObject) collection.findOne(new BasicDBObject(key, value));
	}

	/**
	 * Eine Entität wird aus der DB gelesen.
	 * @param Collection aus welcher ausgelesen werden soll
	 * @param id welche gesucht werden soll
	 * @return Objekt mit der id
	 * @throws NoContentException falls das Objekt nicht gefunden wurde
	 */
	@Override
	public BasicDBObject getEntity(ModelCollection collectionC, String id) throws NoContentException {
		DBCollection collection = db.getCollection(getCollectionName(collectionC));
		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			logger.error("id " + id + " not found");
			throw new NoContentException("id not found");
		}

		return (BasicDBObject) collection.findOne(query);
	}

	/**
	 * Eine Liste mit allen Objekten aus der angegebenen Collection wird zurueck gegeben.
	 * @param collectionC
	 * @return Liste aller Objekte
	 */
	@Override
	public List<DBObject> getAllEntities(ModelCollection collectionC) {
		DBCollection collection = db.getCollection(getCollectionName(collectionC));
		return collection.find().toArray();
	}

	/**
	 * Eine Enität wird ueberschrieben.
	 * @param collectionC Collection der Entität
	 * @param id der Entität
	 * @param newEntity neues Objekt, welches das alte ueberschreibt
	 * @throws NoContentException falls die id nicht gefunden wurde
	 */
	@Override
	public void updateEntity(ModelCollection collectionC, String id, BasicDBObject newEntity) throws NoContentException {
		DBCollection collection = db
				.getCollection(getCollectionName(collectionC));
		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			logger.error("id " + id + " not found");
			throw new NoContentException("id not found");
		}
		collection.findAndModify(query, newEntity);
	}

	/**
	 * Eine Entität wird aus der DB geloescht.
	 * @param collectionC Collection aus welcher geloescht werden soll
	 * @param id des Objektes
	 * @throws NoContentException falls die id nicht gefunden wird
	 */
	@Override
	public void deleteEntity(ModelCollection collectionC, String id) throws NoContentException {
		DBCollection collection = db.getCollection(getCollectionName(collectionC));

		BasicDBObject query = new BasicDBObject();
		try {
			query.put("_id", new ObjectId(id));
		} catch (Exception e) {
			logger.error("id " + id + " not found");
			throw new NoContentException("id not found");
		}

		collection.remove(query);
	}

	/**
	 * Die Anzahl der Objekte in dieser Collection wird zurueck gegeben.
	 * @param collectionClass welche gezaehlt werden soll
	 */
	@Override
	public long getCollectionCount(ModelCollection collectionClass) {
		DBCollection collection = db.getCollection(getCollectionName(collectionClass));
		return collection.count();
	}

	/**
	 * Eine neue ID wird generiert.
	 */
	@Override
	public String getNewId() {
		return new ObjectId().toString();
	}
	
}
