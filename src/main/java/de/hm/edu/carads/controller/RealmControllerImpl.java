package de.hm.edu.carads.controller;

import java.util.Collection;

import javax.ws.rs.core.NoContentException;

import com.google.gson.Gson;
import com.mongodb.util.JSON;
import com.mongodb.BasicDBObject;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.models.util.Person;

/**
 *
 * @author florian
 */
public class RealmControllerImpl implements RealmController {
	protected DatabaseController dbController;
	
    public RealmControllerImpl(DatabaseController database) {
        this.dbController = database;
    }
    
	@Override
	public User getRealmByUsername(String username) {
		BasicDBObject dbObj = dbController.getEntityByKeyValue(ModelCollection.REALM, "username", username);
		
		return makeUser(dbObj);
	}

	@Override
	public void addUser(User user) {
		Gson gson = new Gson();
		dbController.addEntity(ModelCollection.REALM, (BasicDBObject) JSON.parse(gson.toJson(user)));
	}
	
	private User makeUser(BasicDBObject dbObj){
		if(dbObj == null)
			return null;
		Gson gson = new Gson();

		User user = gson.fromJson(JSON.serialize(dbObj), User.class);
		user.setId(dbObj.getString("_id"));
		return user;		
	}
	
    
}

