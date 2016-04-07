package de.hm.edu.carads.models;

public class Driver extends Person{

	private String birthdate;
	private Car car;
	private Image image;
	
	public Driver(String id, String email, String firstName, String lastName, String age) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.birthdate = age;
		this.lastName = lastName;
		this.meta = new MetaInformation();
		this.car = null;
		this.image = null;
	}

	public String getId() {
		return id;
	}
	
	public boolean setId(String id){
		if(this.id == null || this.id.isEmpty()){
			this.id = id;
			return true;
		}
		else return false;
	}

	public String getEmail() {
		return email;
	}
	
	public MetaInformation getMetaInformation(){
		if(this.meta == null)
			this.meta = new MetaInformation();
		return this.meta;
	}
	
	public void removeIdForUpdate(){
		this.id = "";
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public MetaInformation getMeta() {
		return meta;
	}

	public void setMeta(MetaInformation meta) {
		this.meta = meta;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}

