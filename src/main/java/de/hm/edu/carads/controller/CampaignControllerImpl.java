package de.hm.edu.carads.controller;

import java.util.Collection;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Campaign;
import de.hm.edu.carads.models.Model;

public class CampaignControllerImpl<Campaign> extends EntityControllerImpl{

	public CampaignControllerImpl(Model model, DatabaseController database) {
		super(model, database);
	}

	@Override
	public Model addEntity(Model entity) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
