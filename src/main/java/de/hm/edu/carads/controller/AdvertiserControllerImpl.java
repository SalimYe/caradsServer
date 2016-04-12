package de.hm.edu.carads.controller;

import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Advertiser;

public class AdvertiserControllerImpl extends AbstractEntityControllerImpl<Advertiser> implements AdvertiserController{

	public AdvertiserControllerImpl(DatabaseController database) {
		super(Advertiser.class, database);
	}

	@Override
	public Advertiser changeEntity(String id, Advertiser updatedEntity) throws Exception {
		return null;
	}

	@Override
	public Advertiser addEntity(Advertiser entity) throws Exception {
		return null;
	}
	
}
