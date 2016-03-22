package de.hm.edu.carads.models;

public class Driver {
	private String id;
	private String email;
	private String firstName;
	private String birthdate;
	private String lastName;
	private MetaInformation meta;
	
	public Driver(String id, String email, String firstName, String lastName, String age) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.birthdate = age;
		this.lastName = lastName;
		this.meta = new MetaInformation();
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
	
	
}

