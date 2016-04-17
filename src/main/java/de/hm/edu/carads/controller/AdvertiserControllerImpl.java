package de.hm.edu.carads.controller;

import javax.naming.directory.InvalidAttributesException;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Advertiser;

public class AdvertiserControllerImpl extends AbstractEntityControllerImpl<Advertiser> implements AdvertiserController{

	public AdvertiserControllerImpl(DatabaseController database) {
		super(Advertiser.class, database);
	}
	
	@Override
	public Advertiser changeEntity(String id, Advertiser entityData) throws Exception {
		if(!EntityValidator.isEntityValid((entityData)))
			throw new InvalidAttributesException();
		
		Advertiser d = getAdvertiserByEmail(entityData.getEmail());
		if(d!=null)
			if (!d.getId().equals(id))
				throw new AlreadyExistsException();

		return super.changeEntity(id, entityData);
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
	
	private Advertiser getAdvertiserByEmail(String email) {
		return this.makeEntityFromBasicDBObject(dbController
				.getEntityByKeyValue(Advertiser.class, "email", email));
	}
}
