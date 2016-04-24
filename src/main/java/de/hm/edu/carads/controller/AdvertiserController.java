package de.hm.edu.carads.controller;

import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Campaign;

public interface AdvertiserController extends AbstractEntityController<Advertiser>{
	public Campaign addCampaign(String advertiserId, Campaign campaign) throws Exception;
}
