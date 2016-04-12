package de.hm.edu.carads.controller;

import javax.naming.directory.InvalidAttributesException;

import com.mongodb.BasicDBObject;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Driver;

public class DriverController2 extends EntityControllerImpl<Driver>{

	public DriverController2(Class<Driver> model, DatabaseController database) {
		super(model, database);
	}

	@Override
	public Driver addEntity(Driver entity) throws Exception{
		if(existDriverByEmail(entity.getEmail()))
			throw new AlreadyExistsException();
		
		if(!EntityValidator.isNewDriverValid(entity))
			throw new InvalidAttributesException("Driver is not valid");
		
		entity.getMetaInformation().setCreated(MetaInformationController.makeDate());
		BasicDBObject dbObj = dbController.addEntity(Driver.class, BasicDBObject.parse(gson.toJson(entity)));

		return makeEntityFromBasicDBObject(dbObj);
	}
	
	private boolean existDriverByEmail(String email) {
		if(dbController.existEntityByKeyValue(Driver.class, "email", email))
			return true;
		return false;
	}

}
