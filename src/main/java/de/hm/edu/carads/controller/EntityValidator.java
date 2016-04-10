package de.hm.edu.carads.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

public class EntityValidator {
	
	//TODO richtige Validierung
	public static boolean isNewDriverValid(Driver driver){
		if(isEmailValid(driver.getEmail()))
			if(driver.getId() == null)
				return true;

		return false;
	}
	
	//TODO
	public static boolean isNewCarValid(Car car){
		if(car.getBrand() == null || car.getBrand().isEmpty())
			return false;
		if(car.getModel() == null || car.getModel().isEmpty())
			return false;
		return true;
	}
	
	private static boolean isEmailValid(String email){
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		
	
		

		return pattern.matcher(email).matches();
	}
}
