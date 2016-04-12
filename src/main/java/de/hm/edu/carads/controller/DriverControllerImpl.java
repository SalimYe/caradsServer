package de.hm.edu.carads.controller;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import com.mongodb.BasicDBObject;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

public class DriverControllerImpl extends AbstractEntityControllerImpl<Driver> implements DriverController{

	public DriverControllerImpl(DatabaseController database) {
		super(Driver.class, database);
	}

	@Override
	public Car getCar(String entitiyid) throws Exception {
		Car car = getEntity(entitiyid).getCar();
		if(car == null)
			throw new NoContentException("no car found");
		return car;
	}

	@Override
	public Car addCar(String entitiyid, Car car) throws Exception {
		if(!EntityValidator.isNewCarValid(car)){
			throw new InvalidAttributesException();
		}
		
		Driver driver = getEntity(entitiyid);
		
		driver.setCar(car);
		driver.getMetaInformation().setLastModified(MetaInformationController.makeDate());
		
		dbController.updateEntity(Driver.class, driver.getId(), BasicDBObject.parse(gson.toJson(driver)));
		return car;
	}

	@Override
	public void deleteCar(String entitiyid) throws Exception {
		Driver driver = getEntity(entitiyid);
		driver.setCar(null);
		driver.getMetaInformation().setLastModified(MetaInformationController.makeDate());
		dbController.updateEntity(Driver.class, driver.getId(), BasicDBObject.parse(gson.toJson(driver)));
	}

	@Override
	public Driver addEntity(Driver entity) throws Exception {
		if(existDriverByEmail(entity.getEmail()))
			throw new AlreadyExistsException();
		
		entity.getMetaInformation().setCreated(MetaInformationController.makeDate());
		
		return super.addEntity(entity);
	}
	
	private boolean existDriverByEmail(String email) {
		if(dbController.existEntityByKeyValue(Driver.class, "email", email))
			return true;
		return false;
	}	
}
