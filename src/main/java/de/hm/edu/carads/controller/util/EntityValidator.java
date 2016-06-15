package de.hm.edu.carads.controller.util;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.User;
import de.hm.edu.carads.models.util.Address;
import de.hm.edu.carads.models.util.Model;

/**
 * Diese Klasse ueberprueft alle Entitaeten auf Gueltigkeit.
 * Dabei werden alle benoetigten Attribute gelesen und mit Mindestangaben verglichen.
 * @author BK
 *
 */
public class EntityValidator {
	final static Logger logger = Logger.getLogger(EntityValidator.class);
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
	
	/**
	 * Eine Adresse wird auf Gueltigkeit geprueft.
	 * @param address
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isAddressValid(Address address){
		if(isNotEmpty(address.getCity()) &&
				isNotEmpty(address.getCountry()) &&
				isNotEmpty(address.getStreet()) &&
				isNotEmpty(address.getZip()))
			return true;
		logger.error("invalid Address");
		return false;
	}
	
	/**
	 * Ein Werbender wird ueberprueft.
	 * @param advertiser
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isNewAdvertiserValid(Advertiser advertiser){
		if(isEmailValid(advertiser.getEmail()) &&
				isNotEmpty(advertiser.getTitle()) &&
				isNotEmpty(advertiser.getCompany()) &&
				isNotEmpty(advertiser.getFirstName()) &&
				isNotEmpty(advertiser.getLastName()) &&
				isNotEmpty(advertiser.getPhone()) &&
				isAddressValid(advertiser.getAddress())
				)
			return true;
		logger.error("invalid Advertiser");
		return false;
	}

	/**
	 * Ein Fahrer wird ueberprueft.
	 * Notwendige Angaben:titel, Vorname, Nachname, Mail, Telefon, Strasse, PLZ, Stadt, Land, Geburtstag, Lizenztag
	 * @param driver
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isNewDriverValid(Driver driver){
		if(isEmailValid(driver.getEmail()) &&
				isAddressValid(driver.getAddress()) &&
				isNotEmpty(driver.getTitle()) &&
				isNotEmpty(driver.getPhone()) &&
				isNotEmpty(driver.getBirthdate()) &&
				isNotEmpty(driver.getLastName()) &&
				isNotEmpty(driver.getFirstName()) &&
				isNotEmpty(driver.getLicenseDate()))
			return true;
		logger.error("Invalid Driver");
		return false;
	}	

	/**
	 * Ein Fahrzeug wird ueberprueft.
	 * @param car
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isNewCarValid(Car car){
		if(isNotEmpty(car.getBrand()) &&
				isNotEmpty(car.getModel())&&
				isNotEmpty(car.getColor()))
			return true;
		logger.error("Invalid Car");
		return false;
	}
	
	/**
	 * Eine Kampagne wird ueberpreugt.
	 * @param campaign
	 * @return Wahrheitswert ueber die Gueltigkeit
	 */
	private static boolean isCampaignValid(Campaign campaign){
		if(isNotEmpty(campaign.getName()) &&
				isNotEmpty(campaign.getStartDate()) &&
				isNotEmpty(campaign.getEndDate()) &&
				isNotEmpty(campaign.getCarBudget()) &&
				isNotEmpty(campaign.getDescription()) &&
				!campaign.getImages().isEmpty())
			if(DateController.isABeforeB(campaign.getStartDate(), campaign.getEndDate()))
				return true;
		logger.error("Invalid Campaign");
		return false;
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
        logger.error("Invalid Realm");
		return false;
    }
}
