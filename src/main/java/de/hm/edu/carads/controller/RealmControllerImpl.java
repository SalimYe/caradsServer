package de.hm.edu.carads.controller;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Realm;

/**
 *
 * @author florian
 */
public class RealmControllerImpl extends AbstractEntityControllerImpl<Realm> implements RealmController {
    
    public RealmControllerImpl(DatabaseController database) {
        super(Realm.class, database);
    }
    
}
