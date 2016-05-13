package de.hm.edu.carads.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import com.mongodb.BasicDBObject;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.db.ModelCollection;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

public class DriverControllerImpl extends AbstractEntityControllerImpl<Driver>
		implements DriverController {

	public DriverControllerImpl(DatabaseController database) {
		super(ModelCollection.DRIVER, database);
	}

	@Override
	public Car getCar(String driverId, String carId) throws Exception {
		Car car = getEntity(driverId).getCar(carId);
		if (car == null)
			throw new NoContentException("no car found");
		return car;
	}

	@Override
	public Car addCar(String driverId, Car car) throws Exception {
		if (!EntityValidator.isEntityValid(car)) {
			throw new InvalidAttributesException();
		}

		Driver driver = getEntity(driverId);

		car.setId(dbController.getNewId());
		car.renewMetaInformation();
		driver.addCar(car);

		driver.getMetaInformation().update();
		
		dbController.updateEntity(ModelCollection.DRIVER, driver.getId(),
				BasicDBObject.parse(gson.toJson(driver)));
		return car;
	}

	@Override
	public void deleteCar(String driverId, String carId) throws Exception {
		Driver driver = getEntity(driverId);
		driver.removeCar(carId);
		driver.getMetaInformation().update();
		dbController.updateEntity(ModelCollection.DRIVER, driver.getId(),
				BasicDBObject.parse(gson.toJson(driver)));
	}

//	@Override
//	public Driver addEntity(Driver entity) throws Exception {
//		if (existDriverByEmail(entity.getEmail()))
//			throw new AlreadyExistsException();
//
//		entity.getMetaInformation().makeNewMetaInformation();
//
//		return super.addEntity(entity);
//	}
	
	@Override
	public Collection<Car> getCars(String driverId) throws Exception {
		Driver driver = getEntity(driverId);
		return driver.getCars();
	}

//	@Override
//	public Driver changeEntity(String id, Driver entityData) throws Exception {
//		if(!EntityValidator.isEntityValid((entityData)))
//			throw new InvalidAttributesException();
//		
//		Driver d = getDriverByEmail(entityData.getEmail());
//		if(d!=null)
//			if (!d.getId().equals(id))
//				throw new AlreadyExistsException();
//
//		return super.changeEntity(id, entityData);
//	}
	
	@Override
	public Car updateCar(String driverId, String carId, Car car) throws Exception{
		if(!EntityValidator.isEntityValid(car))
			throw new InvalidAttributesException();
		
		Driver driver = getEntity(driverId);
		Car oldCar = driver.getCar(carId);
		if(!driver.removeCar(carId))
			throw new NoContentException("Could not find Car in Driver "+driverId);
		
		car.update(oldCar.getMetaInformation());
		car.setId(carId);
		driver.addCar(car);
		
		dbController.updateEntity(ModelCollection.DRIVER, driverId, BasicDBObject.parse(gson.toJson(driver)));
		return car;
	}

	private boolean existDriverByEmail(String email) {
		if (dbController.existEntityByKeyValue(ModelCollection.DRIVER, "email", email))
			return true;
		return false;
	}

	private Driver getDriverByEmail(String email) {
		return this.makeEntityFromBasicDBObject(dbController
				.getEntityByKeyValue(ModelCollection.DRIVER, "email", email));
	}

	@Override
	public Collection<Car> getAllCars(){
		Collection<Car> allCars = new ArrayList<Car>();
		
		Iterator<Driver> drivers = this.getAllEntities().iterator();
		while(drivers.hasNext()){
			allCars.addAll(drivers.next().getCars());
		}
		return allCars;
	}

	
}
