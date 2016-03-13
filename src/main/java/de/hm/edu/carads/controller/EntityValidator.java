package de.hm.edu.carads.controller;

import de.hm.edu.carads.models.Driver;

public class EntityValidator {
	
	//TODO richtige Validierung
	public static boolean isNewDriverValid(Driver driver){
		if(driver.getEmail()!= null && !driver.getEmail().isEmpty())
			return true;
		
		return false;
	}
}
