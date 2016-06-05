package de.hm.edu.carads.transaction;

import java.util.Collection;

import de.hm.edu.carads.models.Campaign;

public class EnrichedCampaign extends Campaign{
	
	private Collection<EnrichedFellow> enrichedFellows;
	
	public EnrichedCampaign(Campaign campaign, Collection<EnrichedFellow> eFellows){
	
		this.setId(campaign.getId());
		this.setName(campaign.getName());
		this.setStartDate(campaign.getStartDate());
		this.setEndDate(campaign.getEndDate());
		this.setImages(campaign.getImages());
		this.setCampaignBudget(campaign.getCampaignBudget());
		this.setDescription(campaign.getDescription());
		
		//Fellows ausblenden
		this.setFellows(null);
		this.enrichedFellows = eFellows;
		if(this.enrichedFellows.isEmpty())
			this.enrichedFellows=null;
	}
}
