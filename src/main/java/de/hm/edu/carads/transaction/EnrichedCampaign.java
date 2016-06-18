package de.hm.edu.carads.transaction;

import java.util.Collection;

import de.hm.edu.carads.models.Campaign;

/**
 * Eine Kampagne wird mit Fahrerinformationen ergaenzt.
 * Diese Klasse wird als JSON konvertiert und ueber die API an den Kunden geleitet.
 * @author BK
 *
 */
public class EnrichedCampaign extends Campaign{
	/**
	 * Die erweiterten Fahrzeuge.
	 */
	private Collection<EnrichedFellow> enrichedFellows;
	
	/**
	 * Der Konstruktor baut aus den empfangenen Daten die erweiterte Kampagne zusammen.
	 * @param campaign
	 * @param eFellows
	 */
	public EnrichedCampaign(Campaign campaign, Collection<EnrichedFellow> eFellows){
	
		this.setId(campaign.getId());
		this.setName(campaign.getName());
		this.setStartDate(campaign.getStartDate());
		this.setEndDate(campaign.getEndDate());
		this.setImages(campaign.getImages());
		this.setCampaignBudget(campaign.getCampaignBudget());
		this.setCarBudget(campaign.getCarBudget());
		this.setDescription(campaign.getDescription());
		
		//Fellows ausblenden
		this.setFellows(null);
		this.enrichedFellows = eFellows;
		if(this.enrichedFellows.isEmpty())
			this.enrichedFellows=null;
	}
}
