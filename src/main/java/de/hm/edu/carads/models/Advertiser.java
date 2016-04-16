package de.hm.edu.carads.models;

public class Advertiser extends Person{

	private String company;
	private Image logo;
	
	public Advertiser(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
	}
	
	@Override
	public void updateAttributes(Model model){
		super.updateAttributes(model);
		Advertiser newAdvertiser = (Advertiser) model;
		
		if(newAdvertiser.getLogo() != null )
			this.setLogo(newAdvertiser.getLogo());
		if(newAdvertiser.getCompany() != null )
			this.setCompany(newAdvertiser.getCompany());
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
