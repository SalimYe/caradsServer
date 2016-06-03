package de.hm.edu.carads.transaction;

import de.hm.edu.carads.models.Advertiser;

/**
 *
 * @author florian, BK
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
