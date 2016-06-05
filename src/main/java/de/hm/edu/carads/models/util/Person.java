package de.hm.edu.carads.models.util;


/**
 * Eine Person hat diverse Attribute. Diese werden innerhalb dieser abstrakten Klasse gesammelt.
 * @author BK
 *
 */
public abstract class Person extends Model{

	/**
	 * Titel der Person
	 */
	protected String title;
	
	/**
	 * Vorname.
	 */
	protected String firstname;
	
	/**
	 * Nachname.
	 */
	protected String lastname;
	
	/**
	 * E-Mail Adresse.
	 */
	protected String email;
	
	/**
	 * Telefonnummer.
	 */
	protected String phone;
	
	/**
	 * Adresse.
	 */
	protected Address address;
	
	/**
	 * Beschreibung.
	 */
	protected String description;
	
	/**
	 * Konstruktor mit drei Attributen.
	 * @param email
	 * @param firstName
	 * @param lastName
	 */
	public Person(String email, String firstName, String lastName){
		super();
		this.email = email;
		this.firstname = firstName;
		this.lastname = lastName;
		this.address = new Address();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Address getAddress(){
		checkAdress();
		return address;
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
	
	public void setStreet(String street){
		checkAdress();
		this.address.setStreet(street);
	}
	
	public String getStreet(){
		checkAdress();
		return this.address.getStreet();
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
