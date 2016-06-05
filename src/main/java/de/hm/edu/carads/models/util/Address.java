package de.hm.edu.carads.models.util;

/**
 * Diese Klasse repraesentiert eine Adresse mit allen benoetigten Daten.
 * @author BK
 *
 */
public class Address {
	
	/**
	 * Das Land.
	 */
	private String country;
	
	/**
	 * Die Stadt.
	 */
	private String city;
	
	/**
	 * Die Postleitzahl.
	 */
	private String zip;
	
	/**
	 * Die Strasse und Hausnummer.
	 */
	private String street;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
}
