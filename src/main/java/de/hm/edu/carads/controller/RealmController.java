package de.hm.edu.carads.controller;

import de.hm.edu.carads.models.User;

/**
 *
 * @author florian
 */
public interface RealmController extends AbstractEntityController<User>{
    
    public User getRealmByUsername(String username);
    
}
