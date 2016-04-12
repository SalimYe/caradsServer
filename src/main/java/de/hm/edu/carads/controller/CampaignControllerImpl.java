package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Model;

public class CampaignControllerImpl extends AbstractEntityControllerImpl<Campaign>{

	public CampaignControllerImpl(DatabaseController database) {
		super(Campaign.class, database);
	}

	@Override
	public Campaign changeEntity(String id, Campaign updatedEntity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Campaign addEntity(Campaign entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

}
