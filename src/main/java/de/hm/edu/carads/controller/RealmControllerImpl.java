package de.hm.edu.carads.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import javax.ws.rs.core.NoContentException;

import com.google.gson.Gson;
import com.mongodb.util.JSON;
import com.mongodb.BasicDBObject;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.models.util.Hasher;
import de.hm.edu.carads.models.util.Person;
import de.hm.edu.carads.transaction.Credidentials;

/**
 *
 * @author florian
 */
public class RealmControllerImpl implements RealmController {
	protected DatabaseController dbController;
	private Gson gson = new Gson();
    public RealmControllerImpl(DatabaseController database) {
        this.dbController = database;
    }
    
	@Override
	public User getUser(String username) throws NoContentException {
		BasicDBObject dbObj = dbController.getEntityByKeyValue(ModelCollection.REALM, "username", username);
		
		return makeUser(dbObj);
	}

	@Override
	public void addUser(User user) throws Exception {
		//Passwort muss noch gehasht werden
		user.setPassword(Hasher.getShaHash(user.getPassword()));
		
		//Die Id wird hier heraus genommen um nur einmal in der DB zu erscheinen (unter _id)
		String userId = user.getId();
		user.clearBeforeSaving();
		
		dbController.addEntity(ModelCollection.REALM, (BasicDBObject) JSON.parse(gson.toJson(user)), userId);
	}
	
	private User makeUser(BasicDBObject dbObj) throws NoContentException{
		if(dbObj == null)
			throw new NoContentException("no entitiy to convert");
		Gson gson = new Gson();

		User user = (User) gson.fromJson(JSON.serialize(dbObj), User.class);
		user.setId(dbObj.getString("_id"));
		return user;		
	}

	@Override
	public void changeCredentials(String id, Credidentials credidentials) throws Exception{
		User user = makeUser(dbController.getEntity(ModelCollection.REALM, id));
		
		if(user.getPassword().equals(Hasher.getShaHash(credidentials.getOldPassword()))){
			user.setPassword(Hasher.getShaHash(credidentials.getNewPassword()));
			dbController.updateEntity(ModelCollection.REALM, id, (BasicDBObject) JSON.parse(gson.toJson(user)));
		}else{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void changeUsername(String id, String username) throws Exception {
		User user = getUserById(id);
		
		user.setUsername(username);
		dbController.updateEntity(ModelCollection.REALM, id, (BasicDBObject) JSON.parse(gson.toJson(user)));
	}

	@Override
	public User getUserById(String id) throws Exception {
		return makeUser(dbController.getEntity(ModelCollection.REALM, id));
	}

	@Override
	public void deleteUser(String id) throws Exception {
		dbController.deleteEntity(ModelCollection.REALM, id);
	}     
}

