package de.hm.edu.carads.models.util;



public abstract class Person extends Model{

	/**
	 * Titel der Person
	 */
	protected String title;
	protected String firstname;
	protected String lastname;
	protected String email;
	protected String phone;
	protected Address address;
	protected String description;
	
	public Person(String email, String firstName, String lastName){
		super();
		this.email = email;
		this.firstname = firstName;
		this.lastname = lastName;
		this.address = new Address();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}

	public String getLastName() {
		return lastname;
	}

	public void setLastName(String lastName) {
		this.lastname = lastName;
	}

	public MetaInformation getMetaInformation() {
		if(this.meta == null)
			this.meta = new MetaInformation();
		return this.meta;
	}

	public String getCity() {
		checkAdress();
		return address.getCity();
	}

	public void setCity(String city) {
		checkAdress();
		this.address.setCity(city);
	}

	public String getZip() {
		checkAdress();
		return address.getZip();
	}

	public void setZip(String zip) {
		checkAdress();
		this.address.setZip(zip);
	}

	public String getCountry() {
		checkAdress();
		return address.getCountry();
	}

	public void setCountry(String country) {
		checkAdress();
		this.address.setCountry(country);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	private void checkAdress(){
		if(this.address==null)
			this.address= new Address();
	}
}
