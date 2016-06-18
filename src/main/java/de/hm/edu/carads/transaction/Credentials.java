package de.hm.edu.carads.transaction;

/**
 * 
 * 
 * Das alte und neue Passwort wird bei Passwortaenderung vom Client empfangen.
 * @author BK
 *
 */
public class Credentials {
	private String oldPassword;
	private String newPassword;
	
	public Credentials(String oldPassword, String newPassword){
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	
	
}
