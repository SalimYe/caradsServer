package de.hm.edu.carads.models;

public class Advertiser extends Person{

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
