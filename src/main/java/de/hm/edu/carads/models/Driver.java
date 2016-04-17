package de.hm.edu.carads.models;


public class Driver extends Person{

	private String birthdate;
	private String occupation;
	private String licenseDate;
	private Car car;
	private Image profilePicture;
	
	public Driver(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
		this.meta = new MetaInformation();
		this.car = null;
		this.profilePicture = null;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
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

