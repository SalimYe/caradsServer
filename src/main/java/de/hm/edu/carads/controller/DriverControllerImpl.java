package de.hm.edu.carads.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.activity.InvalidActivityException;
import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NoContentException;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.DatabaseControllerImpl;
import de.hm.edu.carads.db.PropertiesLoader;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;
import de.hm.edu.carads.models.MetaInformation;

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
	public Driver changeEntity(String id, Driver updatedEntity) throws Exception {
		Driver updatedDriver;
		if(!EntityValidator.isNewDriverValid(updatedEntity))
			throw new InvalidAttributesException();
		
		try {
			updatedDriver = changeIfNew(getEntity(id), updatedEntity);
			dbController.updateEntity(Driver.class, updatedDriver.getId(), BasicDBObject.parse(gson.toJson(updatedDriver)));
			return updatedDriver;
		} catch (NoContentException e) {
			throw new NoContentException("Driver not found");
		}
	}

	@Override
	public Driver addEntity(Driver entity) throws Exception {
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
	
	private Driver changeIfNew(Driver oldDriver, Driver newDriver){
		if(newDriver.getEmail()!=null)
			oldDriver.setEmail(newDriver.getEmail());
		if(newDriver.getFirstName() != null )
			oldDriver.setFirstName(newDriver.getFirstName());
		if(newDriver.getLastName() != null )
			oldDriver.setLastName(newDriver.getLastName());
		if(newDriver.getBirthdate() != null )
			oldDriver.setBirthdate(newDriver.getBirthdate());
		if(newDriver.getCar() != null)
			oldDriver.setCar(newDriver.getCar());
		if(newDriver.getImage() != null)
			oldDriver.setImage(newDriver.getImage());
		
		oldDriver.getMetaInformation().setLastModified(MetaInformationController.makeDate());
		return oldDriver;
	}

	
}
