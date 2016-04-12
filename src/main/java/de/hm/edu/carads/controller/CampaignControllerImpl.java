package de.hm.edu.carads.controller;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Campaign;

public class CampaignControllerImpl extends AbstractEntityControllerImpl<Campaign>{

	public CampaignControllerImpl(DatabaseController database) {
		super(Campaign.class, database);
	}	

}
