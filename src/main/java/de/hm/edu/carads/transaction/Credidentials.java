package de.hm.edu.carads.transaction;

public class Credidentials {
	private String oldPassword;
	private String newPassword;
	
	public Credidentials(String oldPassword, String newPassword){
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
