package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class Driver extends Person{

	private String birthdate;
	private String occupation;
	private String licenseDate;
	private Collection<Car> cars = new ArrayList<Car>();
	private Image profilePicture;

	
	public Driver(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
		this.meta = new MetaInformation();
		this.cars = new ArrayList<Car>();
		this.profilePicture = null;
	}
	
	public Car getCar(String carId){
		Iterator<Car> it = cars.iterator();
		while(it.hasNext()){
			Car tmp = it.next();
			if(tmp.getId().equals(carId))
				return tmp;
		}
		return null;
	}

	public Collection<Car> getCars(){
		return cars;
	}
	public void addCar(Car car) {
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

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
}

