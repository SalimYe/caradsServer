package de.hm.edu.carads.models;

import java.util.Collection;

public class Advertiser extends Person{

	private String company;
	private Image logo;
	private Collection<Campaign> campaigns;
	
	public Advertiser(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Image getLogo() {
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}
}
