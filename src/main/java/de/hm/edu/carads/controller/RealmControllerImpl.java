package de.hm.edu.carads.controller;

import java.util.Collection;

import javax.ws.rs.core.NoContentException;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		
	}
	
    
}

