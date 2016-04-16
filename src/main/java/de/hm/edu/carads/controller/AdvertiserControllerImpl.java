package de.hm.edu.carads.controller;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Advertiser;
import de.hm.edu.carads.models.Driver;

public class AdvertiserControllerImpl extends AbstractEntityControllerImpl<Advertiser> implements AdvertiserController{

	public AdvertiserControllerImpl(DatabaseController database) {
		super(Advertiser.class, database);
	}
	
	@Override
	public Advertiser addEntity(Advertiser entity) throws Exception {
		if(existAdvertiserByEmail(entity.getEmail()))
			throw new AlreadyExistsException();
		return super.addEntity(entity);
	}
	
	private boolean existAdvertiserByEmail(String email) {
		if(dbController.existEntityByKeyValue(Advertiser.class, "email", email))
			return true;
		return false;
	}	
}
