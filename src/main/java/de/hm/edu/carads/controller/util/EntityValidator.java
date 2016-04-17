package de.hm.edu.carads.controller.util;

import java.util.regex.Pattern;

import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.Model;

public class EntityValidator {
	
	public static boolean isEntityValid(Model model){
		if(model instanceof Driver){
			return isNewDriverValid((Driver)model);
		}
		else if(model instanceof Advertiser)
			return isNewAdvertiserValid((Advertiser) model);
		return false;
	}
	
	private static boolean isNewAdvertiserValid(Advertiser advertiser){
		if(isEmailValid(advertiser.getEmail()))
			return true;
		return false;
	}
	
	//TODO richtige Validierung
	/**
	 * Email muss da sein.
	 * @param driver
	 * @return
	 */
	private static boolean isNewDriverValid(Driver driver){
		if(isEmailValid(driver.getEmail()))
			if(driver.getId() == null)
				return true;

		return false;
	}
	
	/**
	 * Alle vorhanden Daten sollen geprüft werden. Wenn ein Attribut nicht da ist, macht es nichts.
	 * @param driver
	 * @return
	 */
	private static boolean isDriverValid(Driver driver){
		return true;
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
		if(email == null)
			return false;
		String EMAIL_PATTERN = 	"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		return pattern.matcher(email).matches();
	}
}
