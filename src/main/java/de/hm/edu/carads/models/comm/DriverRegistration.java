package de.hm.edu.carads.models.comm;

import de.hm.edu.carads.models.Driver;

/**
 *
 * @author florian, BK
 */
public class DriverRegistration extends Driver{
	private String password;
    public DriverRegistration(String email, String firstName, String lastName, String password) {
		super(email, firstName, lastName);
		this.password = password;
	}

    public String getPassword(){
    	return password;
    }
	
}
