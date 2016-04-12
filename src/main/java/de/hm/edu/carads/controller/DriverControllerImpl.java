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

public class DriverControllerImpl implements DriverController{

	private DatabaseController dbController;
	private Gson gson;
	

	public DriverControllerImpl(DatabaseController database){
		dbController = database;
		gson = new Gson();
	}

	@Override
	public Driver getDriver(String id) throws NoContentException {
		BasicDBObject dbObj = dbController.getEntity(Driver.class, id);
		if(dbObj == null)
			throw new NoContentException("Driver not found");
		return makeDriverFromBasicDBObject(dbObj);
	}

	@Override
	public Driver addDriver(Driver driver) throws Exception{
		if(existDriverByEmail(driver.getEmail()))
			throw new AlreadyExistsException();
		
		if(!EntityValidator.isNewDriverValid(driver))
			throw new InvalidAttributesException("Driver is not valid");
		
		driver.getMetaInformation().setCreated(MetaInformationController.makeDate());
		BasicDBObject dbObj = dbController.addEntity(Driver.class, BasicDBObject.parse(gson.toJson(driver)));

		return makeDriverFromBasicDBObject(dbObj);
	}

	@Override
	public void deleteDriver(String id) throws Exception{
		
		Driver driver = getDriver(id);
		
		dbController.deleteEntity(Driver.class, id);
		if(dbController.existEntityByKeyValue(Driver.class, "email", driver.getEmail()))
			throw new Exception("entity not deleted");
	}

	@Override
	public Collection<Driver> getDrivers(int startAt, int length) {
		List<DBObject> drivers = dbController.getAllEntities(Driver.class);
		List<Driver> smallList = new ArrayList<Driver>();
		
		Iterator<DBObject> it = drivers.iterator();
		
		while(it.hasNext()){
			smallList.add(makeDriverFromBasicDBObject((BasicDBObject)it.next()));
		}
		
		return smallList;
	}

	@Override
	public long getDriverCount() {
		return dbController.getCollectionCount(Driver.class);
	}
	
	public String makeNewId(String email){
		try {
			byte[] bytesOfMessage = email.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			return new StringBuffer().append(thedigest).toString();
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	@Override
	public Driver changeDriver(String driverid, Driver driver) throws Exception{
		Driver updatedDriver;
		if(!EntityValidator.isNewDriverValid(driver))
			throw new InvalidAttributesException();
		
		try {
			updatedDriver = changeIfNew(getDriver(driverid), driver);
			dbController.updateEntity(Driver.class, updatedDriver.getId(), BasicDBObject.parse(gson.toJson(updatedDriver)));
			return updatedDriver;
		} catch (NoContentException e) {
			throw new NoContentException("Driver not found");
		}
		
	}
	public boolean existDriverByEmail(String email) {
		if(dbController.existEntityByKeyValue(Driver.class, "email", email))
			return true;
		
		return false;
	}

	private Driver makeDriverFromBasicDBObject(BasicDBObject dbObj){
		
		Driver driver = gson.fromJson(dbObj.toJson(), Driver.class);
		driver.setId(dbObj.getString("_id"));
		return driver;
	}

	@Override
	public Car getCar(String driverid) throws Exception{
		Car car = getDriver(driverid).getCar();
		if(car == null)
			throw new NoContentException("no car found");
		return car;
	}

	@Override
	public Car addCar(String driverid, Car car) throws Exception {
		if(!EntityValidator.isNewCarValid(car)){
			throw new InvalidAttributesException();
		}
		
		Driver driver = getDriver(driverid);
		
		driver.setCar(car);
		driver.getMetaInformation().setLastModified(MetaInformationController.makeDate());
		
		dbController.updateEntity(Driver.class, driver.getId(), BasicDBObject.parse(gson.toJson(driver)));
		return car;
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

	@Override
	public void deleteCar(String driverid) throws Exception {
		Driver driver = getDriver(driverid);
		driver.setCar(null);
		driver.getMetaInformation().setLastModified(MetaInformationController.makeDate());
		dbController.updateEntity(Driver.class, driver.getId(), BasicDBObject.parse(gson.toJson(driver)));
	}
}
