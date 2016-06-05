package de.hm.edu.carads.controller.util;

import java.util.regex.Pattern;

import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.models.util.Address;
import de.hm.edu.carads.models.util.DateController;
import de.hm.edu.carads.models.util.Model;

/**
 * Diese Klasse ueberprueft alle Entitaeten auf Gueltigkeit.
 * Dabei werden alle benoetigten Attribute gelesen und mit Mindestangaben verglichen.
 * @author BK
 *
 */
public class EntityValidator {
	
	/**
	 * Eine Objekt wird empfangen und auf Gueltigkeit geprueft. Dabei wird an dieser
	 * Stelle lediglich die Klasse des Objektes ueberprueft und an eine weitere
	 * Pruefmethode weitergeleitet.
	 * @param model 
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	public static boolean isEntityValid(Model model){
		if(model instanceof Driver)
			return isNewDriverValid((Driver)model);
		else if(model instanceof Advertiser)
			return isNewAdvertiserValid((Advertiser) model);
		else if(model instanceof Car)
			return isNewCarValid((Car) model);
		else if(model instanceof Campaign)
			return isCampaignValid((Campaign)model);
		else if(model instanceof User)
			return isRealmValid((User)model);
		return false;
	}
	
	/**
	 * Hilfsmethode welche ein String auf Inhalt ueberprueft.
	 * @param str
	 * @return
	 */
	private static boolean isNotEmpty(String str){
		if(str!=null && !str.isEmpty())
			return true;
		return false;
	}
	
	private static boolean isAddressValid(Address address){
		if(isNotEmpty(address.getCity()) &&
				isNotEmpty(address.getCountry()) &&
				isNotEmpty(address.getStreet()) &&
				isNotEmpty(address.getZip()))
			return true;
		return false;
	}
	
	/**
	 * Ein Werbender wird ueberprueft.
	 * @param advertiser
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isNewAdvertiserValid(Advertiser advertiser){
		if(isEmailValid(advertiser.getEmail()) &&
				isNotEmpty(advertiser.getCompany()) &&
				isNotEmpty(advertiser.getFirstName()) &&
				isNotEmpty(advertiser.getLastName()) &&
				isNotEmpty(advertiser.getPhone()) &&
				isAddressValid(advertiser.getAddress())
				)
			return true;
		return false;
	}
	
	//TODO richtige Validierung
	/**
	 * Ein Fahrer wird ueberprueft.
	 * @param driver
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isNewDriverValid(Driver driver){
		if(isEmailValid(driver.getEmail()) &&
				isNotEmpty(driver.getBirthdate()) &&
				isNotEmpty(driver.getLastName()) &&
				isNotEmpty(driver.getFirstName()) &&
				isNotEmpty(driver.getOccupation()) &&
				isNotEmpty(driver.getPhone()) &&
				isNotEmpty(driver.getDescription()) &&
				isNotEmpty(driver.getLicenseDate()))
			return true;

		return false;
	}	

	/**
	 * Ein Fahrzeug wird ueberprueft.
	 * @param car
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isNewCarValid(Car car){
		if(car.getBrand() == null || car.getBrand().isEmpty())
			return false;
		if(car.getModel() == null || car.getModel().isEmpty())
			return false;
		return true;
	}
	
	/**
	 * Eine Kampagne wird ueberpreugt.
	 * @param campaign
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isCampaignValid(Campaign campaign){
		if(campaign.getName() == null)
			return false;
		if(!campaign.getStartDate().isEmpty() && !campaign.getEndDate().isEmpty())
			if(!DateController.isABeforeB(campaign.getStartDate(), campaign.getEndDate()))
				return false;
		return true;
	}
	
	/**
	 * Mittels einem regex Muster wird eine E-Mail auf gueltigkeit ueberprueft.
	 * @param email
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isEmailValid(String email){
		if(email == null)
			return false;
		String EMAIL_PATTERN = 	"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		return pattern.matcher(email).matches();
	}

	/**
	 * Logindaten werden ueberprueft.
	 * @param realm
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
    private static boolean isRealmValid(User realm) {
        if(isEmailValid(realm.getUsername()))
			return true;
		return false;
    }
}
