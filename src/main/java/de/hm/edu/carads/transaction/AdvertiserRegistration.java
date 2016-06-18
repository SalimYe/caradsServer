package de.hm.edu.carads.transaction;

import de.hm.edu.carads.models.Advertiser;

/**
 * Der Werbende registriert sich und sendet diese Klasse im JSON-Format an den Server.
 * @author FS, BK
 */
public class AdvertiserRegistration extends Advertiser{

    private String password;

    public AdvertiserRegistration(String email, String firstName, String lastName, String password) {
        super(email, firstName, lastName);
        this.password = password;
    }

    public String getPassword(){
    	return password;
    }
    
}
