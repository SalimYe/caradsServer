package de.hm.edu.carads.transaction;

import de.hm.edu.carads.models.Driver;

/**
 * 
 * @author FS, BK
 *
 */
public class DriverRegistration extends Driver{
	
	private String password;
	public DriverRegistration(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
	}
	public String getPassword() {
		return password;
	}	
	
}
