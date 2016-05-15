package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.hm.edu.carads.models.util.MetaInformation;
import de.hm.edu.carads.models.util.Person;

public class Driver extends Person {

    private String birthdate;
    private String occupation;
    private String licenseDate;
    private Collection<Car> cars;
    private Image profilePicture;

    public Driver(String email, String firstName, String lastName) {
        super(email, firstName, lastName);
        this.meta = new MetaInformation();
        this.cars = new ArrayList<>();
        this.profilePicture = null;
    }

    public Driver(String birthdate, String occupation, String licenseDate, Collection<Car> cars, Image profilePicture, String email, String firstName, String lastName) {
        super(email, firstName, lastName);
        this.birthdate = birthdate;
        this.occupation = occupation;
        this.licenseDate = licenseDate;
        this.cars = cars;
        this.profilePicture = profilePicture;
    }

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

    public boolean removeCar(String carId) {
        checkCars();
        return cars.remove(getCar(carId));
    }

    public Collection<Car> getCars() {
    	checkCars();
		return enrichCars();
    }
    
    private Collection<Car> enrichCars(){
    	Collection<Car> enrichedCars = new ArrayList<Car>();
    	Iterator<Car> it = cars.iterator();
        while (it.hasNext()) {
            Car tmp = it.next();
            tmp.setDriverInformation(this.getId(), zip);
            enrichedCars.add(tmp);
        }
    	return enrichedCars;
    }

    public void addCar(Car car) {
    	if(car.getId()==null || car.getId().isEmpty())
    		throw new IllegalArgumentException();
        checkCars();
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

    private void checkCars() {
        if (cars == null) {
            cars = new ArrayList<Car>();
        }
    }
    
    public void deleteSomeInformation(){
    	cars = null;
    }
}
