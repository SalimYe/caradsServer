package de.hm.edu.carads.models;

public class Advertiser extends Person{

	public Advertiser(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
	}

	private String company;
	private Image logo;
	
	public boolean setId(String id){
		if(this.id == null || this.id.isEmpty()){
			this.id = id;
			return true;
		}
		else return false;
	}

}
