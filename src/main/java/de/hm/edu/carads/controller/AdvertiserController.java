package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;

public interface AdvertiserController extends AbstractEntityController<Advertiser>{
	public Campaign addCampaign(String advertiserId, Campaign campaign) throws Exception;
	public Campaign getCampaign(String advertiserId, String campaignId) throws Exception;
	public void deleteCampaign(String advertiserId, String campaignId) throws Exception;
	public Campaign updateCampaign(String advertiserId, String campaignId, Campaign campaign) throws Exception;
	public Collection<Campaign> getCampaigns(String advertiserId) throws Exception;
	
	public Campaign addVehicleToCampaign(String advertiserId, String campaignId, String carId) throws Exception;
	public Collection<Campaign> getCarRequestingCampaigns(String carid);
	public Advertiser getAdvertiserFromCampaign(String campaignId) throws Exception;
}
