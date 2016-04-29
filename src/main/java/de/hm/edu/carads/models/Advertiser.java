package de.hm.edu.carads.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ws.rs.core.NoContentException;

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
	
	public void addCampaign(Campaign campaign) {
		checkCampaigns();
		this.campaigns.add(campaign);
	}
	
	public Campaign getCampaign(String id){
		checkCampaigns();
		Iterator<Campaign> it = campaigns.iterator();
		
		while(it.hasNext()){
			Campaign c = it.next();
			if(c.getId().equals(id))
				return c;
		}
		return null;
	}
	
	public boolean removeCampaign(String id){
		checkCampaigns();
		return campaigns.remove(this.getCampaign(id));
	}
	
	public Collection<Campaign> getCampaigns(){ 
		checkCampaigns();
		return this.campaigns;
	}
	
	private void checkCampaigns(){
		if(this.campaigns == null)
			this.campaigns = new ArrayList<Campaign>();
	}
}
