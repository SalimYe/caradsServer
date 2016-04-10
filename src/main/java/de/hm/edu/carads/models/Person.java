package de.hm.edu.carads.models;

public abstract class Person {

	protected String id;
	protected String email;
	protected String firstName;
	protected String lastName;
	protected MetaInformation meta;
	protected String city;
	protected String zip;
	protected String country;
	protected String phone;
	
	public Person(String email, String firstName, String lastName){
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
