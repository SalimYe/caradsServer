package de.hm.edu.carads.controller;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.models.User;

/**
 *
 * @author florian
 */
public class RealmControllerImpl extends AbstractEntityControllerImpl<User> implements RealmController {
    
    public RealmControllerImpl(DatabaseController database) {
        super(ModelCollection.REALM, database);
    }
    
    @Override
    public User getRealmByUsername(String username) {
        return this.makeEntityFromBasicDBObject(dbController
				.getEntityByKeyValue(ModelCollection.REALM, "username", username));
    } 
}
