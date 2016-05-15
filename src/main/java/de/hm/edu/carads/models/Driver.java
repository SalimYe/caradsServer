package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hm.edu.carads.models.util.MetaInformation;
import de.hm.edu.carads.models.util.Person;

/**
 * Diese Klasse repreasentiert einen Fahrer. Die Klasse Person wird erweitert.
 * @author BK
 *
 */
public class Driver extends Person {

	/**
	 * Geburtsdatum als String.
	 */
    private String birthdate;
    
    /**
     * Beruf.
     */
    private String occupation;
    
    /**
     * Ausstellungsdatum des Fuehrerscheins.
     */
    private String licenseDate;
    
    /**
     * Sammlung der Fahrzeuge des Fahrers.
     */
    private Collection<Car> cars;
    
    /**
     * Profilbild.
     */
    private Image profilePicture;

    /**
     * Konstruktor mit den wichtigsten Informationen.
     * @param email
     * @param firstName
     * @param lastName
     */
    public Driver(String email, String firstName, String lastName) {
        super(email, firstName, lastName);
        this.meta = new MetaInformation();
        this.cars = new ArrayList<>();
        this.profilePicture = null;
    }

    /**
     * Ausfuehrlicher Konstruktor.
     * @param birthdate
     * @param occupation
     * @param licenseDate
     * @param cars
     * @param profilePicture
     * @param email
     * @param firstName
     * @param lastName
     
    public Driver(String birthdate, String occupation, String licenseDate, Collection<Car> cars, Image profilePicture, String email, String firstName, String lastName) {
        super(email, firstName, lastName);
        this.birthdate = birthdate;
        this.occupation = occupation;
        this.licenseDate = licenseDate;
        this.cars = cars;
        this.profilePicture = profilePicture;
    }
	*/
    
    /**
     * Ein spezielles Fahrzeug des Fahreres wird zurueck gegeben. 
     * @param carId
     * @return Fahrzeug
     */
    public Car getCar(String carId) {
        Iterator<Car> it = getCars().iterator();
        while (it.hasNext()) {
            Car tmp = it.next();
            if (tmp.getId().equals(carId)) {
                return tmp;
            }
        }
        return null;
    }

    /**
     * Ein spezielles Fahrzeug wird entfernt.
     * @param carId
     * @return true if removed successfully
     */
    public boolean removeCar(String carId) {
    	if(cars==null)
    		this.cars= new ArrayList<Car>();
        return cars.remove(getCar(carId));
    }

    /**
     * Alles Fahrzeuge werden zurueck gegeben.
     * @return
     */
    public Collection<Car> getCars() {
		return enrichCars();
    }
    
    /**
     * Diese Methode wird vor der Rueckgabe an den Benutzer aufgerufen.
     * Die Informationen (ID und PLZ) des Fahreres werden innerhalb der Fahrzeugobjekte gespeichert.
     * @return
     */
    private Collection<Car> enrichCars(){
    	Collection<Car> enrichedCars = new ArrayList<Car>();
    	if(cars==null)
    		return enrichedCars;
    	Iterator<Car> it = cars.iterator();
        while (it.hasNext()) {
            Car tmp = it.next();
            tmp.setDriverInformation(this.getId(), zip);
            enrichedCars.add(tmp);
        }
    	return enrichedCars;
    }

    /**
     * Ein Fahrzeug wird zu der Sammlung hinzugefuegt.
     * @param car
     */
    public void addCar(Car car) {
    	if(car.getId()==null || car.getId().isEmpty())
    		throw new IllegalArgumentException();
    	if(cars==null)
    		this.cars= new ArrayList<Car>();
        this.cars.add(car);
    }
    
    public Image getImage() {
        return profilePicture;
    }

    public void setImage(Image image) {
        this.profilePicture = image;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }
    
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    
    /**
     * Die Sammlung der Autos wird auf null gesetzt um weniger Informationen
     * an den Benutzer auszugeben.
     */
    public void deleteSomeInformation(){
    	cars = null;
    }
}
