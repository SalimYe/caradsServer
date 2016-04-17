package de.hm.edu.carads.models;

import de.hm.edu.carads.controller.MetaInformationController;

public abstract class Person extends Model{

	
	protected String firstname;
	protected String lastname;
	protected String email;
	protected String phone;
	protected String country;
	protected String city;
	protected String zip;
	protected String description;
	protected MetaInformation meta;
	
	public Person(String email, String firstName, String lastName){
		this.email = email;
		this.firstname = firstName;
		this.lastname = lastName;
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
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void updateAttributes(Model newModel) {
		
		Person newPerson = (Person) newModel;
		
		if(newPerson.getEmail()!=null)
			this.setEmail(newPerson.getEmail());
		if(newPerson.getFirstName() != null )
			this.setFirstName(newPerson.getFirstName());
		if(newPerson.getLastName() != null )
			this.setLastName(newPerson.getLastName());
		
		this.getMetaInformation().setLastModified(MetaInformationController.makeDate());
	}
}
