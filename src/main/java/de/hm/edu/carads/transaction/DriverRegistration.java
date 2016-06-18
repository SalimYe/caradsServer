package de.hm.edu.carads.transaction;

import de.hm.edu.carads.models.Driver;

/**
 * Die Repreasentation einer Fahreranmeldung, welche vom Fahrer entgegengenommen wird.
 * @author FS, BK
 *
 */
public class DriverRegistration extends Driver{
	
	/**
	 * Passwort im Klartext.
	 */
	private String password;
	public DriverRegistration(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
	}
	public String getPassword() {
		return password;
	}	
	
}
