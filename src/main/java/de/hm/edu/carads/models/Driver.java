package de.hm.edu.carads.models;

public class Driver extends Person{

	private String birthdate;
	private Car car;
	private Image image;
	
	public Driver(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
		this.meta = new MetaInformation();
		this.car = null;
		this.image = null;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	
}

