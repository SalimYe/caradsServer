package de.hm.edu.carads.controller;

import java.util.Collection;

import javax.naming.directory.InvalidAttributesException;
import javax.ws.rs.core.NoContentException;

import com.mongodb.BasicDBObject;

import de.hm.edu.carads.controller.exceptions.AlreadyExistsException;
import de.hm.edu.carads.controller.util.EntityValidator;
import de.hm.edu.carads.db.DatabaseController;
import de.hm.edu.carads.models.Car;
import de.hm.edu.carads.models.Driver;

public class DriverControllerImpl extends AbstractEntityControllerImpl<Driver>
		implements DriverController {

	public DriverControllerImpl(DatabaseController database) {
		super(Driver.class, database);
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
		if (!EntityValidator.isNewCarValid(car)) {
			throw new InvalidAttributesException();
		}

		Driver driver = getEntity(driverId);

		driver.addCar(car);
		driver.getMetaInformation().update();

		dbController.updateEntity(Driver.class, driver.getId(),
				BasicDBObject.parse(gson.toJson(driver)));
		return car;
	}

	@Override
	public void deleteCar(String driverId, String carId) throws Exception {
		Driver driver = getEntity(driverId);

		driver.getMetaInformation().update();
		dbController.updateEntity(Driver.class, driver.getId(),
				BasicDBObject.parse(gson.toJson(driver)));
	}

	@Override
	public Driver addEntity(Driver entity) throws Exception {
		if (existDriverByEmail(entity.getEmail()))
			throw new AlreadyExistsException();

		entity.getMetaInformation().makeNewMetaInformation();

		return super.addEntity(entity);
	}
	
	@Override
	public Collection<Car> getCars(String driverId) throws Exception {
		Driver driver = getEntity(driverId);
		return driver.getCars();
	}

	@Override
	public Driver changeEntity(String id, Driver entityData) throws Exception {
		if(!EntityValidator.isEntityValid((entityData)))
			throw new InvalidAttributesException();
		
		Driver d = getDriverByEmail(entityData.getEmail());
		if(d!=null)
			if (!d.getId().equals(id))
				throw new AlreadyExistsException();

		return super.changeEntity(id, entityData);
	}

	private boolean existDriverByEmail(String email) {
		if (dbController.existEntityByKeyValue(Driver.class, "email", email))
			return true;
		return false;
	}

	private Driver getDriverByEmail(String email) {
		return this.makeEntityFromBasicDBObject(dbController
				.getEntityByKeyValue(Driver.class, "email", email));
	}

	
}
