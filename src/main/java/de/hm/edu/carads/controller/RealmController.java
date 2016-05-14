package de.hm.edu.carads.controller;

import de.hm.edu.carads.models.User;

/**
 *
 * @author florian
 */
public interface RealmController{
    
	public void addUser(User user);
    public User getRealmByUsername(String username);
    
}
